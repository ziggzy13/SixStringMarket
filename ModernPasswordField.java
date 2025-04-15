package com.sixstringmarket.ui.components;

import com.sixstringmarket.util.Constants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Modern password field with animation on focus and customizable appearance
 */
public class ModernPasswordField extends JPasswordField {
    
    private String placeholder;
    private Color placeholderColor;
    private boolean isRound;
    private Color borderColor;
    private Color focusBorderColor;
    private boolean isShowingPlaceholder;
    
    /**
     * Creates a modern password field
     * @param columns Number of columns
     */
    public ModernPasswordField(int columns) {
        this(null, columns);
    }
    
    /**
     * Creates a modern password field with placeholder
     * @param placeholder Placeholder text
     * @param columns Number of columns
     */
    public ModernPasswordField(String placeholder, int columns) {
        super(columns);
        this.placeholder = placeholder;
        this.placeholderColor = Constants.TEXT_SECONDARY_COLOR;
        this.isRound = true;
        this.borderColor = new Color(220, 220, 220);
        this.focusBorderColor = Constants.PRIMARY_COLOR;
        this.isShowingPlaceholder = placeholder != null && placeholder.length() > 0;
        
        setup();
    }
    
    /**
     * Initializes styles and events for the password field
     */
    private void setup() {
        // Appearance setup
        setFont(Constants.DEFAULT_FONT);
        setForeground(Constants.TEXT_COLOR);
        setBackground(Color.WHITE);
        setCaretColor(Constants.TEXT_COLOR);
        setBorder(new EmptyBorder(10, 15, 10, 15));
        setPreferredSize(Constants.TEXT_FIELD_DIMENSION);
        setEchoChar('•');
        
        // Add focus effects and placeholder behavior
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
    
    /**
     * Custom rendering of the password field
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
        
        // Draw placeholder if password field is empty and not focused
        if (placeholder != null && getPassword().length == 0 && !isFocusOwner()) {
            g = getGraphics();
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
    
    /**
     * Sets placeholder text
     * @param placeholder Placeholder text
     */
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        repaint();
    }
    
    /**
     * Sets placeholder text color
     * @param color Color
     */
    public void setPlaceholderColor(Color color) {
        this.placeholderColor = color;
        repaint();
    }
    
    /**
     * Sets whether the field has rounded corners
     * @param isRound Flag for rounded corners
     */
    public void setRound(boolean isRound) {
        this.isRound = isRound;
        repaint();
    }
    
    /**
     * Sets border color
     * @param color Color
     */
    public void setBorderColor(Color color) {
        this.borderColor = color;
        repaint();
    }
    
    /**
     * Sets focused border color
     * @param color Color
     */
    public void setFocusBorderColor(Color color) {
        this.focusBorderColor = color;
        repaint();
    }
}