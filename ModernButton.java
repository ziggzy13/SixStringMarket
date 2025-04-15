package com.sixstringmarket.ui.components;

import com.sixstringmarket.util.StyleManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Modern button with styling and hover effects
 */
public class ModernButton extends JButton {
    private Color backgroundColor;
    private Color hoverColor;
    private Color pressedColor;
    private boolean isRound;
    private boolean isHovered;
    private boolean isPressed;
    
    /**
     * Creates a primary button
     * 
     * @param text Button text
     */
    public ModernButton(String text) {
        this(text, StyleManager.BUTTON_PRIMARY_COLOR, Color.WHITE, true);
    }
    
    /**
     * Creates a button with custom colors
     * 
     * @param text Button text
     * @param isAccent Whether to use accent color
     */
    public ModernButton(String text, boolean isAccent) {
        this(text, isAccent ? StyleManager.SECONDARY_COLOR : StyleManager.BUTTON_SECONDARY_COLOR, 
             Color.WHITE, true);
    }
    
    /**
     * Creates a button with fully custom styling
     * 
     * @param text Button text
     * @param backgroundColor Button background color
     * @param textColor Button text color
     * @param isRound Whether to use rounded corners
     */
    public ModernButton(String text, Color backgroundColor, Color textColor, boolean isRound) {
        super(text);
        this.backgroundColor = backgroundColor;
        this.hoverColor = StyleManager.lighten(backgroundColor, 0.1f);
        this.pressedColor = StyleManager.darken(backgroundColor, 0.1f);
        this.isRound = isRound;
        this.isHovered = false;
        this.isPressed = false;
        
        setup();
    }
    
    /**
     * Setup the button styling and event handlers
     */
    private void setup() {
        setFont(StyleManager.DEFAULT_FONT);
        setForeground(Color.WHITE);
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setOpaque(false);
        
        // Add hover/press effects
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isEnabled()) {
                    isHovered = true;
                    repaint();
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                isPressed = false;
                repaint();
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                if (isEnabled()) {
                    isPressed = true;
                    repaint();
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Determine button color based on state
        if (!isEnabled()) {
            g2d.setColor(StyleManager.BUTTON_DISABLED_COLOR);
        } else if (isPressed) {
            g2d.setColor(pressedColor);
        } else if (isHovered) {
            g2d.setColor(hoverColor);
        } else {
            g2d.setColor(backgroundColor);
        }
        
        // Draw button background
        if (isRound) {
            g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 
                    StyleManager.CORNER_RADIUS, StyleManager.CORNER_RADIUS));
        } else {
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
        
        g2d.dispose();
        
        super.paintComponent(g);
    }
    
    /**
     * Set the button as a primary button
     */
    public void setPrimary() {
        setButtonColor(StyleManager.BUTTON_PRIMARY_COLOR);
    }
    
    /**
     * Set the button as a secondary button
     */
    public void setSecondary() {
        setButtonColor(StyleManager.BUTTON_SECONDARY_COLOR);
    }
    
    /**
     * Set the button as a success button
     */
    public void setSuccess() {
        setButtonColor(StyleManager.SUCCESS_COLOR);
    }
    
    /**
     * Set the button as a danger button
     */
    public void setDanger() {
        setButtonColor(StyleManager.ERROR_COLOR);
    }
    
    /**
     * Set a custom button color
     * 
     * @param color Button background color
     */
    public void setButtonColor(Color color) {
        this.backgroundColor = color;
        this.hoverColor = StyleManager.lighten(color, 0.1f);
        this.pressedColor = StyleManager.darken(color, 0.1f);
        repaint();
    }
    
    /**
     * Set whether to use rounded corners
     * 
     * @param isRound Whether to use rounded corners
     */
    public void setRound(boolean isRound) {
        this.isRound = isRound;
        repaint();
    }
}