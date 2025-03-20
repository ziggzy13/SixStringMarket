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
 * Помощен клас за работа със снимки и икони
 */
public class ImageHandler {
    
    // Директория за съхранение на снимки
    private static final String IMAGES_DIR = "resources/images/";
    
    // Директория за икони на приложението
    private static final String ICONS_DIR = "/icons/";
    
    // Максимални размери на снимките
    private static final int MAX_WIDTH = 800;
    private static final int MAX_HEIGHT = 600;
    
    // Кеш за зареждане на икони
    private static final Map<String, ImageIcon> iconCache = new HashMap<>();
    
    /**
     * Записва снимка във файловата система
     * @param sourcePath Път до оригиналната снимка
     * @param subdir Поддиректория (напр. "guitars", "users")
     * @return Път до записаната снимка или null при грешка
     */
    public static String saveImage(String sourcePath, String subdir) {
        try {
            // Създаване на директорията, ако не съществува
            String targetDir = IMAGES_DIR + subdir + "/";
            Path dirPath = Paths.get(targetDir);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
            
            // Генериране на уникално име на файла
            String fileName = UUID.randomUUID().toString() + getFileExtension(sourcePath);
            String targetPath = targetDir + fileName;
            
            // Четене на оригиналното изображение
            BufferedImage originalImage = ImageIO.read(new File(sourcePath));
            
            // Оразмеряване на изображението, ако е необходимо
            BufferedImage resizedImage = resizeImage(originalImage);
            
            // Записване на изображението
            String extension = getFileExtension(sourcePath).substring(1); // премахване на точката
            ImageIO.write(resizedImage, extension, new File(targetPath));
            
            return subdir + "/" + fileName;
            
        } catch (IOException e) {
            System.err.println("Грешка при запазване на изображение: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Изтрива снимка от файловата система
     * @param imagePath Път до снимката за изтриване
     * @return true ако операцията е успешна, false в противен случай
     */
    public static boolean deleteImage(String imagePath) {
        if (imagePath == null || imagePath.trim().isEmpty()) {
            return false;
        }
        
        try {
            Path path = Paths.get(IMAGES_DIR + imagePath);
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            System.err.println("Грешка при изтриване на изображение: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Зарежда снимка от файловата система
     * @param imagePath Път до снимката
     * @return BufferedImage обект или null при грешка
     */
    public static BufferedImage loadImage(String imagePath) {
        if (imagePath == null || imagePath.trim().isEmpty()) {
            return null;
        }
        
        try {
            Path path = Paths.get(IMAGES_DIR + imagePath);
            if (Files.exists(path)) {
                return ImageIO.read(path.toFile());
            } else {
                // Опит за зареждане от ресурси
                InputStream is = ImageHandler.class.getResourceAsStream("/" + imagePath);
                if (is != null) {
                    return ImageIO.read(is);
                }
            }
            return null;
        } catch (IOException e) {
            System.err.println("Грешка при зареждане на изображение: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Зарежда икона от ресурсите на приложението
     * @param iconName Име на иконата (без път)
     * @return ImageIcon обект или null при грешка
     */
    public static ImageIcon loadIcon(String iconName) {
        // Проверка в кеша
        if (iconCache.containsKey(iconName)) {
            return iconCache.get(iconName);
        }
        
        // Опит за зареждане от различни места
        ImageIcon icon = null;
        
        // 1. Опит за зареждане от ресурсите (вградени в JAR файла)
        URL iconURL = ImageHandler.class.getResource(ICONS_DIR + iconName);
        if (iconURL != null) {
            icon = new ImageIcon(iconURL);
        }
        
        // 2. Опит за зареждане от директорията за ресурси на проекта
        if (icon == null || icon.getImageLoadStatus() != MediaTracker.COMPLETE) {
            try {
                File file = new File("resources/icons/" + iconName);
                if (file.exists()) {
                    icon = new ImageIcon(file.getAbsolutePath());
                }
            } catch (Exception e) {
                // Игнориране и продължаване към следващия опит
            }
        }
        
        // 3. Опит за зареждане от директорията src/icons или icons
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
                // Игнориране и продължаване към следващия опит
            }
        }
        
        // 4. Създаване на генерирана икона, ако не може да се намери
        if (icon == null || icon.getImageLoadStatus() != MediaTracker.COMPLETE) {
            icon = createDefaultIcon(iconName);
            System.out.println("Използва се генерирана икона за: " + iconName);
        }
        
        // Добавяне в кеша за бъдеща употреба
        iconCache.put(iconName, icon);
        
        return icon;
    }
    
    /**
     * Зарежда икона с определен размер
     * @param iconName Име на иконата
     * @param width Желана ширина
     * @param height Желана височина
     * @return ImageIcon обект с указания размер
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
     * Създава генерирана икона с инициали
     * @param iconName Име на иконата
     * @return ImageIcon обект
     */
    private static ImageIcon createDefaultIcon(String iconName) {
        // Създаване на цветна икона със символ
        int size = 32;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // Включване на anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Определяне на цвят на базата на името
        int hash = iconName.hashCode();
        Color bgColor = new Color(
            Math.abs(hash) % 200 + 55,
            Math.abs(hash / 256) % 200 + 55,
            Math.abs(hash / 65536) % 200 + 55
        );
        
        // Рисуване на фона
        g2d.setColor(bgColor);
        g2d.fillRoundRect(0, 0, size, size, 10, 10);
        
        // Рисуване на инициал или символ
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 16));
        String initial = String.valueOf(iconName.charAt(0)).toUpperCase();
        
        // Центриране на текста
        FontMetrics fm = g2d.getFontMetrics();
        int textX = (size - fm.stringWidth(initial)) / 2;
        int textY = (size - fm.getHeight()) / 2 + fm.getAscent();
        
        g2d.drawString(initial, textX, textY);
        g2d.dispose();
        
        return new ImageIcon(image);
    }
    
    /**
     * Преоразмерява изображение, запазвайки пропорциите
     * @param originalImage Оригиналното изображение
     * @return Преоразмереното изображение
     */
    private static BufferedImage resizeImage(BufferedImage originalImage) {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        
        // Ако изображението е по-малко от максималните размери, връщаме го без промяна
        if (originalWidth <= MAX_WIDTH && originalHeight <= MAX_HEIGHT) {
            return originalImage;
        }
        
        // Изчисляване на новите размери, запазвайки пропорциите
        int newWidth, newHeight;
        
        if (originalWidth > originalHeight) {
            // Ако ширината е по-голяма от височината
            newWidth = MAX_WIDTH;
            newHeight = (int) (originalHeight * ((double) MAX_WIDTH / originalWidth));
        } else {
            // Ако височината е по-голяма от ширината
            newHeight = MAX_HEIGHT;
            newWidth = (int) (originalWidth * ((double) MAX_HEIGHT / originalHeight));
        }
        
        // Създаване на ново изображение с преоразмерените размери
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, originalImage.getType());
        Graphics2D g = resizedImage.createGraphics();
        
        // Рисуване на преоразмереното изображение
        g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g.dispose();
        
        return resizedImage;
    }
    
    /**
     * Извлича разширението на файл от пътя
     * @param filePath Път до файла
     * @return Разширението на файла (с точката)
     */
    private static String getFileExtension(String filePath) {
        int lastDotIndex = filePath.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return ".jpg"; // по подразбиране
        }
        
        String extension = filePath.substring(lastDotIndex).toLowerCase();
        
        // Проверка за поддържани разширения
        if (extension.equals(".jpg") || extension.equals(".jpeg") || 
            extension.equals(".png") || extension.equals(".gif")) {
            return extension;
        } else {
            return ".jpg"; // по подразбиране
        }
    }
}