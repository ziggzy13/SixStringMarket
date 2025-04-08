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
        setBackground(Constants.PANEL_COLOR); // Бял фон
        
        initComponents();
        
        // Зареждане на китарите
        loadGuitars();
    }
    
    /**
     * Инициализира компонентите на панела
     */
    private void initComponents() {
        // Използваме BorderLayout за главния панел
        setLayout(new BorderLayout(0, 0));
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        setBackground(Constants.BACKGROUND_COLOR);
        
        // Панел за заглавието
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Constants.BACKGROUND_COLOR);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        
        String titleText = (sellerId == 0) ? "Всички китари" : "Моите обяви";
        JLabel titleLabel = new JLabel(titleText, JLabel.LEFT);
        titleLabel.setFont(Constants.TITLE_FONT);
        titleLabel.setForeground(Constants.PRIMARY_COLOR);
        titlePanel.add(titleLabel, BorderLayout.WEST);
        
        add(titlePanel, BorderLayout.NORTH);
        
        // Контейнер за филтър и съдържание
        JPanel contentPanel = new JPanel(new BorderLayout(0, 0));
        contentPanel.setBackground(Constants.BACKGROUND_COLOR);
        
        // Добавяне на филтъра само когато показваме всички китари
        if (sellerId == 0) {
            // Създаване на панел за филтъра със специфичен стил
            JPanel filterPanel = new JPanel(new BorderLayout());
            filterPanel.setBackground(Constants.BACKGROUND_COLOR);
            filterPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(0, 10, 0, 10)
            ));
            
            // Добавяне на филтър компонента
            searchFilterPanel = new SearchFilterPanel(this);
            filterPanel.add(searchFilterPanel, BorderLayout.CENTER);
            
            contentPanel.add(filterPanel, BorderLayout.NORTH);
        }
        
        // Панел за списъка с китари
        guitarsPanel = new JPanel();
        guitarsPanel.setLayout(new BoxLayout(guitarsPanel, BoxLayout.Y_AXIS));
        guitarsPanel.setBackground(Constants.BACKGROUND_COLOR);
        
        JScrollPane scrollPane = new JScrollPane(guitarsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(Constants.BACKGROUND_COLOR);
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(contentPanel, BorderLayout.CENTER);
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
            noGuitarsLabel.setForeground(Constants.TEXT_COLOR); // Черен текст
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
            noResultsLabel.setForeground(Constants.TEXT_COLOR); // Черен текст
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