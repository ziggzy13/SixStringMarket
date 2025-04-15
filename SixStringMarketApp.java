package com.sixstringmarket.ui;

import com.sixstringmarket.dao.DBConnection;
import com.sixstringmarket.service.AuthenticationService;
import com.sixstringmarket.util.Constants;
import com.sixstringmarket.util.ResourceInitializer;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * Main class for the SixStringMarket application
 */
public class SixStringMarketApp {
    
    /**
     * Main method to start the application
     */
    public static void main(String[] args) {
        try {
            // Set Look and Feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Initialize resources
            if (!ResourceInitializer.initialize()) {
                System.err.println("Failed to initialize resources");
                System.exit(1);
            }
            
            // Initialize UI defaults
            initializeUIDefaults();
            
            // Check database connection
            try {
                DBConnection.getConnection();
                System.out.println("Database connection successful.");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, 
                    "Error connecting to database: " + e.getMessage(), 
                    "Database Error", 
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
            
            // Start application in EDT (Event Dispatch Thread)
            SwingUtilities.invokeLater(() -> {
                // If no user is logged in, show login screen
                if (!AuthenticationService.getInstance().isAuthenticated()) {
                    new LoginFrame().setVisible(true);
                } else {
                    // Otherwise show main window
                    new MainFrame().setVisible(true);
                }
            });
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "An error occurred while starting the application: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Set up UI component defaults
     */
    private static void initializeUIDefaults() {
        // Set up fonts
        Font defaultFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font boldFont = new Font("Segoe UI", Font.BOLD, 14);
        Font titleFont = new Font("Segoe UI", Font.BOLD, 18);
        
        // Store fonts in constants
        Constants.DEFAULT_FONT = defaultFont;
        Constants.BOLD_FONT = boldFont;
        Constants.TITLE_FONT = titleFont;
        
        // UI Manager settings for all components
        UIManager.put("Panel.background", Constants.BACKGROUND_COLOR);
        UIManager.put("Label.font", defaultFont);
        UIManager.put("Button.font", defaultFont);
        UIManager.put("TextField.font", defaultFont);
        UIManager.put("PasswordField.font", defaultFont);
        UIManager.put("TextArea.font", defaultFont);
        UIManager.put("ComboBox.font", defaultFont);
        UIManager.put("CheckBox.font", defaultFont);
        UIManager.put("RadioButton.font", defaultFont);
        UIManager.put("TabbedPane.font", defaultFont);
        UIManager.put("Table.font", defaultFont);
        UIManager.put("TableHeader.font", boldFont);
        UIManager.put("MenuItem.font", defaultFont);
        UIManager.put("Menu.font", defaultFont);
        UIManager.put("MenuBar.font", defaultFont);
        
        // Button settings
        UIManager.put("Button.background", Constants.PRIMARY_COLOR);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.focus", new Color(0, 0, 0, 0));
    }
}