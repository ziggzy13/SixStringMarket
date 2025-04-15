package com.sixstringmarket.util;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * StyleManager loads CSS-like styles and applies them to Swing components
 */
public class StyleManager {
	private static final String CSS_PATH = "resources/css/modern-style.css";
    private static final String PROPERTIES_PATH = "resources/css/modern-style.css";
    
    private static Map<String, String> cssProperties = new HashMap<>();
    private static boolean isInitialized = false;
    
    // Application colors
    public static Color PRIMARY_COLOR;
    public static Color SECONDARY_COLOR;
    public static Color ACCENT_COLOR = new Color(0, 150, 136);
    public static Color BACKGROUND_COLOR;
    public static Color CARD_BG_COLOR;
    public static Color TEXT_COLOR;
    public static Color TEXT_SECONDARY_COLOR;
    
    // UI dimensions
    public static int PADDING = 10;
    public static int CORNER_RADIUS = 8;
 // Font sizes
    public static int FONT_SIZE_SMALL = 12;
    public static int FONT_SIZE_DEFAULT = 14;
    public static int FONT_SIZE_LARGE = 16;
    public static int FONT_SIZE_TITLE = 20;
    // Functional colors
    public static Color SUCCESS_COLOR;
    public static Color WARNING_COLOR;
    public static Color ERROR_COLOR;
    public static Color INFO_COLOR;
    
    // Field colors
    public static Color FIELD_BG_COLOR;
    public static Color FIELD_BORDER_COLOR;
    public static Color FIELD_FOCUS_COLOR;
    
    // Button colors
    public static Color BUTTON_PRIMARY_COLOR;
    public static Color BUTTON_SECONDARY_COLOR;
    public static Color BUTTON_DISABLED_COLOR;
    
    // Effects
    public static Color HOVER_HIGHLIGHT_COLOR;
    public static Color SHADOW_COLOR;
    
    // Spacing
    public static int PADDING_SMALL;
    public static int PADDING_MEDIUM;
    public static int PADDING_LARGE;
    public static int BORDER_RADIUS;
    
    // Fonts
    public static Font DEFAULT_FONT;
    public static Font TITLE_FONT;
    public static Font SUBTITLE_FONT;
    public static Font SMALL_FONT;
    
    // Borders
    public static Border DEFAULT_BORDER;
    public static Border CARD_BORDER;
    public static Border FIELD_BORDER;
    public static Border FIELD_FOCUS_BORDER;
    
    /**
     * Initialize the style manager
     */
    public static void initialize() {
        if (isInitialized) {
            return;
        }
        
        loadCssProperties();
        initializeColors();
        initializeDimensions();
        initializeFonts();
        initializeBorders();
        applyToUIManager();
        
        isInitialized = true;
    }
    
    /**
     * Load CSS properties from file
     */
    private static void loadCssProperties() {
        try {
            InputStream is = StyleManager.class.getResourceAsStream("/css/modern-style.css");
            if (is == null) {
                System.err.println("CSS file not found. Using default styles.");
                loadDefaultProperties();
                return;
            }
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder css = new StringBuilder();
            String line;
            
            while ((line = reader.readLine()) != null) {
                css.append(line).append("\n");
            }
            
            reader.close();
            
            // Extract root CSS variables
            Pattern pattern = Pattern.compile("--([\\w-]+):\\s*([^;]+);");
            Matcher matcher = pattern.matcher(css.toString());
            
            while (matcher.find()) {
                String key = matcher.group(1);
                String value = matcher.group(2).trim();
                cssProperties.put(key, value);
            }
            
        } catch (Exception e) {
            System.err.println("Error loading CSS: " + e.getMessage());
            loadDefaultProperties();
        }
    }
    private void createDefaultCssFile() {
        try {
            // Create default styles directory
            File styleDir = new File("resources/styles/modern-style.css");
            if (!styleDir.exists()) {
                styleDir.mkdirs();
            }
            
            // Create default properties file
            java.io.PrintWriter propWriter = new java.io.PrintWriter(PROPERTIES_PATH);
            propWriter.println("# SixStringMarket default style properties");
            propWriter.println("# Generated on " + new java.util.Date());
            propWriter.println();
            propWriter.println("# Colors (in hex format)");
            propWriter.println("color.primary=" + colorToHex(PRIMARY_COLOR));
            propWriter.println("color.secondary=" + colorToHex(SECONDARY_COLOR));
            propWriter.println("color.accent=" + colorToHex(ACCENT_COLOR));
            propWriter.println("color.background=" + colorToHex(BACKGROUND_COLOR));
            propWriter.println("color.card=" + colorToHex(CARD_BG_COLOR));
            propWriter.println("color.text=" + colorToHex(TEXT_COLOR));
            propWriter.println("color.text.secondary=" + colorToHex(TEXT_SECONDARY_COLOR));
            propWriter.println();
            propWriter.println("color.success=" + colorToHex(SUCCESS_COLOR));
            propWriter.println("color.warning=" + colorToHex(WARNING_COLOR));
            propWriter.println("color.error=" + colorToHex(ERROR_COLOR));
            propWriter.println();
            propWriter.println("# Dimensions");
            propWriter.println("dimension.padding=" + PADDING);
            propWriter.println("dimension.corner.radius=" + CORNER_RADIUS);
            propWriter.println();
            propWriter.println("# Font sizes");
            propWriter.println("font.size.small=" + FONT_SIZE_SMALL);
            propWriter.println("font.size.default=" + FONT_SIZE_DEFAULT);
            propWriter.println("font.size.large=" + FONT_SIZE_LARGE);
            propWriter.println("font.size.title=" + FONT_SIZE_TITLE);
            propWriter.close();
            
            // Create basic CSS file
            java.io.PrintWriter cssWriter = new java.io.PrintWriter(CSS_PATH);
            cssWriter.println("/* SixStringMarket default CSS */");
            cssWriter.println("/* Generated on " + new java.util.Date() + " */");
            cssWriter.println();
            cssWriter.println(":root {");
            cssWriter.println("  --primary-color: " + colorToHex(PRIMARY_COLOR) + ";");
            cssWriter.println("  --secondary-color: " + colorToHex(SECONDARY_COLOR) + ";");
            cssWriter.println("  --accent-color: " + colorToHex(ACCENT_COLOR) + ";");
            cssWriter.println("  --background-color: " + colorToHex(BACKGROUND_COLOR) + ";");
            cssWriter.println("  --card-bg-color: " + colorToHex(CARD_BG_COLOR) + ";");
            cssWriter.println("  --text-color: " + colorToHex(TEXT_COLOR) + ";");
            cssWriter.println("  --text-secondary-color: " + colorToHex(TEXT_SECONDARY_COLOR) + ";");
            cssWriter.println("  --success-color: " + colorToHex(SUCCESS_COLOR) + ";");
            cssWriter.println("  --warning-color: " + colorToHex(WARNING_COLOR) + ";");
            cssWriter.println("  --error-color: " + colorToHex(ERROR_COLOR) + ";");
            cssWriter.println("  --padding: " + PADDING + "px;");
            cssWriter.println("  --corner-radius: " + CORNER_RADIUS + "px;");
            cssWriter.println("}");
            cssWriter.println();
            cssWriter.println(".app-container {");
            cssWriter.println("  background-color: var(--background-color);");
            cssWriter.println("}");
            cssWriter.println();
            cssWriter.println(".card {");
            cssWriter.println("  background-color: var(--card-bg-color);");
            cssWriter.println("  border-radius: var(--corner-radius);");
            cssWriter.println("  padding: var(--padding);");
            cssWriter.println("}");
            cssWriter.close();
            
            System.out.println("Default style files created successfully");
        } catch (IOException e) {
            System.err.println("Error creating default style files: " + e.getMessage());
        }
    }
    private String colorToHex(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }
    
    /**
     * Parse hex color string to Color
     */
    private static Color parseColor(String hexColor) {
        if (hexColor == null || !hexColor.startsWith("#")) {
            return Color.BLACK; // Default
        }
        
        try {
            hexColor = hexColor.substring(1); // Remove # character
            return new Color(
                Integer.parseInt(hexColor.substring(0, 2), 16),
                Integer.parseInt(hexColor.substring(2, 4), 16),
                Integer.parseInt(hexColor.substring(4, 6), 16)
            );
        } catch (Exception e) {
            System.err.println("Error parsing color: " + hexColor);
            return Color.BLACK; // Default
        }
    }
    
    /**
     * Parse integer with default value
     */
    
    
    /**
     * Load default properties if CSS file is not found
     */
    private static void loadDefaultProperties() {
        // Default colors
        cssProperties.put("primary", "#25283D");
        cssProperties.put("secondary", "#BA5050");
        cssProperties.put("background", "#1C1E2E");
        cssProperties.put("card-bg", "#2D3047");
        cssProperties.put("text", "#E6E6EB");
        cssProperties.put("text-secondary", "#AAAAB4");
        
        cssProperties.put("success", "#48BB78");
        cssProperties.put("warning", "#F6AD55");
        cssProperties.put("error", "#E74C3C");
        cssProperties.put("info", "#5897FB");
        
        cssProperties.put("field-bg", "#353853");
        cssProperties.put("field-border", "#4C4F69");
        cssProperties.put("field-focus", "#BA5050");
        
        cssProperties.put("button-primary", "#BA5050");
        cssProperties.put("button-secondary", "#454966");
        cssProperties.put("button-disabled", "#5A5D78");
        
        cssProperties.put("hover-highlight", "rgba(255, 255, 255, 0.15)");
        cssProperties.put("shadow", "rgba(10, 10, 15, 0.4)");
        
        // Default dimensions
        cssProperties.put("padding-small", "5px");
        cssProperties.put("padding-medium", "10px");
        cssProperties.put("padding-large", "20px");
        cssProperties.put("border-radius", "8px");
    }
    
    /**
     * Initialize colors from CSS properties
     */
    private static void initializeColors() {
        PRIMARY_COLOR = parseColor(cssProperties.get("primary"));
        SECONDARY_COLOR = parseColor(cssProperties.get("secondary"));
        BACKGROUND_COLOR = parseColor(cssProperties.get("background"));
        CARD_BG_COLOR = parseColor(cssProperties.get("card-bg"));
        TEXT_COLOR = parseColor(cssProperties.get("text"));
        TEXT_SECONDARY_COLOR = parseColor(cssProperties.get("text-secondary"));
        
        SUCCESS_COLOR = parseColor(cssProperties.get("success"));
        WARNING_COLOR = parseColor(cssProperties.get("warning"));
        ERROR_COLOR = parseColor(cssProperties.get("error"));
        INFO_COLOR = parseColor(cssProperties.get("info"));
        
        FIELD_BG_COLOR = parseColor(cssProperties.get("field-bg"));
        FIELD_BORDER_COLOR = parseColor(cssProperties.get("field-border"));
        FIELD_FOCUS_COLOR = parseColor(cssProperties.getOrDefault("field-focus", cssProperties.get("secondary")));
        
        BUTTON_PRIMARY_COLOR = parseColor(cssProperties.getOrDefault("button-primary", cssProperties.get("secondary")));
        BUTTON_SECONDARY_COLOR = parseColor(cssProperties.get("button-secondary"));
        BUTTON_DISABLED_COLOR = parseColor(cssProperties.get("button-disabled"));
        
        // Parse colors with alpha
        HOVER_HIGHLIGHT_COLOR = parseColorWithAlpha(cssProperties.get("hover-highlight"));
        SHADOW_COLOR = parseColorWithAlpha(cssProperties.get("shadow"));
    }
    
    /**
     * Initialize dimensions from CSS properties
     */
    private static void initializeDimensions() {
        PADDING_SMALL = parseDimension(cssProperties.get("padding-small"));
        PADDING_MEDIUM = parseDimension(cssProperties.get("padding-medium"));
        PADDING_LARGE = parseDimension(cssProperties.get("padding-large"));
        BORDER_RADIUS = parseDimension(cssProperties.get("border-radius"));
    }
    
    /**
     * Initialize fonts
     */
    private static void initializeFonts() {
        DEFAULT_FONT = new Font("Segoe UI", Font.PLAIN, 14);
        TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
        SUBTITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
        SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    }
    
    /**
     * Initialize borders
     */
    private static void initializeBorders() {
        DEFAULT_BORDER = new LineBorder(FIELD_BORDER_COLOR, 1, true);
        CARD_BORDER = new CompoundBorder(
            new LineBorder(darken(CARD_BG_COLOR, 0.1f), 1, true),
            new EmptyBorder(PADDING_MEDIUM, PADDING_MEDIUM, PADDING_MEDIUM, PADDING_MEDIUM)
        );
        FIELD_BORDER = new CompoundBorder(
            new LineBorder(FIELD_BORDER_COLOR, 1, true),
            new EmptyBorder(8, 8, 8, 8)
        );
        FIELD_FOCUS_BORDER = new CompoundBorder(
            new LineBorder(FIELD_FOCUS_COLOR, 1, true),
            new EmptyBorder(8, 8, 8, 8)
        );
    }
    
    /**
     * Apply styles to UIManager for consistent look
     */
    private static void applyToUIManager() {
        UIManager.put("Panel.background", BACKGROUND_COLOR);
        UIManager.put("Panel.foreground", TEXT_COLOR);
        
        UIManager.put("Label.font", DEFAULT_FONT);
        UIManager.put("Label.foreground", TEXT_COLOR);
        
        UIManager.put("Button.background", BUTTON_PRIMARY_COLOR);
        UIManager.put("Button.foreground", TEXT_COLOR);
        UIManager.put("Button.font", DEFAULT_FONT);
        UIManager.put("Button.focus", new Color(0, 0, 0, 0)); // Transparent focus
        
        UIManager.put("TextField.background", FIELD_BG_COLOR);
        UIManager.put("TextField.foreground", TEXT_COLOR);
        UIManager.put("TextField.caretForeground", TEXT_COLOR);
        UIManager.put("TextField.font", DEFAULT_FONT);
        
        UIManager.put("PasswordField.background", FIELD_BG_COLOR);
        UIManager.put("PasswordField.foreground", TEXT_COLOR);
        UIManager.put("PasswordField.caretForeground", TEXT_COLOR);
        UIManager.put("PasswordField.font", DEFAULT_FONT);
        
        UIManager.put("TextArea.background", FIELD_BG_COLOR);
        UIManager.put("TextArea.foreground", TEXT_COLOR);
        UIManager.put("TextArea.caretForeground", TEXT_COLOR);
        UIManager.put("TextArea.font", DEFAULT_FONT);
        
        UIManager.put("ComboBox.background", FIELD_BG_COLOR);
        UIManager.put("ComboBox.foreground", TEXT_COLOR);
        UIManager.put("ComboBox.font", DEFAULT_FONT);
        
        UIManager.put("List.background", FIELD_BG_COLOR);
        UIManager.put("List.foreground", TEXT_COLOR);
        UIManager.put("List.font", DEFAULT_FONT);
        
        UIManager.put("Table.background", CARD_BG_COLOR);
        UIManager.put("Table.foreground", TEXT_COLOR);
        UIManager.put("Table.gridColor", FIELD_BORDER_COLOR);
        UIManager.put("Table.font", DEFAULT_FONT);
        
        UIManager.put("TableHeader.background", PRIMARY_COLOR);
        UIManager.put("TableHeader.foreground", TEXT_COLOR);
        UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 14));
        
        UIManager.put("ScrollPane.background", BACKGROUND_COLOR);
        UIManager.put("ScrollPane.border", null);
    }
    
    /**
     * Parse color from CSS hex value
     */
    private int parseInt(String value, int defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    /**
     * Parse color with alpha channel
     */
    private static Color parseColorWithAlpha(String colorStr) {
        if (colorStr == null) {
            return new Color(0, 0, 0, 128); // Default semi-transparent black
        }
        
        colorStr = colorStr.trim();
        
        // Handle var() references
        if (colorStr.startsWith("var(--")) {
            String varName = colorStr.substring(5, colorStr.length() - 1);
            colorStr = cssProperties.get(varName);
            if (colorStr == null) {
                return new Color(0, 0, 0, 128);
            }
        }
        
        // RGB/RGBA color
        if (colorStr.startsWith("rgba(")) {
            colorStr = colorStr.substring(5, colorStr.length() - 1);
            String[] parts = colorStr.split(",");
            if (parts.length == 4) {
                int r = Integer.parseInt(parts[0].trim());
                int g = Integer.parseInt(parts[1].trim());
                int b = Integer.parseInt(parts[2].trim());
                float a = Float.parseFloat(parts[3].trim());
                return new Color(r, g, b, (int)(a * 255));
            }
        }
        
        return parseColor(colorStr);
    }
    
    /**
     * Parse dimension from CSS (e.g., "10px" to 10)
     */
    private static int parseDimension(String dimension) {
        if (dimension == null) {
            return 10; // Default fallback
        }
        
        dimension = dimension.trim();
        
        // Handle var() references
        if (dimension.startsWith("var(--")) {
            String varName = dimension.substring(5, dimension.length() - 1);
            dimension = cssProperties.get(varName);
            if (dimension == null) {
                return 10;
            }
        }
        
        // Remove px suffix
        if (dimension.endsWith("px")) {
            dimension = dimension.substring(0, dimension.length() - 2);
        }
        
        try {
            return Integer.parseInt(dimension);
        } catch (NumberFormatException e) {
            return 10;
        }
    }
    
    /**
     * Lighten a color by a given factor
     */
    public static Color lighten(Color color, float factor) {
        int r = Math.min(255, (int)(color.getRed() + (255 - color.getRed()) * factor));
        int g = Math.min(255, (int)(color.getGreen() + (255 - color.getGreen()) * factor));
        int b = Math.min(255, (int)(color.getBlue() + (255 - color.getBlue()) * factor));
        return new Color(r, g, b, color.getAlpha());
    }
    
    /**
     * Darken a color by a given factor
     */
    public static Color darken(Color color, float factor) {
        int r = Math.max(0, (int)(color.getRed() * (1 - factor)));
        int g = Math.max(0, (int)(color.getGreen() * (1 - factor)));
        int b = Math.max(0, (int)(color.getBlue() * (1 - factor)));
        return new Color(r, g, b, color.getAlpha());
    }

	public static StyleManager getInstance() {
		// TODO Auto-generated method stub
		return null;
	}
}