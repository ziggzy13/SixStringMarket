package com.sixstringmarket.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 * Utility class to initialize all required resources for the application
 */
public class ResourceInitializer {
    
    /**
     * Initializes all required resources
     * @return true if initialization is successful, false otherwise
     */
    public static boolean initialize() {
        try {
            // Create required directories
            createDirectories();
            
            // Create default images
            createDefaultImages();
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Error initializing resources: " + e.getMessage(), 
                "Initialization Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Creates all required directories if they don't exist
     */
    private static void createDirectories() throws Exception {
        // Main directories
        createDirectory("resources");
        createDirectory("resources/images");
        createDirectory("resources/icons");
        createDirectory("resources/config");
        createDirectory("resources/css");
        
        // Subdirectories
        createDirectory("resources/images/guitars");
        createDirectory("resources/images/users");
    }
    
    /**
     * Creates a directory if it doesn't exist
     */
    private static void createDirectory(String dirPath) throws Exception {
        Path path = Paths.get(dirPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
            System.out.println("Created directory: " + dirPath);
        }
    }
    
    /**
     * Creates default images for the application
     */
    private static void createDefaultImages() throws Exception {
        // Create default guitar image
        createDefaultGuitarImage();
        
        // Create default user image
        createDefaultUserImage();
        
        // Create application logo
        createLogoImage();
        
        // Create icon images
        createIconImage("user.png", new Color(66, 103, 178), "👤");
        createIconImage("guitar.png", new Color(240, 98, 146), "🎸");
        createIconImage("cart.png", new Color(76, 175, 80), "🛒");
        createIconImage("save.png", new Color(66, 103, 178), "💾");
        createIconImage("search.png", new Color(117, 117, 117), "🔍");
        createIconImage("settings.png", new Color(33, 33, 33), "⚙️");
    }
    
    /**
     * Creates a default guitar image
     */
    private static void createDefaultGuitarImage() throws Exception {
        File file = new File("resources/images/guitars/default_guitar.png");
        if (!file.exists()) {
            // Create image
            BufferedImage image = new BufferedImage(300, 300, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = image.createGraphics();
            
            // Enable anti-aliasing
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Fill background
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, 300, 300);
            
            // Draw guitar
            g2d.setColor(new Color(200, 200, 200));
            
            // Guitar body
            g2d.fillOval(100, 150, 100, 130);
            
            // Guitar neck
            g2d.fillRect(145, 50, 10, 110);
            
            // Guitar head
            g2d.fillRect(135, 30, 30, 20);
            
            // Sound hole
            g2d.setColor(Color.WHITE);
            g2d.fillOval(130, 190, 40, 40);
            
            // Add text
            g2d.setColor(Color.DARK_GRAY);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            g2d.drawString("Default Guitar Image", 80, 100);
            
            g2d.dispose();
            
            // Save image
            ImageIO.write(image, "PNG", file);
            System.out.println("Created default guitar image");
        }
    }
    
    /**
     * Creates a default user image
     */
    private static void createDefaultUserImage() throws Exception {
        File file = new File("resources/images/users/default_user.png");
        if (!file.exists()) {
            // Create image
            BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = image.createGraphics();
            
            // Enable anti-aliasing
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Fill background with light gray
            g2d.setColor(new Color(240, 240, 240));
            g2d.fillRect(0, 0, 200, 200);
            
            // Draw user silhouette
            g2d.setColor(new Color(200, 200, 200));
            
            // Head
            g2d.fillOval(75, 40, 50, 50);
            
            // Body
            g2d.fillRect(75, 90, 50, 70);
            
            g2d.dispose();
            
            // Save image
            ImageIO.write(image, "PNG", file);
            System.out.println("Created default user image");
        }
    }
    
    /**
     * Creates the application logo
     */
    private static void createLogoImage() throws Exception {
        File file = new File("resources/icons/logo.png");
        if (!file.exists()) {
            // Create image
            BufferedImage image = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = image.createGraphics();
            
            // Enable anti-aliasing
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Fill background with primary color
            g2d.setColor(new Color(66, 103, 178));
            g2d.fillOval(0, 0, 128, 128);
            
            // Draw guitar silhouette
            g2d.setColor(Color.WHITE);
            
            // Guitar body
            g2d.fillOval(39, 64, 50, 50);
            
            // Guitar neck
            g2d.fillRect(59, 24, 10, 40);
            
            // Guitar head
            g2d.fillRect(54, 14, 20, 10);
            
            // Sound hole
            g2d.setColor(new Color(66, 103, 178));
            g2d.fillOval(49, 74, 30, 30);
            
            g2d.dispose();
            
            // Save image
            ImageIO.write(image, "PNG", file);
            System.out.println("Created logo image");
        }
    }
    
    /**
     * Creates an icon image with specified color and symbol
     */
    private static void createIconImage(String filename, Color color, String symbol) throws Exception {
        File file = new File("resources/icons/" + filename);
        if (!file.exists()) {
            // Create image
            BufferedImage image = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = image.createGraphics();
            
            // Enable anti-aliasing
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Fill background with color
            g2d.setColor(color);
            g2d.fillOval(0, 0, 32, 32);
            
            // Draw symbol
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
            
            // Center the symbol
            java.awt.FontMetrics fm = g2d.getFontMetrics();
            int x = (32 - fm.stringWidth(symbol)) / 2;
            int y = (32 - fm.getHeight()) / 2 + fm.getAscent();
            
            g2d.drawString(symbol, x, y);
            
            g2d.dispose();
            
            // Save image
            ImageIO.write(image, "PNG", file);
            System.out.println("Created icon: " + filename);
        }
    }
}