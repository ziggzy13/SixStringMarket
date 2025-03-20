package com.sixstringmarket.ui.components;

import com.sixstringmarket.util.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Модерен бутон с анимация при hover и натискане
 */
public class ModernButton extends JButton {
    
    private Color backgroundColor;
    private Color hoverColor;
    private Color pressedColor;
    private Color textColor;
    private boolean isRound;
    
    /**
     * Създава модерен бутон с основен стил
     * @param text Текст на бутона
     */
    public ModernButton(String text) {
        this(text, Constants.PRIMARY_COLOR, Color.WHITE, true);
    }
    
    /**
     * Създава модерен бутон с акцент стил
     * @param text Текст на бутона
     * @param isAccent Флаг дали бутонът е акцент
     */
    public ModernButton(String text, boolean isAccent) {
        this(text, isAccent ? Constants.ACCENT_COLOR : Constants.PRIMARY_COLOR, Color.WHITE, true);
    }
    
    /**
     * Създава модерен бутон с персонализиран стил
     * @param text Текст на бутона
     * @param backgroundColor Цвят на фона
     * @param textColor Цвят на текста
     * @param isRound Флаг дали бутонът е със заоблени ъгли
     */
    public ModernButton(String text, Color backgroundColor, Color textColor, boolean isRound) {
        super(text);
        this.backgroundColor = backgroundColor;
        this.hoverColor = lightenColor(backgroundColor, 0.2f);
        this.pressedColor = darkenColor(backgroundColor, 0.2f);
        this.textColor = textColor;
        this.isRound = isRound;
        
        setup();
    }
    
    /**
     * Инициализира стиловете и събитията на бутона
     */
    private void setup() {
        // Настройка на външния вид
        setFont(Constants.DEFAULT_FONT);
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setForeground(textColor);
        setOpaque(false);
        
        // Настройка на размерите
        setPreferredSize(Constants.BUTTON_DIMENSION);
        
        // Добавяне на ефекти при hover и click
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
     * Персонализирано рисуване на бутона
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Рисуване на фона на бутона
        if (isRound) {
            // Заоблен бутон
            int arc = Constants.ROUNDED_CORNER_RADIUS;
            g2d.setColor(isEnabled() ? getBackground() != null ? getBackground() : backgroundColor : Constants.TEXT_SECONDARY_COLOR);
            g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), arc, arc));
        } else {
            // Правоъгълен бутон
            g2d.setColor(isEnabled() ? getBackground() != null ? getBackground() : backgroundColor : Constants.TEXT_SECONDARY_COLOR);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
        
        g2d.dispose();
        
        super.paintComponent(g);
    }
    
    /**
     * Осветлява цвят
     * @param color Цветът, който ще се осветли
     * @param factor Степен на осветляване (0.0 - 1.0)
     * @return Осветлен цвят
     */
    private Color lightenColor(Color color, float factor) {
        int r = Math.min(255, (int) (color.getRed() + (255 - color.getRed()) * factor));
        int g = Math.min(255, (int) (color.getGreen() + (255 - color.getGreen()) * factor));
        int b = Math.min(255, (int) (color.getBlue() + (255 - color.getBlue()) * factor));
        return new Color(r, g, b);
    }
    
    /**
     * Затъмнява цвят
     * @param color Цветът, който ще се затъмни
     * @param factor Степен на затъмняване (0.0 - 1.0)
     * @return Затъмнен цвят
     */
    private Color darkenColor(Color color, float factor) {
        int r = Math.max(0, (int) (color.getRed() * (1 - factor)));
        int g = Math.max(0, (int) (color.getGreen() * (1 - factor)));
        int b = Math.max(0, (int) (color.getBlue() * (1 - factor)));
        return new Color(r, g, b);
    }
    
    /**
     * Задава основен цвят на бутона
     * @param color Цвят
     */
    public void setButtonColor(Color color) {
        this.backgroundColor = color;
        this.hoverColor = lightenColor(color, 0.2f);
        this.pressedColor = darkenColor(color, 0.2f);
        setBackground(color);
        repaint();
    }
    
    /**
     * Задава малки размери на бутона
     */
    public void setSmallSize() {
        setPreferredSize(Constants.SMALL_BUTTON_DIMENSION);
    }
}