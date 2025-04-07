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
        
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        setBackground(Constants.PANEL_COLOR); // Бял фон
        
        initComponents();
        addEventListeners();
    }
    
    /**
     * Инициализира компонентите на панела
     */
    private void initComponents() {
        // Поле за търсене
        searchField = new JTextField(15);
        searchField.setToolTipText("Търсене по заглавие, марка, модел или описание");
        searchField.setForeground(Constants.TEXT_COLOR); // Черен текст
        
        // Падащо меню за марка
        brandCombo = new JComboBox<>(Constants.GUITAR_BRANDS);
        brandCombo.setPreferredSize(new Dimension(120, 25));
        brandCombo.insertItemAt("Всички марки", 0);
        brandCombo.setSelectedIndex(0);
        brandCombo.setForeground(Constants.TEXT_COLOR); // Черен текст
        
        // Падащо меню за тип
        String[] types = {"Всички типове", "Акустична", "Електрическа", "Класическа", "Бас", "Друга"};
        typeCombo = new JComboBox<>(types);
        typeCombo.setPreferredSize(new Dimension(120, 25));
        typeCombo.setForeground(Constants.TEXT_COLOR); // Черен текст
        
        // Падащо меню за състояние
        String[] conditions = {"Всички състояния", "Нова", "Употребявана", "Винтидж"};
        conditionCombo = new JComboBox<>(conditions);
        conditionCombo.setPreferredSize(new Dimension(120, 25));
        conditionCombo.setForeground(Constants.TEXT_COLOR); // Черен текст
        
        // Полета за цена
        minPriceField = new JTextField(5);
        minPriceField.setToolTipText("Минимална цена");
        minPriceField.setForeground(Constants.TEXT_COLOR); // Черен текст
        
        maxPriceField = new JTextField(5);
        maxPriceField.setToolTipText("Максимална цена");
        maxPriceField.setForeground(Constants.TEXT_COLOR); // Черен текст
        
        // Бутони
        searchButton = new JButton("Търси");
        searchButton.setBackground(Constants.PRIMARY_COLOR);
        searchButton.setForeground(Color.WHITE);
        
        resetButton = new JButton("Изчисти");
        resetButton.setBackground(Color.LIGHT_GRAY);
        resetButton.setForeground(Color.BLACK);
        
        // Добавяне на компонентите към панела
        JLabel searchLabel = new JLabel("Търсене:");
        searchLabel.setForeground(Constants.TEXT_COLOR); // Черен текст
        add(searchLabel);
        add(searchField);
        
        JLabel brandLabel = new JLabel("Марка:");
        brandLabel.setForeground(Constants.TEXT_COLOR); // Черен текст
        add(brandLabel);
        add(brandCombo);
        
        JLabel typeLabel = new JLabel("Тип:");
        typeLabel.setForeground(Constants.TEXT_COLOR); // Черен текст
        add(typeLabel);
        add(typeCombo);
        
        JLabel conditionLabel = new JLabel("Състояние:");
        conditionLabel.setForeground(Constants.TEXT_COLOR); // Черен текст
        add(conditionLabel);
        add(conditionCombo);
        
        JLabel minPriceLabel = new JLabel("Цена от:");
        minPriceLabel.setForeground(Constants.TEXT_COLOR); // Черен текст
        add(minPriceLabel);
        add(minPriceField);
        
        JLabel maxPriceLabel = new JLabel("до:");
        maxPriceLabel.setForeground(Constants.TEXT_COLOR); // Черен текст
        add(maxPriceLabel);
        add(maxPriceField);
        
        add(searchButton);
        add(resetButton);
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