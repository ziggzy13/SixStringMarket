package com.sixstringmarket.ui;

import com.sixstringmarket.model.User;
import com.sixstringmarket.service.AuthenticationService;
import com.sixstringmarket.util.ColorScheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Main application window of SixStringMarket with responsive design,
 * smooth animated transitions, and enhanced user experience
 */
public class MainFrame extends JFrame {
    
    // User info
    private User currentUser;
    
    // Main panels
    private JPanel mainPanel;
    private JPanel sidebarPanel;
    private JPanel contentPanel;
    private JPanel headerPanel;
    
    // Navigation components
    private List<NavButton> navButtons;
    private int activeNavIndex = 0;
    
    // Content panels
    private CardLayout contentCardLayout;
    private JPanel contentCardPanel;
    
    // Animation components
    private Timer animationTimer;
    private boolean sidebarCollapsed = false;
    private final int EXPANDED_SIDEBAR_WIDTH = 220;
    private final int COLLAPSED_SIDEBAR_WIDTH = 60;
    
    /**
     * NavButton class for custom navigation buttons
     */
    private class NavButton extends JPanel {
        private final String text;
        private final String icon;
        private boolean selected;
        private ActionListener actionListener;
        
        public NavButton(String icon, String text) {
            this.icon = icon;
            this.text = text;
            this.selected = false;
            
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBackground(ColorScheme.TRANSPARENT);
            setPreferredSize(new Dimension(EXPANDED_SIDEBAR_WIDTH, 40));
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
            
            // Draw text only if sidebar is expanded
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
        // Get current user
        currentUser = AuthenticationService.getInstance().getCurrentUser();
        
        // Window settings
        setTitle("SixStringMarket");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 800);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        
        // Initialize UI components with better error handling
        try {
            initComponents();
            
            // Show home screen by default
            setActiveNavButton(0);
            showGuitarListPanel();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error initializing UI: " + e.getMessage(),
                "Initialization Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Initialize UI components
     */
    private void initComponents() {
        // Main container panel
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ColorScheme.BACKGROUND);
        
        // Create sidebar
        sidebarPanel = createSidebar();
        
        // Create header
        headerPanel = createHeader();
        
        // Create content panel with card layout for panel switching
        contentCardLayout = new CardLayout();
        contentCardPanel = new JPanel(contentCardLayout);
        contentCardPanel.setOpaque(false);
        
        // Create main content area with header and content
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(ColorScheme.BACKGROUND);
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
    }
    
    /**
     * Create the sidebar with navigation buttons
     */
    private JPanel createSidebar() {
        // Sidebar container
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(ColorScheme.PRIMARY);
        sidebar.setPreferredSize(new Dimension(EXPANDED_SIDEBAR_WIDTH, 0));
        
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
        
        JTextField searchField = new JTextField(25);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBackground(ColorScheme.FIELD_BG);
        searchField.setForeground(ColorScheme.TEXT);
        searchField.setCaretColor(ColorScheme.TEXT);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorScheme.FIELD_BORDER),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        searchField.putClientProperty("JTextField.placeholderText", "Search for guitars...");
        
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
        
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
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
        final int targetWidth = sidebarCollapsed ? COLLAPSED_SIDEBAR_WIDTH : EXPANDED_SIDEBAR_WIDTH;
        final int startWidth = sidebarPanel.getWidth();
        final int distance = targetWidth - startWidth;
        
        // Disable toggle button during animation
        JButton toggleButton = (JButton) ((JPanel) sidebarPanel.getComponent(0)).getComponent(2);
        toggleButton.setEnabled(false);
        toggleButton.setText(sidebarCollapsed ? "»" : "«");
        
        // Stop any running animation
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
                
                // Calculate intermediate width using easing for smoother animation
                float progress = (float) currentStep[0] / steps;
                // Quadratic easing for smoother transition
                float easedProgress = progress * (2 - progress);
                int newWidth = startWidth + (int) (distance * easedProgress);
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
        if (index < 0 || index >= navButtons.size()) {
            return;
        }
        
        activeNavIndex = index;
        
        // Update navigation buttons
        for (int i = 0; i < navButtons.size(); i++) {
            navButtons.get(i).setSelected(i == index);
        }
    }
    
    /**
     * Update header title
     */
    private void updateHeaderTitle(String title) {
        if (headerPanel == null || headerPanel.getComponentCount() == 0) {
            return;
        }
        
        try {
            JLabel titleLabel = (JLabel) ((JPanel) headerPanel.getComponent(0)).getComponent(0);
            titleLabel.setText(title);
        } catch (Exception e) {
            System.err.println("Error updating header title: " + e.getMessage());
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
            try {
                AuthenticationService.getInstance().logout();
                dialog.dispose();
                dispose();
                new LoginFrame().setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    dialog,
                    "Error during logout: " + ex.getMessage(),
                    "Logout Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
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
            // Check if panel already exists
            if (contentCardPanel.getComponentCount() > 0) {
                try {
                    contentCardLayout.show(contentCardPanel, "guitarList");
                    updateHeaderTitle("All Guitars");
                    return;
                } catch (IllegalArgumentException e) {
                    // Panel doesn't exist yet, create it
                }
            }
            
            // Create panel
            GuitarListPanel guitarListPanel = new GuitarListPanel(this);
            contentCardPanel.add(guitarListPanel, "guitarList");
            contentCardLayout.show(contentCardPanel, "guitarList");
            updateHeaderTitle("All Guitars");
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage("Error loading guitar list: " + e.getMessage());
        }
    }
    
    /**
     * Show guitar details panel
     */
    public void showGuitarDetailsPanel(int guitarId) {
        try {
            // Create unique key for this guitar
            String key = "guitarDetails_" + guitarId;
            
            // Create panel
            GuitarDetailsPanel guitarDetailsPanel = new GuitarDetailsPanel(this, guitarId);
            contentCardPanel.add(guitarDetailsPanel, key);
            contentCardLayout.show(contentCardPanel, key);
            updateHeaderTitle("Guitar Details");
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage("Error loading guitar details: " + e.getMessage());
        }
    }
    
    /**
     * Show my guitars panel
     */
    public void showMyGuitarsPanel() {
        try {
            // Check if panel already exists
            if (contentCardPanel.getComponentCount() > 0) {
                try {
                    contentCardLayout.show(contentCardPanel, "myGuitars");
                    updateHeaderTitle("My Listings");
                    return;
                } catch (IllegalArgumentException e) {
                    // Panel doesn't exist yet, create it
                }
            }
            
            // Create panel
            GuitarListPanel myGuitarsPanel = new GuitarListPanel(this, currentUser.getUserId());
            contentCardPanel.add(myGuitarsPanel, "myGuitars");
            contentCardLayout.show(contentCardPanel, "myGuitars");
            updateHeaderTitle("My Listings");
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage("Error loading your guitar listings: " + e.getMessage());
        }
    }
    
    /**
     * Show saved guitars panel
     */
    public void showSavedGuitarsPanel() {
        try {
            // Check if panel already exists
            if (contentCardPanel.getComponentCount() > 0) {
                try {
                    contentCardLayout.show(contentCardPanel, "savedGuitars");
                    updateHeaderTitle("Saved Guitars");
                    return;
                } catch (IllegalArgumentException e) {
                    // Panel doesn't exist yet, create it
                }
            }
            
            // Create panel
            SavedGuitarsPanel savedGuitarsPanel = new SavedGuitarsPanel(this);
            contentCardPanel.add(savedGuitarsPanel, "savedGuitars");
            contentCardLayout.show(contentCardPanel, "savedGuitars");
            updateHeaderTitle("Saved Guitars");
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage("Error loading saved guitars: " + e.getMessage());
        }
    }
    
    /**
     * Show order history panel
     */
    public void showOrderHistoryPanel() {
        try {
            // Check if panel already exists
            if (contentCardPanel.getComponentCount() > 0) {
                try {
                    contentCardLayout.show(contentCardPanel, "orderHistory");
                    updateHeaderTitle("Order History");
                    return;
                } catch (IllegalArgumentException e) {
                    // Panel doesn't exist yet, create it
                }
            }
            
            // Create panel
            OrderHistoryPanel orderHistoryPanel = new OrderHistoryPanel(this);
            contentCardPanel.add(orderHistoryPanel, "orderHistory");
            contentCardLayout.show(contentCardPanel, "orderHistory");
            updateHeaderTitle("Order History");
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage("Error loading order history: " + e.getMessage());
        }
    }
    
    /**
     * Show user profile panel
     */
    public void showUserProfilePanel() {
        try {
            // Check if panel already exists
            if (contentCardPanel.getComponentCount() > 0) {
                try {
                    contentCardLayout.show(contentCardPanel, "userProfile");
                    updateHeaderTitle("My Profile");
                    return;
                } catch (IllegalArgumentException e) {
                    // Panel doesn't exist yet, create it
                }
            }
            
            // Create panel
            UserProfilePanel userProfilePanel = new UserProfilePanel(this);
            contentCardPanel.add(userProfilePanel, "userProfile");
            contentCardLayout.show(contentCardPanel, "userProfile");
            updateHeaderTitle("My Profile");
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage("Error loading user profile: " + e.getMessage());
        }
    }
    
    /**
     * Show admin panel
     */
    public void showAdminPanel() {
        try {
            // Make sure user is admin
            if (!AuthenticationService.getInstance().isAdmin()) {
                JOptionPane.showMessageDialog(this,
                    "You do not have permission to access the admin panel.",
                    "Access Denied",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Check if panel already exists
            if (contentCardPanel.getComponentCount() > 0) {
                try {
                    contentCardLayout.show(contentCardPanel, "adminPanel");
                    updateHeaderTitle("Admin Panel");
                    return;
                } catch (IllegalArgumentException e) {
                    // Panel doesn't exist yet, create it
                }
            }
            
            // Create panel
            AdminPanel adminPanel = new AdminPanel(this);
            contentCardPanel.add(adminPanel, "adminPanel");
            contentCardLayout.show(contentCardPanel, "adminPanel");
            updateHeaderTitle("Admin Panel");
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage("Error loading admin panel: " + e.getMessage());
        }
    }
    
    /**
     * Show add guitar frame
     */
    public void showAddGuitarFrame() {
        try {
            AddGuitarFrame addGuitarFrame = new AddGuitarFrame(this);
            addGuitarFrame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage("Error opening add guitar form: " + e.getMessage());
        }
    }
    
    /**
     * Show edit guitar frame
     */
    public void showEditGuitarFrame(int guitarId) {
        try {
            EditGuitarFrame editGuitarFrame = new EditGuitarFrame(this, guitarId);
            editGuitarFrame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage("Error opening edit guitar form: " + e.getMessage());
        }
    }
    
    /**
     * Show error message dialog
     */
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }
}