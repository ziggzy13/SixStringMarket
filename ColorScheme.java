package com.sixstringmarket.util;

import java.awt.Color;

/**
 * Defines the color scheme for the entire application
 * Uses a modern, high-contrast theme with accent colors
 */
public class ColorScheme {
    // Main application colors
    public static final Color PRIMARY = new Color(37, 40, 61);         // Deep navy blue
    public static final Color SECONDARY = new Color(186, 80, 80);      // Rich red
    public static final Color BACKGROUND = new Color(28, 30, 46);      // Dark blue-gray
    public static final Color CARD_BG = new Color(45, 48, 71);         // Slightly lighter than background
    
    // Text colors with high contrast to backgrounds
    public static final Color TEXT = new Color(230, 230, 235);         // Off-white for dark backgrounds
    public static final Color TEXT_SECONDARY = new Color(170, 170, 180); // Light gray for dark backgrounds
    public static final Color TEXT_ON_LIGHT = Color.BLACK;             // Black text for light backgrounds
    public static final Color TEXT_SECONDARY_ON_LIGHT = new Color(70, 70, 70); // Dark gray for light backgrounds
    
    // Functional colors
    public static final Color SUCCESS = new Color(72, 187, 120);       // Green
    public static final Color WARNING = new Color(246, 173, 85);       // Orange
    public static final Color ERROR = new Color(231, 76, 60);          // Red
    public static final Color INFO = new Color(88, 151, 251);          // Blue
    
    // Input fields
    public static final Color FIELD_BG = new Color(53, 56, 83);        // Lighter than card background
    public static final Color FIELD_BORDER = new Color(76, 79, 105);   // Subtle border
    public static final Color FIELD_FOCUS = SECONDARY;                 // Accent color when focused
    
    // Button colors
    public static final Color BUTTON_PRIMARY = SECONDARY;              // Primary buttons use accent color
    public static final Color BUTTON_SECONDARY = new Color(69, 73, 102); // Secondary buttons
    public static final Color BUTTON_DISABLED = new Color(90, 93, 120); // Disabled state
    
    // Transparent color (for non-opaque components)
    public static final Color TRANSPARENT = new Color(0, 0, 0, 0);
    
    // Hover effect colors
    public static final Color HOVER_HIGHLIGHT = new Color(255, 255, 255, 15); // Subtle white overlay
    
    // Shadows
    public static final Color SHADOW = new Color(10, 10, 15, 100);     // Subtle shadow
    
    /**
     * Returns an appropriate text color based on the background color
     * @param background The background color
     * @return White for dark backgrounds, black for light backgrounds
     */
    public static Color getTextColorForBackground(Color background) {
        // Calculate luminance (simplified formula)
        double luminance = (0.299 * background.getRed() + 0.587 * background.getGreen() + 0.114 * background.getBlue()) / 255;
        
        // Use black text on light backgrounds, white text on dark backgrounds
        return luminance > 0.5 ? Color.BLACK : Color.WHITE;
    }
    
    /**
     * Returns an appropriate secondary text color based on the background color
     * @param background The background color
     * @return Light gray for dark backgrounds, dark gray for light backgrounds
     */
    public static Color getSecondaryTextColorForBackground(Color background) {
        // Calculate luminance (simplified formula)
        double luminance = (0.299 * background.getRed() + 0.587 * background.getGreen() + 0.114 * background.getBlue()) / 255;
        
        // Use dark gray on light backgrounds, light gray on dark backgrounds
        return luminance > 0.5 ? TEXT_SECONDARY_ON_LIGHT : TEXT_SECONDARY;
    }
    
    /**
     * Returns a lighter version of the given color
     * @param color The color to lighten
     * @param amount Amount to lighten (0.0 to 1.0)
     * @return Lightened color
     */
    public static Color lighten(Color color, float amount) {
        int r = Math.min(255, (int)(color.getRed() + (255 - color.getRed()) * amount));
        int g = Math.min(255, (int)(color.getGreen() + (255 - color.getGreen()) * amount));
        int b = Math.min(255, (int)(color.getBlue() + (255 - color.getBlue()) * amount));
        return new Color(r, g, b, color.getAlpha());
    }
    
    /**
     * Returns a darker version of the given color
     * @param color The color to darken
     * @param amount Amount to darken (0.0 to 1.0)
     * @return Darkened color
     */
    public static Color darken(Color color, float amount) {
        int r = Math.max(0, (int)(color.getRed() * (1 - amount)));
        int g = Math.max(0, (int)(color.getGreen() * (1 - amount)));
        int b = Math.max(0, (int)(color.getBlue() * (1 - amount)));
        return new Color(r, g, b, color.getAlpha());
    }
}