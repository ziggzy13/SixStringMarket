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
 * Panel for searching and filtering guitars
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
     * @param parentPanel Parent guitar list panel
     */
    public SearchFilterPanel(GuitarListPanel parentPanel) {
        this.parentPanel = parentPanel;
        this.searchService = new SearchService();
        
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        setBackground(Constants.BACKGROUND_COLOR);
        
        try {
            initComponents();
            addEventListeners();
        } catch (Exception e) {
            System.err.println("Error initializing SearchFilterPanel: " + e.getMessage());
            e.printStackTrace();
            
            // Clear any existing components
            removeAll();
            
            // Add an error message
            JLabel errorLabel = new JLabel("Error: Could not initialize search filters");
            errorLabel.setForeground(Color.RED);
            add(errorLabel);
        }
    }
    
    /**
     * Initialize panel components
     */
    private void initComponents() {
        // Search field
        searchField = new JTextField(15);
        searchField.setToolTipText("Search by title, brand, model or description");
        
        // Brand dropdown
        brandCombo = new JComboBox<>(Constants.GUITAR_BRANDS);
        brandCombo.setPreferredSize(new Dimension(120, 25));
        brandCombo.insertItemAt("All brands", 0);
        brandCombo.setSelectedIndex(0);
        
        // Type dropdown
        String[] types = {"All types", "Acoustic", "Electric", "Classical", "Bass", "Other"};
        typeCombo = new JComboBox<>(types);
        typeCombo.setPreferredSize(new Dimension(120, 25));
        
        // Condition dropdown
        String[] conditions = {"All conditions", "New", "Used", "Vintage"};
        conditionCombo = new JComboBox<>(conditions);
        conditionCombo.setPreferredSize(new Dimension(120, 25));
        
        // Price fields
        minPriceField = new JTextField(5);
        minPriceField.setToolTipText("Minimum price");
        
        maxPriceField = new JTextField(5);
        maxPriceField.setToolTipText("Maximum price");
        
        // Buttons
        searchButton = new JButton("Search");
        searchButton.setBackground(Constants.PRIMARY_COLOR);
        searchButton.setForeground(Color.WHITE);
        
        resetButton = new JButton("Reset");
        resetButton.setBackground(Color.LIGHT_GRAY);
        resetButton.setForeground(Color.BLACK);
        
        // Add components to panel using separate methods for each section
        // This prevents one error from affecting the entire panel
        
        // Search section
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        searchPanel.setBackground(Constants.BACKGROUND_COLOR);
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        add(searchPanel);
        
        // Brand section
        JPanel brandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        brandPanel.setBackground(Constants.BACKGROUND_COLOR);
        brandPanel.add(new JLabel("Brand:"));
        brandPanel.add(brandCombo);
        add(brandPanel);
        
        // Type section
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        typePanel.setBackground(Constants.BACKGROUND_COLOR);
        typePanel.add(new JLabel("Type:"));
        typePanel.add(typeCombo);
        add(typePanel);
        
        // Condition section
        JPanel conditionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        conditionPanel.setBackground(Constants.BACKGROUND_COLOR);
        conditionPanel.add(new JLabel("Condition:"));
        conditionPanel.add(conditionCombo);
        add(conditionPanel);
        
        // Price section
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pricePanel.setBackground(Constants.BACKGROUND_COLOR);
        pricePanel.add(new JLabel("Price from:"));
        pricePanel.add(minPriceField);
        pricePanel.add(new JLabel("to:"));
        pricePanel.add(maxPriceField);
        add(pricePanel);
        
        // Button section
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        buttonPanel.setBackground(Constants.BACKGROUND_COLOR);
        buttonPanel.add(searchButton);
        buttonPanel.add(resetButton);
        add(buttonPanel);
    }
    
    /**
     * Add event listeners to components
     */
    private void addEventListeners() {
        // Search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    searchGuitars();
                } catch (Exception ex) {
                    System.err.println("Error searching guitars: " + ex.getMessage());
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                        SearchFilterPanel.this,
                        "Error searching guitars: " + ex.getMessage(),
                        "Search Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
        
        // Reset button
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    resetFilters();
                } catch (Exception ex) {
                    System.err.println("Error resetting filters: " + ex.getMessage());
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                        SearchFilterPanel.this,
                        "Error resetting filters: " + ex.getMessage(),
                        "Reset Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
    }
    
    /**
     * Search guitars with current filters
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
        String minPriceText = minPriceField.getText().trim();
        if (!minPriceText.isEmpty()) {
            try {
                minPrice = new BigDecimal(minPriceText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Invalid minimum price. Please enter a number.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        BigDecimal maxPrice = null;
        String maxPriceText = maxPriceField.getText().trim();
        if (!maxPriceText.isEmpty()) {
            try {
                maxPrice = new BigDecimal(maxPriceText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Invalid maximum price. Please enter a number.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        // Check if minimum price is less than maximum price
        if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
            JOptionPane.showMessageDialog(this,
                "Minimum price cannot be greater than maximum price.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Call search in parent panel
        if (parentPanel != null) {
            parentPanel.searchGuitars(keyword, type, brand, minPrice, maxPrice, condition);
        } else {
            JOptionPane.showMessageDialog(this,
                "Cannot perform search: parent panel is null.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
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
        
        // Reload all guitars
        if (parentPanel != null) {
            parentPanel.loadGuitars();
        } else {
            JOptionPane.showMessageDialog(this,
                "Cannot reset: parent panel is null.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}