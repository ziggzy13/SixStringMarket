package com.sixstringmarket.model;

import java.util.Date;

/**
 * Модел на потребител в системата SixStringMarket
 */
public class User {
    private int userId;
    private String username;
    private String password; // Хеширана парола
    private String email;
    private String phone;
    private String address;
    private UserRole role;
    private Date registrationDate;
    
    /**
     * Възможни роли на потребителите
     */
    public enum UserRole {
        ADMIN, // Администратор с пълни права
        USER   // Обикновен потребител с ограничени права
    }
    
    /**
     * Конструктор по подразбиране
     */
    public User() {
    }
    
    /**
     * Конструктор с основни параметри
     */
    public User(int userId, String username, String email, UserRole role) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role = role;
    }
    
    // Getters и Setters
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public UserRole getRole() {
        return role;
    }
    
    public void setRole(UserRole role) {
        this.role = role;
    }
    
    public Date getRegistrationDate() {
        return registrationDate;
    }
    
    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
}