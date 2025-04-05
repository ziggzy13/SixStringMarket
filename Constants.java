package com.sixstringmarket.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * Class with constants and styles for the application design
 */
public class Constants {
    
    // Application title
    public static final String APP_TITLE = "SixStringMarket - Guitar buying and selling";
    
    // Application version
    public static final String APP_VERSION = "1.0.0";
    
    // Color scheme - modern and elegant
    public static final Color PRIMARY_COLOR = new Color(66, 103, 178);      // Blue (primary color)
    public static final Color SECONDARY_COLOR = new Color(144, 202, 249);   // Light blue
    public static final Color ACCENT_COLOR = new Color(240, 98, 146);       // Pink (accent)
    public static final Color BACKGROUND_COLOR = new Color(250, 250, 250);  // Almost white
    public static final Color PANEL_COLOR = new Color(255, 255, 255);       // White
    
    // Text colors with contrast to backgrounds
    public static final Color TEXT_COLOR = new Color(33, 33, 33);           // Dark gray
    public static final Color TEXT_SECONDARY_COLOR = new Color(117, 117, 117); // Medium gray
    
    // Text colors for dark backgrounds
    public static final Color TEXT_ON_DARK = new Color(240, 240, 240);      // Almost white
    public static final Color TEXT_SECONDARY_ON_DARK = new Color(200, 200, 200); // Light gray
    
    // Functional colors
    public static final Color SUCCESS_COLOR = new Color(76, 175, 80);       // Green
    public static final Color WARNING_COLOR = new Color(255, 152, 0);       // Orange
    public static final Color ERROR_COLOR = new Color(211, 47, 47);         // Red
    
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
    public static final String ICON_PATH = "resources/icons/";
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
    
    // Resource paths
    public static final String RESOURCES_PATH = "resources/";
    public static final String IMAGES_PATH = RESOURCES_PATH + "images/";
    public static final String CONFIG_PATH = RESOURCES_PATH + "config/";
    
    /**
     * Returns an appropriate text color based on the background color
     * @param background The background color
     * @return TEXT_COLOR for light backgrounds, TEXT_ON_DARK for dark backgrounds
     */
    public static Color getTextColorForBackground(Color background) {
        // Calculate luminance (simplified formula)
        double luminance = (0.299 * background.getRed() + 0.587 * background.getGreen() + 0.114 * background.getBlue()) / 255;
        
        // Use TEXT_COLOR on light backgrounds, TEXT_ON_DARK on dark backgrounds
        return luminance > 0.5 ? TEXT_COLOR : TEXT_ON_DARK;
    }
    
    /**
     * Returns an appropriate secondary text color based on the background color
     * @param background The background color
     * @return TEXT_SECONDARY_COLOR for light backgrounds, TEXT_SECONDARY_ON_DARK for dark backgrounds
     */
    public static Color getSecondaryTextColorForBackground(Color background) {
        // Calculate luminance (simplified formula)
        double luminance = (0.299 * background.getRed() + 0.587 * background.getGreen() + 0.114 * background.getBlue()) / 255;
        
        // Use TEXT_SECONDARY_COLOR on light backgrounds, TEXT_SECONDARY_ON_DARK on dark backgrounds
        return luminance > 0.5 ? TEXT_SECONDARY_COLOR : TEXT_SECONDARY_ON_DARK;
    }
}