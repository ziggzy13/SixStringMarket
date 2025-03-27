package com.sixstringmarket.ui;

import com.sixstringmarket.service.AuthenticationService;
import com.sixstringmarket.util.ColorScheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Modern login screen with animations, subtle guitar-themed design elements,
 * and intuitive, user-friendly interaction
 */
public class LoginFrame extends JFrame {
    
    // UI Components
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel statusLabel;
    
    // Animation components
    private Timer fadeTimer;
    private Timer shakeTimer;
    
    // Initial mouse position for dragging
    private Point initialClick;
    
    /**
     * Constructor
     */
    public LoginFrame() {
        // Window settings
        setTitle("SixStringMarket - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);
        
        // Initialize components
        try {
            initComponents();
            setupDragSupport();
            
            // Set rounded corners
            setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Error initializing login screen: " + e.getMessage(),
                "Initialization Error",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        
        // Focus on username field
        SwingUtilities.invokeLater(() -> usernameField.requestFocusInWindow());
    }
    
    /**
     * Initialize UI components
     */
    private void initComponents() {
        // Main background panel with gradient and guitar silhouettes
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
                
                // Draw decorative guitar silhouettes
                drawGuitarDecorations(g2d);
                
                g2d.dispose();
            }
            
            private void drawGuitarDecorations(Graphics2D g2d) {
                g2d.setColor(new Color(255, 255, 255, 15)); // Very subtle white
                
                // Bottom right large guitar
                drawGuitar(g2d, getWidth() - 100, getHeight() - 130, 180);
                
                // Top left small guitar
                drawGuitar(g2d, 80, 150, 100);
                
                // Musical notes
                g2d.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 20));
                g2d.drawString("♪", 50, 400);
                g2d.drawString("♫", 350, 200);
                g2d.drawString("♪", 280, 450);
                
                // Decorative strings/waves
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
        
        // Window controls panel
        JPanel controlsPanel = createWindowControlsPanel();
        backgroundPanel.add(controlsPanel, BorderLayout.NORTH);
        
        // Login form panel
        JPanel loginFormPanel = createLoginFormPanel();
        
        // Center the login panel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(loginFormPanel);
        
        backgroundPanel.add(centerPanel, BorderLayout.CENTER);
        
        setContentPane(backgroundPanel);
    }
    
    /**
     * Create window control buttons (close, minimize)
     */
    private JPanel createWindowControlsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panel.setOpaque(false);
        
        // Minimize button
        JButton minimizeBtn = new JButton("—");
        minimizeBtn.setForeground(ColorScheme.TEXT);
        minimizeBtn.setFont(new Font("Arial", Font.BOLD, 12));
        minimizeBtn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        minimizeBtn.setContentAreaFilled(false);
        minimizeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        minimizeBtn.setFocusPainted(false);
        
        // Close button
        JButton closeBtn = new JButton("✕");
        closeBtn.setForeground(ColorScheme.TEXT);
        closeBtn.setFont(new Font("Arial", Font.BOLD, 12));
        closeBtn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        closeBtn.setContentAreaFilled(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.setFocusPainted(false);
        
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
        
        // Add actions
        minimizeBtn.addActionListener(e -> setState(JFrame.ICONIFIED));
        closeBtn.addActionListener(e -> System.exit(0));
        
        panel.add(minimizeBtn);
        panel.add(closeBtn);
        
        return panel;
    }
    
    /**
     * Create the main login form panel
     */
    private JPanel createLoginFormPanel() {
        // Card panel with modern styling
        JPanel loginPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Panel background
                g2d.setColor(ColorScheme.CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                // Add subtle highlight at top
                GradientPaint topHighlight = new GradientPaint(
                    0, 0, new Color(255, 255, 255, 30),
                    0, 40, new Color(255, 255, 255, 0)
                );
                g2d.setPaint(topHighlight);
                g2d.fillRoundRect(0, 0, getWidth(), 40, 20, 20);
                
                // Add subtle accent line
                g2d.setColor(ColorScheme.SECONDARY);
                g2d.fillRect(0, 70, getWidth(), 2);
            }
        };
        
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setOpaque(false);
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));
        loginPanel.setPreferredSize(new Dimension(340, 500));
        
        // Logo and app name
        JLabel logoLabel = createLogoLabel();
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("SixStringMarket");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(ColorScheme.SECONDARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Guitar Trading Marketplace");
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        subtitleLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Login form
        JLabel loginHeaderLabel = new JLabel("Sign In");
        loginHeaderLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        loginHeaderLabel.setForeground(ColorScheme.TEXT);
        loginHeaderLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Username field with icon
        JPanel usernamePanel = new JPanel(new BorderLayout(8, 0));
        usernamePanel.setOpaque(false);
        usernamePanel.setMaximumSize(new Dimension(300, 70));
        usernamePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel userIcon = new JLabel("👤");  // Using emoji for simplicity
        userIcon.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        userIcon.setForeground(ColorScheme.TEXT);
        
        usernameField = createStyledTextField("Username");
        
        usernamePanel.add(createFieldHeaderLabel("Username"), BorderLayout.NORTH);
        usernamePanel.add(userIcon, BorderLayout.WEST);
        usernamePanel.add(usernameField, BorderLayout.CENTER);
        
        // Password field with icon
        JPanel passwordPanel = new JPanel(new BorderLayout(8, 0));
        passwordPanel.setOpaque(false);
        passwordPanel.setMaximumSize(new Dimension(300, 70));
        passwordPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel passwordIcon = new JLabel("🔒");  // Using emoji for simplicity
        passwordIcon.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordIcon.setForeground(ColorScheme.TEXT);
        
        passwordField = createStyledPasswordField("Password");
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
        });
        
        passwordPanel.add(createFieldHeaderLabel("Password"), BorderLayout.NORTH);
        passwordPanel.add(passwordIcon, BorderLayout.WEST);
        passwordPanel.add(passwordField, BorderLayout.CENTER);
        
        // Status message label (for errors)
        statusLabel = new JLabel(" ");  // Empty space to reserve height
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        statusLabel.setForeground(ColorScheme.ERROR);
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Login button
        loginButton = new JButton("Sign In");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(ColorScheme.BUTTON_PRIMARY);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(300, 40));
        
        // Button hover effect
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(ColorScheme.lighten(ColorScheme.BUTTON_PRIMARY, 0.1f));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(ColorScheme.BUTTON_PRIMARY);
            }
        });
        
        // Login action
        loginButton.addActionListener(e -> performLogin());
        
        // Register link
        registerButton = new JButton("Don't have an account? Create one");
        registerButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        registerButton.setForeground(ColorScheme.TEXT_SECONDARY);
        registerButton.setBorderPainted(false);
        registerButton.setContentAreaFilled(false);
        registerButton.setFocusPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Hover effect for register button
        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                registerButton.setForeground(ColorScheme.SECONDARY);
                registerButton.setText("<html><u>Don't have an account? Create one</u></html>");
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                registerButton.setForeground(ColorScheme.TEXT_SECONDARY);
                registerButton.setText("Don't have an account? Create one");
            }
        });
        
        registerButton.addActionListener(e -> openRegistrationForm());
        
        // Add all components to panel
        loginPanel.add(logoLabel);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        loginPanel.add(titleLabel);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        loginPanel.add(subtitleLabel);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        loginPanel.add(loginHeaderLabel);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        loginPanel.add(usernamePanel);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        loginPanel.add(passwordPanel);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        loginPanel.add(statusLabel);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        loginPanel.add(loginButton);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        loginPanel.add(registerButton);
        
        return loginPanel;
    }
    
    /**
     * Create a styled text field with visual enhancements
     */
    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField(15);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, ColorScheme.FIELD_BORDER),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(ColorScheme.FIELD_BG);
        field.setForeground(ColorScheme.TEXT);
        field.setCaretColor(ColorScheme.TEXT);
        field.putClientProperty("JTextField.placeholderText", placeholder);
        
        // Add focus effect
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 2, 0, ColorScheme.SECONDARY),
                    BorderFactory.createEmptyBorder(8, 8, 8, 8)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 2, 0, ColorScheme.FIELD_BORDER),
                    BorderFactory.createEmptyBorder(8, 8, 8, 8)
                ));
            }
        });
        
        return field;
    }
    
    /**
     * Create a styled password field with visual enhancements
     */
    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField(15);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, ColorScheme.FIELD_BORDER),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(ColorScheme.FIELD_BG);
        field.setForeground(ColorScheme.TEXT);
        field.setCaretColor(ColorScheme.TEXT);
        field.putClientProperty("JTextField.placeholderText", placeholder);
        
        // Add focus effect
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 2, 0, ColorScheme.SECONDARY),
                    BorderFactory.createEmptyBorder(8, 8, 8, 8)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 2, 0, ColorScheme.FIELD_BORDER),
                    BorderFactory.createEmptyBorder(8, 8, 8, 8)
                ));
            }
        });
        
        return field;
    }
    
    /**
     * Create field header label
     */
    private JLabel createFieldHeaderLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(ColorScheme.TEXT_SECONDARY);
        return label;
    }
    
    /**
     * Create guitar logo label
     */
    private JLabel createLogoLabel() {
        JLabel logoLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw circle background
                g2d.setColor(ColorScheme.SECONDARY);
                g2d.fillOval(0, 0, 70, 70);
                
                // Draw simplified guitar silhouette
                g2d.setColor(ColorScheme.CARD_BG);
                
                // Body
                g2d.fillOval(18, 35, 35, 35);
                
                // Neck
                g2d.fillRect(32, 10, 7, 35);
                
                // Sound hole
                g2d.setColor(ColorScheme.SECONDARY);
                g2d.drawOval(25, 45, 20, 20);
                
                // Strings
                g2d.setColor(ColorScheme.CARD_BG);
                g2d.setStroke(new BasicStroke(1.0f));
                for (int i = 0; i < 6; i++) {
                    g2d.drawLine(32, 15 + i * 3, 39, 15 + i * 3);
                }
                
                g2d.dispose();
            }
            
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(70, 70);
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
        
        return logoLabel;
    }
    
    /**
     * Setup draggable functionality to move the window
     */
    private void setupDragSupport() {
        // Add a mouse listener to track when the mouse is pressed
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
            }
        });
        
        // Add a mouse motion listener to move the window
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
     * Perform login with animation
     */
    private void performLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        // Validate input
        if (username.trim().isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password");
            shakeLoginButton();
            return;
        }
        
        // Show loading state
        loginButton.setEnabled(false);
        loginButton.setText("Signing in...");
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        // Use SwingWorker for background processing
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                // Small delay for better UX
                Thread.sleep(800);
                return AuthenticationService.getInstance().login(username, password);
            }
            
            @Override
            protected void done() {
                try {
                    boolean success = get();
                    
                    if (success) {
                        // Successful login
                        statusLabel.setText("");
                        // Open main frame
                        dispose();
                        new MainFrame().setVisible(true);
                    } else {
                        // Failed login
                        showError("Invalid username or password");
                        shakeLoginButton();
                        passwordField.setText("");
                        loginButton.setText("Sign In");
                        loginButton.setEnabled(true);
                        setCursor(Cursor.getDefaultCursor());
                    }
                } catch (Exception ex) {
                    showError("Login error: " + ex.getMessage());
                    loginButton.setText("Sign In");
                    loginButton.setEnabled(true);
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        };
        
        worker.execute();
    }
    
    /**
     * Show error message with fade-in effect
     */
    private void showError(String message) {
        statusLabel.setForeground(new Color(231, 76, 60, 0)); // Start transparent
        statusLabel.setText(message);
        
        // Stop any running fade animation
        if (fadeTimer != null && fadeTimer.isRunning()) {
            fadeTimer.stop();
        }
        
        // Start fade-in animation
        fadeTimer = new Timer(20, null);
        fadeTimer.addActionListener(new ActionListener() {
            int alpha = 0;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                alpha += 10;
                if (alpha > 255) {
                    alpha = 255;
                    fadeTimer.stop();
                    return;
                }
                
                statusLabel.setForeground(new Color(231, 76, 60, alpha));
            }
        });
        
        fadeTimer.start();
    }
    
    /**
     * Apply shake animation to login button
     */
    private void shakeLoginButton() {
        final int originalX = loginButton.getLocation().x;
        
        // Stop any running shake animation
        if (shakeTimer != null && shakeTimer.isRunning()) {
            shakeTimer.stop();
        }
        
        shakeTimer = new Timer(30, null);
        
        shakeTimer.addActionListener(new ActionListener() {
            int count = 0;
            int amplitude = 5;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (count >= 8) {
                    loginButton.setLocation(originalX, loginButton.getLocation().y);
                    shakeTimer.stop();
                    return;
                }
                
                int offset = (count % 2 == 0) ? amplitude : -amplitude;
                loginButton.setLocation(originalX + offset, loginButton.getLocation().y);
                count++;
                
                // Decrease amplitude slightly for natural effect
                if (count % 4 == 0) {
                    amplitude--;
                }
            }
        });
        
        shakeTimer.start();
    }
    
    /**
     * Open registration form
     */
    private void openRegistrationForm() {
        dispose();
        new RegistrationFrame().setVisible(true);
    }
}