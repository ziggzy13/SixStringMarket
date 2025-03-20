package com.sixstringmarket.util;

import java.util.regex.Pattern;

/**
 * Помощен клас с методи за валидация на различни видове данни
 */
public class ValidationUtils {
    
    // Регулярен израз за валидация на имейл адрес
    private static final Pattern EMAIL_PATTERN = 
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    
    // Регулярен израз за валидация на парола (поне 6 символа, с поне 1 цифра и 1 главна буква)
    private static final Pattern STRONG_PASSWORD_PATTERN = 
            Pattern.compile("^(?=.*[0-9])(?=.*[A-Z]).{6,}$");
    
    // Регулярен израз за валидация на телефонен номер (10 цифри, започващи с 0)
    private static final Pattern PHONE_PATTERN = 
            Pattern.compile("^0[0-9]{9}$");
    
    /**
     * Проверява дали даден имейл адрес е валиден
     * @param email Имейл адрес за проверка
     * @return true ако имейл адресът е валиден, false в противен случай
     */
    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Проверява дали дадена парола е достатъчно силна
     * @param password Парола за проверка
     * @return true ако паролата е силна, false в противен случай
     */
    public static boolean isStrongPassword(String password) {
        if (password == null) {
            return false;
        }
        return STRONG_PASSWORD_PATTERN.matcher(password).matches();
    }
    
    /**
     * Проверява дали дадена парола е валидна
     * @param password Парола за проверка
     * @return true ако паролата е валидна, false в противен случай
     */
    public static boolean isValidPassword(String password) {
        if (password == null) {
            return false;
        }
        return password.length() >= 6;
    }
    
    /**
     * Проверява дали даден телефонен номер е валиден
     * @param phone Телефонен номер за проверка
     * @return true ако телефонният номер е валиден, false в противен случай
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }
    
    /**
     * Изчислява силата на паролата като процент (0-100)
     * @param password Парола за проверка
     * @return Число от 0 до 100, представляващо силата на паролата
     */
    public static int calculatePasswordStrength(String password) {
        if (password == null || password.isEmpty()) {
            return 0;
        }
        
        int strength = 0;
        
        // Критерии за сила на паролата
        if (password.length() >= 8) {
            strength += 25; // За дължина >= 8
        } else if (password.length() >= 6) {
            strength += 10; // За дължина >= 6
        } else {
            strength += 5; // За по-къса дължина
        }
        
        // Проверка за различни типове символи
        if (Pattern.compile("[A-Z]").matcher(password).find()) {
            strength += 25; // Съдържа главни букви
        }
        
        if (Pattern.compile("[a-z]").matcher(password).find()) {
            strength += 10; // Съдържа малки букви
        }
        
        if (Pattern.compile("[0-9]").matcher(password).find()) {
            strength += 25; // Съдържа цифри
        }
        
        if (Pattern.compile("[^A-Za-z0-9]").matcher(password).find()) {
            strength += 25; // Съдържа специални символи
        }
        
        // Ограничаване на резултата до 100
        return Math.min(strength, 100);
    }
    
    /**
     * Проверява дали даден текст съдържа само букви
     * @param text Текст за проверка
     * @return true ако текстът съдържа само букви, false в противен случай
     */
    public static boolean isAlphabetic(String text) {
        if (text == null) {
            return false;
        }
        return text.matches("[A-Za-zА-Яа-я\\s]+");
    }
    
    /**
     * Проверява дали даден текст съдържа само цифри
     * @param text Текст за проверка
     * @return true ако текстът съдържа само цифри, false в противен случай
     */
    public static boolean isNumeric(String text) {
        if (text == null) {
            return false;
        }
        return text.matches("[0-9]+");
    }
    
    /**
     * Проверява дали даден текст е валидно потребителско име
     * @param username Потребителско име за проверка
     * @return true ако потребителското име е валидно, false в противен случай
     */
    public static boolean isValidUsername(String username) {
        if (username == null) {
            return false;
        }
        // Потребителското име трябва да е поне 4 символа и да съдържа само букви, цифри и долна черта
        return username.matches("[A-Za-z0-9_]{4,}");
    }
    
    /**
     * Проверява дали дадена година е валидна
     * @param yearText Текст, представляващ година
     * @return true ако годината е валидна, false в противен случай
     */
    public static boolean isValidYear(String yearText) {
        if (yearText == null || !yearText.matches("\\d+")) {
            return false;
        }
        
        int year = Integer.parseInt(yearText);
        int currentYear = java.time.Year.now().getValue();
        
        return year >= 1900 && year <= currentYear;
    }
}