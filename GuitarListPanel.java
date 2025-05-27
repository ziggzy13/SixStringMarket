package com.sixstringmarket.ui;

import com.sixstringmarket.components.GuitarCard;
import com.sixstringmarket.model.Guitar;
import com.sixstringmarket.model.Guitar.GuitarType;
import com.sixstringmarket.ui.components.SearchFilterPanel;
import com.sixstringmarket.model.Guitar.Condition;
import com.sixstringmarket.service.AuthenticationService;
import com.sixstringmarket.service.GuitarService;
import com.sixstringmarket.service.SearchService;
import com.sixstringmarket.ui.MainFrame;
import com.sixstringmarket.util.Constants;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Панел за показване на списък с китари
 */
public class GuitarListPanel extends JPanel {
    
    private MainFrame parentFrame;
    private GuitarService guitarService;
    private SearchService searchService;
    private JPanel guitarsPanel;
    private SearchFilterPanel searchFilterPanel;
    private List<Guitar> currentGuitars;
    private int sellerId; // Ако != 0, то показва китари само на конкретен продавач
    
    /**
     * Конструктор за всички китари
     * @param parentFrame Родителският прозорец
     */
    public GuitarListPanel(MainFrame parentFrame) {
        this(parentFrame, 0);
    }
    
    /**
     * Конструктор за китари на конкретен продавач
     * @param parentFrame Родителският прозорец
     * @param sellerId ID на продавача
     */
    public GuitarListPanel(MainFrame parentFrame, int sellerId) {
        this.parentFrame = parentFrame;
        this.guitarService = new GuitarService();
        this.searchService = new SearchService();
        this.sellerId = sellerId;
        
        setLayout(new BorderLayout());
        setBackground(Constants.BACKGROUND_COLOR);
        
        initComponents();
        
        // Зареждане на китарите
        loadGuitars();
    }
    
    /**
     * Инициализира компонентите на панела
     */
    private void initComponents() {
        // Заглавие на панела
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Constants.BACKGROUND_COLOR);
        
        String title = (sellerId == 0) ? "" : "Моите обяви";
        JLabel titleLabel = new JLabel(title, JLabel.LEFT);
        titleLabel.setFont(Constants.TITLE_FONT);
        titleLabel.setForeground(Constants.PRIMARY_COLOR);
        titlePanel.add(titleLabel, BorderLayout.WEST);
        
        // Ако това е списък с всички китари, добавяме панел за търсене
        if (sellerId == 0) {
            searchFilterPanel = new SearchFilterPanel(this);
            titlePanel.add(searchFilterPanel, BorderLayout.CENTER);
        }
        
        // Бутон за добавяне на нова китара - само един бутон в горната част
        
        
        add(titlePanel, BorderLayout.NORTH);
        
        // Панел за списъка с китари
        guitarsPanel = new JPanel();
        guitarsPanel.setLayout(new BoxLayout(guitarsPanel, BoxLayout.Y_AXIS));
        guitarsPanel.setBackground(Constants.BACKGROUND_COLOR);
        
        JScrollPane scrollPane = new JScrollPane(guitarsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Зарежда китарите от базата данни
     */
    public void loadGuitars() {
        guitarsPanel.removeAll();
        
        if (sellerId == 0) {
            currentGuitars = guitarService.getAllActiveGuitars();
        } else {
            currentGuitars = guitarService.getGuitarsBySeller(sellerId);
        }
        
        if (currentGuitars.isEmpty()) {
            JLabel noGuitarsLabel = new JLabel("Няма налични китари");
            noGuitarsLabel.setFont(Constants.DEFAULT_FONT);
            noGuitarsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            guitarsPanel.add(Box.createVerticalStrut(50));
            guitarsPanel.add(noGuitarsLabel);
        } else {
            for (Guitar guitar : currentGuitars) {
                GuitarCard card = new GuitarCard(guitar, parentFrame);
                guitarsPanel.add(card);
                guitarsPanel.add(Box.createVerticalStrut(10));
            }
        }
        
        guitarsPanel.revalidate();
        guitarsPanel.repaint();
    }
    
    /**
     * Търсене и филтриране на китари
     * @param keyword Ключова дума
     * @param type Тип на китарата
     * @param brand Марка
     * @param minPrice Минимална цена
     * @param maxPrice Максимална цена
     * @param condition Състояние
     */
    public void searchGuitars(String keyword, GuitarType type, String brand, 
                             BigDecimal minPrice, BigDecimal maxPrice, Condition condition) {
        
        currentGuitars = searchService.search(keyword, type, brand, minPrice, maxPrice, condition);
        
        guitarsPanel.removeAll();
        
        if (currentGuitars.isEmpty()) {
            JLabel noResultsLabel = new JLabel("Няма намерени китари по зададените критерии");
            noResultsLabel.setFont(Constants.DEFAULT_FONT);
            noResultsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            guitarsPanel.add(Box.createVerticalStrut(50));
            guitarsPanel.add(noResultsLabel);
        } else {
            for (Guitar guitar : currentGuitars) {
                GuitarCard card = new GuitarCard(guitar, parentFrame);
                guitarsPanel.add(card);
                guitarsPanel.add(Box.createVerticalStrut(10));
            }
        }
        
        guitarsPanel.revalidate();
        guitarsPanel.repaint();
    }
}