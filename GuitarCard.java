package com.sixstringmarket.components;

import com.sixstringmarket.model.Guitar;
import com.sixstringmarket.ui.MainFrame;
import com.sixstringmarket.util.ImageHandler;
import com.sixstringmarket.util.StyleManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.NumberFormat;
import java.util.Locale;
public class GuitarCard extends JPanel {
    
    private Guitar guitar;
    private MainFrame parentFrame;
    private boolean isHovered;
    private boolean isPressed;
    
    public GuitarCard(Guitar guitar, MainFrame parentFrame) {
        super(); // Use ModernPanel as base
        setRound(true);
        setHasShadow(true);
        
        this.guitar = guitar;
        this.parentFrame = parentFrame;
        
        setLayout(new BorderLayout(10, 0));
        setBackground(StyleManager.CARD_BG_COLOR);
        setBorder(null);
        setPreferredSize(new Dimension(0, 150));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        
        initComponents();
        
        // Event listeners
        addMouseListener(new MouseAdapter() {
        	@Override
            public void mouseClicked(MouseEvent e) {
                parentFrame.showGuitarDetailsPanel(guitar.getGuitarId());
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                isPressed = false;
                repaint();
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
                
                if (contains(e.getPoint())) {
                    parentFrame.showGuitarDetailsPanel(guitar.getGuitarId());
                }
            }
        });
    }
    private void setHasShadow(boolean b) {
		// TODO Auto-generated method stub
		
	}
	private void setRound(boolean b) {
		// TODO Auto-generated method stub
		
	}
	@Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        int arc = StyleManager.CORNER_RADIUS;
        
        // Draw background
        if (isPressed) {
            g2d.setColor(StyleManager.darken(StyleManager.CARD_BG_COLOR, 0.1f));
        } else if (isHovered) {
            g2d.setColor(StyleManager.lighten(StyleManager.CARD_BG_COLOR, 0.05f));
        } else {
            g2d.setColor(StyleManager.CARD_BG_COLOR);
        }
        
        g2d.fill(new RoundRectangle2D.Double(0, 0, width, height, arc, arc));
        
        // Draw border
        g2d.setColor(isHovered ? StyleManager.SECONDARY_COLOR : 
                      StyleManager.darken(StyleManager.CARD_BG_COLOR, 0.1f));
        g2d.setStroke(new BasicStroke(1));
        g2d.draw(new RoundRectangle2D.Double(0, 0, width - 1, height - 1, arc, arc));
        
        g2d.dispose();
    }
    
    private void initComponents() {
        // Image panel - fixed size and centered
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setPreferredSize(new Dimension(140, 140));
        imagePanel.setBackground(Color.WHITE);
        imagePanel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        
        // Try to load the guitar image
        BufferedImage image = null;
        try {
            if (guitar.getImagePath() != null && !guitar.getImagePath().isEmpty()) {
                File imageFile = new File("resources/images/" + guitar.getImagePath());
                if (imageFile.exists()) {
                    image = ImageIO.read(imageFile);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
        }
        
        JLabel imageLabel;
        if (image != null) {
            Image scaledImage = image.getScaledInstance(130, 130, Image.SCALE_SMOOTH);
            imageLabel = new JLabel(new ImageIcon(scaledImage));
        } else {
            // Create a better-looking placeholder
            imageLabel = new JLabel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Background
                    g2d.setColor(new Color(245, 245, 250));
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    
                    // Guitar silhouette
                    g2d.setColor(new Color(180, 180, 200));
                    
                    // Draw guitar body
                    int bodyWidth = 60;
                    int bodyHeight = 80;
                    g2d.fillOval((getWidth() - bodyWidth)/2, (getHeight() - bodyHeight)/2 + 10, bodyWidth, bodyHeight);
                    
                    // Draw neck
                    int neckWidth = 12;
                    int neckHeight = 60;
                    g2d.fillRect((getWidth() - neckWidth)/2, (getHeight() - bodyHeight)/2 - neckHeight + 10, neckWidth, neckHeight);
                    
                    // Draw head
                    int headWidth = 20;
                    int headHeight = 15;
                    g2d.fillRect((getWidth() - headWidth)/2, (getHeight() - bodyHeight)/2 - neckHeight - headHeight + 10, headWidth, headHeight);
                    
                    g2d.dispose();
                }
            };
            imageLabel.setPreferredSize(new Dimension(130, 130));
        }
        
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        add(imagePanel, BorderLayout.WEST);
        
        // Information panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));
        
        // Guitar title
        JLabel titleLabel = new JLabel(guitar.getTitle());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(66, 103, 178));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(titleLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        
        // Brand and model
        String brandModel = guitar.getBrand();
        if (guitar.getModel() != null && !guitar.getModel().isEmpty()) {
            brandModel += " " + guitar.getModel();
        }
        JLabel brandModelLabel = new JLabel(brandModel);
        brandModelLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        brandModelLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(brandModelLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        
        // Type and condition
        String typeCondition = getGuitarTypeText(guitar.getType()) + ", " + getConditionText(guitar.getCondition());
        if (guitar.getManufacturingYear() != null) {
            typeCondition += ", " + guitar.getManufacturingYear() + " г.";
        }
        JLabel detailsLabel = new JLabel(typeCondition);
        detailsLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        detailsLabel.setForeground(new Color(100, 100, 100));
        detailsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(detailsLabel);
        
        infoPanel.add(Box.createVerticalGlue());
        
        // Price with nice formatting
        JLabel priceLabel = new JLabel(guitar.getPrice() + " лв.");
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        priceLabel.setForeground(new Color(76, 175, 80)); // Green
        priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(priceLabel);
        
        add(infoPanel, BorderLayout.CENTER);
    }
    
    // Helper methods for text conversion
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
    
    private String getConditionText(Guitar.Condition condition) {
        switch (condition) {
            case NEW: return "Нова";
            case USED: return "Употребявана";
            case VINTAGE: return "Винтидж";
            default: return "Неизвестно";
        }
    }
}