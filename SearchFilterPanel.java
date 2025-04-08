package com.sixstringmarket.ui.components;

import com.sixstringmarket.model.Guitar.GuitarType;
import com.sixstringmarket.model.Guitar.Condition;
import com.sixstringmarket.service.SearchService;
import com.sixstringmarket.ui.GuitarListPanel;
import com.sixstringmarket.util.Constants;
import com.sixstringmarket.util.ValidationUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;

/**
 * Панел за търсене и филтриране на китари
 */
public class SearchFilterPanel extends JPanel {
    
    private GuitarListPanel parentPanel;
    private SearchService searchService;
    
    private JTextField searchField;
    private JComboBox<String> brandCombo;
    private JComboBox<String> typeCombo;
    private JComboBox<String> conditionCombo;
    private JTextField minPriceField;
    private JTextField maxPriceField;
    private JButton searchButton;
    private JButton resetButton;
    
    /**
     * Конструктор
     * @param parentPanel Родителският панел с китари
     */
    public SearchFilterPanel(GuitarListPanel parentPanel) {
        this.parentPanel = parentPanel;
        this.searchService = new SearchService();
        
        // Използваме GridBagLayout за по-добро подреждане
        setLayout(new GridBagLayout());
        setBackground(Constants.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        initComponents();
        addEventListeners();
    }
    
    /**
     * Инициализира компонентите на панела
     */
    private void initComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Първи ред с полетата за търсене
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        add(new JLabel("Търсене:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 2.0; // Даваме по-голяма тежест за повече място
        gbc.gridwidth = 1;
        // FIX 1: По-голямо поле за търсене с повече колони
        searchField = new JTextField(30);
        add(searchField, gbc);
        
        gbc.gridx = 2;
        gbc.weightx = 0;
        add(new JLabel("Марка:"), gbc);
        
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        brandCombo = new JComboBox<>(Constants.GUITAR_BRANDS);
        brandCombo.insertItemAt("Всички марки", 0);
        brandCombo.setSelectedIndex(0);
        add(brandCombo, gbc);
        
        gbc.gridx = 4;
        gbc.weightx = 0;
        add(new JLabel("Тип:"), gbc);
        
        gbc.gridx = 5;
        gbc.weightx = 1.0;
        String[] types = {"Всички типове", "Акустична", "Електрическа", "Класическа", "Бас", "Друга"};
        typeCombo = new JComboBox<>(types);
        add(typeCombo, gbc);
        
        gbc.gridx = 6;
        gbc.weightx = 0;
        add(new JLabel("Състояние:"), gbc);
        
        gbc.gridx = 7;
        gbc.weightx = 1.0;
        String[] conditions = {"Всички състояния", "Нова", "Употребявана", "Винтидж"};
        conditionCombo = new JComboBox<>(conditions);
        add(conditionCombo, gbc);
        
        gbc.gridx = 8;
        gbc.weightx = 0;
        add(new JLabel("Цена от:"), gbc);
        
        gbc.gridx = 9;
        gbc.weightx = 1.0;
        // FIX 2: По-големи полета за цена с повече колони
        minPriceField = new JTextField(12);
        add(minPriceField, gbc);
        
        gbc.gridx = 10;
        gbc.weightx = 0;
        add(new JLabel("до:"), gbc);
        
        gbc.gridx = 11;
        gbc.weightx = 1.0;
        // FIX 2: По-големи полета за цена с повече колони
        maxPriceField = new JTextField(12);
        add(maxPriceField, gbc);
        
        // Бутони на същия ред за максимална компактност
        gbc.gridx = 12;
        gbc.weightx = 0;
        gbc.insets = new Insets(5, 10, 5, 5);
        searchButton = new JButton("Търси");
        searchButton.setBackground(new Color(59, 89, 152)); // Facebook blue
        // FIX 3: Черен текст на бутона за търсене
        searchButton.setForeground(Color.BLACK);
        searchButton.setFocusPainted(false);
        add(searchButton, gbc);
        
        gbc.gridx = 13;
        gbc.weightx = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        resetButton = new JButton("Изчисти");
        resetButton.setBackground(new Color(220, 220, 220));
        resetButton.setForeground(Color.BLACK);
        resetButton.setFocusPainted(false);
        add(resetButton, gbc);
    }
    
    /**
     * Добавя слушатели за събития към компонентите
     */
    private void addEventListeners() {
        // Бутон за търсене
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchGuitars();
            }
        });
        
        // Бутон за изчистване на филтрите
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetFilters();
            }
        });
        
        // Действие при натискане на Enter в полето за търсене
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchGuitars();
                }
            }
        });
    }
    
    /**
     * Извършва търсенето с текущите филтри
     */
    private void searchGuitars() {
        // Извличане на стойностите от филтрите
        String keyword = searchField.getText().trim();
        
        GuitarType type = null;
        if (typeCombo.getSelectedIndex() > 0) {
            switch (typeCombo.getSelectedIndex()) {
                case 1: type = GuitarType.ACOUSTIC; break;
                case 2: type = GuitarType.ELECTRIC; break;
                case 3: type = GuitarType.CLASSICAL; break;
                case 4: type = GuitarType.BASS; break;
                case 5: type = GuitarType.OTHER; break;
            }
        }
        
        String brand = null;
        if (brandCombo.getSelectedIndex() > 0) {
            brand = (String) brandCombo.getSelectedItem();
        }
        
        Condition condition = null;
        if (conditionCombo.getSelectedIndex() > 0) {
            switch (conditionCombo.getSelectedIndex()) {
                case 1: condition = Condition.NEW; break;
                case 2: condition = Condition.USED; break;
                case 3: condition = Condition.VINTAGE; break;
            }
        }
        
        BigDecimal minPrice = null;
        if (!minPriceField.getText().trim().isEmpty()) {
            try {
                minPrice = new BigDecimal(minPriceField.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Невалидна минимална цена. Моля, въведете число.",
                    "Грешка при валидация",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        BigDecimal maxPrice = null;
        if (!maxPriceField.getText().trim().isEmpty()) {
            try {
                maxPrice = new BigDecimal(maxPriceField.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Невалидна максимална цена. Моля, въведете число.",
                    "Грешка при валидация",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        // Проверка дали минималната цена е по-малка от максималната
        if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
            JOptionPane.showMessageDialog(this,
                "Минималната цена не може да бъде по-голяма от максималната.",
                "Грешка при валидация",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Извикване на търсенето в родителския панел
        parentPanel.searchGuitars(keyword, type, brand, minPrice, maxPrice, condition);
    }
    
    /**
     * Изчиства всички филтри
     */
    private void resetFilters() {
        searchField.setText("");
        brandCombo.setSelectedIndex(0);
        typeCombo.setSelectedIndex(0);
        conditionCombo.setSelectedIndex(0);
        minPriceField.setText("");
        maxPriceField.setText("");
        
        // Зареждане на всички китари
        parentPanel.loadGuitars();
    }
}