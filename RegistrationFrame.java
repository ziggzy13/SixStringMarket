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
 * Modern registration screen with improved layout and better visual design
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
                
                g2d.dispose();
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        
        // Window controls
        JPanel windowControlsPanel = createWindowControlsPanel();
        backgroundPanel.add(windowControlsPanel, BorderLayout.NORTH);
        
        // Main container panel - use JScrollPane to ensure everything is visible
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        
        // Left panel with form
        JPanel formPanel = createFormPanel();
        
        // Wrap the form panel in a scroll pane to ensure everything is visible
        JScrollPane formScrollPane = new JScrollPane(formPanel);
        formScrollPane.setBorder(null);
        formScrollPane.setOpaque(false);
        formScrollPane.getViewport().setOpaque(false);
        formScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        formScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        mainPanel.add(formScrollPane, BorderLayout.CENTER);
        
        // Right panel with guitar image and welcome
        JPanel welcomePanel = createWelcomePanel();
        mainPanel.add(welcomePanel, BorderLayout.EAST);
        
        // Make the window draggable
        addDragSupport(backgroundPanel);
        
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
        backButton.setFont(new Font("SansSerif", Font.BOLD, 14));
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
        JButton minimizeBtn = new JButton("_");
        minimizeBtn.setForeground(ColorScheme.TEXT);
        minimizeBtn.setFont(new Font("SansSerif", Font.PLAIN, 18));
        minimizeBtn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        minimizeBtn.setContentAreaFilled(false);
        minimizeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        minimizeBtn.setFocusPainted(false);
        minimizeBtn.addActionListener(e -> setState(JFrame.ICONIFIED));
        
        // Close button
        JButton closeBtn = new JButton("X");
        closeBtn.setForeground(ColorScheme.TEXT);
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
        // Form panel container with card-like styling
        JPanel formContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Panel background
                g2d.setColor(ColorScheme.CARD_BG);
                g2d.fillRoundRect(10, 10, getWidth() - 20, getHeight() - 20, 20, 20);
                
                g2d.dispose();
            }
        };
        formContainer.setLayout(new BorderLayout());
        formContainer.setOpaque(false);
        formContainer.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Form title
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JLabel titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setForeground(ColorScheme.TEXT);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Join the SixStringMarket community");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subtitleLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(subtitleLabel);
        
        // Main form fields container with reduced spacing
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setOpaque(false);
        
        // Username field with validation
        JPanel usernamePanel = createFormFieldPanel("Username *");
        usernameField = createTextField(20);
        usernameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { validateUsername(); }
            @Override
            public void removeUpdate(DocumentEvent e) { validateUsername(); }
            @Override
            public void changedUpdate(DocumentEvent e) { validateUsername(); }
        });
        
        usernameValidationLabel = new JLabel(" ");
        usernameValidationLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        usernameValidationLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        
        usernamePanel.add(usernameField);
        usernamePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        usernamePanel.add(usernameValidationLabel);
        
        // Password field with strength indicator
        JPanel passwordPanel = createFormFieldPanel("Password *");
        passwordField = createPasswordField(20);
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
        passwordStrengthBar.setPreferredSize(new Dimension(Integer.MAX_VALUE, 5));
        passwordStrengthBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 5));
        
        passwordValidationLabel = new JLabel(" ");
        passwordValidationLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        passwordValidationLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        
        passwordPanel.add(passwordField);
        passwordPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        passwordPanel.add(passwordStrengthBar);
        passwordPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        passwordPanel.add(passwordValidationLabel);
        
        // Confirm password field
        JPanel confirmPasswordPanel = createFormFieldPanel("Confirm Password *");
        confirmPasswordField = createPasswordField(20);
        confirmPasswordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { validateConfirmPassword(); }
            @Override
            public void removeUpdate(DocumentEvent e) { validateConfirmPassword(); }
            @Override
            public void changedUpdate(DocumentEvent e) { validateConfirmPassword(); }
        });
        
        confirmPasswordValidationLabel = new JLabel(" ");
        confirmPasswordValidationLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        confirmPasswordValidationLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        
        confirmPasswordPanel.add(confirmPasswordField);
        confirmPasswordPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        confirmPasswordPanel.add(confirmPasswordValidationLabel);
        
        // Email field
        JPanel emailPanel = createFormFieldPanel("Email *");
        emailField = createTextField(20);
        emailField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { validateEmail(); }
            @Override
            public void removeUpdate(DocumentEvent e) { validateEmail(); }
            @Override
            public void changedUpdate(DocumentEvent e) { validateEmail(); }
        });
        
        emailValidationLabel = new JLabel(" ");
        emailValidationLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        emailValidationLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        
        emailPanel.add(emailField);
        emailPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        emailPanel.add(emailValidationLabel);
        
        // Phone field (optional)
        JPanel phonePanel = createFormFieldPanel("Phone (optional)");
        phoneField = createTextField(20);
        
        JLabel phoneFormatLabel = new JLabel("Example: 0885123456");
        phoneFormatLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        phoneFormatLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        
        phonePanel.add(phoneField);
        phonePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        phonePanel.add(phoneFormatLabel);
        
        // Register button
        registerButton = new JButton("Create Account");
        registerButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        registerButton.setBackground(ColorScheme.SECONDARY);
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        registerButton.setPreferredSize(new Dimension(Integer.MAX_VALUE, 45));
        registerButton.addActionListener(e -> register());
        
        // Button hover effect
        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                registerButton.setBackground(lighten(ColorScheme.SECONDARY, 0.1f));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                registerButton.setBackground(ColorScheme.SECONDARY);
            }
        });
        
        // Add fields to the panel with reduced spacing
        fieldsPanel.add(usernamePanel);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Reduced from 15 to 10
        fieldsPanel.add(passwordPanel);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Reduced from 15 to 10
        fieldsPanel.add(confirmPasswordPanel);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Reduced from 15 to 10
        fieldsPanel.add(emailPanel);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Reduced from 15 to 10
        fieldsPanel.add(phonePanel);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Reduced from 25 to 15
        fieldsPanel.add(registerButton);
        
        // Required fields note
        JLabel requiredFieldsNote = new JLabel("* Required fields");
        requiredFieldsNote.setFont(new Font("SansSerif", Font.ITALIC, 12));
        requiredFieldsNote.setForeground(ColorScheme.TEXT_SECONDARY);
        requiredFieldsNote.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        requiredFieldsNote.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        fieldsPanel.add(requiredFieldsNote);
        
        // Add to form container
        formContainer.add(titlePanel, BorderLayout.NORTH);
        formContainer.add(fieldsPanel, BorderLayout.CENTER);
        
        return formContainer;
    }
    
    /**
     * Create welcome panel with benefits
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
        panel.setPreferredSize(new Dimension(400, 0));
        
        // Content panel with welcome text
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(50, 30, 50, 30));
        
        // Welcome text
        JLabel welcomeLabel = new JLabel("Welcome to");
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel appNameLabel = new JLabel("SixStringMarket");
        appNameLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
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
        benefitsPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
        
        for (String benefit : benefits) {
            JPanel itemPanel = new JPanel(new BorderLayout(15, 0));
            itemPanel.setOpaque(false);
            itemPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            
            JLabel checkmarkLabel = new JLabel("✓");
            checkmarkLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
            checkmarkLabel.setForeground(Color.WHITE);
            
            JLabel textLabel = new JLabel(benefit);
            textLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
            textLabel.setForeground(Color.WHITE);
            
            itemPanel.add(checkmarkLabel, BorderLayout.WEST);
            itemPanel.add(textLabel, BorderLayout.CENTER);
            
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
     * Create form field panel with label
     */
    private JPanel createFormFieldPanel(String labelText) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70)); // Reduced height for more compact layout
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setForeground(ColorScheme.TEXT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        return panel;
    }
    
    /**
     * Create styled text field
     */
    private JTextField createTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, ColorScheme.FIELD_BORDER),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        field.setFont(new Font("SansSerif", Font.PLAIN, 16));
        field.setBackground(ColorScheme.FIELD_BG);
        field.setForeground(ColorScheme.TEXT);
        field.setCaretColor(ColorScheme.TEXT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        // Add focus effect
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 2, 0, ColorScheme.SECONDARY),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 2, 0, ColorScheme.FIELD_BORDER),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
            }
        });
        
        return field;
    }
    
    /**
     * Create styled password field
     */
    private JPasswordField createPasswordField(int columns) {
        JPasswordField field = new JPasswordField(columns);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, ColorScheme.FIELD_BORDER),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        field.setFont(new Font("SansSerif", Font.PLAIN, 16));
        field.setBackground(ColorScheme.FIELD_BG);
        field.setForeground(ColorScheme.TEXT);
        field.setCaretColor(ColorScheme.TEXT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        // Add focus effect
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 2, 0, ColorScheme.SECONDARY),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 2, 0, ColorScheme.FIELD_BORDER),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
            }
        });
        
        return field;
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
        panel.setBackground(ColorScheme.CARD_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel iconLabel = new JLabel("!");
        iconLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        iconLabel.setForeground(ColorScheme.ERROR);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setPreferredSize(new Dimension(40, 40));
        
        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        messageLabel.setForeground(ColorScheme.TEXT);
        
        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("SansSerif", Font.BOLD, 14));
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
        iconLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        iconLabel.setForeground(ColorScheme.SUCCESS);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setPreferredSize(new Dimension(40, 40));
        
        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        messageLabel.setForeground(ColorScheme.TEXT);
        
        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("SansSerif", Font.BOLD, 14));
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
     * Utility method to lighten a color
     */
    private Color lighten(Color color, float factor) {
        int r = Math.min(255, (int)(color.getRed() + (255 - color.getRed()) * factor));
        int g = Math.min(255, (int)(color.getGreen() + (255 - color.getGreen()) * factor));
        int b = Math.min(255, (int)(color.getBlue() + (255 - color.getBlue()) * factor));
        return new Color(r, g, b);
    }
}