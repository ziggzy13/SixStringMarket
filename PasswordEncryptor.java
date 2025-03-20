package com.sixstringmarket.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Клас за криптиране и валидация на пароли
 */
public class PasswordEncryptor {
    
    // Размер на солта в байтове
    private static final int SALT_SIZE = 16;
    
    // Хеш алгоритъм
    private static final String HASH_ALGORITHM = "SHA-256";
    
    /**
     * Криптира парола с SHA-256 и добавя сол
     * @param password Паролата, която трябва да бъде криптирана
     * @return Криптирана парола във формат base64(salt):base64(hash)
     */
    public static String encrypt(String password) {
        try {
            // Генериране на случайна сол
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_SIZE];
            random.nextBytes(salt);
            
            // Създаване на хеш
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            md.update(salt);
            byte[] hash = md.digest(password.getBytes());
            
            // Конвертиране на сол и хеш в Base64
            String saltBase64 = Base64.getEncoder().encodeToString(salt);
            String hashBase64 = Base64.getEncoder().encodeToString(hash);
            
            // Връщане на комбинирана стойност
            return saltBase64 + ":" + hashBase64;
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Грешка при криптиране на парола: " + e.getMessage(), e);
        }
    }
    
    /**
     * Проверява дали въведена парола съвпада с криптирана парола
     * @param password Паролата, която трябва да бъде проверена
     * @param encryptedPassword Криптираната парола от базата данни
     * @return true ако паролите съвпадат, false в противен случай
     */
    public static boolean verify(String password, String encryptedPassword) {
        try {
            // Разделяне на сол и хеш
            String[] parts = encryptedPassword.split(":");
            if (parts.length != 2) {
                return false;
            }
            
            // Извличане на сол и хеш
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] hash = Base64.getDecoder().decode(parts[1]);
            
            // Хеширане на въведената парола със същата сол
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            md.update(salt);
            byte[] newHash = md.digest(password.getBytes());
            
            // Сравняване на хешовете
            if (hash.length != newHash.length) {
                return false;
            }
            
            // Проверка дали хешовете съвпадат
            for (int i = 0; i < hash.length; i++) {
                if (hash[i] != newHash[i]) {
                    return false;
                }
            }
            
            return true;
            
        } catch (NoSuchAlgorithmException | IllegalArgumentException e) {
            return false;
        }
    }
}