package com.sixstringmarket.ui.components;

import com.sixstringmarket.model.Guitar.GuitarType;
import com.sixstringmarket.model.Guitar.Condition;
import com.sixstringmarket.service.SearchService;
import com.sixstringmarket.ui.GuitarListPanel;
import com.sixstringmarket.util.Constants;
import com.sixstringmarket.util.ValidationUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

/**
 * Compact search and filter panel for guitars
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
     * Constructor
     * @param parentPanel Parent panel
     */
    public SearchFilterPanel(GuitarListPanel parentPanel) {
        this.parentPanel = parentPanel;
        this.searchService = new SearchService();
        
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5)); // Reduced padding
        setBackground(Constants.BACKGROUND_COLOR);
        setBorder(new EmptyBorder(5, 5, 5, 5)); // Add small padding around the panel
        
        initComponents();
        addEventListeners();
    }
    
    /**
     * Initialize components with compact layout
     */
    private void initComponents() {
        // Search field - make it narrower
        searchField = new JTextField(10); // Reduced from 15 to 10
        searchField.setToolTipText("Търсене по заглавие, марка, модел или описание");
        searchField.setPreferredSize(new Dimension(100, 25)); // Set fixed height
        add(createLabeledComponent("Търсене:", searchField));
        
        // Brand combo - make it more compact
        brandCombo = new JComboBox<>(Constants.GUITAR_BRANDS);
        brandCombo.setPreferredSize(new Dimension(100, 25)); // Reduced from 120 to 100
        brandCombo.insertItemAt("Всички марки", 0);
        brandCombo.setSelectedIndex(0);
        add(createLabeledComponent("Марка:", brandCombo));
        
        // Type combo - make it more compact
        String[] types = {"Всички типове", "Акустична", "Електрическа", "Класическа", "Бас", "Друга"};
        typeCombo = new JComboBox<>(types);
        typeCombo.setPreferredSize(new Dimension(100, 25)); // Reduced from 120 to 100
        add(createLabeledComponent("Тип:", typeCombo));
        
        // Condition combo - make it more compact
        String[] conditions = {"Всички състояния", "Нова", "Употребявана", "Винтидж"};
        conditionCombo = new JComboBox<>(conditions);
        conditionCombo.setPreferredSize(new Dimension(100, 25)); // Reduced from 120 to 100
        add(createLabeledComponent("Състояние:", conditionCombo));
        
        // Price fields - make them narrower
        minPriceField = new JTextField(4); // Reduced from 5 to 4
        minPriceField.setToolTipText("Минимална цена");
        minPriceField.setPreferredSize(new Dimension(50, 25)); // Set fixed size
        
        maxPriceField = new JTextField(4); // Reduced from 5 to 4
        maxPriceField.setToolTipText("Максимална цена");
        maxPriceField.setPreferredSize(new Dimension(50, 25)); // Set fixed size
        
        // Create a compact price range panel
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        pricePanel.setBackground(Constants.BACKGROUND_COLOR);
        pricePanel.add(new JLabel("Цена от:"));
        pricePanel.add(minPriceField);
        pricePanel.add(new JLabel("до:"));
        pricePanel.add(maxPriceField);
        add(pricePanel);
        
        // Buttons - make them properly sized with text
        searchButton = new JButton("Търси");
        searchButton.setToolTipText("Търси");
        searchButton.setBackground(Constants.PRIMARY_COLOR);
        searchButton.setForeground(Color.BLACK);
        searchButton.setPreferredSize(new Dimension(70, 25)); // Wider button to accommodate text
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 12)); // Adjust font
        
        resetButton = new JButton("Изчисти");
        resetButton.setToolTipText("Изчисти");
        resetButton.setBackground(Color.LIGHT_GRAY);
        resetButton.setForeground(Color.BLACK);
        resetButton.setPreferredSize(new Dimension(90, 25)); // Wider button to accommodate text
        resetButton.setFont(new Font("Segoe UI", Font.PLAIN, 12)); // Adjust font
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        buttonPanel.setBackground(Constants.BACKGROUND_COLOR);
        buttonPanel.add(searchButton);
        buttonPanel.add(resetButton);
        add(buttonPanel);
    }
    
    /**
     * Create labeled component panel
     */
    private JPanel createLabeledComponent(String labelText, JComponent component) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        panel.setBackground(Constants.BACKGROUND_COLOR);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12)); // Smaller font
        panel.add(label);
        panel.add(component);
        
        return panel;
    }
    
    /**
     * Add event listeners to components
     */
    private void addEventListeners() {
        // Search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchGuitars();
            }
        });
        
        // Reset button
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetFilters();
            }
        });
        
        // Add Enter key listener for search field
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchGuitars();
            }
        });
    }
    
    /**
     * Search for guitars with current filters
     */
    private void searchGuitars() {
        // Extract values from filters
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
        
        // Check if min price is less than max price
        if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
            JOptionPane.showMessageDialog(this,
                "Минималната цена не може да бъде по-голяма от максималната.",
                "Грешка при валидация",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Call search in parent panel
        parentPanel.searchGuitars(keyword, type, brand, minPrice, maxPrice, condition);
    }
    
    /**
     * Reset all filters
     */
    private void resetFilters() {
        searchField.setText("");
        brandCombo.setSelectedIndex(0);
        typeCombo.setSelectedIndex(0);
        conditionCombo.setSelectedIndex(0);
        minPriceField.setText("");
        maxPriceField.setText("");
        
        // Load all guitars
        parentPanel.loadGuitars();
    }
}