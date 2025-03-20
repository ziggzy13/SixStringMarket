package com.sixstringmarket.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * Клас с константи и стилове за дизайна на приложението
 */
public class Constants {
    
    // Заглавие на приложението
    public static final String APP_TITLE = "SixStringMarket - Покупко-продажба на китари";
    
    // Версия на приложението
    public static final String APP_VERSION = "1.0.0";
    
    // Цветова схема - модерна и елегантна
    public static final Color PRIMARY_COLOR = new Color(66, 103, 178);      // Синьо (основен цвят)
    public static final Color SECONDARY_COLOR = new Color(144, 202, 249);   // Светло синьо
    public static final Color ACCENT_COLOR = new Color(240, 98, 146);       // Розово (акцент)
    public static final Color BACKGROUND_COLOR = new Color(250, 250, 250);  // Почти бяло
    public static final Color PANEL_COLOR = new Color(255, 255, 255);       // Бяло
    public static final Color TEXT_COLOR = new Color(33, 33, 33);           // Тъмно сиво
    public static final Color TEXT_SECONDARY_COLOR = new Color(117, 117, 117); // Средно сиво
    public static final Color SUCCESS_COLOR = new Color(76, 175, 80);       // Зелено
    public static final Color WARNING_COLOR = new Color(255, 152, 0);       // Оранжево
    public static final Color ERROR_COLOR = new Color(211, 47, 47);         // Червено
    
    // Шрифтове - модерни и четливи
    public static Font DEFAULT_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    public static Font LARGE_FONT = new Font("Segoe UI", Font.PLAIN, 16);
    public static Font BOLD_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    public static Font SUBTITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    public static Font CARD_TITLE_FONT = new Font("Segoe UI", Font.BOLD, 16);
    
    // Граници и отстъпи
    public static final int PADDING_SMALL = 5;
    public static final int PADDING_MEDIUM = 10;
    public static final int PADDING_LARGE = 20;
    public static final int ROUNDED_CORNER_RADIUS = 8;
    
    // Готови граници за компоненти
    public static final Border PANEL_BORDER = new LineBorder(new Color(225, 225, 225), 1, true);
    public static final Border CARD_BORDER = new CompoundBorder(
            new LineBorder(new Color(225, 225, 225), 1, true),
            new EmptyBorder(10, 10, 10, 10));
    public static final Border TEXT_FIELD_BORDER = new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(5, 8, 5, 8));
    public static final Border BUTTON_BORDER = new LineBorder(new Color(200, 200, 200), 1, true);
    
    // Размери на компоненти
    public static final Dimension BUTTON_DIMENSION = new Dimension(120, 36);
    public static final Dimension SMALL_BUTTON_DIMENSION = new Dimension(90, 32);
    public static final Dimension TEXT_FIELD_DIMENSION = new Dimension(200, 36);
    public static final Dimension CARD_DIMENSION = new Dimension(300, 350);
    
    // Икони (пътища)
    public static final String ICON_PATH = "resources/icons/";
    public static final String LOGO_PATH = ICON_PATH + "logo.png";
    public static final String USER_ICON = ICON_PATH + "user.png";
    public static final String GUITAR_ICON = ICON_PATH + "guitar.png";
    public static final String CART_ICON = ICON_PATH + "cart.png";
    public static final String SAVE_ICON = ICON_PATH + "save.png";
    public static final String SEARCH_ICON = ICON_PATH + "search.png";
    public static final String SETTINGS_ICON = ICON_PATH + "settings.png";
    
    // Марки китари (за филтъра)
    public static final String[] GUITAR_BRANDS = {
        "Fender", "Gibson", "Ibanez", "Jackson", "ESP", "PRS", "Yamaha", 
        "Epiphone", "Schecter", "Dean", "Gretsch", "Martin", "Taylor", 
        "Washburn", "Cort", "B.C. Rich", "Squier", "Charvel", "Takamine"
    };
    
    // Пътища до ресурси
    public static final String RESOURCES_PATH = "resources/";
    public static final String IMAGES_PATH = RESOURCES_PATH + "images/";
    public static final String CONFIG_PATH = RESOURCES_PATH + "config/";
}