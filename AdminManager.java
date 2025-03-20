package com.sixstringmarket.util;

import com.sixstringmarket.dao.DBConnection;
import com.sixstringmarket.dao.UserDAO;
import com.sixstringmarket.model.User;
import com.sixstringmarket.model.User.UserRole;
import com.sixstringmarket.service.AuthenticationService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Клас, отговарящ за управлението на администраторски акаунти
 */
public class AdminManager {
    
    private static final String DEFAULT_ADMIN_USERNAME = "admin";
    private static final String DEFAULT_ADMIN_PASSWORD = "admin123";
    private static final String DEFAULT_ADMIN_EMAIL = "admin@sixstringmarket.com";
    
    private UserDAO userDAO;
    
    /**
     * Конструктор
     */
    public AdminManager() {
        this.userDAO = new UserDAO();
    }
    
    /**
     * Проверява дали има създаден админ акаунт и създава такъв, ако няма
     * Това трябва да се извиква при стартиране на приложението
     * @return true ако е създаден нов администратор, false ако вече има
     */
    public boolean ensureDefaultAdminExists() {
        try {
            // Проверяваме дали съществува admin акаунт
            User adminUser = userDAO.getUserByUsername(DEFAULT_ADMIN_USERNAME);
            
            if (adminUser == null) {
                // Създаваме admin акаунт, ако няма такъв
                User newAdmin = new User();
                newAdmin.setUsername(DEFAULT_ADMIN_USERNAME);
                newAdmin.setPassword(DEFAULT_ADMIN_PASSWORD); // Ще се хешира от DAO
                newAdmin.setEmail(DEFAULT_ADMIN_EMAIL);
                newAdmin.setRole(UserRole.ADMIN);
                
                userDAO.createUser(newAdmin);
                return true;
            }
            
            return false;
        } catch (Exception e) {
            System.err.println("Грешка при създаване на администратор: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Създава нов администраторски акаунт
     * Трябва да се извиква само от администратор
     * @param username Потребителско име
     * @param password Парола
     * @param email Имейл
     * @return true ако е успешно, false в противен случай
     */
    public boolean createAdminAccount(String username, String password, String email) {
        // Проверяваме дали текущият потребител е администратор
        if (!AuthenticationService.getInstance().isAdmin()) {
            return false;
        }
        
        try {
            // Проверяваме дали потребителското име е заето
            User existingUser = userDAO.getUserByUsername(username);
            if (existingUser != null) {
                return false; // Потребителското име вече съществува
            }
            
            // Създаваме нов администраторски акаунт
            User newAdmin = new User();
            newAdmin.setUsername(username);
            newAdmin.setPassword(password); // Ще се хешира от DAO
            newAdmin.setEmail(email);
            newAdmin.setRole(UserRole.ADMIN);
            
            return userDAO.createUser(newAdmin);
        } catch (Exception e) {
            System.err.println("Грешка при създаване на администратор: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Проверява дали даден акаунт е администраторски
     * @param username Потребителско име
     * @return true ако е админ, false в противен случай
     */
    public boolean isAdmin(String username) {
        User user = userDAO.getUserByUsername(username);
        return user != null && user.getRole() == UserRole.ADMIN;
    }
    
    /**
     * Връща брой администратори в системата
     * @return Брой админи
     */
    public int getAdminCount() {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM users WHERE role = 'ADMIN'");
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            
            return 0;
        } catch (SQLException e) {
            System.err.println("Грешка при броене на администратори: " + e.getMessage());
            return 0;
        }
    }
}