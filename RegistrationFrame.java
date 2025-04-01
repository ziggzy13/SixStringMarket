package com.sixstringmarket.ui;

import com.sixstringmarket.service.UserService;
import com.sixstringmarket.util.Constants;
import com.sixstringmarket.util.ValidationUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Modern registration screen with real-time validation and a clean user interface
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
        setSize(950, 650);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Use the system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Initialize UI components
        initComponents();
        
        // Focus on username field initially
        SwingUtilities.invokeLater(() -> usernameField.requestFocusInWindow());
    }
    
    /**
     * Initialize UI components
     */
    private void initComponents() {
        // Main panel with a gradient background
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                
                // Create gradient background - dark blue to navy
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(37, 40, 61),
                    0, getHeight(), new Color(25, 28, 45)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                g2d.dispose();
            }
        };
        
        // Header with back button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        backButton = new JButton("« Back to Login");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.setForeground(Color.WHITE);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> backToLogin());
        
        // Hover effects
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backButton.setForeground(new Color(186, 80, 80)); // Accent color
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                backButton.setForeground(Color.WHITE);
            }
        });
        
        headerPanel.add(backButton, BorderLayout.WEST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Content panel split into left (form) and right (welcome)
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(550);
        splitPane.setDividerSize(1);
        splitPane.setEnabled(false);
        splitPane.setBorder(null);
        
        // Left panel with form
        JPanel formPanel = createFormPanel();
        
        // Right panel with welcome message
        JPanel welcomePanel = createWelcomePanel();
        
        splitPane.setLeftComponent(formPanel);
        splitPane.setRightComponent(welcomePanel);
        
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
        // Set as content pane
        setContentPane(mainPanel);
    }
    
    /**
     * Create the form panel
     */
    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(37, 40, 61));
        panel.setBorder(new EmptyBorder(30, 40, 40, 40));
        
        // Title
        JLabel titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titleLabel);
        
        JLabel subtitleLabel = new JLabel("Join the SixStringMarket community");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(200, 200, 200));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(subtitleLabel);
        
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Username field
        JPanel usernamePanel = new JPanel();
        usernamePanel.setLayout(new BoxLayout(usernamePanel, BoxLayout.Y_AXIS));
        usernamePanel.setOpaque(false);
        usernamePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        usernamePanel.setMaximumSize(new Dimension(470, 90));
        
        JLabel usernameLabel = new JLabel("Username *");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        usernameLabel.setForeground(Color.WHITE);
        usernamePanel.add(usernameLabel);
        
        usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setPreferredSize(new Dimension(470, 35));
        usernameField.setMaximumSize(new Dimension(470, 35));
        
        // Style the field
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 70, 90), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        usernameField.setBackground(new Color(45, 48, 71));
        usernameField.setForeground(Color.WHITE);
        usernameField.setCaretColor(Color.WHITE);
        
        // Add validation listener
        usernameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { validateUsername(); }
            @Override
            public void removeUpdate(DocumentEvent e) { validateUsername(); }
            @Override
            public void changedUpdate(DocumentEvent e) { validateUsername(); }
        });
        
        usernamePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        usernamePanel.add(usernameField);
        
        usernameValidationLabel = new JLabel(" ");
        usernameValidationLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        usernameValidationLabel.setForeground(new Color(150, 150, 150));
        usernamePanel.add(usernameValidationLabel);
        
        panel.add(usernamePanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Password field
        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.Y_AXIS));
        passwordPanel.setOpaque(false);
        passwordPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordPanel.setMaximumSize(new Dimension(470, 90));
        
        JLabel passwordLabel = new JLabel("Password *");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passwordLabel.setForeground(Color.WHITE);
        passwordPanel.add(passwordLabel);
        
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(470, 35));
        passwordField.setMaximumSize(new Dimension(470, 35));
        
        // Style the field
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 70, 90), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        passwordField.setBackground(new Color(45, 48, 71));
        passwordField.setForeground(Color.WHITE);
        passwordField.setCaretColor(Color.WHITE);
        
        // Add validation listener
        passwordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { validatePassword(); }
            @Override
            public void removeUpdate(DocumentEvent e) { validatePassword(); }
            @Override
            public void changedUpdate(DocumentEvent e) { validatePassword(); }
        });
        
        passwordPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        passwordPanel.add(passwordField);
        
        // Password strength bar
        passwordStrengthBar = new JProgressBar(0, 100);
        passwordStrengthBar.setPreferredSize(new Dimension(470, 3));
        passwordStrengthBar.setMaximumSize(new Dimension(470, 3));
        passwordStrengthBar.setBorder(null);
        passwordStrengthBar.setForeground(new Color(231, 76, 60)); // Start with red
        passwordStrengthBar.setBackground(new Color(45, 48, 71));
        passwordStrengthBar.setBorderPainted(false);
        
        passwordPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        passwordPanel.add(passwordStrengthBar);
        
        passwordValidationLabel = new JLabel(" ");
        passwordValidationLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        passwordValidationLabel.setForeground(new Color(150, 150, 150));
        passwordPanel.add(passwordValidationLabel);
        
        panel.add(passwordPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Confirm Password field
        JPanel confirmPasswordPanel = new JPanel();
        confirmPasswordPanel.setLayout(new BoxLayout(confirmPasswordPanel, BoxLayout.Y_AXIS));
        confirmPasswordPanel.setOpaque(false);
        confirmPasswordPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        confirmPasswordPanel.setMaximumSize(new Dimension(470, 90));
        
        JLabel confirmPasswordLabel = new JLabel("Confirm Password *");
        confirmPasswordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        confirmPasswordLabel.setForeground(Color.WHITE);
        confirmPasswordPanel.add(confirmPasswordLabel);
        
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        confirmPasswordField.setPreferredSize(new Dimension(470, 35));
        confirmPasswordField.setMaximumSize(new Dimension(470, 35));
        
        // Style the field
        confirmPasswordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 70, 90), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        confirmPasswordField.setBackground(new Color(45, 48, 71));
        confirmPasswordField.setForeground(Color.WHITE);
        confirmPasswordField.setCaretColor(Color.WHITE);
        
        // Add validation listener
        confirmPasswordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { validateConfirmPassword(); }
            @Override
            public void removeUpdate(DocumentEvent e) { validateConfirmPassword(); }
            @Override
            public void changedUpdate(DocumentEvent e) { validateConfirmPassword(); }
        });
        
        confirmPasswordPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        confirmPasswordPanel.add(confirmPasswordField);
        
        confirmPasswordValidationLabel = new JLabel(" ");
        confirmPasswordValidationLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        confirmPasswordValidationLabel.setForeground(new Color(150, 150, 150));
        confirmPasswordPanel.add(confirmPasswordValidationLabel);
        
        panel.add(confirmPasswordPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Email field
        JPanel emailPanel = new JPanel();
        emailPanel.setLayout(new BoxLayout(emailPanel, BoxLayout.Y_AXIS));
        emailPanel.setOpaque(false);
        emailPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailPanel.setMaximumSize(new Dimension(470, 90));
        
        JLabel emailLabel = new JLabel("Email *");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        emailLabel.setForeground(Color.WHITE);
        emailPanel.add(emailLabel);
        
        emailField = new JTextField();
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailField.setPreferredSize(new Dimension(470, 35));
        emailField.setMaximumSize(new Dimension(470, 35));
        
        // Style the field
        emailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 70, 90), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        emailField.setBackground(new Color(45, 48, 71));
        emailField.setForeground(Color.WHITE);
        emailField.setCaretColor(Color.WHITE);
        
        // Add validation listener
        emailField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { validateEmail(); }
            @Override
            public void removeUpdate(DocumentEvent e) { validateEmail(); }
            @Override
            public void changedUpdate(DocumentEvent e) { validateEmail(); }
        });
        
        emailPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        emailPanel.add(emailField);
        
        emailValidationLabel = new JLabel(" ");
        emailValidationLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        emailValidationLabel.setForeground(new Color(150, 150, 150));
        emailPanel.add(emailValidationLabel);
        
        panel.add(emailPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Phone field (optional)
        JPanel phonePanel = new JPanel();
        phonePanel.setLayout(new BoxLayout(phonePanel, BoxLayout.Y_AXIS));
        phonePanel.setOpaque(false);
        phonePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        phonePanel.setMaximumSize(new Dimension(470, 75));
        
        JLabel phoneLabel = new JLabel("Phone (optional)");
        phoneLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        phoneLabel.setForeground(Color.WHITE);
        phonePanel.add(phoneLabel);
        
        phoneField = new JTextField();
        phoneField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        phoneField.setPreferredSize(new Dimension(470, 35));
        phoneField.setMaximumSize(new Dimension(470, 35));
        
        // Style the field
        phoneField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 70, 90), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        phoneField.setBackground(new Color(45, 48, 71));
        phoneField.setForeground(Color.WHITE);
        phoneField.setCaretColor(Color.WHITE);
        
        phonePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        phonePanel.add(phoneField);
        
        JLabel phoneHintLabel = new JLabel("Example: 0885123456");
        phoneHintLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        phoneHintLabel.setForeground(new Color(150, 150, 150));
        phonePanel.add(phoneHintLabel);
        
        panel.add(phonePanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Address field (optional)
        JPanel addressPanel = new JPanel();
        addressPanel.setLayout(new BoxLayout(addressPanel, BoxLayout.Y_AXIS));
        addressPanel.setOpaque(false);
        addressPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        addressPanel.setMaximumSize(new Dimension(470, 120));
        
        JLabel addressLabel = new JLabel("Address (optional)");
        addressLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addressLabel.setForeground(Color.WHITE);
        addressPanel.add(addressLabel);
        
        addressArea = new JTextArea();
        addressArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        addressArea.setRows(3);
        
        // Style the field
        addressArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 70, 90), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        addressArea.setBackground(new Color(45, 48, 71));
        addressArea.setForeground(Color.WHITE);
        addressArea.setCaretColor(Color.WHITE);
        
        JScrollPane addressScrollPane = new JScrollPane(addressArea);
        addressScrollPane.setPreferredSize(new Dimension(470, 80));
        addressScrollPane.setMaximumSize(new Dimension(470, 80));
        addressScrollPane.setBorder(null);
        
        addressPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        addressPanel.add(addressScrollPane);
        
        panel.add(addressPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Register button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.setMaximumSize(new Dimension(470, 50));
        
        registerButton = new JButton("Create Account");
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerButton.setPreferredSize(new Dimension(200, 40));
        
        // Style the button
        registerButton.setBackground(new Color(186, 80, 80)); // Red accent color
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effects
        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                registerButton.setBackground(new Color(200, 90, 90)); // Lighter
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                registerButton.setBackground(new Color(186, 80, 80)); // Original
            }
        });
        
        registerButton.addActionListener(e -> register());
        
        buttonPanel.add(registerButton);
        panel.add(buttonPanel);
        
        return panel;
    }
    
    /**
     * Create welcome panel
     */
    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Create a solid color background (accent red)
        panel.setBackground(new Color(186, 80, 80));
        panel.setBorder(new EmptyBorder(30, 40, 40, 40));
        
        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        
        // Guitar icon (can be replaced with an actual icon if available)
        JLabel iconLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw a simplified guitar icon in white
                g2d.setColor(Color.WHITE);
                
                // Guitar body
                g2d.fillOval(10, 30, 50, 70);
                
                // Guitar neck
                g2d.fillRect(32, 0, 6, 40);
                
                g2d.dispose();
            }
            
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(70, 100);
            }
        };
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Welcome text
        JLabel welcomeLabel = new JLabel("Welcome to");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel appNameLabel = new JLabel("SixStringMarket");
        appNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        appNameLabel.setForeground(Color.WHITE);
        appNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(iconLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(welcomeLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contentPanel.add(appNameLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        
        // Benefits
        JPanel benefitsPanel = new JPanel();
        benefitsPanel.setLayout(new BoxLayout(benefitsPanel, BoxLayout.Y_AXIS));
        benefitsPanel.setOpaque(false);
        
        String[] benefits = {
            "Buy and sell guitars easily",
            "Connect with other musicians",
            "Find rare and vintage instruments",
            "Safe and secure transactions",
            "Join our growing community"
        };
        
        for (String benefit : benefits) {
            JPanel benefitRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
            benefitRow.setOpaque(false);
            
            JLabel checkmark = new JLabel("✓");
            checkmark.setFont(new Font("Segoe UI", Font.BOLD, 16));
            checkmark.setForeground(Color.WHITE);
            
            JLabel benefitText = new JLabel(benefit);
            benefitText.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            benefitText.setForeground(Color.WHITE);
            
            benefitRow.add(checkmark);
            benefitRow.add(benefitText);
            
            benefitsPanel.add(benefitRow);
            benefitsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        
        contentPanel.add(benefitsPanel);
        contentPanel.add(Box.createVerticalGlue());
        
        panel.add(contentPanel, BorderLayout.CENTER);
        
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
            usernameValidationLabel.setForeground(new Color(231, 76, 60)); // Red
            return;
        }
        
        // Check if username is available
        if (userService.isUsernameTaken(username)) {
            usernameValidationLabel.setText("This username is already taken");
            usernameValidationLabel.setForeground(new Color(231, 76, 60)); // Red
        } else {
            usernameValidationLabel.setText("This username is available");
            usernameValidationLabel.setForeground(new Color(46, 204, 113)); // Green
        }
    }
    
    /**
     * Validate password and update strength indicator
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
        
        // Update color based on strength
        if (strength < 30) {
            passwordStrengthBar.setForeground(new Color(231, 76, 60)); // Red
            passwordValidationLabel.setText("Weak password");
            passwordValidationLabel.setForeground(new Color(231, 76, 60)); // Red
        } else if (strength < 60) {
            passwordStrengthBar.setForeground(new Color(230, 126, 34)); // Orange
            passwordValidationLabel.setText("Medium strength password");
            passwordValidationLabel.setForeground(new Color(230, 126, 34)); // Orange
        } else {
            passwordStrengthBar.setForeground(new Color(46, 204, 113)); // Green
            passwordValidationLabel.setText("Strong password");
            passwordValidationLabel.setForeground(new Color(46, 204, 113)); // Green
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
            confirmPasswordValidationLabel.setForeground(new Color(231, 76, 60)); // Red
        } else {
            confirmPasswordValidationLabel.setText("Passwords match");
            confirmPasswordValidationLabel.setForeground(new Color(46, 204, 113)); // Green
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
            emailValidationLabel.setForeground(new Color(231, 76, 60)); // Red
        } else {
            emailValidationLabel.setText("Valid email address");
            emailValidationLabel.setForeground(new Color(46, 204, 113)); // Green
        }
    }
    
    /**
     * Navigate back to login screen
     */
    private void backToLogin() {
        dispose();
        new LoginFrame().setVisible(true);
    }
    
    /**
     * Register new user
     */
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
                        showSuccessDialog();
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
        panel.setBackground(new Color(45, 48, 71));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel iconLabel = new JLabel("⚠");
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        iconLabel.setForeground(new Color(231, 76, 60)); // Red
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setPreferredSize(new Dimension(40, 40));
        
        JLabel messageLabel = new JLabel("<html>" + message + "</html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setForeground(Color.WHITE);
        
        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        okButton.setBackground(new Color(186, 80, 80)); // Accent color
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
    private void showSuccessDialog() {
        JDialog dialog = new JDialog(this, "Registration Successful", true);
        dialog.setSize(400, 150);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(new Color(45, 48, 71));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel iconLabel = new JLabel("✓");
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        iconLabel.setForeground(new Color(46, 204, 113)); // Green
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setPreferredSize(new Dimension(40, 40));
        
        JLabel messageLabel = new JLabel("<html>Your account has been created successfully!<br>You can now login.</html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setForeground(Color.WHITE);
        
        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        okButton.setBackground(new Color(186, 80, 80)); // Accent color
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
     * Main method for testing the registration form
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            new RegistrationFrame().setVisible(true);
        });
    }
}