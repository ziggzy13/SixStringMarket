package com.sixstringmarket.ui;

import com.sixstringmarket.service.UserService;
import com.sixstringmarket.util.ColorScheme;
import com.sixstringmarket.util.ValidationUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Modern registration screen with real-time validation feedback,
 * guided user experience, and musical-themed visual design.
 */
public class RegistrationFrame extends JFrame {
    
    // Form fields
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextArea addressArea;
    
    // Validation indicators
    private JLabel usernameValidationLabel;
    private JLabel passwordValidationLabel;
    private JLabel confirmPasswordValidationLabel;
    private JLabel emailValidationLabel;
    private JProgressBar passwordStrengthBar;
    
    // Buttons
    private JButton registerButton;
    private JButton backButton;
    
    // Service
    private UserService userService;
    
    // Animation timers
    private Timer fadeTimer;
    
    // Initial mouse position for window dragging
    private Point initialClick;
    
    /**
     * Constructor
     */
    public RegistrationFrame() {
        this.userService = new UserService();
        
        // Window settings
        setTitle("SixStringMarket - Create Account");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 650);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);
        
        try {
            // Initialize UI components
            initComponents();
            setupDragSupport();
            
            // Set rounded corners for the window
            setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Error initializing registration screen: " + e.getMessage(),
                "Initialization Error",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        
        // Focus on username field initially
        SwingUtilities.invokeLater(() -> usernameField.requestFocusInWindow());
    }
    
    /**
     * Initialize UI components
     */
    private void initComponents() {
        // Main background panel with gradient and decorations
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, ColorScheme.PRIMARY,
                    getWidth(), getHeight(), ColorScheme.BACKGROUND
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Add decorative elements
                drawDecorations(g2d);
                
                g2d.dispose();
            }
            
            private void drawDecorations(Graphics2D g2d) {
                // Draw some guitar silhouettes and musical symbols
                g2d.setColor(new Color(255, 255, 255, 15)); // Very subtle white
                
                // Add some decorative circles in the background
                for (int i = 0; i < 8; i++) {
                    int size = 50 + (int)(Math.random() * 200);
                    int x = (int)(Math.random() * getWidth());
                    int y = (int)(Math.random() * getHeight());
                    g2d.fillOval(x - size/2, y - size/2, size, size);
                }
                
                // Draw a guitar in bottom right
                drawGuitar(g2d, getWidth() - 120, getHeight() - 100, 220);
                
                // Draw music notes
                g2d.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 24));
                g2d.drawString("♪", 100, 150);
                g2d.drawString("♫", 650, 250);
                g2d.drawString("♬", 250, 500);
            }
            
            private void drawGuitar(Graphics2D g2d, int x, int y, int size) {
                // Draw guitar body (oval)
                int bodyWidth = size/2;
                int bodyHeight = (int) (size/1.5f);
                g2d.fillOval(x - bodyWidth/2, y - bodyHeight/2, bodyWidth, bodyHeight);
                
                // Draw guitar neck (rectangle)
                int neckWidth = size/8;
                int neckHeight = size/2;
                g2d.fillRect(x - neckWidth/2, y - bodyHeight/2 - neckHeight, neckWidth, neckHeight);
                
                // Draw guitar head (small rectangle)
                int headWidth = size/6;
                int headHeight = size/10;
                g2d.fillRect(x - headWidth/2, y - bodyHeight/2 - neckHeight - headHeight, headWidth, headHeight);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        
        // Window controls
        JPanel windowControlsPanel = createWindowControlsPanel();
        backgroundPanel.add(windowControlsPanel, BorderLayout.NORTH);
        
        // Main container panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        
        // Left panel with form
        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Right panel with guitar image and welcome
        JPanel welcomePanel = createWelcomePanel();
        mainPanel.add(welcomePanel, BorderLayout.EAST);
        
        backgroundPanel.add(mainPanel, BorderLayout.CENTER);
        
        setContentPane(backgroundPanel);
    }
    
    /**
     * Create window control buttons (close, minimize)
     */
    private JPanel createWindowControlsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        // Back button on left
        backButton = new JButton("« Back to Login");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.setForeground(ColorScheme.TEXT);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> backToLogin());
        
        // Hover effect
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backButton.setForeground(ColorScheme.SECONDARY);
                backButton.setText("<html><u>« Back to Login</u></html>");
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                backButton.setForeground(ColorScheme.TEXT);
                backButton.setText("« Back to Login");
            }
        });
        
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.setOpaque(false);
        backButtonPanel.add(backButton);
        
        // Control buttons on right
        JPanel controlButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        controlButtonsPanel.setOpaque(false);
        
        // Minimize button
        JButton minimizeBtn = new JButton("—");
        minimizeBtn.setForeground(ColorScheme.TEXT);
        minimizeBtn.setFont(new Font("Arial", Font.BOLD, 12));
        minimizeBtn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        minimizeBtn.setContentAreaFilled(false);
        minimizeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        minimizeBtn.setFocusPainted(false);
        minimizeBtn.addActionListener(e -> setState(JFrame.ICONIFIED));
        
        // Close button
        JButton closeBtn = new JButton("✕");
        closeBtn.setForeground(ColorScheme.TEXT);
        closeBtn.setFont(new Font("Arial", Font.BOLD, 12));
        closeBtn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        closeBtn.setContentAreaFilled(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(e -> System.exit(0));
        
        // Hover effects
        minimizeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                minimizeBtn.setForeground(ColorScheme.SECONDARY);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                minimizeBtn.setForeground(ColorScheme.TEXT);
            }
        });
        
        closeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                closeBtn.setForeground(ColorScheme.ERROR);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                closeBtn.setForeground(ColorScheme.TEXT);
            }
        });
        
        controlButtonsPanel.add(minimizeBtn);
        controlButtonsPanel.add(closeBtn);
        
        // Add components to panel
        panel.add(backButtonPanel, BorderLayout.WEST);
        panel.add(controlButtonsPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Create the main form panel
     */
    private JPanel createFormPanel() {
        // Main form container
        JPanel container = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Panel background
                g2d.setColor(ColorScheme.CARD_BG);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Top accent line
                g2d.setColor(ColorScheme.SECONDARY);
                g2d.fillRect(0, 0, getWidth(), 3);
                
                g2d.dispose();
            }
        };
        container.setLayout(new BorderLayout());
        container.setBorder(BorderFactory.createEmptyBorder(25, 30, 30, 30));
        
        // Form title
        JLabel titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(ColorScheme.SECONDARY);
        
        JLabel subtitleLabel = new JLabel("Join the SixStringMarket community");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(subtitleLabel);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Form fields
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        
        // Username field
        JPanel usernamePanel = createInputFieldPanel("Username", "*");
        usernameField = createStyledTextField();
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        usernameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { validateUsername(); }
            @Override
            public void removeUpdate(DocumentEvent e) { validateUsername(); }
            @Override
            public void changedUpdate(DocumentEvent e) { validateUsername(); }
        });
        
        usernameValidationLabel = new JLabel(" ");
        usernameValidationLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        usernameValidationLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        
        usernamePanel.add(usernameField);
        usernamePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        usernamePanel.add(usernameValidationLabel);
        
        // Password field
        JPanel passwordPanel = createInputFieldPanel("Password", "*");
        passwordField = createStyledPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        passwordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { validatePassword(); }
            @Override
            public void removeUpdate(DocumentEvent e) { validatePassword(); }
            @Override
            public void changedUpdate(DocumentEvent e) { validatePassword(); }
        });
        
        // Password strength bar
        passwordStrengthBar = new JProgressBar(0, 100);
        passwordStrengthBar.setForeground(ColorScheme.ERROR);
        passwordStrengthBar.setBackground(ColorScheme.FIELD_BG);
        passwordStrengthBar.setBorderPainted(false);
        passwordStrengthBar.setStringPainted(false);
        passwordStrengthBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 5));
        
        passwordValidationLabel = new JLabel(" ");
        passwordValidationLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        passwordValidationLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        
        passwordPanel.add(passwordField);
        passwordPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        passwordPanel.add(passwordStrengthBar);
        passwordPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        passwordPanel.add(passwordValidationLabel);
        
        // Confirm password field
        JPanel confirmPasswordPanel = createInputFieldPanel("Confirm Password", "*");
        confirmPasswordField = createStyledPasswordField();
        confirmPasswordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        confirmPasswordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { validateConfirmPassword(); }
            @Override
            public void removeUpdate(DocumentEvent e) { validateConfirmPassword(); }
            @Override
            public void changedUpdate(DocumentEvent e) { validateConfirmPassword(); }
        });
        
        confirmPasswordValidationLabel = new JLabel(" ");
        confirmPasswordValidationLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        confirmPasswordValidationLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        
        confirmPasswordPanel.add(confirmPasswordField);
        confirmPasswordPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        confirmPasswordPanel.add(confirmPasswordValidationLabel);
        
        // Email field
        JPanel emailPanel = createInputFieldPanel("Email", "*");
        emailField = createStyledTextField();
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        emailField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { validateEmail(); }
            @Override
            public void removeUpdate(DocumentEvent e) { validateEmail(); }
            @Override
            public void changedUpdate(DocumentEvent e) { validateEmail(); }
        });
        
        emailValidationLabel = new JLabel(" ");
        emailValidationLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        emailValidationLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        
        emailPanel.add(emailField);
        emailPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        emailPanel.add(emailValidationLabel);
        
        // Phone field (optional)
        JPanel phonePanel = createInputFieldPanel("Phone", "optional");
        phoneField = createStyledTextField();
        phoneField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        
        JLabel phoneFormatLabel = new JLabel("Example: 0885123456");
        phoneFormatLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        phoneFormatLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        
        phonePanel.add(phoneField);
        phonePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        phonePanel.add(phoneFormatLabel);
        
        // Address field (optional)
        JPanel addressPanel = createInputFieldPanel("Address", "optional");
        addressArea = new JTextArea();
        addressArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        addressArea.setBackground(ColorScheme.FIELD_BG);
        addressArea.setForeground(ColorScheme.TEXT);
        addressArea.setCaretColor(ColorScheme.TEXT);
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        addressArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorScheme.FIELD_BORDER),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        
        JScrollPane addressScrollPane = new JScrollPane(addressArea);
        addressScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        addressPanel.add(addressScrollPane);
        
        // Register button
        registerButton = new JButton("Create Account");
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerButton.setBackground(ColorScheme.SECONDARY);
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        registerButton.setMaximumSize(new Dimension(200, 40));
        registerButton.addActionListener(e -> register());
        
        // Button hover effect
        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                registerButton.setBackground(ColorScheme.lighten(ColorScheme.SECONDARY, 0.1f));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                registerButton.setBackground(ColorScheme.SECONDARY);
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(registerButton);
        
        // Add all components to form
        formPanel.add(usernamePanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(passwordPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(confirmPasswordPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(emailPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(phonePanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(addressPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(buttonPanel);
        
        // Required fields note
        JLabel requiredFieldsNote = new JLabel("* Required fields");
        requiredFieldsNote.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        requiredFieldsNote.setForeground(ColorScheme.TEXT_SECONDARY);
        
        // Add everything to container
        container.add(titlePanel, BorderLayout.NORTH);
        container.add(formPanel, BorderLayout.CENTER);
        container.add(requiredFieldsNote, BorderLayout.SOUTH);
        
        return container;
    }
    
    /**
     * Create welcome panel with guitar image and text
     */
    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create gradient background for welcome panel
                GradientPaint gradient = new GradientPaint(
                    0, 0, ColorScheme.SECONDARY,
                    0, getHeight(), ColorScheme.darken(ColorScheme.SECONDARY, 0.3f)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                g2d.dispose();
            }
        };
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(300, 0));
        
        // Content panel with welcome text
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 30, 40, 30));
        
        // Logo/guitar icon
        JLabel logoLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw simplified guitar in white
                g2d.setColor(Color.WHITE);
                
                // Body
                g2d.fillOval(25, 40, 50, 60);
                
                // Neck
                g2d.fillRect(45, 10, 10, 40);
                
                // Sound hole
                g2d.setColor(ColorScheme.SECONDARY);
                g2d.fillOval(35, 55, 30, 30);
                
                // Strings
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(1.0f));
                for (int i = 0; i < 6; i++) {
                    g2d.drawLine(47, 15 + i * 3, 53, 15 + i * 3);
                }
                
                g2d.dispose();
            }
            
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(100, 100);
            }
            
            @Override
            public Dimension getMinimumSize() {
                return getPreferredSize();
            }
            
            @Override
            public Dimension getMaximumSize() {
                return getPreferredSize();
            }
        };
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Welcome text
        JLabel welcomeLabel = new JLabel("Welcome to");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel appNameLabel = new JLabel("SixStringMarket");
        appNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        appNameLabel.setForeground(Color.WHITE);
        appNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Benefits list
        String[] benefits = {
            "Buy and sell guitars easily",
            "Connect with other musicians",
            "Find rare and vintage instruments",
            "Safe and secure transactions",
            "Join our growing community"
        };
        
        JPanel benefitsPanel = new JPanel();
        benefitsPanel.setLayout(new BoxLayout(benefitsPanel, BoxLayout.Y_AXIS));
        benefitsPanel.setOpaque(false);
        benefitsPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        
        for (String benefit : benefits) {
            JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
            itemPanel.setOpaque(false);
            
            JLabel iconLabel = new JLabel("✓");
            iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            iconLabel.setForeground(Color.WHITE);
            
            JLabel textLabel = new JLabel(benefit);
            textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            textLabel.setForeground(Color.WHITE);
            
            itemPanel.add(iconLabel);
            itemPanel.add(textLabel);
            
            benefitsPanel.add(itemPanel);
        }
        
        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(logoLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(welcomeLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contentPanel.add(appNameLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        contentPanel.add(benefitsPanel);
        contentPanel.add(Box.createVerticalGlue());
        
        panel.add(contentPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create a styled text field with visual enhancements
     */
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorScheme.FIELD_BORDER),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(ColorScheme.FIELD_BG);
        field.setForeground(ColorScheme.TEXT);
        field.setCaretColor(ColorScheme.TEXT);
        
        // Add focus effect
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ColorScheme.SECONDARY),
                    BorderFactory.createEmptyBorder(8, 8, 8, 8)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ColorScheme.FIELD_BORDER),
                    BorderFactory.createEmptyBorder(8, 8, 8, 8)
                ));
            }
        });
        
        return field;
    }
    
    /**
     * Create a styled password field with visual enhancements
     */
    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorScheme.FIELD_BORDER),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(ColorScheme.FIELD_BG);
        field.setForeground(ColorScheme.TEXT);
        field.setCaretColor(ColorScheme.TEXT);
        
        // Add focus effect
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ColorScheme.SECONDARY),
                    BorderFactory.createEmptyBorder(8, 8, 8, 8)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ColorScheme.FIELD_BORDER),
                    BorderFactory.createEmptyBorder(8, 8, 8, 8)
                ));
            }
        });
        
        return field;
    }
    
    /**
     * Create a panel for input field with label
     */
    private JPanel createInputFieldPanel(String labelText, String required) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Label text
        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        labelPanel.setOpaque(false);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(ColorScheme.TEXT);
        labelPanel.add(label);
        
        // Required indicator
        if (required != null) {
            JLabel requiredLabel = new JLabel(" (" + required + ")");
            requiredLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            
            if (required.equals("*")) {
                requiredLabel.setForeground(ColorScheme.SECONDARY);
            } else {
                requiredLabel.setForeground(ColorScheme.TEXT_SECONDARY);
            }
            
            labelPanel.add(requiredLabel);
        }
        
        panel.add(labelPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        return panel;
    }
    
    /**
     * Setup draggable functionality to move the window
     */
    private void setupDragSupport() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
            }
        });
        
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // Get current location of the frame
                int thisX = getLocation().x;
                int thisY = getLocation().y;
                
                // Determine how much the mouse moved
                int xMoved = e.getX() - initialClick.x;
                int yMoved = e.getY() - initialClick.y;
                
                // Move window to this position
                int newX = thisX + xMoved;
                int newY = thisY + yMoved;
                setLocation(newX, newY);
            }
        });
    }
    
    /**
     * Validate username field
     */
    private void validateUsername() {
        String username = usernameField.getText();
        
        if (username.isEmpty()) {
            usernameValidationLabel.setText(" ");
            return;
        }
        
        if (username.length() < 4) {
            usernameValidationLabel.setText("Username must be at least 4 characters long");
            usernameValidationLabel.setForeground(ColorScheme.ERROR);
            return;
        }
        
        // Check if username is available
        if (userService.isUsernameTaken(username)) {
            usernameValidationLabel.setText("This username is already taken");
            usernameValidationLabel.setForeground(ColorScheme.ERROR);
        } else {
            usernameValidationLabel.setText("This username is available");
            usernameValidationLabel.setForeground(ColorScheme.SUCCESS);
        }
    }
    
    /**
     * Validate password and show strength indicator
     */
    private void validatePassword() {
        String password = new String(passwordField.getPassword());
        
        if (password.isEmpty()) {
            passwordStrengthBar.setValue(0);
            passwordValidationLabel.setText(" ");
            return;
        }
        
        // Calculate password strength
        int strength = ValidationUtils.calculatePasswordStrength(password);
        passwordStrengthBar.setValue(strength);
        
        // Update strength indicator color and text
        if (strength < 30) {
            passwordStrengthBar.setForeground(ColorScheme.ERROR);
            passwordValidationLabel.setText("Weak password");
            passwordValidationLabel.setForeground(ColorScheme.ERROR);
        } else if (strength < 60) {
            passwordStrengthBar.setForeground(ColorScheme.WARNING);
            passwordValidationLabel.setText("Medium strength password");
            passwordValidationLabel.setForeground(ColorScheme.WARNING);
        } else {
            passwordStrengthBar.setForeground(ColorScheme.SUCCESS);
            passwordValidationLabel.setText("Strong password");
            passwordValidationLabel.setForeground(ColorScheme.SUCCESS);
        }
        
        // Also validate confirm password if it's not empty
        if (!new String(confirmPasswordField.getPassword()).isEmpty()) {
            validateConfirmPassword();
        }
    }
    
    /**
     * Validate password confirmation
     */
    private void validateConfirmPassword() {
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        if (confirmPassword.isEmpty()) {
            confirmPasswordValidationLabel.setText(" ");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            confirmPasswordValidationLabel.setText("Passwords don't match");
            confirmPasswordValidationLabel.setForeground(ColorScheme.ERROR);
        } else {
            confirmPasswordValidationLabel.setText("Passwords match");
            confirmPasswordValidationLabel.setForeground(ColorScheme.SUCCESS);
        }
    }
    
    /**
     * Validate email
     */
    private void validateEmail() {
        String email = emailField.getText();
        
        if (email.isEmpty()) {
            emailValidationLabel.setText(" ");
            return;
        }
        
        if (!ValidationUtils.isValidEmail(email)) {
            emailValidationLabel.setText("Please enter a valid email address");
            emailValidationLabel.setForeground(ColorScheme.ERROR);
        } else {
            emailValidationLabel.setText("Valid email address");
            emailValidationLabel.setForeground(ColorScheme.SUCCESS);
        }
    }
    
    /**
     * Go back to login screen
     */
    private void backToLogin() {
        dispose();
        new LoginFrame().setVisible(true);
    }
    
    /**
     * Register new user
     */
    private void register() {
        // Get form values
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String email = emailField.getText();
        String phone = phoneField.getText();
        String address = addressArea.getText();
        
        // Validate required fields
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
            showErrorDialog("Please fill in all required fields");
            return;
        }
        
        // Validate username length
        if (username.length() < 4) {
            showErrorDialog("Username must be at least 4 characters long");
            usernameField.requestFocusInWindow();
            return;
        }
        
        // Check if username is taken
        if (userService.isUsernameTaken(username)) {
            showErrorDialog("This username is already taken");
            usernameField.requestFocusInWindow();
            return;
        }
        
        // Validate password length
        if (password.length() < 6) {
            showErrorDialog("Password must be at least 6 characters long");
            passwordField.requestFocusInWindow();
            return;
        }
        
        // Validate password confirmation
        if (!password.equals(confirmPassword)) {
            showErrorDialog("Passwords don't match");
            confirmPasswordField.requestFocusInWindow();
            return;
        }
        
        // Validate email
        if (!ValidationUtils.isValidEmail(email)) {
            showErrorDialog("Please enter a valid email address");
            emailField.requestFocusInWindow();
            return;
        }
        
        // Validate phone if provided
        if (!phone.isEmpty() && !ValidationUtils.isValidPhone(phone)) {
            showErrorDialog("Please enter a valid phone number (e.g., 0885123456)");
            phoneField.requestFocusInWindow();
            return;
        }
        
        // Show loading state
        registerButton.setEnabled(false);
        registerButton.setText("Creating account...");
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        // Process registration in background
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                // Small delay for better UX
                Thread.sleep(800);
                try {
                    return userService.registerUser(username, password, email, phone, address);
                } catch (IllegalArgumentException e) {
                    SwingUtilities.invokeLater(() -> showErrorDialog(e.getMessage()));
                    return false;
                }
            }
            
            @Override
            protected void done() {
                try {
                    boolean success = get();
                    
                    if (success) {
                        showSuccessDialog("Your account has been created successfully!", "Registration Complete");
                        backToLogin();
                    } else {
                        registerButton.setEnabled(true);
                        registerButton.setText("Create Account");
                        setCursor(Cursor.getDefaultCursor());
                    }
                } catch (Exception ex) {
                    showErrorDialog("Error during registration: " + ex.getMessage());
                    registerButton.setEnabled(true);
                    registerButton.setText("Create Account");
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        };
        
        worker.execute();
    }
    
    /**
     * Show error dialog
     */
    private void showErrorDialog(String message) {
        JDialog dialog = new JDialog(this, "Error", true);
        dialog.setSize(400, 150);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(ColorScheme.CARD_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel iconLabel = new JLabel("!");
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        iconLabel.setForeground(ColorScheme.ERROR);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setPreferredSize(new Dimension(40, 40));
        
        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setForeground(ColorScheme.TEXT);
        
        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        okButton.setBackground(ColorScheme.SECONDARY);
        okButton.setForeground(Color.WHITE);
        okButton.setBorderPainted(false);
        okButton.setFocusPainted(false);
        okButton.addActionListener(e -> dialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(okButton);
        
        panel.add(iconLabel, BorderLayout.WEST);
        panel.add(messageLabel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    /**
     * Show success dialog
     */
    private void showSuccessDialog(String message, String title) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setSize(400, 150);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(ColorScheme.CARD_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel iconLabel = new JLabel("✓");
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        iconLabel.setForeground(ColorScheme.SUCCESS);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setPreferredSize(new Dimension(40, 40));
        
        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setForeground(ColorScheme.TEXT);
        
        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        okButton.setBackground(ColorScheme.SECONDARY);
        okButton.setForeground(Color.WHITE);
        okButton.setBorderPainted(false);
        okButton.setFocusPainted(false);
        okButton.addActionListener(e -> dialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(okButton);
        
        panel.add(iconLabel, BorderLayout.WEST);
        panel.add(messageLabel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
}