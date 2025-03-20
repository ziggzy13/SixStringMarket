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
        setBackground(Constants.BACKGROUND_COLOR);
        
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
        
        // Падащо меню за марка
        brandCombo = new JComboBox<>(Constants.GUITAR_BRANDS);
        brandCombo.setPreferredSize(new Dimension(120, 25));
        brandCombo.insertItemAt("Всички марки", 0);
        brandCombo.setSelectedIndex(0);
        
        // Падащо меню за тип
        String[] types = {"Всички типове", "Акустична", "Електрическа", "Класическа", "Бас", "Друга"};
        typeCombo = new JComboBox<>(types);
        typeCombo.setPreferredSize(new Dimension(120, 25));
        
        // Падащо меню за състояние
        String[] conditions = {"Всички състояния", "Нова", "Употребявана", "Винтидж"};
        conditionCombo = new JComboBox<>(conditions);
        conditionCombo.setPreferredSize(new Dimension(120, 25));
        
        // Полета за цена
        minPriceField = new JTextField(5);
        minPriceField.setToolTipText("Минимална цена");
        
        maxPriceField = new JTextField(5);
        maxPriceField.setToolTipText("Максимална цена");
        
        // Бутони
        searchButton = new JButton("Търси");
        searchButton.setBackground(Constants.PRIMARY_COLOR);
        searchButton.setForeground(Color.WHITE);
        
        resetButton = new JButton("Изчисти");
        resetButton.setBackground(Color.LIGHT_GRAY);
        resetButton.setForeground(Color.BLACK);
        
        // Добавяне на компонентите към панела
        add(new JLabel("Търсене:"));
        add(searchField);
        add(new JLabel("Марка:"));
        add(brandCombo);
        add(new JLabel("Тип:"));
        add(typeCombo);
        add(new JLabel("Състояние:"));
        add(conditionCombo);
        add(new JLabel("Цена от:"));
        add(minPriceField);
        add(new JLabel("до:"));
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