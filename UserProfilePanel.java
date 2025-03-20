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
        setBackground(Constants.BACKGROUND_COLOR);
        
        initComponents();
    }
    
    /**
     * Инициализира компонентите на панела
     */
    private void initComponents() {
        // Проверка дали има влязъл потребител
        if (!AuthenticationService.getInstance().isAuthenticated()) {
            JPanel messagePanel = new JPanel(new BorderLayout());
            messagePanel.setBackground(Constants.BACKGROUND_COLOR);
            
            JLabel notLoggedInLabel = new JLabel("Моля, влезте в системата, за да видите профила си", JLabel.CENTER);
            notLoggedInLabel.setFont(Constants.DEFAULT_FONT);
            messagePanel.add(notLoggedInLabel, BorderLayout.CENTER);
            
            add(messagePanel, BorderLayout.CENTER);
            return;
        }
        
        // Заглавие на панела
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Constants.BACKGROUND_COLOR);
        
        JLabel titleLabel = new JLabel("Профил", JLabel.LEFT);
        titleLabel.setFont(Constants.TITLE_FONT);
        titleLabel.setForeground(Constants.PRIMARY_COLOR);
        titlePanel.add(titleLabel, BorderLayout.WEST);
        
        add(titlePanel, BorderLayout.NORTH);
        
        // Главен панел
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Constants.BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Информация за потребителя
        JPanel infoPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        infoPanel.setBackground(Constants.BACKGROUND_COLOR);
        
        // Потребителско име
        infoPanel.add(new JLabel("Потребителско име:"));
        usernameField = new JTextField(currentUser.getUsername());
        usernameField.setEditable(false); // Не позволяваме промяна на потребителско име
        infoPanel.add(usernameField);
        
        // Имейл
        infoPanel.add(new JLabel("Имейл:"));
        emailField = new JTextField(currentUser.getEmail());
        infoPanel.add(emailField);
        
        // Телефон
        infoPanel.add(new JLabel("Телефон:"));
        phoneField = new JTextField(currentUser.getPhone() != null ? currentUser.getPhone() : "");
        infoPanel.add(phoneField);
        
        // Дата на регистрация
        infoPanel.add(new JLabel("Дата на регистрация:"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        JLabel registrationDateLabel = new JLabel(dateFormat.format(currentUser.getRegistrationDate()));
        infoPanel.add(registrationDateLabel);
        
        // Роля
        infoPanel.add(new JLabel("Роля:"));
        JLabel roleLabel = new JLabel(currentUser.getRole() == User.UserRole.ADMIN ? "Администратор" : "Потребител");
        infoPanel.add(roleLabel);
        
        // Панел с фиксирана ширина
        JPanel infoPanelContainer = new JPanel(new BorderLayout());
        infoPanelContainer.setBackground(Constants.BACKGROUND_COLOR);
        infoPanelContainer.add(infoPanel, BorderLayout.NORTH);
        
        mainPanel.add(infoPanelContainer);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Адрес
        JPanel addressPanel = new JPanel(new BorderLayout());
        addressPanel.setBackground(Constants.BACKGROUND_COLOR);
        addressPanel.add(new JLabel("Адрес:"), BorderLayout.NORTH);
        
        addressArea = new JTextArea(4, 20);
        addressArea.setText(currentUser.getAddress() != null ? currentUser.getAddress() : "");
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        
        JScrollPane addressScrollPane = new JScrollPane(addressArea);
        addressPanel.add(addressScrollPane, BorderLayout.CENTER);
        
        mainPanel.add(addressPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Бутони
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Constants.BACKGROUND_COLOR);
        
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
        
        JPasswordField oldPasswordField = new JPasswordField();
        JPasswordField newPasswordField = new JPasswordField();
        JPasswordField confirmPasswordField = new JPasswordField();
        
        panel.add(new JLabel("Текуща парола:"));
        panel.add(oldPasswordField);
        panel.add(new JLabel("Нова парола:"));
        panel.add(newPasswordField);
        panel.add(new JLabel("Потвърдете новата парола:"));
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