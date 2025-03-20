package com.sixstringmarket.dao;

import com.sixstringmarket.model.SavedGuitar;
import com.sixstringmarket.model.Guitar;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object за работа със запазени китари в базата данни
 */
public class SavedGuitarDAO {
    
    /**
     * Запазване на китара
     * @param userId ID на потребителя
     * @param guitarId ID на китарата
     * @return true ако операцията е успешна, false в противен случай
     */
    public boolean saveGuitar(int userId, int guitarId) {
        String sql = "INSERT INTO saved_guitars (user_id, guitar_id) VALUES (?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, guitarId);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    // Ако е необходимо, може да се върне SavedGuitar обект със зададено saveId
                    return true;
                }
            }
            
            return false;
        } catch (SQLException e) {
            System.err.println("Грешка при запазване на китара: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Премахване на китара от запазените
     * @param userId ID на потребителя
     * @param guitarId ID на китарата
     * @return true ако операцията е успешна, false в противен случай
     */
    public boolean removeFromSaved(int userId, int guitarId) {
        String sql = "DELETE FROM saved_guitars WHERE user_id = ? AND guitar_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, guitarId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Грешка при премахване на запазена китара: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Проверка дали китара е запазена от потребител
     * @param userId ID на потребителя
     * @param guitarId ID на китарата
     * @return true ако китарата е запазена, false в противен случай
     */
    public boolean isGuitarSaved(int userId, int guitarId) {
        String sql = "SELECT COUNT(*) FROM saved_guitars WHERE user_id = ? AND guitar_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, guitarId);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Грешка при проверка на запазена китара: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Получаване на всички запазени китари на потребител
     * @param userId ID на потребителя
     * @return Списък със запазени китари
     */
    public List<SavedGuitar> getSavedGuitarsByUser(int userId) {
        List<SavedGuitar> savedGuitars = new ArrayList<>();
        String sql = "SELECT * FROM saved_guitars WHERE user_id = ? ORDER BY date_saved DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                savedGuitars.add(extractSavedGuitarFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Грешка при получаване на запазени китари: " + e.getMessage());
        }
        
        return savedGuitars;
    }
    
    /**
     * Получаване на запазена китара по ID
     * @param saveId ID на записа
     * @return SavedGuitar обект или null ако няма такъв запис
     */
    public SavedGuitar getSavedGuitarById(int saveId) {
        String sql = "SELECT * FROM saved_guitars WHERE save_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, saveId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractSavedGuitarFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Грешка при получаване на запазена китара по ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Помощен метод за извличане на запазена китара от ResultSet
     */
    private SavedGuitar extractSavedGuitarFromResultSet(ResultSet rs) throws SQLException {
        SavedGuitar savedGuitar = new SavedGuitar();
        savedGuitar.setSaveId(rs.getInt("save_id"));
        savedGuitar.setUserId(rs.getInt("user_id"));
        savedGuitar.setGuitarId(rs.getInt("guitar_id"));
        savedGuitar.setDateSaved(rs.getTimestamp("date_saved"));
        return savedGuitar;
    }
}