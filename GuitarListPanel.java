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
 * Panel for displaying a list of guitars
 */
public class GuitarListPanel extends JPanel {
    
    private MainFrame parentFrame;
    private GuitarService guitarService;
    private SearchService searchService;
    private JPanel guitarsPanel;
    private SearchFilterPanel searchFilterPanel;
    private List<Guitar> currentGuitars;
    private int sellerId; // If not 0, shows guitars for a specific seller only
    
    /**
     * Constructor for all guitars
     * @param parentFrame Parent frame
     */
    public GuitarListPanel(MainFrame parentFrame) {
        this(parentFrame, 0);
    }
    
    /**
     * Constructor for guitars from a specific seller
     * @param parentFrame Parent frame
     * @param sellerId Seller ID
     */
    public GuitarListPanel(MainFrame parentFrame, int sellerId) {
        try {
            System.out.println("Initializing GuitarListPanel...");
            
            this.parentFrame = parentFrame;
            this.guitarService = new GuitarService();
            this.searchService = new SearchService();
            this.sellerId = sellerId;
            
            setLayout(new BorderLayout());
            setBackground(Constants.BACKGROUND_COLOR);
            
            initComponents();
            
            // Load guitars
            loadGuitars();
            
            System.out.println("GuitarListPanel initialized successfully");
        } catch (Exception e) {
            System.err.println("Error initializing GuitarListPanel: " + e.getMessage());
            e.printStackTrace();
            
            // Create error display panel
            setLayout(new BorderLayout());
            setBackground(Color.WHITE);
            
            JLabel errorLabel = new JLabel("Error loading guitar list: " + e.getMessage());
            errorLabel.setForeground(Color.RED);
            errorLabel.setHorizontalAlignment(JLabel.CENTER);
            add(errorLabel, BorderLayout.CENTER);
        }
    }
    
    /**
     * Initialize panel components with error handling
     */
    private void initComponents() {
        try {
            // Panel title
            JPanel titlePanel = new JPanel(new BorderLayout());
            titlePanel.setBackground(Constants.BACKGROUND_COLOR);
            
            String title = (sellerId == 0) ? "All Guitars" : "My Listings";
            JLabel titleLabel = new JLabel(title, JLabel.LEFT);
            titleLabel.setFont(Constants.TITLE_FONT);
            titleLabel.setForeground(Constants.PRIMARY_COLOR);
            titlePanel.add(titleLabel, BorderLayout.WEST);
            
            // Add search panel for all guitars list
            if (sellerId == 0) {
                try {
                    searchFilterPanel = new SearchFilterPanel(this);
                    titlePanel.add(searchFilterPanel, BorderLayout.CENTER);
                } catch (Exception e) {
                    System.err.println("Error creating search filter panel: " + e.getMessage());
                }
            }
            
            // Add new guitar button for authenticated users
            if (AuthenticationService.getInstance().isAuthenticated()) {
                JButton addGuitarButton = new JButton("Add Guitar");
                addGuitarButton.setBackground(Constants.ACCENT_COLOR);
                addGuitarButton.setForeground(Color.WHITE);
                addGuitarButton.addActionListener(e -> parentFrame.showAddGuitarFrame());
                titlePanel.add(addGuitarButton, BorderLayout.EAST);
            }
            
            add(titlePanel, BorderLayout.NORTH);
            
            // Guitar list panel with scrolling
            guitarsPanel = new JPanel();
            guitarsPanel.setLayout(new BoxLayout(guitarsPanel, BoxLayout.Y_AXIS));
            guitarsPanel.setBackground(Constants.BACKGROUND_COLOR);
            
            JScrollPane scrollPane = new JScrollPane(guitarsPanel);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            
            add(scrollPane, BorderLayout.CENTER);
        } catch (Exception e) {
            System.err.println("Error in initComponents: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw to be caught by the constructor
        }
    }
    
    /**
     * Load guitars from database with error handling
     */
    public void loadGuitars() {
        try {
            System.out.println("Loading guitars...");
            guitarsPanel.removeAll();
            
            if (sellerId == 0) {
                currentGuitars = guitarService.getAllActiveGuitars();
                System.out.println("Loaded " + currentGuitars.size() + " active guitars");
            } else {
                currentGuitars = guitarService.getGuitarsBySeller(sellerId);
                System.out.println("Loaded " + currentGuitars.size() + " guitars for seller " + sellerId);
            }
            
            if (currentGuitars.isEmpty()) {
                JLabel noGuitarsLabel = new JLabel("No guitars available");
                noGuitarsLabel.setFont(Constants.DEFAULT_FONT);
                noGuitarsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                guitarsPanel.add(Box.createVerticalStrut(50));
                guitarsPanel.add(noGuitarsLabel);
            } else {
                for (Guitar guitar : currentGuitars) {
                    try {
                        GuitarCard card = new GuitarCard(guitar, parentFrame);
                        guitarsPanel.add(card);
                        guitarsPanel.add(Box.createVerticalStrut(10));
                    } catch (Exception e) {
                        System.err.println("Error creating guitar card for guitar ID " + guitar.getGuitarId() + ": " + e.getMessage());
                        // Skip this guitar and continue with the next one
                    }
                }
            }
            
            // Refresh the UI
            guitarsPanel.revalidate();
            guitarsPanel.repaint();
            
            System.out.println("Guitars loaded successfully");
        } catch (Exception e) {
            System.err.println("Error loading guitars: " + e.getMessage());
            e.printStackTrace();
            
            // Show error message in panel
            guitarsPanel.removeAll();
            
            JLabel errorLabel = new JLabel("Error loading guitar list: " + e.getMessage());
            errorLabel.setForeground(Color.RED);
            errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JButton retryButton = new JButton("Try Again");
            retryButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            retryButton.addActionListener(evt -> loadGuitars());
            
            guitarsPanel.add(Box.createVerticalStrut(50));
            guitarsPanel.add(errorLabel);
            guitarsPanel.add(Box.createVerticalStrut(20));
            guitarsPanel.add(retryButton);
            
            guitarsPanel.revalidate();
            guitarsPanel.repaint();
        }
    }
    
    /**
     * Search and filter guitars
     */
    public void searchGuitars(String keyword, GuitarType type, String brand, 
                             BigDecimal minPrice, BigDecimal maxPrice, Condition condition) {
        try {
            System.out.println("Searching guitars with filters...");
            
            currentGuitars = searchService.search(keyword, type, brand, minPrice, maxPrice, condition);
            System.out.println("Found " + currentGuitars.size() + " matching guitars");
            
            guitarsPanel.removeAll();
            
            if (currentGuitars.isEmpty()) {
                JLabel noResultsLabel = new JLabel("No guitars match your search criteria");
                noResultsLabel.setFont(Constants.DEFAULT_FONT);
                noResultsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                guitarsPanel.add(Box.createVerticalStrut(50));
                guitarsPanel.add(noResultsLabel);
            } else {
                for (Guitar guitar : currentGuitars) {
                    try {
                        GuitarCard card = new GuitarCard(guitar, parentFrame);
                        guitarsPanel.add(card);
                        guitarsPanel.add(Box.createVerticalStrut(10));
                    } catch (Exception e) {
                        System.err.println("Error creating guitar card: " + e.getMessage());
                        // Continue with next guitar
                    }
                }
            }
            
            guitarsPanel.revalidate();
            guitarsPanel.repaint();
            
            System.out.println("Search results displayed");
        } catch (Exception e) {
            System.err.println("Error during search: " + e.getMessage());
            e.printStackTrace();
            
            // Show error message
            guitarsPanel.removeAll();
            
            JLabel errorLabel = new JLabel("Error searching guitars: " + e.getMessage());
            errorLabel.setForeground(Color.RED);
            errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            guitarsPanel.add(Box.createVerticalStrut(50));
            guitarsPanel.add(errorLabel);
            
            guitarsPanel.revalidate();
            guitarsPanel.repaint();
        }
    }
}