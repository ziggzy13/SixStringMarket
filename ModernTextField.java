package com.sixstringmarket.ui.components;

import com.sixstringmarket.util.StyleManager;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.RoundRectangle2D;

/**
 * Modern styled text field with placeholder text and animation effects
 */
public class ModernTextField extends JTextField {
    
    private String placeholder;
    private Color placeholderColor;
    private boolean isRound = true;
    private Border originalBorder;
    private Border focusBorder;
    
    /**
     * Constructor with columns
     * @param columns Number of columns
     */
    public ModernTextField(int columns) {
        this(null, columns);
    }
    
    /**
     * Constructor with placeholder and columns
     * @param placeholder Placeholder text
     * @param columns Number of columns
     */
    public ModernTextField(String placeholder, int columns) {
        super(columns);
        this.placeholder = placeholder;
        this.placeholderColor = StyleManager.TEXT_SECONDARY_COLOR;
        
        setupTextField();
    }
    
    /**
     * Set up text field appearance and behavior
     */
    private void setupTextField() {
        // Set visual properties
        setOpaque(false);
        setBackground(StyleManager.FIELD_BG_COLOR);
        setForeground(StyleManager.TEXT_COLOR);
        setCaretColor(StyleManager.TEXT_COLOR);
        setFont(StyleManager.DEFAULT_FONT);
        
        // Create borders
        originalBorder = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(StyleManager.FIELD_BORDER_COLOR, 1),
            new EmptyBorder(8, 10, 8, 10)
        );
        
        focusBorder = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(StyleManager.SECONDARY_COLOR, 1),
            new EmptyBorder(8, 10, 8, 10)
        );
        
        setBorder(originalBorder);
        
        // Add focus listener for border effect
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                setBorder(focusBorder);
                repaint();
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                setBorder(originalBorder);
                repaint();
            }
        });
    }
    
    /**
     * Set whether the text field has rounded corners
     * @param isRound true for rounded corners, false for square
     */
    public void setRound(boolean isRound) {
        this.isRound = isRound;
        repaint();
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
     * @param color Placeholder color
     */
    public void setPlaceholderColor(Color color) {
        this.placeholderColor = color;
        repaint();
    }
    
    /**
     * Custom painting for background and placeholder
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Paint background
        int width = getWidth();
        int height = getHeight();
        
        // Draw rounded or square background
        g2d.setColor(getBackground());
        
        if (isRound) {
            g2d.fill(new RoundRectangle2D.Double(0, 0, width, height, StyleManager.BORDER_RADIUS, StyleManager.BORDER_RADIUS));
        } else {
            g2d.fillRect(0, 0, width, height);
        }
        
        g2d.dispose();
        
        // Paint the text
        super.paintComponent(g);
        
        // Paint placeholder if field is empty and doesn't have focus
        if (placeholder != null && placeholder.length() > 0 && getText().isEmpty() && !hasFocus()) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(placeholderColor);
            
            // Calculate position for placeholder text
            FontMetrics metrics = g2.getFontMetrics(getFont());
            int x = getInsets().left;
            int y = (getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();
            
            g2.drawString(placeholder, x, y);
            g2.dispose();
        }
    }
}