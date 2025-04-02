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
 * Modern registration screen with improved layout and better visual design
 * Fixed to ensure all elements are visible
 */
public class RegistrationFrame extends JFrame {
    
    // Form fields
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField emailField;
    private JTextField phoneField;
    
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
        setSize(950, 700); // Increased height for better visibility
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true); // Remove window decorations
        
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
        // Main container panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color(37, 40, 61)); // Dark blue background
        
        // Create GridBagConstraints for layout
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Window controls (top bar with back, minimize, close buttons)
        JPanel windowControlsPanel = createWindowControlsPanel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(windowControlsPanel, gbc);
        
        // Left panel with form - CRITICAL: Set a proper size and make sure it's visible
        JPanel formPanel = createFormPanel();
        formPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // Add border for visibility
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.6;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 5);
        mainPanel.add(formPanel, gbc);
        
        // Right panel with welcome message
        JPanel welcomePanel = createWelcomePanel();
        welcomePanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // Add border for visibility
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.4;
        gbc.insets = new Insets(10, 5, 10, 10);
        mainPanel.add(welcomePanel, gbc);
        
        // Make the window draggable
        addDragSupport(mainPanel);
        
        // Set panel as content pane
        setContentPane(mainPanel);
    }
    
    /**
     * Create window control buttons (close, minimize)
     */
    private JPanel createWindowControlsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // Back button on left
        backButton = new JButton("« Back to Login");
        backButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        backButton.setForeground(Color.WHITE);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> backToLogin());
        
        // Hover effect
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backButton.setForeground(new Color(186, 80, 80)); // Red highlight
                backButton.setText("<html><u>« Back to Login</u></html>");
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                backButton.setForeground(Color.WHITE);
                backButton.setText("« Back to Login");
            }
        });
        
        // Control buttons on right
        JPanel controlButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controlButtonsPanel.setOpaque(false);
        
        // Minimize button
        JButton minimizeBtn = new JButton("_");
        minimizeBtn.setForeground(Color.WHITE);
        minimizeBtn.setFont(new Font("SansSerif", Font.PLAIN, 18));
        minimizeBtn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        minimizeBtn.setContentAreaFilled(false);
        minimizeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        minimizeBtn.setFocusPainted(false);
        minimizeBtn.addActionListener(e -> setState(JFrame.ICONIFIED));
        
        // Close button
        JButton closeBtn = new JButton("X");
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFont(new Font("SansSerif", Font.PLAIN, 18));
        closeBtn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        closeBtn.setContentAreaFilled(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(e -> System.exit(0));
        
        // Hover effects
        minimizeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                minimizeBtn.setForeground(new Color(186, 80, 80)); // Red highlight
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                minimizeBtn.setForeground(Color.WHITE);
            }
        });
        
        closeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                closeBtn.setForeground(new Color(211, 47, 47)); // Brighter red
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                closeBtn.setForeground(Color.WHITE);
            }
        });
        
        controlButtonsPanel.add(minimizeBtn);
        controlButtonsPanel.add(closeBtn);
        
        // Add components to panel
        panel.add(backButton, BorderLayout.WEST);
        panel.add(controlButtonsPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Create the main form panel
     */
    private JPanel createFormPanel() {
        // Form panel container with solid background for visibility
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        container.setBackground(Color.WHITE);
        
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        JLabel titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setForeground(new Color(37, 40, 61)); // Dark blue
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Join the SixStringMarket community");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(117, 117, 117)); // Gray
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(subtitleLabel);
        
        // Form fields panel
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setBackground(Color.WHITE);
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        // Username field with validation
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        usernameField.setBackground(new Color(245, 245, 245));
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        
        usernameValidationLabel = new JLabel(" ");
        usernameValidationLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        usernameValidationLabel.setForeground(new Color(117, 117, 117));
        
        // Password field
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        passwordField.setBackground(new Color(245, 245, 245));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        
        // Password strength bar
        passwordStrengthBar = new JProgressBar(0, 100);
        passwordStrengthBar.setForeground(new Color(211, 47, 47)); // Red
        passwordStrengthBar.setBackground(new Color(245, 245, 245));
        passwordStrengthBar.setBorderPainted(false);
        passwordStrengthBar.setStringPainted(false);
        passwordStrengthBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 5));
        
        passwordValidationLabel = new JLabel(" ");
        passwordValidationLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        passwordValidationLabel.setForeground(new Color(117, 117, 117));
        
        // Confirm password field
        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        confirmPasswordField.setBackground(new Color(245, 245, 245));
        confirmPasswordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        confirmPasswordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        
        confirmPasswordValidationLabel = new JLabel(" ");
        confirmPasswordValidationLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        confirmPasswordValidationLabel.setForeground(new Color(117, 117, 117));
        
        // Email field
        emailField = new JTextField(20);
        emailField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        emailField.setBackground(new Color(245, 245, 245));
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        emailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        
        emailValidationLabel = new JLabel(" ");
        emailValidationLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        emailValidationLabel.setForeground(new Color(117, 117, 117));
        
        // Phone field (optional)
        phoneField = new JTextField(20);
        phoneField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        phoneField.setBackground(new Color(245, 245, 245));
        phoneField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        phoneField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        
        // Create Account button
        registerButton = new JButton("Create Account");
        registerButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        registerButton.setBackground(new Color(186, 80, 80)); // Red
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        registerButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        registerButton.addActionListener(e -> register());
        
        // Add form fields with labels
        fieldsPanel.add(createLabeledField("Username *", usernameField, usernameValidationLabel));
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        fieldsPanel.add(createLabeledField("Password *", passwordField, null));
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        fieldsPanel.add(passwordStrengthBar);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        fieldsPanel.add(passwordValidationLabel);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        fieldsPanel.add(createLabeledField("Confirm Password *", confirmPasswordField, confirmPasswordValidationLabel));
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        fieldsPanel.add(createLabeledField("Email *", emailField, emailValidationLabel));
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        fieldsPanel.add(createLabeledField("Phone (optional)", phoneField, null));
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        fieldsPanel.add(registerButton);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Required fields note
        JLabel requiredFieldsNote = new JLabel("* Required fields");
        requiredFieldsNote.setFont(new Font("SansSerif", Font.ITALIC, 12));
        requiredFieldsNote.setForeground(new Color(117, 117, 117));
        requiredFieldsNote.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldsPanel.add(requiredFieldsNote);
        
        // Add validation listeners
        addValidationListeners();
        
        // Add panels to container
        container.add(titlePanel, BorderLayout.NORTH);
        container.add(fieldsPanel, BorderLayout.CENTER);
        
        return container;
    }
    
    /**
     * Create welcome panel with benefits
     */
    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(186, 80, 80)); // Red background
        
        // Content panel with welcome text
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Welcome text
        JLabel welcomeLabel = new JLabel("Welcome to");
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel appNameLabel = new JLabel("SixStringMarket");
        appNameLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
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
            JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            itemPanel.setOpaque(false);
            
            JLabel checkmarkLabel = new JLabel("✓");
            checkmarkLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
            checkmarkLabel.setForeground(Color.WHITE);
            
            JLabel textLabel = new JLabel(benefit);
            textLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
            textLabel.setForeground(Color.WHITE);
            
            itemPanel.add(checkmarkLabel);
            itemPanel.add(textLabel);
            
            benefitsPanel.add(itemPanel);
        }
        
        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(welcomeLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contentPanel.add(appNameLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        contentPanel.add(benefitsPanel);
        contentPanel.add(Box.createVerticalGlue());
        
        panel.add(contentPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create a labeled field panel
     */
    private JPanel createLabeledField(String labelText, JComponent field, JComponent validationLabel) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setForeground(new Color(70, 70, 70));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(field);
        
        if (validationLabel != null) {
            validationLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(Box.createRigidArea(new Dimension(0, 5)));
            panel.add(validationLabel);
        }
        
        return panel;
    }
    
    /**
     * Add validation listeners to form fields
     */
    private void addValidationListeners() {
        // Username validation
        usernameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { validateUsername(); }
            @Override
            public void removeUpdate(DocumentEvent e) { validateUsername(); }
            @Override
            public void changedUpdate(DocumentEvent e) { validateUsername(); }
        });
        
        // Password validation
        passwordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { validatePassword(); }
            @Override
            public void removeUpdate(DocumentEvent e) { validatePassword(); }
            @Override
            public void changedUpdate(DocumentEvent e) { validatePassword(); }
        });
        
        // Confirm password validation
        confirmPasswordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { validateConfirmPassword(); }
            @Override
            public void removeUpdate(DocumentEvent e) { validateConfirmPassword(); }
            @Override
            public void changedUpdate(DocumentEvent e) { validateConfirmPassword(); }
        });
        
        // Email validation
        emailField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { validateEmail(); }
            @Override
            public void removeUpdate(DocumentEvent e) { validateEmail(); }
            @Override
            public void changedUpdate(DocumentEvent e) { validateEmail(); }
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
            usernameValidationLabel.setForeground(new Color(211, 47, 47)); // Red
            return;
        }
        
        // Check if username is available
        if (userService.isUsernameTaken(username)) {
            usernameValidationLabel.setText("This username is already taken");
            usernameValidationLabel.setForeground(new Color(211, 47, 47)); // Red
        } else {
            usernameValidationLabel.setText("This username is available");
            usernameValidationLabel.setForeground(new Color(76, 175, 80)); // Green
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
            passwordStrengthBar.setForeground(new Color(211, 47, 47)); // Red
            passwordValidationLabel.setText("Weak password");
            passwordValidationLabel.setForeground(new Color(211, 47, 47)); // Red
        } else if (strength < 60) {
            passwordStrengthBar.setForeground(new Color(255, 152, 0)); // Orange
            passwordValidationLabel.setText("Medium strength password");
            passwordValidationLabel.setForeground(new Color(255, 152, 0)); // Orange
        } else {
            passwordStrengthBar.setForeground(new Color(76, 175, 80)); // Green
            passwordValidationLabel.setText("Strong password");
            passwordValidationLabel.setForeground(new Color(76, 175, 80)); // Green
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
            confirmPasswordValidationLabel.setForeground(new Color(211, 47, 47)); // Red
        } else {
            confirmPasswordValidationLabel.setText("Passwords match");
            confirmPasswordValidationLabel.setForeground(new Color(76, 175, 80)); // Green
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
            emailValidationLabel.setForeground(new Color(211, 47, 47)); // Red
        } else {
            emailValidationLabel.setText("Valid email address");
            emailValidationLabel.setForeground(new Color(76, 175, 80)); // Green
        }
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
            private String errorMessage = null;
            
            @Override
            protected Boolean doInBackground() throws Exception {
                // Small delay for better UX
                Thread.sleep(800);
                try {
                    return userService.registerUser(username, password, email, phone, null);
                } catch (IllegalArgumentException e) {
                    errorMessage = e.getMessage();
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
                        if (errorMessage != null) {
                            showErrorDialog(errorMessage);
                        } else {
                            showErrorDialog("Error during registration. Please try again.");
                        }
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
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel iconLabel = new JLabel("!");
        iconLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        iconLabel.setForeground(new Color(211, 47, 47)); // Red
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setPreferredSize(new Dimension(40, 40));
        
        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        messageLabel.setForeground(new Color(70, 70, 70));
        
        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        okButton.setBackground(new Color(186, 80, 80)); // Red
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
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel iconLabel = new JLabel("✓");
        iconLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        iconLabel.setForeground(new Color(76, 175, 80)); // Green
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setPreferredSize(new Dimension(40, 40));
        
        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        messageLabel.setForeground(new Color(70, 70, 70));
        
        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        okButton.setBackground(new Color(186, 80, 80)); // Red
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
     * Main method to test the registration frame
     */
    public static void main(String[] args) {
        try {
            // Set look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Create and show registration frame
            SwingUtilities.invokeLater(() -> {
                new RegistrationFrame().setVisible(true);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}