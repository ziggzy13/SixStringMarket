package com.sixstringmarket.components;

import com.sixstringmarket.model.Guitar;
import com.sixstringmarket.model.Guitar.Status;
import com.sixstringmarket.ui.MainFrame;
import com.sixstringmarket.util.Constants;
import com.sixstringmarket.util.ImageHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Modern component for displaying a guitar in list view
 */
public class GuitarCard extends JPanel {
    
    private Guitar guitar;
    private MainFrame parentFrame;
    private boolean isHovered;
    private boolean isPressed;
    private JLabel imageLabel;
    private JLabel titleLabel;
    private JLabel priceLabel;
    private JLabel typeLabel;
    private JLabel statusLabel;
    
    /**
     * Constructor
     * @param guitar Guitar to display
     * @param parentFrame Parent frame
     */
    public GuitarCard(Guitar guitar, MainFrame parentFrame) {
        if (guitar == null) {
            throw new IllegalArgumentException("Guitar cannot be null");
        }
        this.guitar = guitar;
        this.parentFrame = parentFrame;
        this.isHovered = false;
        this.isPressed = false;
        
        setLayout(new BorderLayout(10, 0));
        setBackground(Constants.PANEL_COLOR);
        setBorder(null);
        setPreferredSize(new Dimension(600, 150));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        
        initComponents();
        
        // Add hover and click effects
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                isPressed = false;
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                repaint();
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                if (contains(e.getPoint())) {
                    // Open guitar details
                    parentFrame.showGuitarDetailsPanel(guitar.getGuitarId());
                }
                repaint();
            }
        });
    }
    
    /**
     * Initialize card components
     */
    private void initComponents() {
        try {
            // Left panel with image
            JPanel imagePanel = new JPanel(new BorderLayout());
            imagePanel.setOpaque(false);
            imagePanel.setPreferredSize(new Dimension(130, 130));
            
            // Load image or show placeholder
            BufferedImage image = null;
            if (guitar.getImagePath() != null && !guitar.getImagePath().isEmpty()) {
                image = ImageHandler.loadImage(guitar.getImagePath());
            }
            
            if (image != null) {
                // Scale image to fit
                Image scaledImage = image.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                imageLabel = new JLabel(new ImageIcon(scaledImage));
            } else {
                // Placeholder with guitar icon
                imageLabel = new JLabel();
                imageLabel.setIcon(ImageHandler.loadIcon("guitar.png", 64, 64));
                imageLabel.setHorizontalAlignment(JLabel.CENTER);
            }
            
            imageLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            imagePanel.add(imageLabel, BorderLayout.CENTER);
            add(imagePanel, BorderLayout.WEST);
            
            // Right panel with info
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setOpaque(false);
            infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 15));
            
            // Title
            titleLabel = new JLabel(guitar.getTitle());
            titleLabel.setFont(Constants.CARD_TITLE_FONT);
            titleLabel.setForeground(Constants.PRIMARY_COLOR);
            titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            infoPanel.add(titleLabel);
            
            infoPanel.add(Box.createVerticalStrut(5));
            
            // Brand and model
            String brandModel = guitar.getBrand();
            if (guitar.getModel() != null && !guitar.getModel().isEmpty()) {
                brandModel += " " + guitar.getModel();
            }
            JLabel brandModelLabel = new JLabel(brandModel);
            brandModelLabel.setFont(Constants.DEFAULT_FONT);
            brandModelLabel.setForeground(Constants.TEXT_COLOR);
            brandModelLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            infoPanel.add(brandModelLabel);
            
            infoPanel.add(Box.createVerticalStrut(5));
            
            // Type and condition
            String typeCondition = getGuitarTypeText(guitar.getType()) + ", " + getConditionText(guitar.getCondition());
            if (guitar.getManufacturingYear() != null) {
                typeCondition += ", " + guitar.getManufacturingYear() + " y.";
            }
            typeLabel = new JLabel(typeCondition);
            typeLabel.setFont(Constants.SMALL_FONT);
            typeLabel.setForeground(Constants.TEXT_SECONDARY_COLOR);
            typeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            infoPanel.add(typeLabel);
            
            infoPanel.add(Box.createVerticalStrut(10));
            
            // Bottom panel with price and status
            JPanel bottomPanel = new JPanel(new BorderLayout());
            bottomPanel.setOpaque(false);
            
            // Format price
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("bg", "BG"));
            String formattedPrice = currencyFormatter.format(guitar.getPrice());
            
            // Price
            priceLabel = new JLabel(formattedPrice);
            priceLabel.setFont(new Font(Constants.BOLD_FONT.getName(), Font.BOLD, 18));
            priceLabel.setForeground(Constants.SUCCESS_COLOR);
            bottomPanel.add(priceLabel, BorderLayout.WEST);
            
            // Status (shown only if not ACTIVE)
            if (guitar.getStatus() != null && guitar.getStatus() != Status.ACTIVE) {
                statusLabel = new JLabel(getStatusText(guitar.getStatus()));
                statusLabel.setFont(Constants.BOLD_FONT);
                
                switch (guitar.getStatus()) {
                    case SOLD:
                        statusLabel.setForeground(Constants.ERROR_COLOR);
                        break;
                    case RESERVED:
                        statusLabel.setForeground(Constants.WARNING_COLOR);
                        break;
                    default:
                        statusLabel.setForeground(Constants.TEXT_SECONDARY_COLOR);
                }
                
                bottomPanel.add(statusLabel, BorderLayout.EAST);
            }
            
            infoPanel.add(bottomPanel);
            
            add(infoPanel, BorderLayout.CENTER);
        } catch (Exception e) {
            // If there's any error, display a simple error message
            removeAll();
            setLayout(new BorderLayout());
            JLabel errorLabel = new JLabel("Error loading guitar: " + e.getMessage());
            errorLabel.setForeground(Color.RED);
            add(errorLabel, BorderLayout.CENTER);
            e.printStackTrace();
        }
    }
    
    /**
     * Custom painting of the card
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        int arc = Constants.ROUNDED_CORNER_RADIUS;
        
        // Draw card background
        if (isPressed) {
            g2d.setColor(new Color(240, 240, 240));
        } else if (isHovered) {
            g2d.setColor(new Color(248, 248, 248));
        } else {
            g2d.setColor(getBackground());
        }
        
        g2d.fill(new RoundRectangle2D.Double(0, 0, width, height, arc, arc));
        
        // Draw border
        g2d.setColor(isHovered ? Constants.SECONDARY_COLOR : new Color(230, 230, 230));
        g2d.setStroke(new BasicStroke(1));
        g2d.draw(new RoundRectangle2D.Double(0, 0, width - 1, height - 1, arc, arc));
        
        g2d.dispose();
    }
    
    /**
     * Convert guitar type to text
     * @param type Guitar type
     * @return Text representation
     */
    private String getGuitarTypeText(Guitar.GuitarType type) {
        if (type == null) return "Unknown";
        
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
     * Convert guitar condition to text
     * @param condition Guitar condition
     * @return Text representation
     */
    private String getConditionText(Guitar.Condition condition) {
        if (condition == null) return "Unknown";
        
        switch (condition) {
            case NEW: return "New";
            case USED: return "Used";
            case VINTAGE: return "Vintage";
            default: return "Unknown";
        }
    }
    
    /**
     * Convert guitar status to text
     * @param status Guitar status
     * @return Text representation
     */
    private String getStatusText(Guitar.Status status) {
        if (status == null) return "Unknown";
        
        switch (status) {
            case ACTIVE: return "Active";
            case SOLD: return "Sold";
            case RESERVED: return "Reserved";
            case REMOVED: return "Removed";
            default: return "Unknown";
        }
    }
}