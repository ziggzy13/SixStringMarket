package com.sixstringmarket.util;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Прост помощен клас за работа с изображения
 */
public class ImageHelper {
    
    /**
     * Зарежда изображение от директория 'images'
     * @param imageName Име на файла (например "guitar_icon.png")
     * @return ImageIcon обект или null ако изображението не е намерено
     */
    public static ImageIcon loadImage(String imageName) {
        try {
            // Опитваме първо от директория images в корена на проекта
            File imageFile = new File("images/" + imageName);
            if (imageFile.exists()) {
                return new ImageIcon(imageFile.getAbsolutePath());
            }
            
            // Опитваме алтернативни локации
            imageFile = new File("src/images/" + imageName);
            if (imageFile.exists()) {
                return new ImageIcon(imageFile.getAbsolutePath());
            }
            
            // Опитваме относителни пътища
            imageFile = new File("resources/images/" + imageName);
            if (imageFile.exists()) {
                return new ImageIcon(imageFile.getAbsolutePath());
            }
            
            // Ако не намерим файла, извеждаме къде сме търсили
            System.out.println("Не може да се намери: " + imageName + " в нито една от стандартните директории.");
            return null;
        } catch (Exception e) {
            System.err.println("Грешка при зареждане на изображение: " + imageName);
            return null;
        }
    }
    
    /**
     * Зарежда изображение и го преоразмерява, ако е нужно
     * @param imageName Име на файла
     * @param width Желана ширина
     * @param height Желана височина
     * @return Преоразмерено ImageIcon или null ако изображението не е намерено
     */
    public static ImageIcon loadImage(String imageName, int width, int height) {
        ImageIcon icon = loadImage(imageName);
        if (icon != null) {
            Image img = icon.getImage();
            Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(resizedImg);
        }
        return null;
    }
    
    /**
     * Създава кръгла иконка с инициал (за аватар)
     * @param initial Буква за показване в аватара
     * @param backgroundColor Цвят на фона
     * @param size Размер на аватара
     * @return ImageIcon с кръгъл аватар
     */
    public static ImageIcon createAvatarIcon(String initial, Color backgroundColor, int size) {
        // Създава изображение в паметта
        BufferedImage bufferedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        
        // Включва anti-aliasing за по-гладки краища
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Рисува кръг
        g2d.setColor(backgroundColor);
        g2d.fillOval(0, 0, size, size);
        
        // Рисува инициала
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, size / 2));
        
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(initial);
        int textHeight = fm.getHeight();
        
        g2d.drawString(initial, 
            (size - textWidth) / 2, 
            (size - textHeight) / 2 + fm.getAscent());
        
        g2d.dispose();
        
        return new ImageIcon(bufferedImage);
    }
}