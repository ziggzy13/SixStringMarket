package com.sixstringmarket.dao;

import com.sixstringmarket.model.User;
import com.sixstringmarket.model.User.UserRole;
import com.sixstringmarket.util.PasswordEncryptor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object за работа с потребители в базата данни
 */
public class UserDAO {
    
    /**
     * Създаване на нов потребител
     * @param user Потребителят, който трябва да бъде създаден
     * @return true ако операцията е успешна, false в противен случай
     */
    public boolean createUser(User user) {
        String sql = "INSERT INTO users (username, password, email, phone, address, role) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Хеширане на паролата преди съхранение
            String hashedPassword = PasswordEncryptor.encrypt(user.getPassword());
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, hashedPassword);
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getAddress());
            stmt.setString(6, user.getRole().name());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    user.setUserId(rs.getInt(1));
                }
                return true;
            }
            
            return false;
        } catch (SQLException e) {
            System.err.println("Грешка при създаване на потребител: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Получаване на потребител по ID
     * @param userId ID на потребителя
     * @return User обект или null ако няма такъв потребител
     */
    public User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Грешка при получаване на потребител по ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Получаване на потребител по потребителско име
     * @param username Потребителското име
     * @return User обект или null ако няма такъв потребител
     */
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Грешка при получаване на потребител по потребителско име: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Автентикация на потребител
     * @param username Потребителско име
     * @param password Парола
     * @return User обект при успешна автентикация или null
     */
    public User authenticateUser(String username, String password) {
        // Специална проверка за администратора при първоначалното вход
        if (username.equals("admin") && password.equals("admin123")) {
            User adminUser = getUserByUsername("admin");
            if (adminUser != null) {
                return adminUser;
            }
        }
        
        // Нормалната проверка за автентикация за останалите потребители
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (PasswordEncryptor.verify(password, storedPassword)) {
                    return extractUserFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Грешка при автентикация на потребител: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Обновяване на информацията за потребител
     * @param user Потребителят с обновените данни
     * @return true ако операцията е успешна, false в противен случай
     */
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET username = ?, email = ?, phone = ?, address = ?, role = ? WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPhone());
            stmt.setString(4, user.getAddress());
            stmt.setString(5, user.getRole().name());
            stmt.setInt(6, user.getUserId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Грешка при обновяване на потребител: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Обновяване на паролата на потребител
     * @param userId ID на потребителя
     * @param newPassword Новата парола
     * @return true ако операцията е успешна, false в противен случай
     */
    public boolean updatePassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String hashedPassword = PasswordEncryptor.encrypt(newPassword);
            
            stmt.setString(1, hashedPassword);
            stmt.setInt(2, userId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Грешка при обновяване на парола: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Получаване на всички потребители
     * @return Списък с всички потребители
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Грешка при получаване на всички потребители: " + e.getMessage());
        }
        
        return users;
    }
    
    /**
     * Изтриване на потребител
     * @param userId ID на потребителя
     * @return true ако операцията е успешна, false в противен случай
     */
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Грешка при изтриване на потребител: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Помощен метод за извличане на потребител от ResultSet
     */
    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setAddress(rs.getString("address"));
        user.setRole(UserRole.valueOf(rs.getString("role")));
        user.setRegistrationDate(rs.getTimestamp("registration_date"));
        return user;
    }
    
}