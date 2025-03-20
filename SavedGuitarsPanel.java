package com.sixstringmarket.ui;

import com.sixstringmarket.model.SavedGuitar;
import com.sixstringmarket.model.Guitar;
import com.sixstringmarket.service.AuthenticationService;
import com.sixstringmarket.service.SavedGuitarService;
import com.sixstringmarket.components.GuitarCard;
import com.sixstringmarket.util.Constants;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Панел за показване на запазени китари
 */
public class SavedGuitarsPanel extends JPanel {
    
    private MainFrame parentFrame;
    private SavedGuitarService savedGuitarService;
    private JPanel guitarsPanel;
    private List<SavedGuitar> savedGuitars;
    
    /**
     * Конструктор
     * @param parentFrame Родителският прозорец
     */
    public SavedGuitarsPanel(MainFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.savedGuitarService = new SavedGuitarService();
        
        setLayout(new BorderLayout());
        setBackground(Constants.BACKGROUND_COLOR);
        
        initComponents();
        
        // Зареждане на запазените китари
        loadSavedGuitars();
    }
    
    /**
     * Инициализира компонентите на панела
     */
    private void initComponents() {
        // Заглавие на панела
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Constants.BACKGROUND_COLOR);
        
        JLabel titleLabel = new JLabel("Запазени китари", JLabel.LEFT);
        titleLabel.setFont(Constants.TITLE_FONT);
        titleLabel.setForeground(Constants.PRIMARY_COLOR);
        titlePanel.add(titleLabel, BorderLayout.WEST);
        
        add(titlePanel, BorderLayout.NORTH);
        
        // Панел за списъка с китари
        guitarsPanel = new JPanel();
        guitarsPanel.setLayout(new BoxLayout(guitarsPanel, BoxLayout.Y_AXIS));
        guitarsPanel.setBackground(Constants.BACKGROUND_COLOR);
        
        JScrollPane scrollPane = new JScrollPane(guitarsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Зарежда запазените китари от базата данни
     */
    private void loadSavedGuitars() {
        guitarsPanel.removeAll();
        
        // Проверка дали има влязъл потребител
        if (!AuthenticationService.getInstance().isAuthenticated()) {
            JLabel notLoggedInLabel = new JLabel("Моля, влезте в системата, за да видите запазените китари");
            notLoggedInLabel.setFont(Constants.DEFAULT_FONT);
            notLoggedInLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            guitarsPanel.add(Box.createVerticalStrut(50));
            guitarsPanel.add(notLoggedInLabel);
            
            guitarsPanel.revalidate();
            guitarsPanel.repaint();
            return;
        }
        
        // Получаване на текущия потребител
        int userId = AuthenticationService.getInstance().getCurrentUser().getUserId();
        
        // Зареждане на запазените китари
        savedGuitars = savedGuitarService.getSavedGuitarsByUser(userId);
        
        if (savedGuitars.isEmpty()) {
            JLabel noGuitarsLabel = new JLabel("Нямате запазени китари");
            noGuitarsLabel.setFont(Constants.DEFAULT_FONT);
            noGuitarsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            guitarsPanel.add(Box.createVerticalStrut(50));
            guitarsPanel.add(noGuitarsLabel);
        } else {
            for (SavedGuitar savedGuitar : savedGuitars) {
                Guitar guitar = savedGuitar.getGuitar();
                if (guitar != null) {
                    GuitarCard card = new GuitarCard(guitar, parentFrame);
                    
                    // Добавяне на бутон за премахване от запазени
                    JButton removeButton = new JButton("Премахни от запазени");
                    removeButton.setBackground(Color.GRAY);
                    removeButton.setForeground(Color.WHITE);
                    removeButton.addActionListener(e -> removeFromSaved(savedGuitar.getGuitarId()));
                    
                    JPanel cardPanel = new JPanel(new BorderLayout());
                    cardPanel.setBackground(Constants.BACKGROUND_COLOR);
                    cardPanel.add(card, BorderLayout.CENTER);
                    
                    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                    buttonPanel.setBackground(Color.WHITE);
                    buttonPanel.add(removeButton);
                    
                    cardPanel.add(buttonPanel, BorderLayout.SOUTH);
                    
                    guitarsPanel.add(cardPanel);
                    guitarsPanel.add(Box.createVerticalStrut(10));
                }
            }
        }
        
        guitarsPanel.revalidate();
        guitarsPanel.repaint();
    }
    
    /**
     * Премахва китара от запазените
     * @param guitarId ID на китарата
     */
    private void removeFromSaved(int guitarId) {
        int userId = AuthenticationService.getInstance().getCurrentUser().getUserId();
        
        boolean success = savedGuitarService.removeFromSaved(userId, guitarId);
        
        if (success) {
            // Презареждане на списъка
            loadSavedGuitars();
        } else {
            JOptionPane.showMessageDialog(this,
                "Грешка при премахване на китарата от запазени.",
                "Грешка",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}