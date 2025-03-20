package com.sixstringmarket.components;

import com.sixstringmarket.model.Guitar;
import com.sixstringmarket.model.Guitar.Status;
import com.sixstringmarket.model.Guitar.GuitarType;
import com.sixstringmarket.model.Guitar.Condition;
import com.sixstringmarket.service.UserService;
import com.sixstringmarket.ui.MainFrame;
import com.sixstringmarket.util.ColorScheme;
import com.sixstringmarket.util.ImageHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

/**
 * Modern card component for displaying a guitar in a list view
 * with enhanced visual design, animations, and user interactions
 */
public class ModernGuitarCard extends JPanel {
    
    private Guitar guitar;
    private MainFrame parentFrame;
    private boolean isHovered;
    private boolean isPressed;
    
    // GUI components
    private JLabel imageLabel;
    private JLabel titleLabel;
    private JLabel priceLabel;
    private JLabel typeLabel;
    private JLabel statusLabel;
    private JLabel sellerLabel;
    
    // Cached scaled image for better performance
    private SoftReference<Image> cachedScaledImage;
    
    // Component dimensions
    private static final int CARD_HEIGHT = 160;
    private static final int IMAGE_SIZE = 125;
    private static final int CORNER_RADIUS = 12;
    private static final int PADDING = 15;
    
    // UserService for getting seller information
    private UserService userService;
    
    /**
     * Constructor
     * @param guitar Guitar data to display
     * @param parentFrame Parent frame for navigation
     */
    public ModernGuitarCard(Guitar guitar, MainFrame parentFrame) {
        this.guitar = Objects.requireNonNull(guitar, "Guitar cannot be null");
        this.parentFrame = Objects.requireNonNull(parentFrame, "Parent frame cannot be null");
        this.isHovered = false;
        this.isPressed = false;
        this.userService = new UserService();
        
        setLayout(new BorderLayout(PADDING, 0));
        setBackground(ColorScheme.CARD_BG);
        setBorder(null);
        setPreferredSize(new Dimension(0, CARD_HEIGHT));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, CARD_HEIGHT));
        
        initComponents();
        
        // Add hover and click effects with improved event handling
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isHovered) {
                    isHovered = true;
                    repaint();
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (isHovered || isPressed) {
                    isHovered = false;
                    isPressed = false;
                    repaint();
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                if (!isPressed && SwingUtilities.isLeftMouseButton(e)) {
                    isPressed = true;
                    repaint();
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (isPressed) {
                    isPressed = false;
                    repaint();
                    
                    if (contains(e.getPoint()) && SwingUtilities.isLeftMouseButton(e)) {
                        // Open guitar details
                        parentFrame.showGuitarDetailsPanel(guitar.getGuitarId());
                    }
                }
            }
        };
        
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }
    
    /**
     * Initialize card components
     */
    private void initComponents() {
        // --- Left panel with image ---
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setOpaque(false);
        imagePanel.setPreferredSize(new Dimension(IMAGE_SIZE + PADDING, IMAGE_SIZE + PADDING));
        
        // Create image panel
        imageLabel = createImageLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        add(imagePanel, BorderLayout.WEST);
        
        // --- Right panel with info ---
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(PADDING, 0, PADDING, PADDING));
        
        // Title with brand and model
        titleLabel = new JLabel(guitar.getTitle());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(ColorScheme.TEXT);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(titleLabel);
        
        infoPanel.add(Box.createVerticalStrut(5));
        
        // Brand and model
        String brandModel = guitar.getBrand();
        if (guitar.getModel() != null && !guitar.getModel().isEmpty()) {
            brandModel += " " + guitar.getModel();
        }
        JLabel brandModelLabel = new JLabel(brandModel);
        brandModelLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        brandModelLabel.setForeground(ColorScheme.TEXT);
        brandModelLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(brandModelLabel);
        
        infoPanel.add(Box.createVerticalStrut(5));
        
        // Type and condition
        String typeCondition = getGuitarTypeText(guitar.getType()) + ", " + getConditionText(guitar.getCondition());
        if (guitar.getManufacturingYear() != null) {
            typeCondition += ", " + guitar.getManufacturingYear();
        }
        typeLabel = new JLabel(typeCondition);
        typeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        typeLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        typeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(typeLabel);
        
        // Seller info
        try {
            sellerLabel = new JLabel("Seller: " + getSeller());
            sellerLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            sellerLabel.setForeground(ColorScheme.TEXT_SECONDARY);
            sellerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            infoPanel.add(sellerLabel);
        } catch (Exception e) {
            // If we can't get seller info, just skip this label
            System.err.println("Failed to load seller info: " + e.getMessage());
        }
        
        infoPanel.add(Box.createVerticalGlue());
        
        // Bottom panel with price and status
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        
        // Format price
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("en", "US"));
        String formattedPrice = currencyFormatter.format(guitar.getPrice());
        
        // Price
        priceLabel = new JLabel(formattedPrice);
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        priceLabel.setForeground(ColorScheme.SECONDARY);
        bottomPanel.add(priceLabel, BorderLayout.WEST);
        
        // Status (only shown if not ACTIVE)
        if (guitar.getStatus() != Status.ACTIVE) {
            statusLabel = new JLabel(getStatusTag(guitar.getStatus()));
            statusLabel.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
            statusLabel.setOpaque(true);
            
            switch (guitar.getStatus()) {
                case SOLD:
                    statusLabel.setBackground(ColorScheme.ERROR);
                    break;
                case RESERVED:
                    statusLabel.setBackground(ColorScheme.WARNING);
                    break;
                default:
                    statusLabel.setBackground(ColorScheme.TEXT_SECONDARY);
            }
            
            statusLabel.setForeground(Color.WHITE);
            statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            
            bottomPanel.add(statusLabel, BorderLayout.EAST);
        }
        
        infoPanel.add(bottomPanel);
        
        add(infoPanel, BorderLayout.CENTER);
    }
    
    /**
     * Create image label with proper error handling and caching
     */
    private JLabel createImageLabel() {
        // Check if we have a cached image
        Image scaledImage = null;
        if (cachedScaledImage != null) {
            scaledImage = cachedScaledImage.get();
        }
        
        // If no cached image, load and scale the image
        if (scaledImage == null) {
            try {
                BufferedImage image = null;
                if (guitar.getImagePath() != null && !guitar.getImagePath().isEmpty()) {
                    image = ImageHandler.loadImage(guitar.getImagePath());
                }
                
                if (image != null) {
                    // Use more efficient scaling method for thumbnails
                    scaledImage = createScaledImage(image, IMAGE_SIZE, IMAGE_SIZE);
                    cachedScaledImage = new SoftReference<>(scaledImage);
                    
                    JLabel label = new JLabel(new ImageIcon(scaledImage));
                    label.setBorder(BorderFactory.createLineBorder(
                            darken(ColorScheme.CARD_BG, 0.1f), 1));
                    return label;
                }
            } catch (Exception e) {
                System.err.println("Error loading guitar image: " + e.getMessage());
                // Continue to placeholder creation
            }
        } else {
            JLabel label = new JLabel(new ImageIcon(scaledImage));
            label.setBorder(BorderFactory.createLineBorder(
                    darken(ColorScheme.CARD_BG, 0.1f), 1));
            return label;
        }
        
        // If no image or error occurred, create placeholder
        return createPlaceholderLabel();
    }
    
    /**
     * Create a placeholder label with a guitar icon
     */
    private JLabel createPlaceholderLabel() {
        JLabel placeholderLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Light background
                g2d.setColor(darken(ColorScheme.CARD_BG, 0.05f));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Draw guitar icon
                g2d.setColor(ColorScheme.TEXT_SECONDARY);
                
                // Guitar body
                int bodyWidth = 50;
                int bodyHeight = 70;
                g2d.fillOval((getWidth() - bodyWidth) / 2, (getHeight() - bodyHeight) / 2 + 10, bodyWidth, bodyHeight);
                
                // Guitar neck
                int neckWidth = 10;
                int neckHeight = 50;
                g2d.fillRect((getWidth() - neckWidth) / 2, (getHeight() - bodyHeight) / 2 - neckHeight + 10, neckWidth, neckHeight);
                
                // Guitar head
                int headWidth = 16;
                int headHeight = 15;
                g2d.fillRect((getWidth() - headWidth) / 2, (getHeight() - bodyHeight) / 2 - neckHeight - headHeight + 10, headWidth, headHeight);
                
                // Sound hole
                g2d.setColor(darken(ColorScheme.CARD_BG, 0.05f));
                g2d.fillOval((getWidth() - 20) / 2, (getHeight() - 20) / 2 + 10, 20, 20);
                
                g2d.dispose();
            }
        };
        placeholderLabel.setPreferredSize(new Dimension(IMAGE_SIZE, IMAGE_SIZE));
        placeholderLabel.setBorder(BorderFactory.createLineBorder(darken(ColorScheme.CARD_BG, 0.1f), 1));
        return placeholderLabel;
    }
    
    /**
     * Create a scaled image with high quality for thumbnails
     */
    private Image createScaledImage(BufferedImage image, int width, int height) {
        // Calculate dimensions that preserve aspect ratio
        double scale = Math.min(
                (double) width / image.getWidth(), 
                (double) height / image.getHeight());
        
        int scaledWidth = (int) (image.getWidth() * scale);
        int scaledHeight = (int) (image.getHeight() * scale);
        
        // Create scaled instance with high quality
        Image scaledImage = image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
        
        // Force loading of image to avoid performance issues during painting
        MediaTracker tracker = new MediaTracker(this);
        tracker.addImage(scaledImage, 0);
        try {
            tracker.waitForID(0);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return scaledImage;
    }
    
    /**
     * Custom painting for modern look
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        // Draw card background
        if (isPressed) {
            g2d.setColor(darken(ColorScheme.CARD_BG, 0.1f));
        } else if (isHovered) {
            g2d.setColor(lighten(ColorScheme.CARD_BG, 0.05f));
        } else {
            g2d.setColor(ColorScheme.CARD_BG);
        }
        
        g2d.fill(new RoundRectangle2D.Double(0, 0, width, height, CORNER_RADIUS, CORNER_RADIUS));
        
        // Draw border
        g2d.setColor(isHovered ? 
            ColorScheme.SECONDARY : 
            darken(ColorScheme.CARD_BG, 0.1f));
        g2d.setStroke(new BasicStroke(1));
        g2d.draw(new RoundRectangle2D.Double(0, 0, width - 1, height - 1, CORNER_RADIUS, CORNER_RADIUS));
        
        g2d.dispose();
    }
    
    /**
     * Get guitar type text representation
     */
    private String getGuitarTypeText(GuitarType type) {
        switch (type) {
            case ACOUSTIC: return "Acoustic";
            case ELECTRIC: return "Electric";
            case CLASSICAL: return "Classical";
            case BASS: return "Bass";
            case OTHER: return "Other";
            default: return "Unknown";
        }
    }
    
    /**
     * Get guitar condition text representation
     */
    private String getConditionText(Condition condition) {
        switch (condition) {
            case NEW: return "New";
            case USED: return "Used";
            case VINTAGE: return "Vintage";
            default: return "Unknown";
        }
    }
    
    /**
     * Get status tag text
     */
    private String getStatusTag(Status status) {
        switch (status) {
            case ACTIVE: return "Active";
            case SOLD: return "Sold";
            case RESERVED: return "Reserved";
            case REMOVED: return "Removed";
            default: return "Unknown";
        }
    }
    
    /**
     * Get seller name
     */
    private String getSeller() {
        try {
            return userService.getUserById(guitar.getSellerId()).getUsername();
        } catch (Exception e) {
            return "Unknown";
        }
    }
    
    /**
     * Update the guitar data and refresh the component
     */
    public void updateGuitar(Guitar guitar) {
        this.guitar = Objects.requireNonNull(guitar, "Guitar cannot be null");
        this.cachedScaledImage = null; // Clear cached image
        removeAll();
        initComponents();
        revalidate();
        repaint();
    }
    
    /**
     * Utility method to lighten a color if ColorScheme doesn't provide it
     */
    private Color lighten(Color color, float factor) {
        try {
            // Try to use ColorScheme method if it exists
            return ColorScheme.lighten(color, factor);
        } catch (NoSuchMethodError e) {
            // Fallback implementation
            int r = Math.min(255, (int)(color.getRed() + (255 - color.getRed()) * factor));
            int g = Math.min(255, (int)(color.getGreen() + (255 - color.getGreen()) * factor));
            int b = Math.min(255, (int)(color.getBlue() + (255 - color.getBlue()) * factor));
            return new Color(r, g, b);
        }
    }
    
    /**
     * Utility method to darken a color if ColorScheme doesn't provide it
     */
    private Color darken(Color color, float factor) {
        try {
            // Try to use ColorScheme method if it exists
            return ColorScheme.darken(color, factor);
        } catch (NoSuchMethodError e) {
            // Fallback implementation
            int r = Math.max(0, (int)(color.getRed() * (1 - factor)));
            int g = Math.max(0, (int)(color.getGreen() * (1 - factor)));
            int b = Math.max(0, (int)(color.getBlue() * (1 - factor)));
            return new Color(r, g, b);
        }
    }
}