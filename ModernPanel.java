package com.sixstringmarket.ui.components;

import com.sixstringmarket.util.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Модерен панел с възможност за заоблени ъгли и сянка
 */
public class ModernPanel extends JPanel {
    
    private boolean isRound;
    private boolean hasShadow;
    private Color backgroundColor;
    private String title;
    private Font titleFont;
    
    /**
     * Създава модерен панел с основен стил
     */
    public ModernPanel() {
        this(null, true, false);
    }
    
    /**
     * Създава модерен панел с персонализиран стил
     * @param title Заглавие на панела (null за без заглавие)
     * @param isRound Флаг дали панелът е със заоблени ъгли
     * @param hasShadow Флаг дали панелът има сянка
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
     * Инициализира стиловете на панела
     */
    private void setup() {
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(
                Constants.PADDING_MEDIUM,
                Constants.PADDING_MEDIUM,
                Constants.PADDING_MEDIUM,
                Constants.PADDING_MEDIUM));
        
        // Ако има заглавие, добавяме допълнителен отстъп отгоре
        if (title != null) {
            setBorder(BorderFactory.createEmptyBorder(
                    Constants.PADDING_LARGE + titleFont.getSize(),
                    Constants.PADDING_MEDIUM,
                    Constants.PADDING_MEDIUM,
                    Constants.PADDING_MEDIUM));
        }
    }
    
    /**
     * Персонализирано рисуване на панела
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        // Рисуване на сянка, ако е необходимо
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
        
        // Рисуване на фона на панела
        g2d.setColor(backgroundColor);
        
        if (isRound) {
            g2d.fill(new RoundRectangle2D.Double(
                    0, 0, width, height,
                    Constants.ROUNDED_CORNER_RADIUS, Constants.ROUNDED_CORNER_RADIUS));
        } else {
            g2d.fillRect(0, 0, width, height);
        }
        
        // Рисуване на заглавието, ако има такова
        if (title != null) {
            g2d.setFont(titleFont);
            g2d.setColor(Constants.TEXT_COLOR);
            FontMetrics fm = g2d.getFontMetrics();
            int titleWidth = fm.stringWidth(title);
            int titleHeight = fm.getHeight();
            g2d.drawString(title, Constants.PADDING_MEDIUM, titleHeight);
        }
        
        g2d.dispose();
    }
    
    /**
     * Задава цвят на фона
     * @param color Цвят
     */
    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
        repaint();
    }
    
    /**
     * Задава заглавие на панела
     * @param title Заглавие
     */
    public void setTitle(String title) {
        this.title = title;
        
        // Обновяване на отстъпа
        setBorder(BorderFactory.createEmptyBorder(
                Constants.PADDING_LARGE + titleFont.getSize(),
                Constants.PADDING_MEDIUM,
                Constants.PADDING_MEDIUM,
                Constants.PADDING_MEDIUM));
        
        repaint();
    }
    
    /**
     * Задава шрифт на заглавието
     * @param font Шрифт
     */
    public void setTitleFont(Font font) {
        this.titleFont = font;
        repaint();
    }
}