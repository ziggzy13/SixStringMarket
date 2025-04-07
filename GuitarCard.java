package com.sixstringmarket.components;

import com.sixstringmarket.model.Guitar;
import com.sixstringmarket.model.Guitar.Status;
import com.sixstringmarket.ui.MainFrame;
import com.sixstringmarket.util.Constants;
import com.sixstringmarket.util.ImageHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Модерен компонент за показване на китара в списъчен изглед
 */
public class GuitarCard extends JPanel {
    
    private Guitar guitar;
    private MainFrame parentFrame;
    private boolean isHovered;
    private boolean isPressed;
    private JLabel imageLabel;
    private JLabel titleLabel;
    private JLabel priceLabel;
    private JLabel typeLabel;
    private JLabel statusLabel;
    
    /**
     * Конструктор
     * @param guitar Китарата, която ще се показва
     * @param parentFrame Родителският прозорец
     */
    public GuitarCard(Guitar guitar, MainFrame parentFrame) {
        this.guitar = guitar;
        this.parentFrame = parentFrame;
        this.isHovered = false;
        this.isPressed = false;
        
        setLayout(new BorderLayout(10, 0));
        setBackground(Constants.PANEL_COLOR); // Бял фон
        setBorder(null);
        setPreferredSize(new Dimension(600, 150));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        
        initComponents();
        
        // Добавяне на ефекти при hover и click
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                isPressed = false;
                repaint();
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                if (contains(e.getPoint())) {
                    // Отваряне на детайли за китарата
                    parentFrame.showGuitarDetailsPanel(guitar.getGuitarId());
                }
                repaint();
            }
        });
    }
    
    /**
     * Инициализира компонентите на картата
     */
    private void initComponents() {
        // Ляв панел със снимка
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setOpaque(false);
        imagePanel.setPreferredSize(new Dimension(130, 130));
        
        // Зареждане на снимката или показване на заместваща снимка
        BufferedImage image = null;
        if (guitar.getImagePath() != null && !guitar.getImagePath().isEmpty()) {
            image = ImageHandler.loadImage(guitar.getImagePath());
        }
        
        if (image != null) {
            imageLabel = new JLabel(new ImageIcon(image.getScaledInstance(120, 120, Image.SCALE_SMOOTH)));
        } else {
            // Заместваща снимка с икона на китара
            imageLabel = new JLabel(UIManager.getIcon("OptionPane.questionIcon"));
            imageLabel.setHorizontalAlignment(JLabel.CENTER);
        }
        
        imageLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        add(imagePanel, BorderLayout.WEST);
        
        // Десен панел с информация
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 15));
        
        // Заглавие
        titleLabel = new JLabel(guitar.getTitle());
        titleLabel.setFont(Constants.CARD_TITLE_FONT);
        titleLabel.setForeground(Constants.PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(titleLabel);
        
        infoPanel.add(Box.createVerticalStrut(5));
        
        // Марка и модел
        String brandModel = guitar.getBrand();
        if (guitar.getModel() != null && !guitar.getModel().isEmpty()) {
            brandModel += " " + guitar.getModel();
        }
        JLabel brandModelLabel = new JLabel(brandModel);
        brandModelLabel.setFont(Constants.DEFAULT_FONT);
        brandModelLabel.setForeground(Constants.TEXT_COLOR); // Черен текст
        brandModelLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(brandModelLabel);
        
        infoPanel.add(Box.createVerticalStrut(5));
        
        // Тип и състояние
        String typeCondition = getGuitarTypeText(guitar.getType()) + ", " + getConditionText(guitar.getCondition());
        if (guitar.getManufacturingYear() != null) {
            typeCondition += ", " + guitar.getManufacturingYear() + " г.";
        }
        typeLabel = new JLabel(typeCondition);
        typeLabel.setFont(Constants.SMALL_FONT);
        typeLabel.setForeground(Constants.TEXT_SECONDARY_COLOR);
        typeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(typeLabel);
        
        infoPanel.add(Box.createVerticalStrut(10));
        
        // Долен панел с цена и статус
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        
        // Форматиране на цената
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("bg", "BG"));
        String formattedPrice = currencyFormatter.format(guitar.getPrice());
        
        // Цена
        priceLabel = new JLabel(formattedPrice);
        priceLabel.setFont(new Font(Constants.BOLD_FONT.getName(), Font.BOLD, 18));
        priceLabel.setForeground(Constants.SUCCESS_COLOR);
        bottomPanel.add(priceLabel, BorderLayout.WEST);
        
        // Статус (показва се само ако е различен от ACTIVE)
        if (guitar.getStatus() != Status.ACTIVE) {
            statusLabel = new JLabel(getStatusText(guitar.getStatus()));
            statusLabel.setFont(Constants.BOLD_FONT);
            
            switch (guitar.getStatus()) {
                case SOLD:
                    statusLabel.setForeground(Constants.ERROR_COLOR);
                    break;
                case RESERVED:
                    statusLabel.setForeground(Constants.WARNING_COLOR);
                    break;
                default:
                    statusLabel.setForeground(Constants.TEXT_SECONDARY_COLOR);
            }
            
            bottomPanel.add(statusLabel, BorderLayout.EAST);
        }
        
        infoPanel.add(bottomPanel);
        
        add(infoPanel, BorderLayout.CENTER);
    }
    
    /**
     * Персонализирано рисуване на картата
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        int arc = Constants.ROUNDED_CORNER_RADIUS;
        
        // Рисуване на фона на картата
        if (isPressed) {
            g2d.setColor(new Color(240, 240, 240));
        } else if (isHovered) {
            g2d.setColor(new Color(248, 248, 248));
        } else {
            g2d.setColor(getBackground());
        }
        
        g2d.fill(new RoundRectangle2D.Double(0, 0, width, height, arc, arc));
        
        // Рисуване на границата
        g2d.setColor(isHovered ? Constants.SECONDARY_COLOR : new Color(230, 230, 230));
        g2d.setStroke(new BasicStroke(1));
        g2d.draw(new RoundRectangle2D.Double(0, 0, width - 1, height - 1, arc, arc));
        
        g2d.dispose();
    }
    
    /**
     * Преобразува типа на китарата в текст
     * @param type Типът на китарата
     * @return Текстово представяне
     */
    private String getGuitarTypeText(Guitar.GuitarType type) {
        switch (type) {
            case ACOUSTIC: return "Акустична";
            case ELECTRIC: return "Електрическа";
            case CLASSICAL: return "Класическа";
            case BASS: return "Бас";
            case OTHER: return "Друга";
            default: return "Неизвестен";
        }
    }
    
    /**
     * Преобразува състоянието на китарата в текст
     * @param condition Състоянието на китарата
     * @return Текстово представяне
     */
    private String getConditionText(Guitar.Condition condition) {
        switch (condition) {
            case NEW: return "Нова";
            case USED: return "Употребявана";
            case VINTAGE: return "Винтидж";
            default: return "Неизвестно";
        }
    }
    
    /**
     * Преобразува статуса на китарата в текст
     * @param status Статусът на китарата
     * @return Текстово представяне
     */
    private String getStatusText(Guitar.Status status) {
        switch (status) {
            case ACTIVE: return "Активна";
            case SOLD: return "Продадена";
            case RESERVED: return "Резервирана";
            case REMOVED: return "Премахната";
            default: return "Неизвестен";
        }
    }
}