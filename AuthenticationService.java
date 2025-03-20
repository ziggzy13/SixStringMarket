package com.sixstringmarket.service;

import com.sixstringmarket.dao.UserDAO;
import com.sixstringmarket.model.User;
import com.sixstringmarket.model.User.UserRole;

/**
 * Сервиз за автентикация и управление на сесия
 */
public class AuthenticationService {
    private static AuthenticationService instance;
    private static User currentUser;
    private UserDAO userDAO;
    
    /**
     * Прилагане на шаблона Singleton
     */
    private AuthenticationService() {
        userDAO = new UserDAO();
    }
    
    /**
     * Получаване на единствения екземпляр на класа
     * @return Екземпляр на AuthenticationService
     */
    public static AuthenticationService getInstance() {
        if (instance == null) {
            instance = new AuthenticationService();
        }
        return instance;
    }
    
    /**
     * Вход в системата
     * @param username Потребителско име
     * @param password Парола
     * @return true при успешен вход, false при неуспех
     */
    public boolean login(String username, String password) {
        User user = userDAO.authenticateUser(username, password);
        
        if (user != null) {
            currentUser = user;
            return true;
        }
        
        return false;
    }
    
    /**
     * Изход от системата
     */
    public void logout() {
        currentUser = null;
    }
    
    /**
     * Проверка дали има влязъл потребител
     * @return true ако има влязъл потребител, false в противен случай
     */
    public boolean isAuthenticated() {
        return currentUser != null;
    }
    
    /**
     * Получаване на текущия потребител
     * @return Текущият потребител или null ако няма влязъл потребител
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Проверка дали текущият потребител е администратор
     * @return true ако потребителят е администратор, false в противен случай
     */
    public boolean isAdmin() {
        return isAuthenticated() && currentUser.getRole() == UserRole.ADMIN;
    }
    
    /**
     * Проверка за права
     * @param operation Операцията, която се изпълнява
     * @param resourceOwnerId ID на собственика на ресурса
     * @return true ако потребителят има права, false в противен случай
     */
    public boolean hasPermission(String operation, int resourceOwnerId) {
        if (!isAuthenticated()) {
            return false;
        }
        
        // Администраторите имат всички права
        if (isAdmin()) {
            return true;
        }
        
        // Потребителите могат да модифицират само своите ресурси
        return currentUser.getUserId() == resourceOwnerId;
    }
}