package com.sixstringmarket.util;

import java.awt.Color;
import java.awt.Font;

/**
 * Central manager for application styling
 * Based on the modern-style.css file
 */
public class StyleManager {
    // Main application colors
    public static final Color PRIMARY_COLOR = new Color(37, 40, 61);     // Deep navy blue
    public static final Color SECONDARY_COLOR = new Color(186, 80, 80);  // Rich red
    public static final Color BACKGROUND_COLOR = new Color(28, 30, 46);  // Dark blue-gray
    public static final Color CARD_BG_COLOR = new Color(45, 48, 71);     // Slightly lighter than background
    public static final Color TEXT_COLOR = new Color(230, 230, 235);     // Off-white
    public static final Color TEXT_SECONDARY_COLOR = new Color(170, 170, 180); // Light gray
    
    // Functional colors
    public static final Color SUCCESS_COLOR = new Color(72, 187, 120);   // Green
    public static final Color WARNING_COLOR = new Color(246, 173, 85);   // Orange
    public static final Color ERROR_COLOR = new Color(231, 76, 60);      // Red
    public static final Color INFO_COLOR = new Color(88, 151, 251);      // Blue
    
    // Input fields
    public static final Color FIELD_BG_COLOR = new Color(53, 56, 83);    // Lighter than card background
    public static final Color FIELD_BORDER_COLOR = new Color(76, 79, 105); // Subtle border
    public static final Color FIELD_FOCUS_COLOR = SECONDARY_COLOR;       // Accent color when focused
    
    // Button colors
    public static final Color BUTTON_PRIMARY_COLOR = SECONDARY_COLOR;    // Primary buttons use accent color
    public static final Color BUTTON_SECONDARY_COLOR = new Color(69, 73, 102); // Secondary buttons
    public static final Color BUTTON_DISABLED_COLOR = new Color(90, 93, 120); // Disabled state
    
    // Spacing and dimensions
    public static final int PADDING_SMALL = 5;
    public static final int PADDING_MEDIUM = 10;
    public static final int PADDING_LARGE = 20;
    public static final int CORNER_RADIUS = 8;
    
    // Common fonts
    public static final Font DEFAULT_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BOLD_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    
    // Transparent color
    public static final Color TRANSPARENT = new Color(0, 0, 0, 0);
    
    /**
     * Lightens a color by the given factor
     * @param color The color to lighten
     * @param factor Amount to lighten (0.0 to 1.0)
     * @return Lightened color
     */
    public static Color lighten(Color color, float factor) {
        int r = Math.min(255, (int)(color.getRed() + (255 - color.getRed()) * factor));
        int g = Math.min(255, (int)(color.getGreen() + (255 - color.getGreen()) * factor));
        int b = Math.min(255, (int)(color.getBlue() + (255 - color.getBlue()) * factor));
        return new Color(r, g, b, color.getAlpha());
    }
    
    /**
     * Darkens a color by the given factor
     * @param color The color to darken
     * @param factor Amount to darken (0.0 to 1.0)
     * @return Darkened color
     */
    public static Color darken(Color color, float factor) {
        int r = Math.max(0, (int)(color.getRed() * (1 - factor)));
        int g = Math.max(0, (int)(color.getGreen() * (1 - factor)));
        int b = Math.max(0, (int)(color.getBlue() * (1 - factor)));
        return new Color(r, g, b, color.getAlpha());
    }
}