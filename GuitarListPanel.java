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
    private int sellerId; // If != 0, shows guitars for a specific seller
    
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
        this.parentFrame = parentFrame;
        this.guitarService = new GuitarService();
        this.searchService = new SearchService();
        this.sellerId = sellerId;
        
        setLayout(new BorderLayout());
        setBackground(Constants.BACKGROUND_COLOR);
        
        try {
            initComponents();
            
            // Load guitars
            loadGuitars();
        } catch (Exception e) {
            System.err.println("Error initializing guitar list panel: " + e.getMessage());
            e.printStackTrace();
            
            // Show error message instead of crashing
            removeAll();
            setLayout(new BorderLayout());
            JLabel errorLabel = new JLabel("Error loading guitars: " + e.getMessage(), JLabel.CENTER);
            errorLabel.setForeground(Color.RED);
            add(errorLabel, BorderLayout.CENTER);
        }
    }
    
    /**
     * Initialize panel components
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
            
            // If this is a list of all guitars, add search panel
            if (sellerId == 0) {
                try {
                    // Create the search filter panel in its own container
                    JPanel searchContainer = new JPanel(new BorderLayout());
                    searchContainer.setBackground(Constants.BACKGROUND_COLOR);
                    
                    searchFilterPanel = new SearchFilterPanel(this);
                    searchContainer.add(searchFilterPanel, BorderLayout.CENTER);
                    
                    // Add to the title panel
                    titlePanel.add(searchContainer, BorderLayout.CENTER);
                } catch (Exception e) {
                    System.err.println("Error creating search filter panel: " + e.getMessage());
                    e.printStackTrace();
                    
                    // Add an error message instead
                    JLabel searchErrorLabel = new JLabel("Error loading search filters", JLabel.CENTER);
                    searchErrorLabel.setForeground(Color.RED);
                    titlePanel.add(searchErrorLabel, BorderLayout.CENTER);
                }
            }
            
            // Add guitar button
            if (AuthenticationService.getInstance().isAuthenticated()) {
                JButton addGuitarButton = new JButton("Add Guitar");
                addGuitarButton.setBackground(Constants.ACCENT_COLOR);
                addGuitarButton.setForeground(Color.WHITE);
                addGuitarButton.addActionListener(e -> {
                    try {
                        parentFrame.showAddGuitarFrame();
                    } catch (Exception ex) {
                        System.err.println("Error showing add guitar frame: " + ex.getMessage());
                        JOptionPane.showMessageDialog(
                            this,
                            "Error opening add guitar form: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                });
                titlePanel.add(addGuitarButton, BorderLayout.EAST);
            }
            
            add(titlePanel, BorderLayout.NORTH);
            
            // Panel for list of guitars
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
            // Handle any initialization errors
            System.err.println("Error initializing guitar list panel: " + e.getMessage());
            e.printStackTrace();
            
            // Clear any existing components
            removeAll();
            setLayout(new BorderLayout());
            
            // Create an error panel
            JPanel errorPanel = new JPanel(new BorderLayout());
            errorPanel.setBackground(Constants.BACKGROUND_COLOR);
            
            JLabel errorLabel = new JLabel("<html><center>Error loading guitar list:<br>" + 
                                          e.getMessage() + "</center></html>", JLabel.CENTER);
            errorLabel.setForeground(Color.RED);
            errorLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            errorPanel.add(errorLabel, BorderLayout.CENTER);
            add(errorPanel, BorderLayout.CENTER);
        }
    }
    
    /**
     * Load guitars from database
     */
    public void loadGuitars() {
        try {
            guitarsPanel.removeAll();
            
            if (sellerId == 0) {
                currentGuitars = guitarService.getAllActiveGuitars();
            } else {
                currentGuitars = guitarService.getGuitarsBySeller(sellerId);
            }
            
            if (currentGuitars.isEmpty()) {
                JPanel emptyMessagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                emptyMessagePanel.setBackground(Constants.BACKGROUND_COLOR);
                
                JLabel noGuitarsLabel = new JLabel("No guitars available");
                noGuitarsLabel.setFont(Constants.DEFAULT_FONT);
                emptyMessagePanel.add(noGuitarsLabel);
                
                guitarsPanel.add(Box.createVerticalStrut(50));
                guitarsPanel.add(emptyMessagePanel);
            } else {
                for (Guitar guitar : currentGuitars) {
                    if (guitar != null) {
                        try {
                            GuitarCard card = new GuitarCard(guitar, parentFrame);
                            // Wrap card in a panel to ensure proper layout
                            JPanel cardWrapper = new JPanel(new BorderLayout());
                            cardWrapper.setBackground(Constants.BACKGROUND_COLOR);
                            cardWrapper.add(card, BorderLayout.CENTER);
                            guitarsPanel.add(cardWrapper);
                            guitarsPanel.add(Box.createVerticalStrut(10));
                        } catch (Exception e) {
                            System.err.println("Error creating card for guitar ID " + guitar.getGuitarId() + ": " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            }
            
            guitarsPanel.revalidate();
            guitarsPanel.repaint();
        } catch (Exception e) {
            System.err.println("Error loading guitars: " + e.getMessage());
            e.printStackTrace();
            
            // Show error message in panel
            guitarsPanel.removeAll();
            JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            errorPanel.setBackground(Constants.BACKGROUND_COLOR);
            
            JLabel errorLabel = new JLabel("Error loading guitars: " + e.getMessage());
            errorLabel.setForeground(Color.RED);
            errorPanel.add(errorLabel);
            
            guitarsPanel.add(Box.createVerticalStrut(50));
            guitarsPanel.add(errorPanel);
            
            guitarsPanel.revalidate();
            guitarsPanel.repaint();
        }
    }
    
    /**
     * Search and filter guitars
     * @param keyword Keyword
     * @param type Guitar type
     * @param brand Brand
     * @param minPrice Minimum price
     * @param maxPrice Maximum price
     * @param condition Condition
     */
    public void searchGuitars(String keyword, GuitarType type, String brand, 
                             BigDecimal minPrice, BigDecimal maxPrice, Condition condition) {
        try {
            currentGuitars = searchService.search(keyword, type, brand, minPrice, maxPrice, condition);
            
            guitarsPanel.removeAll();
            
            if (currentGuitars.isEmpty()) {
                JPanel emptyMessagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                emptyMessagePanel.setBackground(Constants.BACKGROUND_COLOR);
                
                JLabel noResultsLabel = new JLabel("No guitars found matching your criteria");
                noResultsLabel.setFont(Constants.DEFAULT_FONT);
                emptyMessagePanel.add(noResultsLabel);
                
                guitarsPanel.add(Box.createVerticalStrut(50));
                guitarsPanel.add(emptyMessagePanel);
            } else {
                for (Guitar guitar : currentGuitars) {
                    if (guitar != null) {
                        try {
                            GuitarCard card = new GuitarCard(guitar, parentFrame);
                            // Wrap card in a panel to ensure proper layout
                            JPanel cardWrapper = new JPanel(new BorderLayout());
                            cardWrapper.setBackground(Constants.BACKGROUND_COLOR);
                            cardWrapper.add(card, BorderLayout.CENTER);
                            guitarsPanel.add(cardWrapper);
                            guitarsPanel.add(Box.createVerticalStrut(10));
                        } catch (Exception e) {
                            System.err.println("Error creating card for guitar ID " + guitar.getGuitarId() + ": " + e.getMessage());
                        }
                    }
                }
            }
            
            guitarsPanel.revalidate();
            guitarsPanel.repaint();
        } catch (Exception e) {
            System.err.println("Error searching guitars: " + e.getMessage());
            e.printStackTrace();
            
            // Show error message in panel
            guitarsPanel.removeAll();
            JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            errorPanel.setBackground(Constants.BACKGROUND_COLOR);
            
            JLabel errorLabel = new JLabel("Error searching guitars: " + e.getMessage());
            errorLabel.setForeground(Color.RED);
            errorPanel.add(errorLabel);
            
            guitarsPanel.add(Box.createVerticalStrut(50));
            guitarsPanel.add(errorPanel);
            
            guitarsPanel.revalidate();
            guitarsPanel.repaint();
        }
    }
}