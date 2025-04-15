package com.sixstringmarket.components;

import com.sixstringmarket.model.Guitar;
import com.sixstringmarket.ui.MainFrame;
import com.sixstringmarket.util.Constants;
import com.sixstringmarket.util.ImageHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Custom component to display a guitar card in a list
 */
public class GuitarCard extends JPanel {
    
    private Guitar guitar;
    private MainFrame parentFrame;
    private boolean isHovered;
    private boolean isPressed;
    private boolean isRound = true;
    private boolean hasShadow = true;
    
    // Shadow properties
    private static final int SHADOW_SIZE = 5;
    private static final Color SHADOW_COLOR = new Color(0, 0, 0, 20);
    
    // Corner radius
    private static final int CORNER_RADIUS = 8;
    
    /**
     * Constructor
     * @param guitar Guitar data to display
     * @param parentFrame Parent frame for navigation
     */
    public GuitarCard(Guitar guitar, MainFrame parentFrame) {
        this.guitar = guitar;
        this.parentFrame = parentFrame;
        
        setLayout(new BorderLayout(10, 0));
        setBackground(Constants.PANEL_COLOR);
        setBorder(null);
        setPreferredSize(new Dimension(0, 150));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        
        // Make panel non-opaque to allow for custom painting
        setOpaque(false);
        
        initComponents();
        
        // Add event listeners for hover and click effects
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                parentFrame.showGuitarDetailsPanel(guitar.getGuitarId());
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                isPressed = false;
                repaint();
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
                
                if (contains(e.getPoint())) {
                    parentFrame.showGuitarDetailsPanel(guitar.getGuitarId());
                }
            }
        });
    }
    
    /**
     * Set whether the card has rounded corners
     * @param isRound True for rounded corners
     */
    public void setRound(boolean isRound) {
        this.isRound = isRound;
        repaint();
    }
    
    /**
     * Set whether the card has a shadow
     * @param hasShadow True for shadow effect
     */
    public void setHasShadow(boolean hasShadow) {
        this.hasShadow = hasShadow;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        int arc = isRound ? CORNER_RADIUS : 0;
        
        // Draw shadow if enabled
        if (hasShadow) {
            for (int i = 0; i < SHADOW_SIZE; i++) {
                g2d.setColor(new Color(SHADOW_COLOR.getRed(), 
                                       SHADOW_COLOR.getGreen(), 
                                       SHADOW_COLOR.getBlue(), 
                                       SHADOW_COLOR.getAlpha() / (i + 1)));
                
                if (isRound) {
                    g2d.fill(new RoundRectangle2D.Double(i, i, width - i * 2, height - i * 2, arc, arc));
                } else {
                    g2d.fillRect(i, i, width - i * 2, height - i * 2);
                }
            }
        }
        
        // Draw background
        if (isPressed) {
            g2d.setColor(darken(Constants.PANEL_COLOR, 0.1f));
        } else if (isHovered) {
            g2d.setColor(lighten(Constants.PANEL_COLOR, 0.05f));
        } else {
            g2d.setColor(Constants.PANEL_COLOR);
        }
        
        if (isRound) {
            g2d.fill(new RoundRectangle2D.Double(0, 0, width, height, arc, arc));
        } else {
            g2d.fillRect(0, 0, width, height);
        }
        
        // Draw border
        g2d.setColor(isHovered ? Constants.SECONDARY_COLOR : 
                     darken(Constants.PANEL_COLOR, 0.1f));
        g2d.setStroke(new BasicStroke(1));
        
        if (isRound) {
            g2d.draw(new RoundRectangle2D.Double(0, 0, width - 1, height - 1, arc, arc));
        } else {
            g2d.drawRect(0, 0, width - 1, height - 1);
        }
        
        g2d.dispose();
    }
    
    /**
     * Initialize card components
     */
    private void initComponents() {
        // Image panel - fixed size and centered
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setPreferredSize(new Dimension(140, 140));
        imagePanel.setOpaque(false);
        imagePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Create image container with border
        JPanel imageContainer = new JPanel(new BorderLayout());
        imageContainer.setBackground(Color.WHITE);
        imageContainer.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        
        // Try to load the guitar image
        BufferedImage image = null;
        if (guitar.getImagePath() != null && !guitar.getImagePath().isEmpty()) {
            image = ImageHandler.loadImage(guitar.getImagePath());
        }
        
        JLabel imageLabel;
        if (image != null) {
            Image scaledImage = image.getScaledInstance(130, 130, Image.SCALE_SMOOTH);
            imageLabel = new JLabel(new ImageIcon(scaledImage));
        } else {
            // Create a better-looking placeholder
            imageLabel = new JLabel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Background
                    g2d.setColor(new Color(245, 245, 250));
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    
                    // Guitar silhouette
                    g2d.setColor(new Color(180, 180, 200));
                    
                    // Draw guitar body
                    int bodyWidth = 60;
                    int bodyHeight = 80;
                    g2d.fillOval((getWidth() - bodyWidth)/2, (getHeight() - bodyHeight)/2 + 10, bodyWidth, bodyHeight);
                    
                    // Draw neck
                    int neckWidth = 12;
                    int neckHeight = 60;
                    g2d.fillRect((getWidth() - neckWidth)/2, (getHeight() - bodyHeight)/2 - neckHeight + 10, 
                                neckWidth, neckHeight);
                    
                    // Draw head
                    int headWidth = 20;
                    int headHeight = 15;
                    g2d.fillRect((getWidth() - headWidth)/2, (getHeight() - bodyHeight)/2 - neckHeight - headHeight + 10, 
                                headWidth, headHeight);
                    
                    // Draw sound hole
                    g2d.setColor(new Color(245, 245, 250));
                    g2d.fillOval((getWidth() - 30)/2, (getHeight() - 30)/2 + 15, 30, 30);
                    
                    g2d.dispose();
                }
            };
            imageLabel.setPreferredSize(new Dimension(130, 130));
        }
        
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageContainer.add(imageLabel, BorderLayout.CENTER);
        imagePanel.add(imageContainer, BorderLayout.CENTER);
        add(imagePanel, BorderLayout.WEST);
        
        // Information panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));
        
        // Guitar title
        JLabel titleLabel = new JLabel(guitar.getTitle());
        titleLabel.setFont(new Font(Constants.BOLD_FONT.getName(), Font.BOLD, 16));
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
            typeCondition += ", " + guitar.getManufacturingYear() + " г.";
        }
        JLabel detailsLabel = new JLabel(typeCondition);
        detailsLabel.setFont(Constants.SMALL_FONT);
        detailsLabel.setForeground(Constants.TEXT_SECONDARY_COLOR);
        detailsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(detailsLabel);
        
        infoPanel.add(Box.createVerticalGlue());
        
        // Price with nice formatting
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("bg", "BG"));
        String formattedPrice = formatter.format(guitar.getPrice()).replace("лв", "лв.");
        
        JLabel priceLabel = new JLabel(formattedPrice);
        priceLabel.setFont(new Font(Constants.BOLD_FONT.getName(), Font.BOLD, 18));
        priceLabel.setForeground(Constants.SUCCESS_COLOR); // Green
        priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(priceLabel);
        
        add(infoPanel, BorderLayout.CENTER);
    }
    
    /**
     * Convert guitar type to display text
     */
    private String getGuitarTypeText(Guitar.GuitarType type) {
        switch (type) {
            case ACOUSTIC: return "Акустична";
            case ELECTRIC: return "Електрическа";
            case CLASSICAL: return "Класическа";
            case BASS: return "Бас";
            case OTHER: return "Друга";
            default: return "Неизвестен";
        }
    }
    
    /**
     * Convert condition to display text
     */
    private String getConditionText(Guitar.Condition condition) {
        switch (condition) {
            case NEW: return "Нова";
            case USED: return "Употребявана";
            case VINTAGE: return "Винтидж";
            default: return "Неизвестно";
        }
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
    
    /**
     * Utility method to darken a color
     */
    private Color darken(Color color, float factor) {
        int r = Math.max(0, (int)(color.getRed() * (1 - factor)));
        int g = Math.max(0, (int)(color.getGreen() * (1 - factor)));
        int b = Math.max(0, (int)(color.getBlue() * (1 - factor)));
        return new Color(r, g, b);
    }
}