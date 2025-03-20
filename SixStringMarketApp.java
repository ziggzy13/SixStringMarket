package com.sixstringmarket.ui;

import com.sixstringmarket.dao.DBConnection;
import com.sixstringmarket.service.AuthenticationService;
import com.sixstringmarket.util.Constants;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * Главен клас на приложението SixStringMarket
 */
public class SixStringMarketApp {
    
    /**
     * Главен метод за стартиране на приложението
     */
    public static void main(String[] args) {
        try {
            // Задаване на Look and Feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Инициализиране на шрифтове и цветове
            initializeUIDefaults();
            
            // Проверка на връзката с базата данни
            try {
                DBConnection.getConnection();
                System.out.println("Успешна връзка с базата данни.");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, 
                    "Грешка при свързване с базата данни: " + e.getMessage(), 
                    "Грешка в базата данни", 
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
            
            // Стартиране на приложението в EDT (Event Dispatch Thread)
            SwingUtilities.invokeLater(() -> {
                // Ако няма влязъл потребител, показваме прозореца за вход
                if (!AuthenticationService.getInstance().isAuthenticated()) {
                    new LoginFrame().setVisible(true);
                } else {
                    // Иначе показваме главния прозорец
                    new MainFrame().setVisible(true);
                }
            });
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Възникна грешка при стартиране на приложението: " + e.getMessage(),
                "Грешка",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Настройка на UI компонентите
     */
    private static void initializeUIDefaults() {
        // Задаване на шрифтове
        Font defaultFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font boldFont = new Font("Segoe UI", Font.BOLD, 14);
        Font titleFont = new Font("Segoe UI", Font.BOLD, 18);
        
        // Записване на шрифтове в константи
        Constants.DEFAULT_FONT = defaultFont;
        Constants.BOLD_FONT = boldFont;
        Constants.TITLE_FONT = titleFont;
        
     
        // Настройка на UI Manager за всички компоненти
        UIManager.put("Panel.background", Constants.BACKGROUND_COLOR);
        UIManager.put("Label.font", defaultFont);
        UIManager.put("Button.font", defaultFont);
        UIManager.put("TextField.font", defaultFont);
        UIManager.put("PasswordField.font", defaultFont);
        UIManager.put("TextArea.font", defaultFont);
        UIManager.put("ComboBox.font", defaultFont);
        UIManager.put("CheckBox.font", defaultFont);
        UIManager.put("RadioButton.font", defaultFont);
        UIManager.put("TabbedPane.font", defaultFont);
        UIManager.put("Table.font", defaultFont);
        UIManager.put("TableHeader.font", boldFont);
        UIManager.put("MenuItem.font", defaultFont);
        UIManager.put("Menu.font", defaultFont);
        UIManager.put("MenuBar.font", defaultFont);
        
        // Настройка на бутони
        UIManager.put("Button.background", Constants.PRIMARY_COLOR);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.focus", new Color(0, 0, 0, 0));
    }
}