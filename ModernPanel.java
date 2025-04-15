package com.sixstringmarket.ui.components;

import com.sixstringmarket.util.StyleManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Modern panel with styling from CSS
 */
public class ModernPanel extends JPanel {
    private boolean isRound;
    private boolean hasShadow;
    private Color backgroundColor;
    private String title;
    private Font titleFont;
    
    /**
     * Creates a standard panel
     */
    public ModernPanel() {
        this(null, true, false);
    }
    
    /**
     * Creates a card-style panel with a title
     * 
     * @param title Panel title
     */
    public ModernPanel(String title) {
        this(title, true, true);
    }
    
    /**
     * Creates a custom panel
     * 
     * @param title Panel title (null for no title)
     * @param isRound Whether to use rounded corners
     * @param hasShadow Whether to show a shadow
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
     * Initialize panel styling
     */
    private void setup() {
        setOpaque(false);
        setBorder(new EmptyBorder(
                StyleManager.PADDING_MEDIUM,
                StyleManager.PADDING_MEDIUM,
                StyleManager.PADDING_MEDIUM,
                StyleManager.PADDING_MEDIUM));
        
        // If there's a title, add extra padding at top
        if (title != null) {
            setBorder(new EmptyBorder(
                    StyleManager.PADDING_LARGE + titleFont.getSize(),
                    StyleManager.PADDING_MEDIUM,
                    StyleManager.PADDING_MEDIUM,
                    StyleManager.PADDING_MEDIUM));
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        // Draw shadow if needed
        if (hasShadow) {
            for (int i = 0; i < 5; i++) {
                g2d.setColor(new Color(0, 0, 0, 10));
                if (isRound) {
                    g2d.fill(new RoundRectangle2D.Double(
                            i, i, width - i * 2, height - i * 2,
                            StyleManager.CORNER_RADIUS, StyleManager.CORNER_RADIUS));
                } else {
                    g2d.fillRect(i, i, width - i * 2, height - i * 2);
                }
            }
        }
        
        // Draw background
        g2d.setColor(backgroundColor);
        if (isRound) {
            g2d.fill(new RoundRectangle2D.Double(0, 0, width, height,
                    StyleManager.CORNER_RADIUS, StyleManager.CORNER_RADIUS));
        } else {
            g2d.fillRect(0, 0, width, height);
        }
        
        // Draw title if present
        if (title != null) {
            g2d.setFont(titleFont);
            g2d.setColor(StyleManager.TEXT_COLOR);
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(title, StyleManager.PADDING_MEDIUM, StyleManager.PADDING_MEDIUM + fm.getAscent());
        }
        
        g2d.dispose();
        super.paintComponent(g);
    }
    
    /**
     * Set panel background color
     * 
     * @param color Background color
     */
    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
        repaint();
    }
    
    /**
     * Set panel title
     * 
     * @param title Panel title
     */
    public void setTitle(String title) {
        this.title = title;
        
        // Update padding if adding/removing title
        if (title != null) {
            setBorder(new EmptyBorder(
                    StyleManager.PADDING_LARGE + titleFont.getSize(),
                    StyleManager.PADDING_MEDIUM,
                    StyleManager.PADDING_MEDIUM,
                    StyleManager.PADDING_MEDIUM));
        } else {
            setBorder(new EmptyBorder(
                    StyleManager.PADDING_MEDIUM,
                    StyleManager.PADDING_MEDIUM,
                    StyleManager.PADDING_MEDIUM,
                    StyleManager.PADDING_MEDIUM));
        }
        
        repaint();
    }
    
    /**
     * Set title font
     * 
     * @param font Title font
     */
    public void setTitleFont(Font font) {
        this.titleFont = font;
        repaint();
    }
    
    /**
     * Set whether panel has rounded corners
     * 
     * @param isRound Whether to use rounded corners
     */
    public void setRound(boolean isRound) {
        this.isRound = isRound;
        repaint();
    }
    
    /**
     * Set whether panel has a shadow
     * 
     * @param hasShadow Whether to show a shadow
     */
    public void setHasShadow(boolean hasShadow) {
        this.hasShadow = hasShadow;
        repaint();
    }
}