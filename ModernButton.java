package com.sixstringmarket.ui.components;

import com.sixstringmarket.util.StyleManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Modern styled button with hover and pressed effects
 */
public class ModernButton extends JButton {
    
    // Button styles
    public enum ButtonStyle {
        PRIMARY, SECONDARY, SUCCESS, DANGER, INFO, WARNING
    }
    
    private ButtonStyle style;
    private boolean isHovered = false;
    private boolean isRound = true;
    private Color mainColor;
    private Color hoverColor;
    private Color pressedColor;
    
    /**
     * Constructor with text
     * @param text Button text
     */
    public ModernButton(String text) {
        this(text, ButtonStyle.PRIMARY);
    }
    
    /**
     * Constructor with text and style
     * @param text Button text
     * @param style Button style
     */
    public ModernButton(String text, ButtonStyle style) {
        super(text);
        this.style = style;
        
        // Initialize colors based on style
        setButtonStyle(style);
        
        setupButton();
    }
    
    /**
     * Set up button appearance and behavior
     */
    private void setupButton() {
        // Set button appearance
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setForeground(Color.WHITE);
        setFont(StyleManager.DEFAULT_FONT);
        
        // Set preferred size
        setPreferredSize(new Dimension(120, 36));
        
        // Add mouse listeners for hover and pressed effects
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isEnabled()) {
                    isHovered = true;
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                    repaint();
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                repaint();
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                if (isEnabled() && SwingUtilities.isLeftMouseButton(e)) {
                    repaint();
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                repaint();
            }
        });
    }
    
    /**
     * Set button style
     * @param style Button style
     */
    public void setButtonStyle(ButtonStyle style) {
        this.style = style;
        
        switch (style) {
            case PRIMARY:
                mainColor = StyleManager.SECONDARY_COLOR;
                break;
            case SECONDARY:
                mainColor = StyleManager.BUTTON_SECONDARY_COLOR;
                break;
            case SUCCESS:
                mainColor = StyleManager.SUCCESS_COLOR;
                break;
            case DANGER:
                mainColor = StyleManager.ERROR_COLOR;
                break;
            case INFO:
                mainColor = StyleManager.INFO_COLOR;
                break;
            case WARNING:
                mainColor = StyleManager.WARNING_COLOR;
                break;
        }
        
        hoverColor = StyleManager.lighten(mainColor, 0.1f);
        pressedColor = StyleManager.darken(mainColor, 0.1f);
        
        repaint();
    }
    
    /**
     * Set whether the button has rounded corners
     * @param isRound true for rounded corners, false for square
     */
    public void setRound(boolean isRound) {
        this.isRound = isRound;
        repaint();
    }
    
    /**
     * Set small size
     */
    public void setSmallSize() {
        setPreferredSize(new Dimension(90, 32));
    }
    
    /**
     * Set large size
     */
    public void setLargeSize() {
        setPreferredSize(new Dimension(150, 40));
    }
    
    /**
     * Custom painting
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        int arcSize = isRound ? StyleManager.BORDER_RADIUS : 0;
        
        // Get current background color based on state
        Color backgroundColor;
        
        if (!isEnabled()) {
            backgroundColor = StyleManager.BUTTON_DISABLED_COLOR;
        } else if (getModel().isPressed()) {
            backgroundColor = pressedColor;
        } else if (isHovered) {
            backgroundColor = hoverColor;
        } else {
            backgroundColor = mainColor;
        }
        
        // Draw background
        if (isRound) {
            g2d.setColor(backgroundColor);
            g2d.fill(new RoundRectangle2D.Double(0, 0, width, height, arcSize, arcSize));
        } else {
            g2d.setColor(backgroundColor);
            g2d.fillRect(0, 0, width, height);
        }
        
        // Draw slight shadow when hovered
        if (isHovered && isEnabled()) {
            g2d.setColor(new Color(0, 0, 0, 20));
            g2d.fill(new RoundRectangle2D.Double(0, height - 2, width, 2, arcSize, arcSize));
        }
        
        g2d.dispose();
        
        // Paint text and icon
        super.paintComponent(g);
    }
    
    /**
     * Set main color manually
     * @param color Main color
     */
    public void setMainColor(Color color) {
        this.mainColor = color;
        this.hoverColor = StyleManager.lighten(color, 0.1f);
        this.pressedColor = StyleManager.darken(color, 0.1f);
        repaint();
    }
}