package com.sixstringmarket.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Параметри за връзка с базата данни
    private static final String URL = "jdbc:mysql://localhost:3306/sixstringmarket";
    private static final String USER = "root";
    private static final String PASSWORD = "KTN_ClanBG123!!!";
    
    private static Connection connection = null;
    
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC Driver не е намерен", e);
            }
        }
        return connection;
    }
    
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                System.err.println("Грешка при затваряне на връзката с базата данни: " + e.getMessage());
            }
        }
    }
}