package com.sixstringmarket.ui.components;

import com.sixstringmarket.util.StyleManager;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Modern styled panel with rounded corners and title
 */
public class ModernPanel extends JPanel {
    
    private boolean isRound = true;
    private boolean hasShadow = false;
    private Color backgroundColor;
    private String title;
    private Font titleFont;
    
    /**
     * Default constructor
     */
    public ModernPanel() {
        this(null, true, false);
    }
    
    /**
     * Constructor with title and style options
     * @param title Panel title (can be null)
     * @param isRound Whether to use rounded corners
     * @param hasShadow Whether to draw shadow
     */
    public ModernPanel(String title, boolean isRound, boolean hasShadow) {
        this.title = title;
        this.isRound = isRound;
        this.hasShadow = hasShadow;
        this.backgroundColor = StyleManager.CARD_BG_COLOR;
        this.titleFont = StyleManager.SUBTITLE_FONT;
        
        setup();
    }
    
    /**
     * Set up panel appearance
     */
    private void setup() {
        setOpaque(false);
        setLayout(new BorderLayout());
        
        // Set padding based on whether there's a title
        if (title != null) {
            setBorder(BorderFactory.createEmptyBorder(
                StyleManager.PADDING_LARGE + titleFont.getSize(),
                StyleManager.PADDING_MEDIUM,
                StyleManager.PADDING_MEDIUM,
                StyleManager.PADDING_MEDIUM
            ));
        } else {
            setBorder(BorderFactory.createEmptyBorder(
                StyleManager.PADDING_MEDIUM,
                StyleManager.PADDING_MEDIUM,
                StyleManager.PADDING_MEDIUM,
                StyleManager.PADDING_MEDIUM
            ));
        }
    }
    
    /**
     * Set panel title
     * @param title Panel title
     */
    public void setTitle(String title) {
        this.title = title;
        
        // Update padding if adding a title
        if (title != null) {
            setBorder(BorderFactory.createEmptyBorder(
                StyleManager.PADDING_LARGE + titleFont.getSize(),
                StyleManager.PADDING_MEDIUM,
                StyleManager.PADDING_MEDIUM,
                StyleManager.PADDING_MEDIUM
            ));
        }
        
        repaint();
    }
    
    /**
     * Set title font
     * @param font Title font
     */
    public void setTitleFont(Font font) {
        this.titleFont = font;
        repaint();
    }
    
    /**
     * Set background color
     * @param color Background color
     */
    @Override
    public void setBackground(Color color) {
        this.backgroundColor = color;
        repaint();
    }
    
    /**
     * Set whether to use rounded corners
     * @param isRound true for rounded corners
     */
    public void setRound(boolean isRound) {
        this.isRound = isRound;
        repaint();
    }
    
    /**
     * Set whether to draw shadow
     * @param hasShadow true for shadow
     */
    public void setShadow(boolean hasShadow) {
        this.hasShadow = hasShadow;
        repaint();
    }
    
    /**
     * Custom painting for background, shadow and title
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        int cornerRadius = isRound ? StyleManager.BORDER_RADIUS : 0;
        
        // Draw shadow if enabled
        if (hasShadow) {
            for (int i = 0; i < 4; i++) {
                g2d.setColor(new Color(0, 0, 0, 10 - i * 2));
                
                if (isRound) {
                    g2d.fill(new RoundRectangle2D.Double(
                        i, i, width - i * 2, height - i * 2, cornerRadius, cornerRadius));
                } else {
                    g2d.fillRect(i, i, width - i * 2, height - i * 2);
                }
            }
        }
        
        // Draw background
        g2d.setColor(backgroundColor);
        
        if (isRound) {
            g2d.fill(new RoundRectangle2D.Double(0, 0, width, height, cornerRadius, cornerRadius));
        } else {
            g2d.fillRect(0, 0, width, height);
        }
        
        // Draw title if provided
        if (title != null) {
            g2d.setFont(titleFont);
            g2d.setColor(StyleManager.TEXT_COLOR);
            
            FontMetrics fm = g2d.getFontMetrics();
            int titleX = StyleManager.PADDING_MEDIUM;
            int titleY = StyleManager.PADDING_MEDIUM + fm.getAscent();
            
            g2d.drawString(title, titleX, titleY);
            
            // Draw title underline
            g2d.setColor(StyleManager.SECONDARY_COLOR);
            g2d.fillRect(titleX, titleY + 4, fm.stringWidth(title), 2);
        }
        
        g2d.dispose();
    }
    
    /**
     * Add a component to the content area
     * @param component Component to add
     */
    public void addContent(Component component) {
        add(component, BorderLayout.CENTER);
    }
}