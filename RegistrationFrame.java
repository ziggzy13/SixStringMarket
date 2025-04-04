package com.sixstringmarket.ui;

import com.sixstringmarket.service.UserService;
import com.sixstringmarket.util.ColorScheme;
import com.sixstringmarket.util.ValidationUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Modern registration screen with animated validation, tooltips, and enhanced user experience
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
    
    /**
     * Constructor
     */
    public RegistrationFrame() {
        userService = new UserService();
        
        // Window settings
        setTitle("SixStringMarket - Create Account");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 650);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);
        
        // Initialize UI components
        initComponents();
        
        // Set rounded corners for the window
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
        
        // Focus on username field initially
        SwingUtilities.invokeLater(() -> usernameField.requestFocusInWindow());
    }
    
    /**
     * Initialize UI components
     */
    private void initComponents() {
        // Main background panel with gradient
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, ColorScheme.PRIMARY,
                    0, getHeight(), ColorScheme.BACKGROUND
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Draw decorative elements (using text instead of emojis)
                drawDecorations(g2d);
                
                g2d.dispose();
            }
            
            private void drawDecorations(Graphics2D g2d) {
                // Draw musical decorations using Unicode symbols with a specified font
                g2d.setColor(new Color(255, 255, 255, 15)); // Very subtle white
                g2d.setFont(new Font("Arial Unicode MS", Font.PLAIN, 24));
                
                // Musical notes (Unicode symbols)
                g2d.drawString("\u266A", 50, 400);  // Musical note
                g2d.drawString("\u266B", 350, 200); // Musical notes
                g2d.drawString("\u266A", 280, 450); // Musical note
                
                // Draw guitar icon (simple shape)
                drawGuitarShape(g2d, getWidth() - 120, getHeight() - 100, 220);
                drawGuitarShape(g2d, 80, 150, 100);
                
                // Strings/waves
                g2d.setStroke(new BasicStroke(1.0f));
                for (int i = 1; i <= 5; i++) {
                    int y = getHeight() - 100 + i * 15;
                    
                    // Create curved strings with sine wave
                    for (int x = 0; x < getWidth(); x += 2) {
                        int y1 = (int)(y + 5 * Math.sin(x * 0.03 + i));
                        int y2 = (int)(y + 5 * Math.sin((x+2) * 0.03 + i));
                        g2d.drawLine(x, y1, x+2, y2);
                    }
                }
            }
            
            private void drawGuitarShape(Graphics2D g2d, int x, int y, int size) {
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
        JPanel controlsPanel = createWindowControlsPanel();
        backgroundPanel.add(controlsPanel, BorderLayout.NORTH);
        
        // Main container panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        
        // Left panel with form
        JPanel formPanel = createFormPanel();
        formPanel.setPreferredSize(new Dimension(450, formPanel.getPreferredSize().height));
        
        // Make the form panel scrollable
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setMinimumSize(new Dimension(450, 0));
        scrollPane.setPreferredSize(new Dimension(450, 0));
        
        // Right panel with guitar image and welcome
        JPanel welcomePanel = createWelcomePanel();
        
        // Make the window draggable
        addDragSupport(backgroundPanel);
        
        // Add panels to main container
        mainPanel.add(scrollPane, BorderLayout.CENTER);
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
        
        // Control buttons on right
        JPanel controlButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        controlButtonsPanel.setOpaque(false);
        
        // Minimize button
        JButton minimizeBtn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(isEnabled() ? getForeground() : getForeground().darker());
                g2d.setStroke(new BasicStroke(2f));
                int width = getWidth();
                int height = getHeight();
                g2d.drawLine(width/4, height/2, width*3/4, height/2);
                g2d.dispose();
            }
        };
        minimizeBtn.setForeground(ColorScheme.TEXT);
        minimizeBtn.setPreferredSize(new Dimension(30, 20));
        minimizeBtn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        minimizeBtn.setContentAreaFilled(false);
        minimizeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        minimizeBtn.setFocusPainted(false);
        minimizeBtn.addActionListener(e -> setState(JFrame.ICONIFIED));
        
        // Close button
        JButton closeBtn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(isEnabled() ? getForeground() : getForeground().darker());
                g2d.setStroke(new BasicStroke(2f));
                int width = getWidth();
                int height = getHeight();
                g2d.drawLine(width/4, height/4, width*3/4, height*3/4);
                g2d.drawLine(width*3/4, height/4, width/4, height*3/4);
                g2d.dispose();
            }
        };
        closeBtn.setForeground(ColorScheme.TEXT);
        closeBtn.setPreferredSize(new Dimension(30, 20));
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
        JPanel buttonContainerPanel = new JPanel(new BorderLayout());
        buttonContainerPanel.setOpaque(false);
        buttonContainerPanel.add(backButton, BorderLayout.WEST);
        buttonContainerPanel.add(controlButtonsPanel, BorderLayout.EAST);
        
        panel.add(buttonContainerPanel, BorderLayout.CENTER);
        
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
        
        // Register button - Bigger and more prominent
        registerButton = new JButton("Create Account");
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        registerButton.setBackground(ColorScheme.SECONDARY);
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        registerButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        registerButton.setPreferredSize(new Dimension(200, 50));
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
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
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
        
        // Logo/guitar icon - using custom painting instead of emojis
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
        
        // Benefits list - using custom drawn checkmarks
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
            
            // Draw custom checkmark
            JLabel iconLabel = new JLabel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(Color.WHITE);
                    g2d.setStroke(new BasicStroke(2f));
                    
                    // Draw checkmark
                    int[] xPoints = {2, 6, 14};
                    int[] yPoints = {8, 12, 2};
                    g2d.drawPolyline(xPoints, yPoints, 3);
                    
                    g2d.dispose();
                }
                
                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(16, 16);
                }
            };
            
            JLabel textLabel = new JLabel(benefit);
            textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            textLabel.setForeground(Color.WHITE);
            
            itemPanel.add(iconLabel);
            itemPanel.add(textLabel);
            
            benefitsPanel.add(itemPanel);
        }
        
        contentPanel.add(logoLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(welcomeLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contentPanel.add(appNameLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        contentPanel.add(benefitsPanel);
        
        panel.add(contentPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Add window drag functionality
     */
    private void addDragSupport(JPanel panel) {
        MouseAdapter dragAdapter = new MouseAdapter() {
            private int dragStartX, dragStartY;
            
            @Override
            public void mousePressed(MouseEvent e) {
                dragStartX = e.getX();
                dragStartY = e.getY();
            }
            
            @Override
            public void mouseDragged(MouseEvent e) {
                int newX = getLocation().x + e.getX() - dragStartX;
                int newY = getLocation().y + e.getY() - dragStartY;
                setLocation(newX, newY);
            }
        };
        
        panel.addMouseListener(dragAdapter);
        panel.addMouseMotionListener(dragAdapter);
    }
    
    /**
     * Create a styled text field
     */
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(ColorScheme.FIELD_BG);
        field.setForeground(ColorScheme.TEXT);
        field.setCaretColor(ColorScheme.TEXT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorScheme.FIELD_BORDER),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        field.setMinimumSize(new Dimension(250, field.getPreferredSize().height));
        field.setPreferredSize(new Dimension(400, field.getPreferredSize().height));
        
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
     * Create a styled password field
     */
    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(ColorScheme.FIELD_BG);
        field.setForeground(ColorScheme.TEXT);
        field.setCaretColor(ColorScheme.TEXT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorScheme.FIELD_BORDER),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        field.setMinimumSize(new Dimension(250, field.getPreferredSize().height));
        field.setPreferredSize(new Dimension(400, field.getPreferredSize().height));
        
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
        if (confirmPasswordField.getPassword().length > 0) {
            validateConfirmPassword();
        }
    }
    
    /**
     * Validate password confirmation
     */
    private void validateConfirmPassword() {
        char[] passwordChars = passwordField.getPassword();
        char[] confirmChars = confirmPasswordField.getPassword();
        
        if (confirmChars.length == 0) {
            confirmPasswordValidationLabel.setText(" ");
            return;
        }
        
        // Convert to strings for comparison
        String password = new String(passwordChars);
        String confirmPassword = new String(confirmChars);
        
        // Debug output (remove in production)
        System.out.println("Password: [" + password + "], Confirm: [" + confirmPassword + "]");
        System.out.println("Length: " + password.length() + ", " + confirmPassword.length());
        
        // Explicit character-by-character comparison
        boolean matches = password.length() == confirmPassword.length();
        if (matches) {
            for (int i = 0; i < password.length(); i++) {
                if (password.charAt(i) != confirmPassword.charAt(i)) {
                    matches = false;
                    break;
                }
            }
        }
        
        if (!matches) {
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
        
        // Custom drawn check mark instead of emoji
        JLabel iconLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(ColorScheme.SUCCESS);
                g2d.setStroke(new BasicStroke(3f));
                
                // Draw checkmark
                int[] xPoints = {5, 15, 30};
                int[] yPoints = {20, 30, 5};
                g2d.drawPolyline(xPoints, yPoints, 3);
                
                g2d.dispose();
            }
            
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(40, 40);
            }
        };
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
     * Main method to launch the application
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        // Set look and feel to the system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Schedule a job for the event dispatch thread:
        // creating and showing this application's GUI.
        SwingUtilities.invokeLater(() -> {
            new RegistrationFrame().setVisible(true);
        });
    }
}
