package com.sixstringmarket.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Помощен клас за създаване на ресурсни директории и икони за приложението
 * Трябва да се изпълни еднократно при първоначално стартиране или инсталация на приложението
 */
public class AppIconCreator {
    
    public static void main(String[] args) {
        createResourceDirectories();
        createDefaultIcons();
        System.out.println("Готово. Всички ресурсни директории и икони са създадени успешно.");
    }
    
    /**
     * Създава ресурсните директории, ако не съществуват
     */
    private static void createResourceDirectories() {
        try {
            // Създаване на главната ресурсна директория
            Path resourcesDir = Paths.get("resources");
            if (!Files.exists(resourcesDir)) {
                Files.createDirectories(resourcesDir);
                System.out.println("Създадена е директория: resources");
            }
            
            // Създаване на поддиректории
            String[] subdirs = {"images", "icons", "config", "images/guitars", "images/users"};
            for (String subdir : subdirs) {
                Path path = Paths.get("resources", subdir);
                if (!Files.exists(path)) {
                    Files.createDirectories(path);
                    System.out.println("Създадена е директория: resources/" + subdir);
                }
            }
        } catch (IOException e) {
            System.err.println("Грешка при създаване на директории: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Създава основните икони за приложението
     */
    private static void createDefaultIcons() {
        // Икона на китара
        createGuitarIcon("resources/icons/guitar_icon.png", 64, 64);
        createGuitarIcon("resources/icons/guitar_small.png", 32, 32);
        
        // Лого
        createGuitarIcon("resources/icons/logo.png", 128, 128);
        
        // Други икони
        createCircleIcon("resources/icons/user.png", 32, new Color(66, 103, 178), "👤", 18);
        createCircleIcon("resources/icons/cart.png", 32, new Color(240, 98, 146), "🛒", 18);
        createCircleIcon("resources/icons/save.png", 32, new Color(76, 175, 80), "💾", 18);
        createCircleIcon("resources/icons/search.png", 32, new Color(103, 58, 183), "🔍", 18);
        createCircleIcon("resources/icons/settings.png", 32, new Color(33, 33, 33), "⚙️", 18);
    }
    
    /**
     * Създава икона на китара
     * @param filePath Път до файла
     * @param width Ширина
     * @param height Височина
     */
    private static void createGuitarIcon(String filePath, int width, int height) {
        try {
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = image.createGraphics();
            
            // Настройка за анти-алиасинг
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            
            // Цветове
            Color mainColor = new Color(66, 103, 178);
            Color secondaryColor = new Color(53, 92, 151);
            
            // Тяло на китарата (елипса)
            g2d.setColor(mainColor);
            g2d.fillOval(width/4, height/3, width/2, height*2/3);
            
            // Грифът на китарата
            g2d.setColor(secondaryColor);
            g2d.fillRect(width*3/8, 0, width/4, height/2);
            
            // Струни
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(1.5f));
            for (int i = 0; i < 6; i++) {
                int yPos = height/4 + i * height/30;
                g2d.drawLine(width*3/8, yPos, width*5/8, yPos);
            }
            
            // Резонаторен отвор
            g2d.setColor(new Color(240, 240, 240));
            g2d.fillOval(width*3/8, height/2, width/4, width/4);
            
            g2d.dispose();
            
            // Запазване на изображението
            File outputFile = new File(filePath);
            outputFile.getParentFile().mkdirs(); // Създаване на директорията, ако не съществува
            ImageIO.write(image, "PNG", outputFile);
            
            System.out.println("Създадена е икона: " + filePath);
        } catch (IOException e) {
            System.err.println("Грешка при създаване на икона: " + e.getMessage());
        }
    }
    
    /**
     * Създава кръгла икона със символ
     * @param filePath Път до файла
     * @param size Размер
     * @param color Цвят
     * @param symbol Символ (emoji или текст)
     * @param fontSize Размер на шрифта
     */
    private static void createCircleIcon(String filePath, int size, Color color, String symbol, int fontSize) {
        try {
            BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = image.createGraphics();
            
            // Настройка за анти-алиасинг
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            
            // Рисуване на кръг
            g2d.setColor(color);
            g2d.fillOval(0, 0, size, size);
            
            // Рисуване на символа
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, fontSize));
            
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(symbol);
            int textHeight = fm.getHeight();
            
            // Центриране на символа
            g2d.drawString(symbol, 
                (size - textWidth) / 2, 
                (size - textHeight) / 2 + fm.getAscent());
            
            g2d.dispose();
            
            // Запазване на изображението
            File outputFile = new File(filePath);
            outputFile.getParentFile().mkdirs();
            ImageIO.write(image, "PNG", outputFile);
            
            System.out.println("Създадена е икона: " + filePath);
        } catch (IOException e) {
            System.err.println("Грешка при създаване на икона: " + e.getMessage());
        }
    }
}