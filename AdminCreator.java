package com.sixstringmarket.util;

import com.sixstringmarket.dao.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdminCreator {
    public static void main(String[] args) {
        String username = "admin1";
        String password = "admin1";
        String email = "admin@sixstringmarket.com";
        
        try {
            // Хеширане на паролата
            String hashedPassword = PasswordEncryptor.encrypt(password);
            
            // Проверка дали администраторът вече съществува
            String checkSql = "SELECT COUNT(*) FROM users WHERE username = ?";
            String insertSql = "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, 'ADMIN')";
            String updateSql = "UPDATE users SET password = ? WHERE username = ?";
            
            Connection conn = DBConnection.getConnection();
            
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, username);
                var rs = checkStmt.executeQuery();
                rs.next();
                int count = rs.getInt(1);
                
                if (count == 0) {
                    // Създаване на нов администратор
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                        insertStmt.setString(1, username);
                        insertStmt.setString(2, hashedPassword);
                        insertStmt.setString(3, email);
                        insertStmt.executeUpdate();
                        System.out.println("Администраторът е създаден успешно.");
                    }
                } else {
                    // Обновяване на паролата на съществуващ администратор
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setString(1, hashedPassword);
                        updateStmt.setString(2, username);
                        updateStmt.executeUpdate();
                        System.out.println("Администраторската парола е обновена успешно.");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Грешка при създаване на администратор: " + e.getMessage());
        }
    }
}