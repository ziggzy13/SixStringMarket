package com.sixstringmarket.ui;

import com.sixstringmarket.ui.components.*;
import com.sixstringmarket.util.StyleManager;
import com.sixstringmarket.util.AnimationUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Example of how to use the modern UI components
 */
public class ModernUIExample extends JFrame {
    
    // Define sidebar state
    private boolean sidebarCollapsed = false;
    private JPanel sidebarPanel;
    private JPanel contentPanel;
    
    public ModernUIExample() {
        // Basic window setup
        setTitle("SixStringMarket - Modern UI Example");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Create main layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(StyleManager.BACKGROUND_COLOR);
        
        // Create sidebar
        sidebarPanel = createSidebar();
        mainPanel.add(sidebarPanel, BorderLayout.WEST);
        
        // Create content area
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(StyleManager.BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Add sample content
        JPanel sampleContent = createSampleContent();
        contentPanel.add(sampleContent, BorderLayout.CENTER);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Set as content pane
        setContentPane(mainPanel);
    }
    
    /**
     * Create the sidebar with navigation
     */
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(StyleManager.PRIMARY_COLOR);
        sidebar.setPreferredSize(new Dimension(220, 0)); // Starting width
        
        // Sidebar header with logo and toggle button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(StyleManager.PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // App logo and title
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        logoPanel.setBackground(StyleManager.PRIMARY_COLOR);
        
        // Simple guitar icon
        JLabel logoIcon = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw guitar icon
                g2d.setColor(StyleManager.SECONDARY_COLOR);
                
                // Body
                g2d.fillOval(5, 12, 20, 25);
                
                // Neck
                g2d.fillRect(13, 2, 4, 15);
                
                // Sound hole
                g2d.setColor(StyleManager.PRIMARY_COLOR);
                g2d.fillOval(10, 20, 10, 10);
                
                g2d.dispose();
            }
            
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(30, 40);
            }
        };
        
        JLabel titleLabel = new JLabel("SixStringMarket");
        titleLabel.setFont(StyleManager.BOLD_FONT);
        titleLabel.setForeground(StyleManager.TEXT_COLOR);
        
        logoPanel.add(logoIcon);
        logoPanel.add(titleLabel);
        
        // Toggle button for sidebar
        JButton toggleButton = new JButton("«");
        toggleButton.setFont(new Font("Arial", Font.BOLD, 16));
        toggleButton.setForeground(StyleManager.TEXT_SECONDARY_COLOR);
        toggleButton.setBackground(StyleManager.PRIMARY_COLOR);
        toggleButton.setBorderPainted(false);
        toggleButton.setFocusPainted(false);
        toggleButton.setContentAreaFilled(false);
        toggleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        toggleButton.addActionListener(e -> toggleSidebar(toggleButton));
        
        headerPanel.add(logoPanel, BorderLayout.CENTER);
        headerPanel.add(toggleButton, BorderLayout.EAST);
        
        // Navigation items
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBackground(StyleManager.PRIMARY_COLOR);
        navPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        
        // Add navigation items
        String[][] navItems = {
            {"🏠", "Home"},
            {"📋", "My Listings"},
            {"❤", "Saved Guitars"},
            {"🛒", "Order History"},
            {"👤", "Profile"}
        };
        
        for (String[] item : navItems) {
            JPanel navItem = createNavItem(item[0], item[1]);
            navPanel.add(navItem);
            navPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        
        // Add header and nav to sidebar
        sidebar.add(headerPanel, BorderLayout.NORTH);
        sidebar.add(navPanel, BorderLayout.CENTER);
        
        return sidebar;
    }
    
    /**
     * Create a navigation item for the sidebar
     */
    private JPanel createNavItem(String icon, String text) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBackground(StyleManager.PRIMARY_COLOR);
        itemPanel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        itemPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Icon
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        iconLabel.setForeground(StyleManager.TEXT_SECONDARY_COLOR);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));
        
        // Text
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(StyleManager.DEFAULT_FONT);
        textLabel.setForeground(StyleManager.TEXT_SECONDARY_COLOR);
        
        itemPanel.add(iconLabel, BorderLayout.WEST);
        itemPanel.add(textLabel, BorderLayout.CENTER);
        
        // Hover effect
        itemPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                itemPanel.setBackground(StyleManager.darken(StyleManager.PRIMARY_COLOR, 0.1f));
                iconLabel.setForeground(StyleManager.TEXT_COLOR);
                textLabel.setForeground(StyleManager.TEXT_COLOR);
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                itemPanel.setBackground(StyleManager.PRIMARY_COLOR);
                iconLabel.setForeground(StyleManager.TEXT_SECONDARY_COLOR);
                textLabel.setForeground(StyleManager.TEXT_SECONDARY_COLOR);
            }
        });
        
        return itemPanel;
    }
    
    /**
     * Toggle sidebar between expanded and collapsed states
     */
    private void toggleSidebar(JButton toggleButton) {
        sidebarCollapsed = !sidebarCollapsed;
        
        // Target width
        final int targetWidth = sidebarCollapsed ? 60 : 220;
        
        // Update toggle button
        toggleButton.setText(sidebarCollapsed ? "»" : "«");
        toggleButton.setEnabled(false);
        
        // Animate sidebar width
        AnimationUtil.animateWidth(sidebarPanel, targetWidth, 300, () -> {
            // Animation complete
            toggleButton.setEnabled(true);
            
            // Update nav items text visibility
            Component[] components = ((JPanel)sidebarPanel.getComponent(1)).getComponents();
            for (Component c : components) {
                if (c instanceof JPanel) {
                    JPanel navItem = (JPanel)c;
                    if (navItem.getComponentCount() > 1) {
                        navItem.getComponent(1).setVisible(!sidebarCollapsed);
                    }
                }
            }
            
            sidebarPanel.revalidate();
            sidebarPanel.repaint();
        });
    }
    
    /**
     * Create sample content with modern components
     */
    private JPanel createSampleContent() {
        // Main container
        ModernPanel container = new ModernPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        // Title
        JLabel titleLabel = new JLabel("Modern UI Components");
        titleLabel.setFont(StyleManager.TITLE_FONT);
        titleLabel.setForeground(StyleManager.TEXT_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Examples of the new styled components");
        subtitleLabel.setFont(StyleManager.DEFAULT_FONT);
        subtitleLabel.setForeground(StyleManager.TEXT_SECONDARY_COLOR);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        container.add(titleLabel);
        container.add(Box.createRigidArea(new Dimension(0, 5)));
        container.add(subtitleLabel);
        container.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Sample card panel
        ModernPanel cardPanel = new ModernPanel("Card Panel Example", true, true);
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setMaximumSize(new Dimension(800, 200));
        cardPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Card content
        JLabel cardContentLabel = new JLabel("This is a styled card panel with title and shadow");
        cardContentLabel.setFont(StyleManager.DEFAULT_FONT);
        cardContentLabel.setForeground(StyleManager.TEXT_COLOR);
        cardContentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel cardDescLabel = new JLabel("Use ModernPanel for consistent styling across your application");
        cardDescLabel.setFont(StyleManager.SMALL_FONT);
        cardDescLabel.setForeground(StyleManager.TEXT_SECONDARY_COLOR);
        cardDescLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        cardPanel.add(cardContentLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cardPanel.add(cardDescLabel);
        
        container.add(cardPanel);
        container.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Buttons section
        JLabel buttonsLabel = new JLabel("Modern Buttons");
        buttonsLabel.setFont(StyleManager.SUBTITLE_FONT);
        buttonsLabel.setForeground(StyleManager.TEXT_COLOR);
        buttonsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        container.add(buttonsLabel);
        container.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonsPanel.setOpaque(false);
        buttonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Primary button
        ModernButton primaryButton = new ModernButton("Primary");
        primaryButton.setPrimary();
        
        // Secondary button
        ModernButton secondaryButton = new ModernButton("Secondary");
        secondaryButton.setSecondary();
        
        // Success button
        ModernButton successButton = new ModernButton("Success");
        successButton.setSuccess();
        
        // Danger button
        ModernButton dangerButton = new ModernButton("Danger");
        dangerButton.setDanger();
        
        // Disabled button
        ModernButton disabledButton = new ModernButton("Disabled");
        disabledButton.setEnabled(false);
        
        buttonsPanel.add(primaryButton);
        buttonsPanel.add(secondaryButton);
        buttonsPanel.add(successButton);
        buttonsPanel.add(dangerButton);
        buttonsPanel.add(disabledButton);
        
        container.add(buttonsPanel);
        container.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Form elements section
        JLabel formLabel = new JLabel("Form Elements");
        formLabel.setFont(StyleManager.SUBTITLE_FONT);
        formLabel.setForeground(StyleManager.TEXT_COLOR);
        formLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        container.add(formLabel);
        container.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Text field
        JLabel textFieldLabel = new JLabel("Modern Text Field:");
        textFieldLabel.setFont(StyleManager.DEFAULT_FONT);
        textFieldLabel.setForeground(StyleManager.TEXT_COLOR);
        textFieldLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        ModernTextField textField = new ModernTextField("Enter text here", 20);
        textField.setAlignmentX(Component.LEFT_ALIGNMENT);
        textField.setMaximumSize(new Dimension(300, 40));
        
        // Password field
        JLabel passwordFieldLabel = new JLabel("Modern Password Field:");
        passwordFieldLabel.setFont(StyleManager.DEFAULT_FONT);
        passwordFieldLabel.setForeground(StyleManager.TEXT_COLOR);
        passwordFieldLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        ModernPasswordField passwordField = new ModernPasswordField("Enter password", 20);
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField.setMaximumSize(new Dimension(300, 40));
        
        formPanel.add(textFieldLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(textField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(passwordFieldLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(passwordField);
        
        container.add(formPanel);
        container.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Animation example section
        JLabel animationLabel = new JLabel("Animation Examples");
        animationLabel.setFont(StyleManager.SUBTITLE_FONT);
        animationLabel.setForeground(StyleManager.TEXT_COLOR);
        animationLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        container.add(animationLabel);
        container.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Animation buttons panel
        JPanel animationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        animationPanel.setOpaque(false);
        animationPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Shake animation button
        ModernButton shakeButton = new ModernButton("Shake Effect");
        shakeButton.addActionListener(e -> {
            AnimationUtil.shake(shakeButton, 10, 300);
        });
        
        // Color animation button
        ModernButton colorButton = new ModernButton("Color Change");
        colorButton.addActionListener(e -> {
            // Animate from current color to success and back
            AnimationUtil.animateColor(
                colorButton.getBackground(),
                StyleManager.SUCCESS_COLOR,
                500,
                colorButton::setButtonColor,
                () -> {
                    // After success color, animate back to primary
                    AnimationUtil.animateColor(
                        StyleManager.SUCCESS_COLOR,
                        StyleManager.BUTTON_PRIMARY_COLOR,
                        500,
                        colorButton::setButtonColor,
                        null
                    );
                }
            );
        });
        
        // Fade animation button and label
        ModernButton fadeButton = new ModernButton("Show Message");
        JLabel fadeLabel = new JLabel("This message fades in!");
        fadeLabel.setFont(StyleManager.DEFAULT_FONT);
        fadeLabel.setForeground(StyleManager.ERROR_COLOR);
        fadeLabel.setVisible(false);
        fadeButton.addActionListener(e -> {
            fadeLabel.setVisible(true);
            AnimationUtil.fadeIn(fadeLabel, 500);
        });
        
        animationPanel.add(shakeButton);
        animationPanel.add(colorButton);
        animationPanel.add(fadeButton);
        
        container.add(animationPanel);
        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(fadeLabel);
        
        return container;
    }
    
    /**
     * Main method to start the example
     */
    public static void main(String[] args) {
        // Set the look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Start the application
        SwingUtilities.invokeLater(() -> {
            ModernUIExample example = new ModernUIExample();
            example.setVisible(true);
        });
    }
}