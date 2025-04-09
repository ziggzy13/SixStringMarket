package com.sixstringmarket.ui.components;

import com.sixstringmarket.util.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Модерен панел с възможност за заоблени ъгли и сянка
 */
public class ModernPanel extends JPanel {
    
    private boolean isRound;
    private boolean hasShadow;
    private Color backgroundColor;
    private String title;
    private Font titleFont;
    
    /**
     * Създава модерен панел с основен стил
     */
    public ModernPanel() {
        this(null, true, false);
    }
    
    /**
     * Създава модерен панел с персонализиран стил
     * @param title Заглавие на панела (null за без заглавие)
     * @param isRound Флаг дали панелът е със заоблени ъгли
     * @param hasShadow Флаг дали панелът има сянка
     */
    public ModernPanel(String title, boolean isRound, boolean hasShadow) {
        this.title = title;
        this.isRound = isRound;
        this.hasShadow = hasShadow;
        this.backgroundColor = Constants.PANEL_COLOR;
        this.titleFont = Constants.SUBTITLE_FONT;
        
        setup();
    }
    
    /**
     * Инициализира стиловете на панела
     */
    private void setup() {
        setOpaque(false);
        
        // Настройка на отстъпите на панела
        // Увеличаваме отстъпите за да има място и за по-големите полета
        int topPadding = (title != null) ? Constants.PADDING_LARGE + titleFont.getSize() : Constants.PADDING_MEDIUM;
        int leftPadding = Constants.PADDING_MEDIUM;
        int bottomPadding = Constants.PADDING_MEDIUM;
        int rightPadding = Constants.PADDING_MEDIUM;
        
        setBorder(BorderFactory.createEmptyBorder(
                topPadding,
                leftPadding,
                bottomPadding,
                rightPadding));
                
        // Настройка на layout manager с по-добра поддръжка за различни размери на компоненти
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }
    
    /**
     * Персонализирано рисуване на панела
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        // Рисуване на сянка, ако е необходимо
        if (hasShadow) {
            for (int i = 0; i < 5; i++) {
                g2d.setColor(new Color(0, 0, 0, 10));
                if (isRound) {
                    g2d.fill(new RoundRectangle2D.Double(
                            i, i, width - i * 2, height - i * 2,
                            Constants.ROUNDED_CORNER_RADIUS, Constants.ROUNDED_CORNER_RADIUS));
                } else {
                    g2d.fillRect(i, i, width - i * 2, height - i * 2);
                }
            }
        }
        
        // Рисуване на фона на панела
        g2d.setColor(backgroundColor);
        
        if (isRound) {
            g2d.fill(new RoundRectangle2D.Double(
                    0, 0, width, height,
                    Constants.ROUNDED_CORNER_RADIUS, Constants.ROUNDED_CORNER_RADIUS));
        } else {
            g2d.fillRect(0, 0, width, height);
        }
        
        // Рисуване на заглавието, ако има такова
        if (title != null) {
            g2d.setFont(titleFont);
            g2d.setColor(Constants.TEXT_COLOR);
            FontMetrics fm = g2d.getFontMetrics();
            int titleWidth = fm.stringWidth(title);
            int titleHeight = fm.getHeight();
            g2d.drawString(title, Constants.PADDING_MEDIUM, titleHeight);
        }
        
        g2d.dispose();
    }
    
    /**
     * Задава цвят на фона
     * @param color Цвят
     */
    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
        repaint();
    }
    
    /**
     * Задава заглавие на панела
     * @param title Заглавие
     */
    public void setTitle(String title) {
        this.title = title;
        
        // Обновяване на отстъпа
        int topPadding = (title != null) ? Constants.PADDING_LARGE + titleFont.getSize() : Constants.PADDING_MEDIUM;
        int leftPadding = Constants.PADDING_MEDIUM;
        int bottomPadding = Constants.PADDING_MEDIUM;
        int rightPadding = Constants.PADDING_MEDIUM;
        
        setBorder(BorderFactory.createEmptyBorder(
                topPadding,
                leftPadding,
                bottomPadding,
                rightPadding));
        
        repaint();
    }
    
    /**
     * Задава шрифт на заглавието
     * @param font Шрифт
     */
    public void setTitleFont(Font font) {
        this.titleFont = font;
        repaint();
    }
    
    /**
     * Добавя компонент с подходящо подравняване за полета с различни размери
     * @param component Компонентът, който се добавя
     * @return Добавеният компонент
     */
    @Override
    public Component add(Component component) {
        // Настройка на подравняването, за да се поддържат различни размери на полета
        if (component instanceof JTextField || component instanceof JComboBox || 
            component instanceof JButton || component instanceof ModernTextField) {
        	if (component instanceof JComponent) {
        	    ((JComponent) component).setAlignmentX(Component.LEFT_ALIGNMENT);
        	}
            
            // Добавяме малък отстъп между компонентите
            super.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        
        return super.add(component);
    }
    
    /**
     * Създава панел за група от форми с адаптивно оформление
     * @return Панел с FlowLayout за групиране на полета от формата
     */
    public JPanel createFormRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        return row;
    }
    /**
     * This example demonstrates how to properly use the fixed ModernPanel
     * with the new larger search and price fields.
     */

    // Example 1: Using ModernPanel with search fields
    private JPanel createSearchPanelExample() {
        // Create a ModernPanel with title
        ModernPanel panel = new ModernPanel("Search Guitars", true, true);
        
        // Create a row for the search field
        JPanel searchRow = panel.createFormRow();
        searchRow.add(new JLabel("Search:"));
        
        // Use the new larger search field
        JTextField searchField = new JTextField(30);
        searchField.setPreferredSize(Constants.SEARCH_FIELD_DIMENSION);
        searchRow.add(searchField);
        
        // Add the row to the panel
        panel.add(searchRow);
        
        // Create a row for brand selection
        JPanel brandRow = panel.createFormRow();
        brandRow.add(new JLabel("Brand:"));
        
        JComboBox<String> brandCombo = new JComboBox<>(Constants.GUITAR_BRANDS);
        brandRow.add(brandCombo);
        
        // Add the row to the panel
        panel.add(brandRow);
        
        // Create a row for price range
        JPanel priceRow = panel.createFormRow();
        priceRow.add(new JLabel("Price Range:"));
        
        // Use the new larger price fields
        JTextField minPriceField = new JTextField(10);
        minPriceField.setPreferredSize(Constants.PRICE_FIELD_DIMENSION);
        priceRow.add(minPriceField);
        
        priceRow.add(new JLabel("to"));
        
        JTextField maxPriceField = new JTextField(10);
        maxPriceField.setPreferredSize(Constants.PRICE_FIELD_DIMENSION);
        priceRow.add(maxPriceField);
        
        // Add the row to the panel
        panel.add(priceRow);
        
        // Create a row for buttons
        JPanel buttonRow = panel.createFormRow();
        JButton searchButton = new JButton("Search");
        JButton resetButton = new JButton("Reset");
        buttonRow.add(searchButton);
        buttonRow.add(resetButton);
        
        // Add the row to the panel
        panel.add(buttonRow);
        
        return panel;
    }

    // Example 2: Using ModernPanel with ModernTextField
    private JPanel createModernFieldExample() {
        ModernPanel panel = new ModernPanel("Advanced Search", true, false);
        
        // Create search field using ModernTextField utility method
        ModernTextField searchField = ModernTextField.createSearchField("Search guitars...");
        panel.add(searchField);
        
        // Add some spacing
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Create a price range group
        JPanel priceGroup = panel.createFormRow();
        priceGroup.add(new JLabel("Price from:"));
        
        // Create price fields using ModernTextField utility method
        ModernTextField minPriceField = ModernTextField.createPriceField("Min");
        priceGroup.add(minPriceField);
        
        priceGroup.add(new JLabel("to:"));
        
        ModernTextField maxPriceField = ModernTextField.createPriceField("Max");
        priceGroup.add(maxPriceField);
        
        panel.add(priceGroup);
        
        // Add a submit button
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        JButton submitButton = new JButton("Find Guitars");
        panel.add(submitButton);
        
        return panel;
    }

    // Example 3: Converting existing SearchFilterPanel to use ModernPanel
    private JPanel createSearchFilterWithModernPanel() {
        // Create a ModernPanel
        ModernPanel panel = new ModernPanel(null, false, false);
        
        // Create components with appropriate sizes
        JTextField searchField = new JTextField(30);
        searchField.setPreferredSize(Constants.SEARCH_FIELD_DIMENSION);
        searchField.setToolTipText("Search by title, brand, model or description");
        
        JComboBox<String> brandCombo = new JComboBox<>(Constants.GUITAR_BRANDS);
        brandCombo.insertItemAt("All Brands", 0);
        brandCombo.setSelectedIndex(0);
        
        String[] types = {"All Types", "Acoustic", "Electric", "Classical", "Bass", "Other"};
        JComboBox<String> typeCombo = new JComboBox<>(types);
        
        // Create rows for better layout handling
        JPanel searchRow = panel.createFormRow();
        searchRow.add(new JLabel("Search:"));
        searchRow.add(searchField);
        panel.add(searchRow);
        
        JPanel filterRow = panel.createFormRow();
        filterRow.add(new JLabel("Brand:"));
        filterRow.add(brandCombo);
        filterRow.add(new JLabel("Type:"));
        filterRow.add(typeCombo);
        panel.add(filterRow);
        
        JPanel priceRow = panel.createFormRow();
        priceRow.add(new JLabel("Price from:"));
        
        JTextField minPriceField = new JTextField(10);
        minPriceField.setPreferredSize(Constants.PRICE_FIELD_DIMENSION);
        priceRow.add(minPriceField);
        
        priceRow.add(new JLabel("to:"));
        
        JTextField maxPriceField = new JTextField(10);
        maxPriceField.setPreferredSize(Constants.PRICE_FIELD_DIMENSION);
        priceRow.add(maxPriceField);
        
        JButton searchButton = new JButton("Search");
        JButton resetButton = new JButton("Reset");
        priceRow.add(searchButton);
        priceRow.add(resetButton);
        
        panel.add(priceRow);
        
        return panel;
    }
}