package com.sixstringmarket.ui.components;

import com.sixstringmarket.util.Constants;
import com.sixstringmarket.util.ColorScheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.RoundRectangle2D;

/**
 * Modern text field with focus animation and placeholder
 */
public class ModernTextField extends JTextField {
    
    private String placeholder;
    private Color placeholderColor;
    private boolean isRound;
    private Color borderColor;
    private Color focusBorderColor;
    
    /**
     * Create modern text field
     * @param columns Number of columns
     */
    public ModernTextField(int columns) {
        this(null, columns);
    }
    
    /**
     * Create modern text field with placeholder
     * @param placeholder Placeholder text
     * @param columns Number of columns
     */
    public ModernTextField(String placeholder, int columns) {
        super(columns);
        this.placeholder = placeholder;
        this.placeholderColor = Constants.TEXT_SECONDARY_COLOR;
        this.isRound = true;
        this.borderColor = new Color(220, 220, 220);
        this.focusBorderColor = Constants.PRIMARY_COLOR;
        
        setup();
    }
    
    /**
     * Initialize styles and events
     */
    private void setup() {
        // Appearance configuration
        setFont(Constants.DEFAULT_FONT);
        setForeground(Constants.TEXT_COLOR); // Using black text on white/light background
        setBackground(Color.WHITE);
        setCaretColor(Constants.TEXT_COLOR); // Black caret on white background
        setBorder(new EmptyBorder(10, 15, 10, 15));
        setPreferredSize(Constants.TEXT_FIELD_DIMENSION);
        
        // Add placeholder and focus effects
        addFocusListener(new FocusListener() {
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
    
    /**
     * Custom text field painting
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw background
        g2d.setColor(getBackground());
        if (isRound) {
            g2d.fill(new RoundRectangle2D.Double(
                    0, 0, getWidth(), getHeight(),
                    Constants.ROUNDED_CORNER_RADIUS, Constants.ROUNDED_CORNER_RADIUS));
        } else {
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
        
        // Draw border
        g2d.setColor(isFocusOwner() ? focusBorderColor : borderColor);
        int borderWidth = isFocusOwner() ? 2 : 1;
        
        if (isRound) {
            g2d.setStroke(new BasicStroke(borderWidth));
            g2d.draw(new RoundRectangle2D.Double(
                    borderWidth/2, borderWidth/2, 
                    getWidth() - borderWidth, getHeight() - borderWidth,
                    Constants.ROUNDED_CORNER_RADIUS, Constants.ROUNDED_CORNER_RADIUS));
        } else {
            g2d.setStroke(new BasicStroke(borderWidth));
            g2d.drawRect(borderWidth/2, borderWidth/2, 
                    getWidth() - borderWidth, getHeight() - borderWidth);
        }
        
        g2d.dispose();
        
        super.paintComponent(g);
        
        // Draw placeholder text when empty
        if (placeholder != null && getText().isEmpty() && !isFocusOwner()) {
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
     * @param placeholder Placeholder text
     */
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        repaint();
    }
    
    /**
     * Set placeholder text color
     * @param color Color
     */
    public void setPlaceholderColor(Color color) {
        this.placeholderColor = color;
        repaint();
    }
    
    /**
     * Set whether field has rounded corners
     * @param isRound Flag whether field has rounded corners
     */
    public void setRound(boolean isRound) {
        this.isRound = isRound;
        repaint();
    }
    
    /**
     * Set border color
     * @param color Color
     */
    public void setBorderColor(Color color) {
        this.borderColor = color;
        repaint();
    }
    
    /**
     * Set focus border color
     * @param color Color
     */
    public void setFocusBorderColor(Color color) {
        this.focusBorderColor = color;
        repaint();
    }
}