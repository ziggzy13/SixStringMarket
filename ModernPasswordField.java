package com.sixstringmarket.ui.components;

import com.sixstringmarket.util.StyleManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Modern password field with styling and placeholder support
 */
public class ModernPasswordField extends JPasswordField {
    private String placeholder;
    private Color placeholderColor;
    private boolean isRound;
    private Color borderColor;
    private Color focusBorderColor;
    
    /**
     * Creates a standard password field
     * 
     * @param columns Number of columns
     */
    public ModernPasswordField(int columns) {
        this(null, columns);
    }
    
    /**
     * Creates a password field with placeholder text
     * 
     * @param placeholder Placeholder text
     * @param columns Number of columns
     */
    public ModernPasswordField(String placeholder, int columns) {
        super(columns);
        this.placeholder = placeholder;
        this.placeholderColor = StyleManager.TEXT_SECONDARY_COLOR;
        this.isRound = true;
        this.borderColor = StyleManager.FIELD_BORDER_COLOR;
        this.focusBorderColor = StyleManager.FIELD_FOCUS_COLOR;
        
        setup();
    }
    
    /**
     * Initialize password field styling
     */
    private void setup() {
        setFont(StyleManager.DEFAULT_FONT);
        setForeground(StyleManager.TEXT_COLOR);
        setBackground(StyleManager.FIELD_BG_COLOR);
        setCaretColor(StyleManager.TEXT_COLOR);
        setBorder(new EmptyBorder(10, 15, 10, 15));
        
        // Add focus effects
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                repaint();
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                repaint();
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        // Draw background
        g2d.setColor(getBackground());
        if (isRound) {
            g2d.fill(new RoundRectangle2D.Double(0, 0, width, height,
                    StyleManager.CORNER_RADIUS, StyleManager.CORNER_RADIUS));
        } else {
            g2d.fillRect(0, 0, width, height);
        }
        
        // Draw border with focus effect
        g2d.setColor(isFocusOwner() ? focusBorderColor : borderColor);
        g2d.setStroke(new BasicStroke(isFocusOwner() ? 2f : 1f));
        
        if (isRound) {
            g2d.draw(new RoundRectangle2D.Double(
                    isFocusOwner() ? 1 : 0.5, 
                    isFocusOwner() ? 1 : 0.5, 
                    width - (isFocusOwner() ? 2 : 1), 
                    height - (isFocusOwner() ? 2 : 1),
                    StyleManager.CORNER_RADIUS, StyleManager.CORNER_RADIUS));
        } else {
            g2d.drawRect(
                    isFocusOwner() ? 1 : 0, 
                    isFocusOwner() ? 1 : 0, 
                    width - (isFocusOwner() ? 2 : 1), 
                    height - (isFocusOwner() ? 2 : 1));
        }
        
        g2d.dispose();
        
        super.paintComponent(g);
        
        // Draw placeholder if password is empty and field doesn't have focus
        if (placeholder != null && getPassword().length == 0 && !isFocusOwner()) {
            g = getGraphics();
            if (g != null) {
                g.setColor(placeholderColor);
                g.setFont(getFont());
                
                // Calculate position
                FontMetrics fm = g.getFontMetrics();
                int x = getInsets().left;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                
                g.drawString(placeholder, x, y);
                g.dispose();
            }
        }
    }
    
    /**
     * Set placeholder text
     * 
     * @param placeholder Placeholder text
     */
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        repaint();
    }
    
    /**
     * Set placeholder text color
     * 
     * @param color Placeholder color
     */
    public void setPlaceholderColor(Color color) {
        this.placeholderColor = color;
        repaint();
    }
    
    /**
     * Set whether password field has rounded corners
     * 
     * @param isRound Whether to use rounded corners
     */
    public void setRound(boolean isRound) {
        this.isRound = isRound;
        repaint();
    }
    
    /**
     * Set border color
     * 
     * @param color Border color
     */
    public void setBorderColor(Color color) {
        this.borderColor = color;
        repaint();
    }
    
    /**
     * Set focus border color
     * 
     * @param color Focus border color
     */
    public void setFocusBorderColor(Color color) {
        this.focusBorderColor = color;
        repaint();
    }
}