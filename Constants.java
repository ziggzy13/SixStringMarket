package com.sixstringmarket.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * Class with constants and styles for application design
 */
public class Constants {
    
    // Application title
    public static final String APP_TITLE = "SixStringMarket - Guitar Trading Platform";
    
    // Application version
    public static final String APP_VERSION = "1.0.0";
    
    // Color scheme - modern and elegant
    public static Color PRIMARY_COLOR = new Color(66, 103, 178);      // Blue (primary color)
    public static Color SECONDARY_COLOR = new Color(144, 202, 249);   // Light blue
    public static Color ACCENT_COLOR = new Color(240, 98, 146);       // Pink (accent)
    public static Color BACKGROUND_COLOR = new Color(250, 250, 250);  // Almost white
    public static Color PANEL_COLOR = new Color(255, 255, 255);       // White
    public static Color TEXT_COLOR = new Color(33, 33, 33);           // Dark gray
    public static Color TEXT_SECONDARY_COLOR = new Color(117, 117, 117); // Medium gray
    public static Color SUCCESS_COLOR = new Color(76, 175, 80);       // Green
    public static Color WARNING_COLOR = new Color(255, 152, 0);       // Orange
    public static Color ERROR_COLOR = new Color(211, 47, 47);         // Red
    
    // Fonts - modern and readable
    public static Font DEFAULT_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    public static Font LARGE_FONT = new Font("Segoe UI", Font.PLAIN, 16);
    public static Font BOLD_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    public static Font SUBTITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    public static Font CARD_TITLE_FONT = new Font("Segoe UI", Font.BOLD, 16);
    
    // Borders and padding
    public static final int PADDING_SMALL = 5;
    public static final int PADDING_MEDIUM = 10;
    public static final int PADDING_LARGE = 20;
    public static final int ROUNDED_CORNER_RADIUS = 8;
    
    // Ready-made borders for components
    public static final Border PANEL_BORDER = new LineBorder(new Color(225, 225, 225), 1, true);
    public static final Border CARD_BORDER = new CompoundBorder(
            new LineBorder(new Color(225, 225, 225), 1, true),
            new EmptyBorder(10, 10, 10, 10));
    public static final Border TEXT_FIELD_BORDER = new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(5, 8, 5, 8));
    public static final Border BUTTON_BORDER = new LineBorder(new Color(200, 200, 200), 1, true);
    
    // Component dimensions
    public static final Dimension BUTTON_DIMENSION = new Dimension(120, 36);
    public static final Dimension SMALL_BUTTON_DIMENSION = new Dimension(90, 32);
    public static final Dimension TEXT_FIELD_DIMENSION = new Dimension(200, 36);
    public static final Dimension CARD_DIMENSION = new Dimension(300, 350);
    
    // Icons (paths)
    public static final String RESOURCES_PATH = "resources/";
    public static final String ICON_PATH = RESOURCES_PATH + "icons/";
    public static final String LOGO_PATH = ICON_PATH + "logo.png";
    public static final String USER_ICON = ICON_PATH + "user.png";
    public static final String GUITAR_ICON = ICON_PATH + "guitar.png";
    public static final String CART_ICON = ICON_PATH + "cart.png";
    public static final String SAVE_ICON = ICON_PATH + "save.png";
    public static final String SEARCH_ICON = ICON_PATH + "search.png";
    public static final String SETTINGS_ICON = ICON_PATH + "settings.png";
    
    // Guitar brands (for filter)
    public static final String[] GUITAR_BRANDS = {
        "Fender", "Gibson", "Ibanez", "Jackson", "ESP", "PRS", "Yamaha", 
        "Epiphone", "Schecter", "Dean", "Gretsch", "Martin", "Taylor", 
        "Washburn", "Cort", "B.C. Rich", "Squier", "Charvel", "Takamine"
    };
    
    // Path to resources
    public static final String IMAGES_PATH = RESOURCES_PATH + "images/";
    public static final String CONFIG_PATH = RESOURCES_PATH + "config/";
    public static final String CSS_PATH = RESOURCES_PATH + "css/";
    
    // Static initializer to load custom styles if available
    static {
        loadCustomStyles();
        createDefaultCssFile();
    }
    
    /**
     * Loads custom styles from a css.properties file
     */
    private static void loadCustomStyles() {
        Properties props = new Properties();
        boolean loaded = false;
        
        // Try to load from resources/css/styles.css
        try {
            File cssFile = new File(CSS_PATH + "styles.css");
            if (cssFile.exists()) {
                try (FileInputStream fis = new FileInputStream(cssFile)) {
                    props.load(fis);
                    loaded = true;
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading custom styles: " + e.getMessage());
        }
        
        // If we couldn't load from file, try to load from classpath
        if (!loaded) {
            try (InputStream is = Constants.class.getResourceAsStream("/css/styles.css")) {
                if (is != null) {
                    props.load(is);
                    loaded = true;
                }
            } catch (Exception e) {
                System.err.println("Error loading custom styles from resources: " + e.getMessage());
            }
        }
        
        if (!loaded) {
            System.err.println("CSS file not found. Using default styles.");
            return;
        }
        
        // Apply loaded properties to constants
        try {
            String primaryColor = props.getProperty("primary_color");
            if (primaryColor != null) {
                PRIMARY_COLOR = Color.decode(primaryColor);
            }
            
            String secondaryColor = props.getProperty("secondary_color");
            if (secondaryColor != null) {
                SECONDARY_COLOR = Color.decode(secondaryColor);
            }
            
            String accentColor = props.getProperty("accent_color");
            if (accentColor != null) {
                ACCENT_COLOR = Color.decode(accentColor);
            }
            
            String backgroundColor = props.getProperty("background_color");
            if (backgroundColor != null) {
                BACKGROUND_COLOR = Color.decode(backgroundColor);
            }
            
            String panelColor = props.getProperty("panel_color");
            if (panelColor != null) {
                PANEL_COLOR = Color.decode(panelColor);
            }
            
            String textColor = props.getProperty("text_color");
            if (textColor != null) {
                TEXT_COLOR = Color.decode(textColor);
            }
            
            String textSecondaryColor = props.getProperty("text_secondary_color");
            if (textSecondaryColor != null) {
                TEXT_SECONDARY_COLOR = Color.decode(textSecondaryColor);
            }
            
            String successColor = props.getProperty("success_color");
            if (successColor != null) {
                SUCCESS_COLOR = Color.decode(successColor);
            }
            
            String warningColor = props.getProperty("warning_color");
            if (warningColor != null) {
                WARNING_COLOR = Color.decode(warningColor);
            }
            
            String errorColor = props.getProperty("error_color");
            if (errorColor != null) {
                ERROR_COLOR = Color.decode(errorColor);
            }
            
            System.out.println("Custom styles loaded successfully.");
        } catch (Exception e) {
            System.err.println("Error applying custom styles: " + e.getMessage());
        }
    }
    
    /**
     * Creates a default CSS file if it doesn't exist
     */
    private static void createDefaultCssFile() {
        try {
            // Create CSS directory if it doesn't exist
            Path cssDir = Paths.get(CSS_PATH);
            if (!Files.exists(cssDir)) {
                Files.createDirectories(cssDir);
            }
            
            // Check if the CSS file exists
            Path cssFile = Paths.get(CSS_PATH + "styles.css");
            if (!Files.exists(cssFile)) {
                // Create a default CSS file
                StringBuilder css = new StringBuilder();
                css.append("# This is the default styles.css file for SixStringMarket\n");
                css.append("# You can modify these values to customize the application's appearance\n\n");
                
                css.append("# Main colors\n");
                css.append("primary_color=#4267B2\n");
                css.append("secondary_color=#90CAF9\n");
                css.append("accent_color=#F06292\n");
                css.append("background_color=#FAFAFA\n");
                css.append("panel_color=#FFFFFF\n\n");
                
                css.append("# Text colors\n");
                css.append("text_color=#212121\n");
                css.append("text_secondary_color=#757575\n\n");
                
                css.append("# Functional colors\n");
                css.append("success_color=#4CAF50\n");
                css.append("warning_color=#FF9800\n");
                css.append("error_color=#D32F2F\n");
                
                // Write the default CSS file
                Files.write(cssFile, css.toString().getBytes());
                System.out.println("Default CSS file created at: " + cssFile.toAbsolutePath());
            }
        } catch (Exception e) {
            System.err.println("Error creating default CSS file: " + e.getMessage());
        }
    }
}