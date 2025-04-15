package com.sixstringmarket.ui.components;

import com.sixstringmarket.util.StyleManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Modern styled tab component
 */
public class ModernTabs extends JPanel {
    
    private List<String> tabTitles = new ArrayList<>();
    private List<Component> tabContents = new ArrayList<>();
    private int selectedIndex = 0;
    private Color backgroundColor;
    private Color tabColor;
    private Color activeTabColor;
    private Color tabTextColor;
    private Color activeTabTextColor;
    
    private JPanel tabsPanel;
    private CardLayout cardLayout;
    private JPanel contentPanel;
    
    /**
     * Constructor
     */
    public ModernTabs() {
        backgroundColor = StyleManager.PRIMARY_COLOR;
        tabColor = StyleManager.darken(StyleManager.PRIMARY_COLOR, 0.1f);
        activeTabColor = StyleManager.CARD_BG_COLOR;
        tabTextColor = StyleManager.TEXT_SECONDARY_COLOR;
        activeTabTextColor = StyleManager.TEXT_COLOR;
        
        setupUI();
    }
    
    /**
     * Set up UI components
     */
    private void setupUI() {
        setLayout(new BorderLayout());
        setOpaque(false);
        
        // Create tabs panel (horizontal tabs at top)
        tabsPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                // Paint background
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(backgroundColor);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                g2d.dispose();
            }
        };
        tabsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tabsPanel.setOpaque(false);
        
        // Create content panel with card layout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(StyleManager.CARD_BG_COLOR);
        
        add(tabsPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
    
    /**
     * Add a tab
     * @param title Tab title
     * @param content Tab content component
     */
    public void addTab(String title, Component content) {
        // Add to lists
        tabTitles.add(title);
        tabContents.add(content);
        
        // Create tab button
        final int tabIndex = tabTitles.size() - 1;
        JPanel tabButton = createTabButton(title, tabIndex);
        tabsPanel.add(tabButton);
        
        // Add content to card layout
        contentPanel.add(content, String.valueOf(tabIndex));
        
        // If this is the first tab, select it
        if (tabTitles.size() == 1) {
            setSelectedIndex(0);
        }
        
        revalidate();
        repaint();
    }
    
    /**
     * Create a tab button
     * @param title Tab title
     * @param index Tab index
     * @return Tab button panel
     */
    private JPanel createTabButton(String title, final int index) {
        JPanel tabButton = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Background color based on selection
                g2d.setColor(index == selectedIndex ? activeTabColor : tabColor);
                
                // Rounded top corners
                g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 
                        StyleManager.BORDER_RADIUS, StyleManager.BORDER_RADIUS));
                
                // Active tab indicator
                if (index == selectedIndex) {
                    g2d.setColor(StyleManager.SECONDARY_COLOR);
                    g2d.fillRect(0, getHeight() - 3, getWidth(), 3);
                }
                
                g2d.dispose();
            }
            
            @Override
            public Dimension getPreferredSize() {
                FontMetrics fm = getFontMetrics(getFont());
                int width = fm.stringWidth(title) + 40; // Add padding
                return new Dimension(width, 40);
            }
        };
        
        tabButton.setLayout(new BorderLayout());
        tabButton.setOpaque(false);
        
        // Tab label
        JLabel tabLabel = new JLabel(title, JLabel.CENTER);
        tabLabel.setFont(StyleManager.DEFAULT_FONT);
        tabLabel.setForeground(index == selectedIndex ? activeTabTextColor : tabTextColor);
        tabButton.add(tabLabel, BorderLayout.CENTER);
        
        // Mouse listener for tab selection
        tabButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setSelectedIndex(index);
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                if (index != selectedIndex) {
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        
        return tabButton;
    }
    
    /**
     * Set selected tab
     * @param index Tab index
     */
    public void setSelectedIndex(int index) {
        if (index < 0 || index >= tabTitles.size()) {
            return;
        }
        
        // Update selection
        selectedIndex = index;
        
        // Show selected content
        cardLayout.show(contentPanel, String.valueOf(index));
        
        // Update tab appearances
        for (int i = 0; i < tabsPanel.getComponentCount(); i++) {
            JPanel tabButton = (JPanel) tabsPanel.getComponent(i);
            JLabel tabLabel = (JLabel) tabButton.getComponent(0);
            tabLabel.setForeground(i == selectedIndex ? activeTabTextColor : tabTextColor);
            tabButton.repaint();
        }
        
        revalidate();
        repaint();
    }
    
    /**
     * Get current selected index
     * @return Selected tab index
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }
    
    /**
     * Get selected tab title
     * @return Selected tab title
     */
    public String getSelectedTitle() {
        if (selectedIndex >= 0 && selectedIndex < tabTitles.size()) {
            return tabTitles.get(selectedIndex);
        }
        return null;
    }
    
    /**
     * Set tab background color
     * @param color Background color
     */
    public void setTabBackgroundColor(Color color) {
        this.backgroundColor = color;
        repaint();
    }
    
    /**
     * Set tab color
     * @param color Tab color
     */
    public void setTabColor(Color color) {
        this.tabColor = color;
        repaint();
    }
    
    /**
     * Set active tab color
     * @param color Active tab color
     */
    public void setActiveTabColor(Color color) {
        this.activeTabColor = color;
        repaint();
    }
}