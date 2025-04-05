package com.sixstringmarket.ui.components;

import com.sixstringmarket.util.Constants;
import com.sixstringmarket.util.ColorScheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Modern button with hover and press animations
 */
public class ModernButton extends JButton {
    
    private Color backgroundColor;
    private Color hoverColor;
    private Color pressedColor;
    private Color textColor;
    private boolean isRound;
    
    /**
     * Create modern button with basic style
     * @param text Button text
     */
    public ModernButton(String text) {
        this(text, Constants.PRIMARY_COLOR, Color.WHITE, true);
    }
    
    /**
     * Create modern button with accent style
     * @param text Button text
     * @param isAccent Flag whether button is accent
     */
    public ModernButton(String text, boolean isAccent) {
        this(text, isAccent ? Constants.ACCENT_COLOR : Constants.PRIMARY_COLOR, Color.WHITE, true);
    }
    
    /**
     * Create modern button with custom style
     * @param text Button text
     * @param backgroundColor Background color
     * @param textColor Text color
     * @param isRound Flag whether button has rounded corners
     */
    public ModernButton(String text, Color backgroundColor, Color textColor, boolean isRound) {
        super(text);
        this.backgroundColor = backgroundColor;
        this.hoverColor = lightenColor(backgroundColor, 0.2f);
        this.pressedColor = darkenColor(backgroundColor, 0.2f);
        
        // Calculate appropriate text color based on background brightness
        this.textColor = ColorScheme.getTextColorForBackground(backgroundColor);
        
        // Override if explicit text color is provided
        if (textColor != null) {
            this.textColor = textColor;
        }
        
        this.isRound = isRound;
        
        setup();
    }
    
    /**
     * Initialize styles and events
     */
    private void setup() {
        // Appearance configuration
        setFont(Constants.DEFAULT_FONT);
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setForeground(textColor);
        setOpaque(false);
        
        // Size configuration
        setPreferredSize(Constants.BUTTON_DIMENSION);
        
        // Add hover and click effects
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor);
                repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(backgroundColor);
                repaint();
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(pressedColor);
                repaint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (contains(e.getPoint())) {
                    setBackground(hoverColor);
                } else {
                    setBackground(backgroundColor);
                }
                repaint();
            }
        });
    }
    
    /**
     * Custom button painting
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw button background
        if (isRound) {
            // Rounded button
            int arc = Constants.ROUNDED_CORNER_RADIUS;
            g2d.setColor(isEnabled() ? getBackground() != null ? getBackground() : backgroundColor : Constants.TEXT_SECONDARY_COLOR);
            g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), arc, arc));
        } else {
            // Rectangular button
            g2d.setColor(isEnabled() ? getBackground() != null ? getBackground() : backgroundColor : Constants.TEXT_SECONDARY_COLOR);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
        
        g2d.dispose();
        
        super.paintComponent(g);
    }
    
    /**
     * Lighten color
     * @param color Color to lighten
     * @param factor Lighten factor (0.0 to 1.0)
     * @return Lightened color
     */
    private Color lightenColor(Color color, float factor) {
        int r = Math.min(255, (int) (color.getRed() + (255 - color.getRed()) * factor));
        int g = Math.min(255, (int) (color.getGreen() + (255 - color.getGreen()) * factor));
        int b = Math.min(255, (int) (color.getBlue() + (255 - color.getBlue()) * factor));
        return new Color(r, g, b);
    }
    
    /**
     * Darken color
     * @param color Color to darken
     * @param factor Darken factor (0.0 to 1.0)
     * @return Darkened color
     */
    private Color darkenColor(Color color, float factor) {
        int r = Math.max(0, (int) (color.getRed() * (1 - factor)));
        int g = Math.max(0, (int) (color.getGreen() * (1 - factor)));
        int b = Math.max(0, (int) (color.getBlue() * (1 - factor)));
        return new Color(r, g, b);
    }
    
    /**
     * Set button color
     * @param color Color
     */
    public void setButtonColor(Color color) {
        this.backgroundColor = color;
        this.hoverColor = lightenColor(color, 0.2f);
        this.pressedColor = darkenColor(color, 0.2f);
        
        // Update text color based on background brightness
        this.textColor = ColorScheme.getTextColorForBackground(color);
        setForeground(textColor);
        
        setBackground(color);
        repaint();
    }
    
    /**
     * Set small button size
     */
    public void setSmallSize() {
        setPreferredSize(Constants.SMALL_BUTTON_DIMENSION);
    }
}