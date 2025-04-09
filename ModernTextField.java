package com.sixstringmarket.ui.components;

import com.sixstringmarket.util.Constants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.RoundRectangle2D;

/**
 * Модерно текстово поле с анимация при focus и placeholder
 */
public class ModernTextField extends JTextField {
    
    private String placeholder;
    private Color placeholderColor;
    private boolean isRound;
    private Color borderColor;
    private Color focusBorderColor;
    private Dimension customDimension;
    
    /**
     * Създава модерно текстово поле
     * @param columns Брой колони
     */
    public ModernTextField(int columns) {
        this(null, columns);
    }
    
    /**
     * Създава модерно текстово поле с placeholder
     * @param placeholder Текст за подсказка
     * @param columns Брой колони
     */
    public ModernTextField(String placeholder, int columns) {
        super(columns);
        this.placeholder = placeholder;
        this.placeholderColor = Constants.TEXT_SECONDARY_COLOR;
        this.isRound = true;
        this.borderColor = new Color(220, 220, 220);
        this.focusBorderColor = Constants.PRIMARY_COLOR;
        this.customDimension = Constants.TEXT_FIELD_DIMENSION; // Стандартен размер по подразбиране
        
        setup();
    }
    
    /**
     * Създава модерно текстово поле с персонализиран размер
     * @param placeholder Текст за подсказка
     * @param columns Брой колони
     * @param dimension Персонализиран размер
     */
    public ModernTextField(String placeholder, int columns, Dimension dimension) {
        super(columns);
        this.placeholder = placeholder;
        this.placeholderColor = Constants.TEXT_SECONDARY_COLOR;
        this.isRound = true;
        this.borderColor = new Color(220, 220, 220);
        this.focusBorderColor = Constants.PRIMARY_COLOR;
        this.customDimension = dimension;
        
        setup();
    }
    
    /**
     * Инициализира стиловете и събитията на текстовото поле
     */
    private void setup() {
        // Настройка на външния вид
        setFont(Constants.DEFAULT_FONT);
        setForeground(Constants.TEXT_COLOR);
        setBackground(Color.WHITE);
        setCaretColor(Constants.TEXT_COLOR);
        setBorder(new EmptyBorder(10, 15, 10, 15));
        setPreferredSize(customDimension);
        
        // Добавяне на подсказка и ефекти при focus
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
     * Персонализирано рисуване на текстовото поле
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Рисуване на фона
        g2d.setColor(getBackground());
        if (isRound) {
            g2d.fill(new RoundRectangle2D.Double(
                    0, 0, getWidth(), getHeight(),
                    Constants.ROUNDED_CORNER_RADIUS, Constants.ROUNDED_CORNER_RADIUS));
        } else {
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
        
        // Рисуване на границата
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
        
        // Рисуване на placeholder
        if (placeholder != null && getText().isEmpty() && !isFocusOwner()) {
            g = getGraphics();
            g.setColor(placeholderColor);
            g.setFont(getFont());
            
            // Изчисляване на позицията
            FontMetrics fm = g.getFontMetrics();
            int x = getInsets().left;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            
            g.drawString(placeholder, x, y);
            g.dispose();
        }
    }
    
    /**
     * Задава текст за подсказка
     * @param placeholder Текст за подсказка
     */
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        repaint();
    }
    
    /**
     * Задава цвят на текста за подсказка
     * @param color Цвят
     */
    public void setPlaceholderColor(Color color) {
        this.placeholderColor = color;
        repaint();
    }
    
    /**
     * Задава дали полето е със заоблени ъгли
     * @param isRound Флаг дали полето е със заоблени ъгли
     */
    public void setRound(boolean isRound) {
        this.isRound = isRound;
        repaint();
    }
    
    /**
     * Задава цвят на границата
     * @param color Цвят
     */
    public void setBorderColor(Color color) {
        this.borderColor = color;
        repaint();
    }
    
    /**
     * Задава цвят на границата при focus
     * @param color Цвят
     */
    public void setFocusBorderColor(Color color) {
        this.focusBorderColor = color;
        repaint();
    }
    
    /**
     * Задава персонализиран размер
     * @param dimension Размер
     */
    public void setCustomDimension(Dimension dimension) {
        this.customDimension = dimension;
        setPreferredSize(dimension);
        revalidate();
    }
    
    /**
     * Създава текстово поле за търсене
     * @param placeholder Текст за подсказка
     * @return Текстово поле с размер за търсене
     */
    public static ModernTextField createSearchField(String placeholder) {
        return new ModernTextField(placeholder, 30, Constants.SEARCH_FIELD_DIMENSION);
    }
    
    /**
     * Създава текстово поле за цена
     * @param placeholder Текст за подсказка
     * @return Текстово поле с размер за цена
     */
    public static ModernTextField createPriceField(String placeholder) {
        return new ModernTextField(placeholder, 10, Constants.PRICE_FIELD_DIMENSION);
    }
}