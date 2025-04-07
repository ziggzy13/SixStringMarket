package com.sixstringmarket.ui;

import com.sixstringmarket.model.User;
import com.sixstringmarket.service.AuthenticationService;
import com.sixstringmarket.service.UserService;
import com.sixstringmarket.util.Constants;
import com.sixstringmarket.util.ValidationUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

/**
 * Панел за преглед и редактиране на потребителски профил
 */
public class UserProfilePanel extends JPanel {
    
    private MainFrame parentFrame;
    private UserService userService;
    private User currentUser;
    
    private JTextField usernameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextArea addressArea;
    private JButton updateButton;
    private JButton changePasswordButton;
    
    /**
     * Конструктор
     * @param parentFrame Родителският прозорец
     */
    public UserProfilePanel(MainFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.userService = new UserService();
        this.currentUser = AuthenticationService.getInstance().getCurrentUser();
        
        setLayout(new BorderLayout());
        setBackground(Constants.PANEL_COLOR); // Бял фон
        
        initComponents();
    }
    
    /**
     * Инициализира компонентите на панела
     */
    private void initComponents() {
        // Проверка дали има влязъл потребител
        if (!AuthenticationService.getInstance().isAuthenticated()) {
            JPanel messagePanel = new JPanel(new BorderLayout());
            messagePanel.setBackground(Constants.PANEL_COLOR); // Бял фон
            
            JLabel notLoggedInLabel = new JLabel("Моля, влезте в системата, за да видите профила си", JLabel.CENTER);
            notLoggedInLabel.setFont(Constants.DEFAULT_FONT);
            notLoggedInLabel.setForeground(Constants.TEXT_COLOR); // Черен текст
            messagePanel.add(notLoggedInLabel, BorderLayout.CENTER);
            
            add(messagePanel, BorderLayout.CENTER);
            return;
        }
        
        // Заглавие на панела
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Constants.PANEL_COLOR); // Бял фон
        
        JLabel titleLabel = new JLabel("Профил", JLabel.LEFT);
        titleLabel.setFont(Constants.TITLE_FONT);
        titleLabel.setForeground(Constants.PRIMARY_COLOR);
        titlePanel.add(titleLabel, BorderLayout.WEST);
        
        add(titlePanel, BorderLayout.NORTH);
        
        // Главен панел
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Constants.PANEL_COLOR); // Бял фон
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Информация за потребителя
        JPanel infoPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        infoPanel.setBackground(Constants.PANEL_COLOR); // Бял фон
        
        // Потребителско име
        JLabel usernameLabel = new JLabel("Потребителско име:");
        usernameLabel.setForeground(Constants.TEXT_COLOR); // Черен текст
        infoPanel.add(usernameLabel);
        usernameField = new JTextField(currentUser.getUsername());
        usernameField.setEditable(false); // Не позволяваме промяна на потребителско име
        usernameField.setForeground(Constants.TEXT_COLOR); // Черен текст
        infoPanel.add(usernameField);
        
        // Имейл
        JLabel emailLabel = new JLabel("Имейл:");
        emailLabel.setForeground(Constants.TEXT_COLOR); // Черен текст
        infoPanel.add(emailLabel);
        emailField = new JTextField(currentUser.getEmail());
        emailField.setForeground(Constants.TEXT_COLOR); // Черен текст
        infoPanel.add(emailField);
        
        // Телефон
        JLabel phoneLabel = new JLabel("Телефон:");
        phoneLabel.setForeground(Constants.TEXT_COLOR); // Черен текст
        infoPanel.add(phoneLabel);
        phoneField = new JTextField(currentUser.getPhone() != null ? currentUser.getPhone() : "");
        phoneField.setForeground(Constants.TEXT_COLOR); // Черен текст
        infoPanel.add(phoneField);
        
        // Дата на регистрация
        JLabel registrationDateTitleLabel = new JLabel("Дата на регистрация:");
        registrationDateTitleLabel.setForeground(Constants.TEXT_COLOR); // Черен текст
        infoPanel.add(registrationDateTitleLabel);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        JLabel registrationDateLabel = new JLabel(dateFormat.format(currentUser.getRegistrationDate()));
        registrationDateLabel.setForeground(Constants.TEXT_COLOR); // Черен текст
        infoPanel.add(registrationDateLabel);
        
        // Роля
        JLabel roleTitleLabel = new JLabel("Роля:");
        roleTitleLabel.setForeground(Constants.TEXT_COLOR); // Черен текст
        infoPanel.add(roleTitleLabel);
        JLabel roleLabel = new JLabel(currentUser.getRole() == User.UserRole.ADMIN ? "Администратор" : "Потребител");
        roleLabel.setForeground(Constants.TEXT_COLOR); // Черен текст
        infoPanel.add(roleLabel);
        
        // Панел с фиксирана ширина
        JPanel infoPanelContainer = new JPanel(new BorderLayout());
        infoPanelContainer.setBackground(Constants.PANEL_COLOR); // Бял фон
        infoPanelContainer.add(infoPanel, BorderLayout.NORTH);
        
        mainPanel.add(infoPanelContainer);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Адрес
        JPanel addressPanel = new JPanel(new BorderLayout());
        addressPanel.setBackground(Constants.PANEL_COLOR); // Бял фон
        
        JLabel addressTitleLabel = new JLabel("Адрес:");
        addressTitleLabel.setForeground(Constants.TEXT_COLOR); // Черен текст
        addressPanel.add(addressTitleLabel, BorderLayout.NORTH);
        
        addressArea = new JTextArea(4, 20);
        addressArea.setText(currentUser.getAddress() != null ? currentUser.getAddress() : "");
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        addressArea.setForeground(Constants.TEXT_COLOR); // Черен текст
        
        JScrollPane addressScrollPane = new JScrollPane(addressArea);
        addressPanel.add(addressScrollPane, BorderLayout.CENTER);
        
        mainPanel.add(addressPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Бутони
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Constants.PANEL_COLOR); // Бял фон
        
        updateButton = new JButton("Запази промените");
        updateButton.setBackground(Constants.PRIMARY_COLOR);
        updateButton.setForeground(Color.WHITE);
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProfile();
            }
        });
        buttonPanel.add(updateButton);
        
        changePasswordButton = new JButton("Смяна на парола");
        changePasswordButton.setBackground(Constants.SECONDARY_COLOR);
        changePasswordButton.setForeground(Color.WHITE);
        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showChangePasswordDialog();
            }
        });
        buttonPanel.add(changePasswordButton);
        
        mainPanel.add(buttonPanel);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    /**
     * Обновява профила с въведените данни
     */
    private void updateProfile() {
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addressArea.getText().trim();
        
        // Валидация на имейла
        if (!ValidationUtils.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this,
                "Невалиден имейл адрес.",
                "Грешка при валидация",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Валидация на телефона
        if (!phone.isEmpty() && !ValidationUtils.isValidPhone(phone)) {
            JOptionPane.showMessageDialog(this,
                "Невалиден телефонен номер.",
                "Грешка при валидация",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Обновяване на данните
        currentUser.setEmail(email);
        currentUser.setPhone(phone);
        currentUser.setAddress(address);
        
        try {
            boolean success = userService.updateUserProfile(currentUser);
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Профилът е обновен успешно.",
                    "Успешно обновяване",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Грешка при обновяване на профила.",
                    "Грешка",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Грешка при валидация",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Показва диалог за смяна на парола
     */
    private void showChangePasswordDialog() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.setBackground(Constants.PANEL_COLOR); // Бял фон
        
        JLabel oldPasswordLabel = new JLabel("Текуща парола:");
        oldPasswordLabel.setForeground(Constants.TEXT_COLOR); // Черен текст
        panel.add(oldPasswordLabel);
        
        JPasswordField oldPasswordField = new JPasswordField();
        oldPasswordField.setForeground(Constants.TEXT_COLOR); // Черен текст
        panel.add(oldPasswordField);
        
        JLabel newPasswordLabel = new JLabel("Нова парола:");
        newPasswordLabel.setForeground(Constants.TEXT_COLOR); // Черен текст
        panel.add(newPasswordLabel);
        
        JPasswordField newPasswordField = new JPasswordField();
        newPasswordField.setForeground(Constants.TEXT_COLOR); // Черен текст
        panel.add(newPasswordField);
        
        JLabel confirmPasswordLabel = new JLabel("Потвърдете новата парола:");
        confirmPasswordLabel.setForeground(Constants.TEXT_COLOR); // Черен текст
        panel.add(confirmPasswordLabel);
        
        JPasswordField confirmPasswordField = new JPasswordField();
        confirmPasswordField.setForeground(Constants.TEXT_COLOR); // Черен текст
        panel.add(confirmPasswordField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Смяна на парола", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String oldPassword = new String(oldPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            
            // Проверка дали всички полета са попълнени
            if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Моля, попълнете всички полета.",
                    "Грешка при валидация",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Проверка дали новата парола и потвърждението съвпадат
            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this,
                    "Новите пароли не съвпадат.",
                    "Грешка при валидация",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Проверка дали новата парола е валидна
            if (!ValidationUtils.isValidPassword(newPassword)) {
                JOptionPane.showMessageDialog(this,
                    "Новата парола трябва да бъде поне 8 символа и да съдържа букви и цифри.",
                    "Грешка при валидация",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Промяна на паролата
            try {
                boolean success = userService.changePassword(currentUser.getUserId(), oldPassword, newPassword);
                
                if (success) {
                    JOptionPane.showMessageDialog(this,
                        "Паролата е променена успешно.",
                        "Успешна промяна",
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Грешка при промяна на паролата. Проверете текущата парола.",
                        "Грешка",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Грешка при валидация",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}