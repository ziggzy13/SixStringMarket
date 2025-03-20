package com.sixstringmarket.service;

import com.sixstringmarket.dao.UserDAO;
import com.sixstringmarket.model.User;
import com.sixstringmarket.model.User.UserRole;
import com.sixstringmarket.util.ValidationUtils;

import java.util.List;

/**
 * Сервизен клас за работа с потребители
 */
public class UserService {
    private UserDAO userDAO;
    
    /**
     * Конструктор
     */
    public UserService() {
        this.userDAO = new UserDAO();
    }
    
    /**
     * Регистрация на нов потребител с валидация
     * @param username Потребителско име
     * @param password Парола
     * @param email Имейл адрес
     * @param phone Телефонен номер
     * @param address Адрес
     * @return true ако регистрацията е успешна, false в противен случай
     * @throws IllegalArgumentException ако данните са невалидни
     */
    public boolean registerUser(String username, String password, String email, String phone, String address) {
        // Проверка дали потребителското име вече съществува
        if (userDAO.getUserByUsername(username) != null) {
            throw new IllegalArgumentException("Потребителското име вече съществува");
        }
        
        // Валидация на входните данни
        if (!ValidationUtils.isValidUsername(username)) {
            throw new IllegalArgumentException("Невалидно потребителско име");
        }
        
        if (!ValidationUtils.isValidPassword(password)) {
            throw new IllegalArgumentException("Паролата трябва да бъде поне 8 символа и да съдържа букви и цифри");
        }
        
        if (!ValidationUtils.isValidEmail(email)) {
            throw new IllegalArgumentException("Невалиден имейл адрес");
        }
        
        if (phone != null && !ValidationUtils.isValidPhone(phone)) {
            throw new IllegalArgumentException("Невалиден телефонен номер");
        }
        
        // Създаване и запазване на потребител
        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // Паролата ще бъде хеширана в UserDAO
        user.setEmail(email);
        user.setPhone(phone);
        user.setAddress(address);
        user.setRole(UserRole.USER); // По подразбиране всички нови потребители са с роля USER
        
        return userDAO.createUser(user);
    }
    
    /**
     * Проверява дали потребителското име е заето
     * @param username Потребителското име
     * @return true ако потребителското име вече съществува, false в противен случай
     */
    public boolean isUsernameTaken(String username) {
        return userDAO.getUserByUsername(username) != null;
    }
    
    /**
     * Вход в системата
     * @param username Потребителско име
     * @param password Парола
     * @return Потребителят при успешен вход или null
     */
    public User login(String username, String password) {
        return userDAO.authenticateUser(username, password);
    }
    
    /**
     * Обновяване на профила на потребител
     * @param user Потребителят с обновените данни
     * @return true ако обновяването е успешно, false в противен случай
     * @throws IllegalArgumentException ако данните са невалидни
     */
    public boolean updateUserProfile(User user) {
        // Валидации преди обновяване
        if (!ValidationUtils.isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Невалиден имейл адрес");
        }
        
        if (user.getPhone() != null && !ValidationUtils.isValidPhone(user.getPhone())) {
            throw new IllegalArgumentException("Невалиден телефонен номер");
        }
        
        return userDAO.updateUser(user);
    }
    
    /**
     * Промяна на парола
     * @param userId ID на потребителя
     * @param oldPassword Стара парола
     * @param newPassword Нова парола
     * @return true ако промяната е успешна, false в противен случай
     * @throws IllegalArgumentException ако данните са невалидни
     */
    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        // Получаване на потребителя
        User user = userDAO.getUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("Потребителят не съществува");
        }
        
        // Проверка на старата парола
        if (userDAO.authenticateUser(user.getUsername(), oldPassword) == null) {
            throw new IllegalArgumentException("Неправилна текуща парола");
        }
        
        // Валидация на новата парола
        if (!ValidationUtils.isValidPassword(newPassword)) {
            throw new IllegalArgumentException("Новата парола трябва да бъде поне 8 символа и да съдържа букви и цифри");
        }
        
        return userDAO.updatePassword(userId, newPassword);
    }
    
    /**
     * Получаване на потребител по ID
     * @param userId ID на потребителя
     * @return Потребителят или null ако не е намерен
     */
    public User getUserById(int userId) {
        return userDAO.getUserById(userId);
    }
    
    // Администраторски функции
    
    /**
     * Получаване на всички потребители (само за админ)
     * @return Списък с всички потребители
     */
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }
    
    /**
     * Промяна на роля на потребител (само за админ)
     * @param userId ID на потребителя
     * @param newRole Новата роля
     * @return true ако промяната е успешна, false в противен случай
     * @throws IllegalArgumentException ако потребителят не съществува
     */
    public boolean changeUserRole(int userId, UserRole newRole) {
        User user = userDAO.getUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("Потребителят не съществува");
        }
        
        user.setRole(newRole);
        return userDAO.updateUser(user);
    }
    
    /**
     * Изтриване на потребител (само за админ)
     * @param userId ID на потребителя
     * @return true ако изтриването е успешно, false в противен случай
     */
    public boolean deleteUser(int userId) {
        return userDAO.deleteUser(userId);
    }
}