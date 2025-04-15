package com.sixstringmarket.util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Utility class for handling images and icons
 */
public class ImageHandler {
    
    // Directory for storing images
    private static final String IMAGES_DIR = "resources/images/";
    
    // Directory for application icons
    private static final String ICONS_DIR = "/icons/";
    
    // Maximum dimensions for images
    private static final int MAX_WIDTH = 800;
    private static final int MAX_HEIGHT = 600;
    
    // Cache for loaded icons
    private static final Map<String, ImageIcon> iconCache = new HashMap<>();
    
    /**
     * Creates all required directories for image storage if they don't exist
     */
    static {
        try {
            // Create main resources directory
            createDirectoryIfNotExists(IMAGES_DIR);
            
            // Create subdirectories
            createDirectoryIfNotExists(IMAGES_DIR + "guitars");
            createDirectoryIfNotExists(IMAGES_DIR + "users");
            createDirectoryIfNotExists("resources/icons");
        } catch (IOException e) {
            System.err.println("Error creating image directories: " + e.getMessage());
        }
    }
    
    /**
     * Creates a directory if it doesn't exist
     * @param dirPath Path to directory
     * @throws IOException if there's an error creating the directory
     */
    private static void createDirectoryIfNotExists(String dirPath) throws IOException {
        Path path = Paths.get(dirPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
            System.out.println("Created directory: " + dirPath);
        }
    }
    
    /**
     * Saves an image to the file system
     * @param sourcePath Path to the original image
     * @param subdir Subdirectory (e.g., "guitars", "users")
     * @return Path to the saved image or null if there's an error
     */
    public static String saveImage(String sourcePath, String subdir) {
        try {
            // Create directory if it doesn't exist
            String targetDir = IMAGES_DIR + subdir + "/";
            Path dirPath = Paths.get(targetDir);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
            
            // Generate unique filename
            String fileName = UUID.randomUUID().toString() + getFileExtension(sourcePath);
            String targetPath = targetDir + fileName;
            
            // Read the original image
            File sourceFile = new File(sourcePath);
            if (!sourceFile.exists()) {
                System.err.println("Source image file not found: " + sourcePath);
                return getDefaultImagePath(subdir);
            }
            
            BufferedImage originalImage = ImageIO.read(sourceFile);
            if (originalImage == null) {
                System.err.println("Could not read image file: " + sourcePath);
                return getDefaultImagePath(subdir);
            }
            
            // Resize the image if necessary
            BufferedImage resizedImage = resizeImage(originalImage);
            
            // Save the image
            String extension = getFileExtension(sourcePath).substring(1); // remove the dot
            File targetFile = new File(targetPath);
            ImageIO.write(resizedImage, extension, targetFile);
            
            return subdir + "/" + fileName;
            
        } catch (IOException e) {
            System.err.println("Error saving image: " + e.getMessage());
            return getDefaultImagePath(subdir);
        }
    }
    
    /**
     * Returns a path to a default image for the given subdirectory
     * @param subdir Subdirectory
     * @return Path to default image
     */
    private static String getDefaultImagePath(String subdir) {
        if ("guitars".equals(subdir)) {
            return "guitars/default_guitar.png";
        } else if ("users".equals(subdir)) {
            return "users/default_user.png";
        } else {
            return "default.png";
        }
    }
    
    /**
     * Deletes an image from the file system
     * @param imagePath Path to the image to delete
     * @return true if the operation is successful, false otherwise
     */
    public static boolean deleteImage(String imagePath) {
        if (imagePath == null || imagePath.trim().isEmpty()) {
            return false;
        }
        
        try {
            Path path = Paths.get(IMAGES_DIR + imagePath);
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            System.err.println("Error deleting image: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Loads an image from the file system
     * @param imagePath Path to the image
     * @return BufferedImage object or null if there's an error
     */
    public static BufferedImage loadImage(String imagePath) {
        if (imagePath == null || imagePath.trim().isEmpty()) {
            return createDefaultImage();
        }
        
        try {
            // Try loading from full path first
            Path fullPath = Paths.get(IMAGES_DIR + imagePath);
            if (Files.exists(fullPath)) {
                BufferedImage img = ImageIO.read(fullPath.toFile());
                if (img != null) {
                    return img;
                }
            }
            
            // Try loading as a resource
            InputStream is = ImageHandler.class.getResourceAsStream("/" + imagePath);
            if (is != null) {
                BufferedImage img = ImageIO.read(is);
                if (img != null) {
                    return img;
                }
            }
            
            // Try loading from resources directory
            Path resourcePath = Paths.get("resources/" + imagePath);
            if (Files.exists(resourcePath)) {
                BufferedImage img = ImageIO.read(resourcePath.toFile());
                if (img != null) {
                    return img;
                }
            }
            
            // If we get here, we couldn't load the image - create a default one
            System.err.println("Could not load image from: " + imagePath);
            return createDefaultImage();
            
        } catch (IOException e) {
            System.err.println("Error loading image: " + e.getMessage());
            return createDefaultImage();
        }
    }
    
    /**
     * Creates a default image for when an image can't be loaded
     * @return A default image
     */
    private static BufferedImage createDefaultImage() {
        // Create a simple placeholder image
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        
        // Fill background
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 200, 200);
        
        // Draw border
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawRect(0, 0, 199, 199);
        
        // Draw an "X" to indicate no image
        g2d.setColor(Color.GRAY);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(50, 50, 150, 150);
        g2d.drawLine(150, 50, 50, 150);
        
        // Add text
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("No Image", 60, 110);
        
        g2d.dispose();
        return image;
    }
    
    /**
     * Loads an icon from the application resources
     * @param iconName Icon name (without path)
     * @return ImageIcon object or null if there's an error
     */
    public static ImageIcon loadIcon(String iconName) {
        // Check the cache
        if (iconCache.containsKey(iconName)) {
            return iconCache.get(iconName);
        }
        
        // Try to load from various locations
        ImageIcon icon = null;
        
        // 1. Try loading from resources (embedded in JAR file)
        URL iconURL = ImageHandler.class.getResource(ICONS_DIR + iconName);
        if (iconURL != null) {
            icon = new ImageIcon(iconURL);
        }
        
        // 2. Try loading from project's resource directory
        if (icon == null || icon.getImageLoadStatus() != MediaTracker.COMPLETE) {
            try {
                File file = new File("resources/icons/" + iconName);
                if (file.exists()) {
                    icon = new ImageIcon(file.getAbsolutePath());
                }
            } catch (Exception e) {
                // Ignore and continue to next attempt
            }
        }
        
        // 3. Try loading from src/icons or icons directory
        if (icon == null || icon.getImageLoadStatus() != MediaTracker.COMPLETE) {
            try {
                File file = new File("src/icons/" + iconName);
                if (file.exists()) {
                    icon = new ImageIcon(file.getAbsolutePath());
                } else {
                    file = new File("icons/" + iconName);
                    if (file.exists()) {
                        icon = new ImageIcon(file.getAbsolutePath());
                    }
                }
            } catch (Exception e) {
                // Ignore and continue to next attempt
            }
        }
        
        // 4. Create a generated icon if we can't find it
        if (icon == null || icon.getImageLoadStatus() != MediaTracker.COMPLETE) {
            icon = createDefaultIcon(iconName);
            System.out.println("Using generated icon for: " + iconName);
        }
        
        // Add to cache for future use
        iconCache.put(iconName, icon);
        
        return icon;
    }
    
    /**
     * Loads an icon with a specific size
     * @param iconName Icon name
     * @param width Desired width
     * @param height Desired height
     * @return ImageIcon object with the specified size
     */
    public static ImageIcon loadIcon(String iconName, int width, int height) {
        ImageIcon originalIcon = loadIcon(iconName);
        if (originalIcon != null) {
            Image img = originalIcon.getImage();
            Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(resizedImg);
        }
        return null;
    }
    
    /**
     * Creates a generated icon with initials
     * @param iconName Icon name
     * @return ImageIcon object
     */
    private static ImageIcon createDefaultIcon(String iconName) {
        // Create a colored icon with a symbol
        int size = 32;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Determine color based on name
        int hash = iconName.hashCode();
        Color bgColor = new Color(
            Math.abs(hash) % 200 + 55,
            Math.abs(hash / 256) % 200 + 55,
            Math.abs(hash / 65536) % 200 + 55
        );
        
        // Draw background
        g2d.setColor(bgColor);
        g2d.fillRoundRect(0, 0, size, size, 10, 10);
        
        // Draw initial or symbol
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 16));
        String initial = String.valueOf(iconName.charAt(0)).toUpperCase();
        
        // Center the text
        FontMetrics fm = g2d.getFontMetrics();
        int textX = (size - fm.stringWidth(initial)) / 2;
        int textY = (size - fm.getHeight()) / 2 + fm.getAscent();
        
        g2d.drawString(initial, textX, textY);
        g2d.dispose();
        
        return new ImageIcon(image);
    }
    
    /**
     * Resizes an image, preserving aspect ratio
     * @param originalImage The original image
     * @return The resized image
     */
    private static BufferedImage resizeImage(BufferedImage originalImage) {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        
        // If the image is smaller than the maximum dimensions, return it unchanged
        if (originalWidth <= MAX_WIDTH && originalHeight <= MAX_HEIGHT) {
            return originalImage;
        }
        
        // Calculate new dimensions, preserving aspect ratio
        int newWidth, newHeight;
        
        if (originalWidth > originalHeight) {
            // If width is greater than height
            newWidth = MAX_WIDTH;
            newHeight = (int) (originalHeight * ((double) MAX_WIDTH / originalWidth));
        } else {
            // If height is greater than width
            newHeight = MAX_HEIGHT;
            newWidth = (int) (originalWidth * ((double) MAX_HEIGHT / originalHeight));
        }
        
        // Create a new image with the resized dimensions
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, originalImage.getType());
        Graphics2D g = resizedImage.createGraphics();
        
        // Draw the resized image
        g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g.dispose();
        
        return resizedImage;
    }
    
    /**
     * Extracts the file extension from a path
     * @param filePath Path to the file
     * @return The file extension (with the dot)
     */
    private static String getFileExtension(String filePath) {
        int lastDotIndex = filePath.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return ".jpg"; // default
        }
        
        String extension = filePath.substring(lastDotIndex).toLowerCase();
        
        // Check for supported extensions
        if (extension.equals(".jpg") || extension.equals(".jpeg") || 
            extension.equals(".png") || extension.equals(".gif")) {
            return extension;
        } else {
            return ".jpg"; // default
        }
    }
}