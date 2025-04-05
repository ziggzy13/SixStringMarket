package com.sixstringmarket.ui.components;

import com.sixstringmarket.util.Constants;
import com.sixstringmarket.util.ColorScheme;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Modern panel with rounded corners and shadow options
 */
public class ModernPanel extends JPanel {
    
    private boolean isRound;
    private boolean hasShadow;
    private Color backgroundColor;
    private String title;
    private Font titleFont;
    
    /**
     * Create a modern panel with basic style
     */
    public ModernPanel() {
        this(null, true, false);
    }
    
    /**
     * Create a modern panel with custom style
     * @param title Panel title (null for no title)
     * @param isRound Flag whether panel has rounded corners
     * @param hasShadow Flag whether panel has shadow
     */
    public ModernPanel(String title, boolean isRound, boolean hasShadow) {
        this.title = title;
        this.isRound = isRound;
        this.hasShadow = hasShadow;
        this.backgroundColor = Constants.PANEL_COLOR;
        this.titleFont = Constants.SUBTITLE_FONT;
        
        setup();
    }
    
    /**
     * Initialize panel styles
     */
    private void setup() {
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(
                Constants.PADDING_MEDIUM,
                Constants.PADDING_MEDIUM,
                Constants.PADDING_MEDIUM,
                Constants.PADDING_MEDIUM));
        
        // If there's a title, add extra padding at top
        if (title != null) {
            setBorder(BorderFactory.createEmptyBorder(
                    Constants.PADDING_LARGE + titleFont.getSize(),
                    Constants.PADDING_MEDIUM,
                    Constants.PADDING_MEDIUM,
                    Constants.PADDING_MEDIUM));
        }
    }
    
    /**
     * Custom panel painting
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
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
                            Constants.ROUNDED_CORNER_RADIUS, Constants.ROUNDED_CORNER_RADIUS));
                } else {
                    g2d.fillRect(i, i, width - i * 2, height - i * 2);
                }
            }
        }
        
        // Draw panel background
        g2d.setColor(backgroundColor);
        
        if (isRound) {
            g2d.fill(new RoundRectangle2D.Double(
                    0, 0, width, height,
                    Constants.ROUNDED_CORNER_RADIUS, Constants.ROUNDED_CORNER_RADIUS));
        } else {
            g2d.fillRect(0, 0, width, height);
        }
        
        // Draw title with appropriate text color
        if (title != null) {
            g2d.setFont(titleFont);
            // Check if background is light and adjust text color accordingly
            Color textColor = Constants.getTextColorForBackground(backgroundColor);
            g2d.setColor(textColor);
            FontMetrics fm = g2d.getFontMetrics();
            int titleWidth = fm.stringWidth(title);
            int titleHeight = fm.getHeight();
            g2d.drawString(title, Constants.PADDING_MEDIUM, titleHeight);
        }
        
        g2d.dispose();
    }
    
    /**
     * Set background color
     * @param color Color
     */
    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
        repaint();
    }
    
    /**
     * Set panel title
     * @param title Title
     */
    public void setTitle(String title) {
        this.title = title;
        
        // Update padding
        setBorder(BorderFactory.createEmptyBorder(
                Constants.PADDING_LARGE + titleFont.getSize(),
                Constants.PADDING_MEDIUM,
                Constants.PADDING_MEDIUM,
                Constants.PADDING_MEDIUM));
        
        repaint();
    }
    
    /**
     * Set title font
     * @param font Font
     */
    public void setTitleFont(Font font) {
        this.titleFont = font;
        repaint();
    }
}