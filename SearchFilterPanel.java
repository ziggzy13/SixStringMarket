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
 * Панел за търсене и филтриране на китари с двуредов дизайн
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
        
        // Използване на BorderLayout за организация на двата реда
        setLayout(new BorderLayout(0, 5));
        setBackground(Constants.BACKGROUND_COLOR);
        
        initComponents();
        addEventListeners();
    }
    
    /**
     * Инициализира компонентите на панела
     */
    private void initComponents() {
        // Горен ред - поле за търсене и бутони
        JPanel topRow = new JPanel(new BorderLayout(10, 0));
        topRow.setBackground(Constants.BACKGROUND_COLOR);
        
        // Панел за поле за търсене
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBackground(Constants.BACKGROUND_COLOR);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        
        JLabel searchLabel = new JLabel("Търсене:");
        searchPanel.add(searchLabel, BorderLayout.WEST);
        
        // Поле за търсене - увеличена ширина
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(400, 30));
        searchField.setToolTipText("Търсене по заглавие, марка, модел или описание");
        searchPanel.add(searchField, BorderLayout.CENTER);
        
        topRow.add(searchPanel, BorderLayout.CENTER);
        
        // Панел за бутони
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonsPanel.setBackground(Constants.BACKGROUND_COLOR);
        
        searchButton = new JButton("Търси");
        searchButton.setBackground(Constants.PRIMARY_COLOR);
        searchButton.setForeground(Color.BLACK); // Changed to black text color
        buttonsPanel.add(searchButton);
        
        resetButton = new JButton("Изчисти");
        resetButton.setBackground(Color.LIGHT_GRAY);
        resetButton.setForeground(Color.BLACK);
        buttonsPanel.add(resetButton);
        
        topRow.add(buttonsPanel, BorderLayout.EAST);
        
        // Добавяне на горния ред към панела
        add(topRow, BorderLayout.NORTH);
        
        // Долен ред - филтри
        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        bottomRow.setBackground(Constants.BACKGROUND_COLOR);
        bottomRow.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        
        // Падащо меню за марка
        bottomRow.add(new JLabel("Марка:"));
        brandCombo = new JComboBox<>(Constants.GUITAR_BRANDS);
        brandCombo.setPreferredSize(new Dimension(150, 30));
        brandCombo.insertItemAt("Всички марки", 0);
        brandCombo.setSelectedIndex(0);
        bottomRow.add(brandCombo);
        
        // Падащо меню за тип
        bottomRow.add(new JLabel("Тип:"));
        String[] types = {"Всички типове", "Акустична", "Електрическа", "Класическа", "Бас", "Друга"};
        typeCombo = new JComboBox<>(types);
        typeCombo.setPreferredSize(new Dimension(150, 30));
        bottomRow.add(typeCombo);
        
        // Падащо меню за състояние
        bottomRow.add(new JLabel("Състояние:"));
        String[] conditions = {"Всички състояния", "Нова", "Употребявана", "Винтидж"};
        conditionCombo = new JComboBox<>(conditions);
        conditionCombo.setPreferredSize(new Dimension(150, 30));
        bottomRow.add(conditionCombo);
        
        // Полета за цена
        bottomRow.add(new JLabel("Цена от:"));
        minPriceField = new JTextField(7);
        minPriceField.setPreferredSize(new Dimension(70, 30));
        minPriceField.setToolTipText("Минимална цена");
        bottomRow.add(minPriceField);
        
        bottomRow.add(new JLabel("до:"));
        maxPriceField = new JTextField(7);
        maxPriceField.setPreferredSize(new Dimension(70, 30));
        maxPriceField.setToolTipText("Максимална цена");
        bottomRow.add(maxPriceField);
        
        // Добавяне на долния ред към панела
        add(bottomRow, BorderLayout.CENTER);
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
        
        // Добавяне на действие при натискане на Enter в полето за търсене
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchGuitars();
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