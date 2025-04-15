package com.sixstringmarket.ui;

import com.sixstringmarket.dao.DBConnection;
import com.sixstringmarket.service.AuthenticationService;
import com.sixstringmarket.util.Constants;
import com.sixstringmarket.util.AdminManager;
import com.sixstringmarket.util.StyleManager;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * Main application class for SixStringMarket
 * Entry point for the application with comprehensive initialization
 */
public class SixStringMarketApp {
    
    /**
     * Main method to start the application
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("Starting SixStringMarket application...");
        
        try {
            // Initialize styles early - this creates default CSS file if needed
            try {
                StyleManager.getInstance().initialize();
            } catch (Exception e) {
                System.err.println("Warning: Style initialization failed: " + e.getMessage());
                e.printStackTrace();
            }
            
            // Set system look and feel
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                System.out.println("System look and feel applied");
            } catch (Exception e) {
                System.err.println("Warning: Could not set system look and feel: " + e.getMessage());
            }
            
            // Initialize UI defaults
            initializeUIDefaults();
            
            // Check database connection
            try {
                DBConnection.getConnection();
                System.out.println("Database connection successful.");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, 
                    "Database connection error: " + e.getMessage(), 
                    "Database Error", 
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
            
            // Ensure default admin exists
            try {
                AdminManager adminManager = new AdminManager();
                boolean adminCreated = adminManager.ensureDefaultAdminExists();
                if (adminCreated) {
                    System.out.println("Default admin account created.");
                }
            } catch (Exception e) {
                System.err.println("Warning: Error creating default admin: " + e.getMessage());
            }
            
            // Start application in the Event Dispatch Thread
            SwingUtilities.invokeLater(() -> {
                try {
                    System.out.println("Opening login screen...");
                    LoginFrame loginFrame = new LoginFrame();
                    loginFrame.setVisible(true);
                } catch (Exception e) {
                    System.err.println("Error creating login frame: " + e.getMessage());
                    e.printStackTrace();
                    
                    // Show error dialog
                    JOptionPane.showMessageDialog(null,
                        "Error starting application: " + e.getMessage(),
                        "Startup Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            });
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "An error occurred while starting the application: " + e.getMessage(),
                "Fatal Error",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    
    /**
     * Initialize UI components configuration
     */
    private static void initializeUIDefaults() {
        System.out.println("Initializing UI defaults...");
        
        try {
            // Set default fonts
            Font defaultFont = new Font("Segoe UI", Font.PLAIN, 14);
            Font boldFont = new Font("Segoe UI", Font.BOLD, 14);
            Font titleFont = new Font("Segoe UI", Font.BOLD, 18);
            
            // Store fonts in constants
            Constants.DEFAULT_FONT = defaultFont;
            Constants.BOLD_FONT = boldFont;
            Constants.TITLE_FONT = titleFont;
            
            // Configure UI Manager for all components
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
            
            // Configure button styling
            UIManager.put("Button.background", Constants.PRIMARY_COLOR);
            UIManager.put("Button.foreground", Color.WHITE);
            UIManager.put("Button.focus", new Color(0, 0, 0, 0));
            
            // Configure focus highlighting
            UIManager.put("Button.select", Constants.SECONDARY_COLOR);
            UIManager.put("TextField.selectionBackground", Constants.SECONDARY_COLOR);
            UIManager.put("PasswordField.selectionBackground", Constants.SECONDARY_COLOR);
            UIManager.put("TextArea.selectionBackground", Constants.SECONDARY_COLOR);
            
            // Additional UI tweaks
            UIManager.put("ScrollBar.width", 12);
            UIManager.put("TabbedPane.contentAreaColor", Constants.BACKGROUND_COLOR);
            UIManager.put("TabbedPane.selected", Constants.SECONDARY_COLOR);
            
            System.out.println("UI defaults initialized successfully");
        } catch (Exception e) {
            System.err.println("Error initializing UI defaults: " + e.getMessage());
            e.printStackTrace();
        }
    }
}