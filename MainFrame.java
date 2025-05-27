package com.sixstringmarket.ui;

import com.sixstringmarket.model.User;
import com.sixstringmarket.service.AuthenticationService;
import com.sixstringmarket.util.ColorScheme;
import com.sixstringmarket.util.Constants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Modern main application window with responsive design,
 * animated transitions, and enhanced user experience
 */
public class MainFrame extends JFrame {
    
    // User info
    private User currentUser;
    
    // Main panels
    private JPanel mainPanel;
    private JPanel sidebarPanel;
    private JPanel contentPanel;
    private JPanel headerPanel;
    
    private JLabel headerTitleLabel;
    // Navigation components
    private List<NavButton> navButtons;
    private int activeNavIndex = 0;
    
    // Content panels
    private CardLayout contentCardLayout;
    private JPanel contentCardPanel;
    private Map<String, JPanel> cachedPanels;
    
    // Animation components
    private Timer animationTimer;
    private boolean sidebarCollapsed = false;
    
    /**
     * NavButton class for custom navigation buttons
     */
    private class NavButton extends JPanel {
        private String text;
        private String icon;
        private boolean selected;
        private ActionListener actionListener;
        
        public NavButton(String icon, String text) {
            this.icon = icon;
            this.text = text;
            this.selected = false;
            
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBackground(ColorScheme.TRANSPARENT);
            setPreferredSize(new Dimension(200, 40));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!selected) {
                        setBackground(ColorScheme.HOVER_HIGHLIGHT);
                        repaint();
                    }
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    if (!selected) {
                        setBackground(ColorScheme.TRANSPARENT);
                        repaint();
                    }
                }
                
                @Override
                public void mousePressed(MouseEvent e) {
                    if (!selected && actionListener != null) {
                        actionListener.actionPerformed(new ActionEvent(NavButton.this, ActionEvent.ACTION_PERFORMED, text));
                    }
                }
            });
        }
        
        public void setSelected(boolean selected) {
            this.selected = selected;
            setBackground(selected ? ColorScheme.SECONDARY : ColorScheme.TRANSPARENT);
            repaint();
        }
        
        public void addNavActionListener(ActionListener listener) {
            this.actionListener = listener;
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw background if selected or hovered
            if (selected || !getBackground().equals(ColorScheme.TRANSPARENT)) {
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            }
            
            // Draw selection indicator if selected
            if (selected) {
                g2d.setColor(ColorScheme.TEXT);
                g2d.fillRoundRect(0, 0, 3, getHeight(), 2, 2);
            }
            
            // Draw icon
            g2d.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 16));
            g2d.setColor(selected ? ColorScheme.TEXT : ColorScheme.TEXT_SECONDARY);
            g2d.drawString(icon, 15, 25);
            
            // Draw text
            if (!sidebarCollapsed) {
                g2d.setFont(new Font("Segoe UI", selected ? Font.BOLD : Font.PLAIN, 14));
                g2d.drawString(text, 45, 25);
            }
            
            g2d.dispose();
        }
    }
    
    /**
     * Constructor
     */
    public MainFrame() {
        try {
            System.out.println("Initializing MainFrame...");
            
            // Get current user
            currentUser = AuthenticationService.getInstance().getCurrentUser();
            if (currentUser == null) {
                System.err.println("Warning: Current user is null in MainFrame constructor");
            } else {
                System.out.println("Current user: " + currentUser.getUsername());
            }
            
            // Initialize panel cache
            cachedPanels = new HashMap<>();
            
            // Window settings
            setTitle("SixStringMarket");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(1280, 800);
            setMinimumSize(new Dimension(900, 600));
            setLocationRelativeTo(null);
            
            // Initialize UI components
            initComponents();
            
            // Show home screen by default - using try/catch to handle potential errors
            try {
                System.out.println("Setting active nav button");
                setActiveNavButton(0);
                
                System.out.println("Showing guitar list panel");
                showGuitarListPanel();
            } catch (Exception e) {
                System.err.println("Error initializing default view: " + e.getMessage());
                e.printStackTrace();
            }
            
            // Add window listener to clean up resources on close
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    cleanupResources();
                }
            });
            
            System.out.println("MainFrame initialization complete");
        } catch (Exception e) {
            System.err.println("Error in MainFrame constructor: " + e.getMessage());
            e.printStackTrace();
            throw e; // Rethrow to see the error
        }
    }
    
    /**
     * Initialize UI components
     */
    private void initComponents() {
        try {
            // Main container panel
            mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBackground(Constants.BACKGROUND_COLOR);
            
            // Create sidebar
            sidebarPanel = createSidebar();
            
            // Create header
            headerPanel = createHeader();
            
            // Create content panel with card layout for panel switching
            createContentCardPanel();
            
            // Create main content area with header and content
            contentPanel = new JPanel(new BorderLayout());
            contentPanel.setBackground(Constants.BACKGROUND_COLOR);
            contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            contentPanel.add(headerPanel, BorderLayout.NORTH);
            contentPanel.add(contentCardPanel, BorderLayout.CENTER);
            
            // Add main components to the frame
            mainPanel.add(sidebarPanel, BorderLayout.WEST);
            mainPanel.add(contentPanel, BorderLayout.CENTER);
            
            // Add status bar
            JPanel statusBar = createStatusBar();
            mainPanel.add(statusBar, BorderLayout.SOUTH);
            
            // Set as content pane
            setContentPane(mainPanel);
        } catch (Exception e) {
            // Handle any initialization errors
            System.err.println("Error initializing main frame: " + e.getMessage());
            e.printStackTrace();
            
            // Create a simple error panel
            JPanel errorPanel = new JPanel(new BorderLayout());
            errorPanel.setBackground(new Color(240, 240, 240));
            
            JLabel errorLabel = new JLabel("<html><h2>Error Initializing Application</h2>" + 
                                          "<p>The application could not initialize properly. Please restart.</p>" +
                                          "<p>Error details: " + e.getMessage() + "</p></html>", JLabel.CENTER);
            errorLabel.setForeground(Color.RED);
            errorLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            errorPanel.add(errorLabel, BorderLayout.CENTER);
            
            // Set as content pane
            setContentPane(errorPanel);
        }
    }
    
    /**
     * Create the sidebar with navigation buttons
     */
    private JPanel createSidebar() {
        // Sidebar container
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(ColorScheme.PRIMARY);
        sidebar.setPreferredSize(new Dimension(220, 0));
        
        // App logo and title at top
        JPanel logoPanel = new JPanel(new BorderLayout(10, 0));
        logoPanel.setOpaque(false);
        logoPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Guitar logo icon
        JLabel logoIcon = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw guitar icon
                g2d.setColor(ColorScheme.SECONDARY);
                
                // Body
                g2d.fillOval(5, 12, 20, 25);
                
                // Neck
                g2d.fillRect(13, 2, 4, 15);
                
                // Sound hole
                g2d.setColor(ColorScheme.PRIMARY);
                g2d.fillOval(10, 20, 10, 10);
                
                g2d.dispose();
            }
            
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(30, 40);
            }
        };
        
        // App name label
        JLabel appNameLabel = new JLabel("SixStringMarket");
        appNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        appNameLabel.setForeground(ColorScheme.TEXT);
        
        logoPanel.add(logoIcon, BorderLayout.WEST);
        logoPanel.add(appNameLabel, BorderLayout.CENTER);
        
        // Toggle button for collapsing sidebar
        JButton toggleButton = new JButton("«") {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(25, 25);
            }
        };
        toggleButton.setFont(new Font("Arial", Font.BOLD, 12));
        toggleButton.setForeground(ColorScheme.TEXT_SECONDARY);
        toggleButton.setBackground(ColorScheme.PRIMARY);
        toggleButton.setBorderPainted(false);
        toggleButton.setFocusPainted(false);
        toggleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        toggleButton.addActionListener(e -> toggleSidebar());
        
        logoPanel.add(toggleButton, BorderLayout.EAST);
        
        // Navigation buttons container
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setOpaque(false);
        navPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        
        // Navigation section label
        JLabel navLabel = new JLabel("NAVIGATION");
        navLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        navLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        navLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 0));
        navLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        navPanel.add(navLabel);
        
        // Navigation buttons
        navButtons = new ArrayList<>();
        
        // Home/All Guitars button
        NavButton homeButton = new NavButton("🏠", "All Guitars");
        homeButton.addNavActionListener(e -> {
            setActiveNavButton(0);
            showGuitarListPanel();
        });
        navButtons.add(homeButton);
        navPanel.add(homeButton);
        navPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        // My Listings button
        NavButton myListingsButton = new NavButton("📋", "My Listings");
        myListingsButton.addNavActionListener(e -> {
            setActiveNavButton(1);
            showMyGuitarsPanel();
        });
        navButtons.add(myListingsButton);
        navPanel.add(myListingsButton);
        navPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        // Saved Guitars button
        NavButton savedGuitarsButton = new NavButton("❤", "Saved Guitars");
        savedGuitarsButton.addNavActionListener(e -> {
            setActiveNavButton(2);
            showSavedGuitarsPanel();
        });
        navButtons.add(savedGuitarsButton);
        navPanel.add(savedGuitarsButton);
        navPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        // Order History button
        NavButton orderHistoryButton = new NavButton("🛒", "Order History");
        orderHistoryButton.addNavActionListener(e -> {
            setActiveNavButton(3);
            showOrderHistoryPanel();
        });
        navButtons.add(orderHistoryButton);
        navPanel.add(orderHistoryButton);
        navPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        // Profile button
        NavButton profileButton = new NavButton("👤", "Profile");
        profileButton.addNavActionListener(e -> {
            setActiveNavButton(4);
            showUserProfilePanel();
        });
        navButtons.add(profileButton);
        navPanel.add(profileButton);
        
        // Add filler to push buttons to top
        navPanel.add(Box.createVerticalGlue());
        
        // Separator before admin panel button
        JSeparator separator = new JSeparator();
        separator.setBackground(ColorScheme.TEXT_SECONDARY);
        separator.setForeground(ColorScheme.TEXT_SECONDARY);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        navPanel.add(separator);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Admin Panel button (if user is admin)
        if (AuthenticationService.getInstance().isAdmin()) {
            NavButton adminButton = new NavButton("⚙", "Admin Panel");
            adminButton.addNavActionListener(e -> {
                // Deselect all nav buttons
                for (NavButton btn : navButtons) {
                    btn.setSelected(false);
                }
                adminButton.setSelected(true);
                showAdminPanel();
            });
            navPanel.add(adminButton);
            navPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        
        // Logout button
        NavButton logoutButton = new NavButton("🚪", "Logout");
        logoutButton.addNavActionListener(e -> confirmLogout());
        navPanel.add(logoutButton);
        navPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // User info at bottom
        JPanel userInfoPanel = createUserInfoPanel();
        
        // Add components to sidebar
        sidebar.add(logoPanel, BorderLayout.NORTH);
        sidebar.add(navPanel, BorderLayout.CENTER);
        sidebar.add(userInfoPanel, BorderLayout.SOUTH);
        
        return sidebar;
    }
    
    /**
     * Create user info panel for sidebar
     */
    private JPanel createUserInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ColorScheme.darken(ColorScheme.PRIMARY, 0.05f));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // User avatar
        JPanel avatarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw circle
                g2d.setColor(ColorScheme.SECONDARY);
                g2d.fillOval(0, 0, getWidth(), getHeight());
                
                // Draw initial
                String initial = currentUser.getUsername().substring(0, 1).toUpperCase();
                g2d.setColor(ColorScheme.PRIMARY);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
                
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(initial)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                
                g2d.drawString(initial, x, y);
                
                g2d.dispose();
            }
            
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(35, 35);
            }
        };
        
        // User info
        JPanel userTextPanel = new JPanel();
        userTextPanel.setLayout(new BoxLayout(userTextPanel, BoxLayout.Y_AXIS));
        userTextPanel.setOpaque(false);
        
        JLabel usernameLabel = new JLabel(currentUser.getUsername());
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        usernameLabel.setForeground(ColorScheme.TEXT);
        
        JLabel roleLabel = new JLabel(currentUser.getRole().name().toLowerCase());
        roleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        roleLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        
        userTextPanel.add(usernameLabel);
        userTextPanel.add(roleLabel);
        
        // Only add user text if sidebar is expanded
        if (!sidebarCollapsed) {
            panel.add(avatarPanel, BorderLayout.WEST);
            panel.add(Box.createRigidArea(new Dimension(10, 0)), BorderLayout.CENTER);
            panel.add(userTextPanel, BorderLayout.EAST);
        } else {
            panel.add(avatarPanel, BorderLayout.CENTER);
        }
        
        return panel;
    }
    
    /**
     * Create header panel
     */
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(ColorScheme.CARD_BG);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, ColorScheme.darken(ColorScheme.CARD_BG, 0.1f)),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        
        // Left section with title
        JLabel pageTitle = new JLabel("All Guitars");
        pageTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        pageTitle.setForeground(ColorScheme.TEXT);
        
        // Center section with search
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        searchPanel.setOpaque(false);
       
        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchButton.setBackground(ColorScheme.SECONDARY);
        searchButton.setForeground(Color.WHITE);
        searchButton.setBorderPainted(false);
        searchButton.setFocusPainted(false);
        
        // Hover effect
        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                searchButton.setBackground(ColorScheme.lighten(ColorScheme.SECONDARY, 0.1f));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                searchButton.setBackground(ColorScheme.SECONDARY);
            }
        });
        
        
        // Right section with add guitar button
        JButton addGuitarButton = new JButton("+ Add Guitar");
        addGuitarButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addGuitarButton.setBackground(ColorScheme.SECONDARY);
        addGuitarButton.setForeground(Color.WHITE);
        addGuitarButton.setBorderPainted(false);
        addGuitarButton.setFocusPainted(false);
        addGuitarButton.addActionListener(e -> showAddGuitarFrame());
        
        // Hover effect
        addGuitarButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                addGuitarButton.setBackground(ColorScheme.lighten(ColorScheme.SECONDARY, 0.1f));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                addGuitarButton.setBackground(ColorScheme.SECONDARY);
            }
        });
        
        header.add(pageTitle, BorderLayout.WEST);
        header.add(searchPanel, BorderLayout.CENTER);
        header.add(addGuitarButton, BorderLayout.EAST);
        
        return header;
    }
    
    /**
     * Create status bar
     */
    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(ColorScheme.CARD_BG);
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, ColorScheme.darken(ColorScheme.CARD_BG, 0.1f)));
        statusBar.add(Box.createVerticalStrut(1));
        
        // Version info
        JLabel versionLabel = new JLabel("SixStringMarket v1.0");
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        versionLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        versionLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // Copyright info
        JLabel copyrightLabel = new JLabel("© " + java.time.Year.now().getValue() + " SixStringMarket");
        copyrightLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        copyrightLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        copyrightLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        statusBar.add(versionLabel, BorderLayout.WEST);
        statusBar.add(copyrightLabel, BorderLayout.EAST);
        
        return statusBar;
    }
    
    /**
     * Toggle sidebar between expanded and collapsed states
     */
    private void toggleSidebar() {
        sidebarCollapsed = !sidebarCollapsed;
        
        // Target width
        final int targetWidth = sidebarCollapsed ? 60 : 220;
        final int startWidth = sidebarPanel.getWidth();
        final int distance = targetWidth - startWidth;
        
        // Disable toggle button during animation
        JButton toggleButton = (JButton) ((JPanel) sidebarPanel.getComponent(0)).getComponent(2);
        toggleButton.setEnabled(false);
        toggleButton.setText(sidebarCollapsed ? "»" : "«");
        
        // Animation
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
        
        final int steps = 10;
        final int[] currentStep = {0};
        
        animationTimer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentStep[0]++;
                if (currentStep[0] >= steps) {
                    // Animation complete
                    sidebarPanel.setPreferredSize(new Dimension(targetWidth, 0));
                    mainPanel.revalidate();
                    
                    // Update user info panel
                    JPanel userInfoPanel = createUserInfoPanel();
                    sidebarPanel.remove(sidebarPanel.getComponentCount() - 1);
                    sidebarPanel.add(userInfoPanel, BorderLayout.SOUTH);
                    
                    toggleButton.setEnabled(true);
                    animationTimer.stop();
                    
                    sidebarPanel.repaint();
                    return;
                }
                
                // Calculate intermediate width
                float progress = (float) currentStep[0] / steps;
                int newWidth = startWidth + (int) (distance * progress);
                sidebarPanel.setPreferredSize(new Dimension(newWidth, 0));
                mainPanel.revalidate();
            }
        });
        
        animationTimer.start();
    }
    
    /**
     * Set active navigation button
     */
    private void setActiveNavButton(int index) {
        activeNavIndex = index;
        
        // Update navigation buttons
        for (int i = 0; i < navButtons.size(); i++) {
            navButtons.get(i).setSelected(i == index);
        }
    }
    
    /**
     * Update header title
     */
    /**
     * Update header title
     */
    private void updateHeaderTitle(String title) {
        try {
            // Look through all components in the header panel to find the title label
            for (Component component : headerPanel.getComponents()) {
                // Check if the component is the title label directly
                if (component instanceof JLabel) {
                    JLabel titleLabel = (JLabel) component;
                    titleLabel.setText(title);
                    return;
                }
                
                // If not, check if it's a container that might contain the title label
                if (component instanceof Container) {
                    Container container = (Container) component;
                    for (Component innerComponent : container.getComponents()) {
                        if (innerComponent instanceof JLabel) {
                            JLabel titleLabel = (JLabel) innerComponent;
                            titleLabel.setText(title);
                            return;
                        }
                        
                        // Look one level deeper if needed
                        if (innerComponent instanceof Container) {
                            Container innerContainer = (Container) innerComponent;
                            for (Component deepComponent : innerContainer.getComponents()) {
                                if (deepComponent instanceof JLabel) {
                                    JLabel titleLabel = (JLabel) deepComponent;
                                    titleLabel.setText(title);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
            
            // If we couldn't find the title label, log a warning
            System.out.println("Warning: Could not find title label in header panel");
        } catch (Exception e) {
            System.err.println("Error updating header title: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Confirm logout dialog
     */
    private void confirmLogout() {
        JDialog dialog = new JDialog(this, "Confirm Logout", true);
        dialog.setSize(350, 180);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(ColorScheme.CARD_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel messageLabel = new JLabel("Are you sure you want to logout?");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        messageLabel.setForeground(ColorScheme.TEXT);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelButton.setBackground(ColorScheme.BUTTON_SECONDARY);
        cancelButton.setForeground(ColorScheme.TEXT);
        cancelButton.setBorderPainted(false);
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(e -> dialog.dispose());
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutButton.setBackground(ColorScheme.SECONDARY);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBorderPainted(false);
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(e -> {
            AuthenticationService.getInstance().logout();
            dialog.dispose();
            dispose();
            new LoginFrame().setVisible(true);
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(logoutButton);
        
        panel.add(messageLabel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    /**
     * Show guitar list panel
     */
    public void showGuitarListPanel() {
        try {
            System.out.println("Showing guitar list panel");
            
            // Update title first to avoid issues if panel creation fails
            updateHeaderTitle("All Guitars");
            
            // Check if panel already exists in cache
            if (cachedPanels.containsKey("guitarList")) {
                contentCardLayout.show(contentCardPanel, "guitarList");
                return;
            }
            
            // Create panel
            System.out.println("Creating new guitar list panel");
            GuitarListPanel guitarListPanel = new GuitarListPanel(this);
            contentCardPanel.add(guitarListPanel, "guitarList");
            cachedPanels.put("guitarList", guitarListPanel);
            contentCardLayout.show(contentCardPanel, "guitarList");
            
            System.out.println("Guitar list panel shown successfully");
        } catch (Exception e) {
            System.err.println("Error showing guitar list panel: " + e.getMessage());
            e.printStackTrace();
            
            // Show error in the content panel
            JPanel errorPanel = new JPanel(new BorderLayout());
            errorPanel.setBackground(Color.WHITE);
            errorPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            JLabel errorLabel = new JLabel("<html>Error loading guitar list:<br>" + e.getMessage() + "</html>");
            errorLabel.setForeground(Color.RED);
            errorPanel.add(errorLabel, BorderLayout.CENTER);
            
            // Add a refresh button
            JButton refreshButton = new JButton("Try Again");
            refreshButton.addActionListener(ev -> showGuitarListPanel());
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.add(refreshButton);
            errorPanel.add(buttonPanel, BorderLayout.SOUTH);
            
            // Add error panel to content area
            contentCardPanel.add(errorPanel, "error");
            contentCardLayout.show(contentCardPanel, "error");
        }
    }
    
    /**
     * Show guitar details panel
     */
    public void showGuitarDetailsPanel(int guitarId) {
        // Create unique key for this guitar
        String key = "guitarDetails_" + guitarId;
        
        // Create panel if it doesn't exist in cache
        if (!cachedPanels.containsKey(key)) {
            GuitarDetailsPanel guitarDetailsPanel = new GuitarDetailsPanel(this, guitarId);
            contentCardPanel.add(guitarDetailsPanel, key);
            cachedPanels.put(key, guitarDetailsPanel);
        }
        
        contentCardLayout.show(contentCardPanel, key);
        updateHeaderTitle("Guitar Details");
    }
    
    /**
     * Show my guitars panel
     */
    public void showMyGuitarsPanel() {
        // Check if panel already exists in cache
        if (cachedPanels.containsKey("myGuitars")) {
            // Get the panel and refresh it
            GuitarListPanel panel = (GuitarListPanel) cachedPanels.get("myGuitars");
            panel.loadGuitars(); // Refresh the panel content
            
            contentCardLayout.show(contentCardPanel, "myGuitars");
            updateHeaderTitle("My Listings");
            return;
        }
        
        // Create panel
        GuitarListPanel myGuitarsPanel = new GuitarListPanel(this, currentUser.getUserId());
        contentCardPanel.add(myGuitarsPanel, "myGuitars");
        cachedPanels.put("myGuitars", myGuitarsPanel);
        contentCardLayout.show(contentCardPanel, "myGuitars");
        updateHeaderTitle("My Listings");
    }
    
    /**
     * Show saved guitars panel
     */
    public void showSavedGuitarsPanel() {
        // Check if panel already exists in cache
        if (cachedPanels.containsKey("savedGuitars")) {
            // Get the panel and refresh it to ensure up-to-date content
            SavedGuitarsPanel panel = (SavedGuitarsPanel) cachedPanels.get("savedGuitars");
            panel.loadSavedGuitars(); // Need to ensure this method exists
            
            contentCardLayout.show(contentCardPanel, "savedGuitars");
            updateHeaderTitle("Saved Guitars");
            return;
        }
        
        // Create panel
        SavedGuitarsPanel savedGuitarsPanel = new SavedGuitarsPanel(this);
        contentCardPanel.add(savedGuitarsPanel, "savedGuitars");
        cachedPanels.put("savedGuitars", savedGuitarsPanel);
        contentCardLayout.show(contentCardPanel, "savedGuitars");
        updateHeaderTitle("Saved Guitars");
    }
    
    /**
     * Show order history panel
     */
    public void showOrderHistoryPanel() {
        // Check if panel already exists in cache
        if (cachedPanels.containsKey("orderHistory")) {
            // Get the panel and refresh its content
            OrderHistoryPanel panel = (OrderHistoryPanel) cachedPanels.get("orderHistory");
            panel.loadOrders(); // Ensure this method exists for refreshing
            
            contentCardLayout.show(contentCardPanel, "orderHistory");
            updateHeaderTitle("Order History");
            return;
        }
        
        // Create panel
        OrderHistoryPanel orderHistoryPanel = new OrderHistoryPanel(this);
        contentCardPanel.add(orderHistoryPanel, "orderHistory");
        cachedPanels.put("orderHistory", orderHistoryPanel);
        contentCardLayout.show(contentCardPanel, "orderHistory");
        updateHeaderTitle("Order History");
    }
    
    /**
     * Show user profile panel
     */
    public void showUserProfilePanel() {
        // Check if panel already exists in cache
        if (cachedPanels.containsKey("userProfile")) {
            contentCardLayout.show(contentCardPanel, "userProfile");
            updateHeaderTitle("My Profile");
            return;
        }
        
        // Create panel
        UserProfilePanel userProfilePanel = new UserProfilePanel(this);
        contentCardPanel.add(userProfilePanel, "userProfile");
        cachedPanels.put("userProfile", userProfilePanel);
        contentCardLayout.show(contentCardPanel, "userProfile");
        updateHeaderTitle("My Profile");
    }
    
    /**
     * Show admin panel
     */
    public void showAdminPanel() {
        // Make sure user is admin
        if (!AuthenticationService.getInstance().isAdmin()) {
            JOptionPane.showMessageDialog(this,
                "You do not have permission to access the admin panel.",
                "Access Denied",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Check if panel already exists in cache
        if (cachedPanels.containsKey("adminPanel")) {
            contentCardLayout.show(contentCardPanel, "adminPanel");
            updateHeaderTitle("Admin Panel");
            return;
        }
        
        // Create panel
        AdminPanel adminPanel = new AdminPanel(this);
        contentCardPanel.add(adminPanel, "adminPanel");
        cachedPanels.put("adminPanel", adminPanel);
        contentCardLayout.show(contentCardPanel, "adminPanel");
        updateHeaderTitle("Admin Panel");
    }
    
    /**
     * Show add guitar frame
     */
    public void showAddGuitarFrame() {
        AddGuitarFrame addGuitarFrame = new AddGuitarFrame(this);
        addGuitarFrame.setVisible(true);
    }
    
    /**
     * Show edit guitar frame
     */
    public void showEditGuitarFrame(int guitarId) {
        EditGuitarFrame editGuitarFrame = new EditGuitarFrame(this, guitarId);
        editGuitarFrame.setVisible(true);
    }
    private void createContentCardPanel() {
        try {
            // Create the main card layout
            contentCardLayout = new CardLayout();
            contentCardPanel = new JPanel(contentCardLayout);
            contentCardPanel.setOpaque(false);
            
            // Add an initial empty panel as placeholder
            JPanel emptyPanel = new JPanel(new BorderLayout());
            emptyPanel.setBackground(Constants.BACKGROUND_COLOR);
            JLabel welcomeLabel = new JLabel("Welcome to SixStringMarket", JLabel.CENTER);
            welcomeLabel.setFont(Constants.TITLE_FONT);
            welcomeLabel.setForeground(Constants.PRIMARY_COLOR);
            emptyPanel.add(welcomeLabel, BorderLayout.CENTER);
            
            // Add the empty panel to the card layout
            contentCardPanel.add(emptyPanel, "empty");
            contentCardLayout.show(contentCardPanel, "empty");
        } catch (Exception e) {
            System.err.println("Error creating content card panel: " + e.getMessage());
            e.printStackTrace();
            
            // Create a simple panel with error message if the card layout fails
            contentCardPanel = new JPanel(new BorderLayout());
            contentCardPanel.setBackground(Constants.BACKGROUND_COLOR);
            JLabel errorLabel = new JLabel("Error initializing content panel: " + e.getMessage(), JLabel.CENTER);
            errorLabel.setForeground(Color.RED);
            contentCardPanel.add(errorLabel, BorderLayout.CENTER);
        }
    }
    /**
     * Clean up resources when the frame is closed
     */
    private void cleanupResources() {
        // Stop any running timers
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
        
        // Clear caches
        cachedPanels.clear();
    }
}