package com.sixstringmarket.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

/**
 * Клас, представляващ метод на плащане с кредитна карта.
 * Съдържа логика за валидация на данни от кредитна карта и обработка на плащането.
 */
public class CreditCardPayment extends PaymentMethod {
    private String cardNumber;
    private String cardHolderName;
    private String expiryDate; // Формат: MM/YY
    private String cvv;
    
    /**
     * Конструктор с име и описание
     */
    public CreditCardPayment() {
        super("Кредитна карта", "Плащане с кредитна или дебитна карта (Visa, Mastercard, Maestro)", 
              new BigDecimal("0.00"), true);
    }
    
    /**
     * Конструктор с всички параметри
     */
    public CreditCardPayment(String cardNumber, String cardHolderName, String expiryDate, String cvv) {
        super("Кредитна карта", "Плащане с кредитна или дебитна карта (Visa, Mastercard, Maestro)", 
              new BigDecimal("0.00"), true);
        this.cardNumber = cardNumber.replaceAll("\\s", ""); // Премахване на интервалите
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }
    
    @Override
    public boolean validatePaymentData() {
        return isValidCardNumber() && 
               isValidCardHolderName() && 
               isValidExpiryDate() && 
               isValidCVV();
    }
    
    @Override
    public boolean processPayment(BigDecimal amount) {
        // Тази функция би съдържала логика за свързване с платежен шлюз
        // За целите на демонстрацията, просто връщаме true
        return true;
    }
    
    /**
     * Проверява дали номерът на картата е валиден, използвайки алгоритъма на Луун
     * @return true ако номерът е валиден, false в противен случай
     */
    public boolean isValidCardNumber() {
        if (cardNumber == null || cardNumber.isEmpty()) {
            return false;
        }
        
        // Премахване на интервалите
        String sanitizedNumber = cardNumber.replaceAll("\\s", "");
        
        // Проверка дали съдържа само цифри
        if (!sanitizedNumber.matches("\\d+")) {
            return false;
        }
        
        // Проверка на дължината (повечето карти са 13-19 цифри)
        if (sanitizedNumber.length() < 13 || sanitizedNumber.length() > 19) {
            return false;
        }
        
        // Алгоритъм на Луун (Luhn algorithm)
        int sum = 0;
        boolean alternate = false;
        
        for (int i = sanitizedNumber.length() - 1; i >= 0; i--) {
            int n = Character.digit(sanitizedNumber.charAt(i), 10);
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        
        return (sum % 10 == 0);
    }
    
    /**
     * Проверява дали името на притежателя на картата е валидно
     * @return true ако името е валидно, false в противен случай
     */
    public boolean isValidCardHolderName() {
        return cardHolderName != null && 
               !cardHolderName.trim().isEmpty() && 
               cardHolderName.trim().length() >= 3;
    }
    
    /**
     * Проверява дали датата на валидност е валидна
     * @return true ако датата е валидна и не е изтекла, false в противен случай
     */
    public boolean isValidExpiryDate() {
        if (expiryDate == null || !expiryDate.matches("\\d{2}/\\d{2}")) {
            return false;
        }
        
        String[] parts = expiryDate.split("/");
        int month = Integer.parseInt(parts[0]);
        int year = Integer.parseInt(parts[1]) + 2000; // Добавяме 2000 към двуцифрената година
        
        // Проверка дали месецът е валиден
        if (month < 1 || month > 12) {
            return false;
        }
        
        // Проверка дали датата не е изтекла
        YearMonth expiryYearMonth = YearMonth.of(year, month);
        YearMonth currentYearMonth = YearMonth.now();
        
        return !expiryYearMonth.isBefore(currentYearMonth);
    }
    
    /**
     * Проверява дали CVV кодът е валиден
     * @return true ако CVV кодът е валиден, false в противен случай
     */
    public boolean isValidCVV() {
        return cvv != null && 
               cvv.matches("\\d{3,4}"); // CVV е 3 или 4 цифри
    }
    
    // Getters and setters
    public String getCardNumber() {
        return cardNumber;
    }
    
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber.replaceAll("\\s", "");
    }
    
    public String getCardHolderName() {
        return cardHolderName;
    }
    
    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }
    
    public String getExpiryDate() {
        return expiryDate;
    }
    
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
    
    public String getCvv() {
        return cvv;
    }
    
    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
    
    /**
     * Връща маскиран номер на картата за показване
     * Показва само последните 4 цифри, останалите са заменени с '*'
     * @return Маскиран номер на картата
     */
    public String getMaskedCardNumber() {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "";
        }
        
        String sanitized = cardNumber.replaceAll("\\s", "");
        int cardLength = sanitized.length();
        
        // Форматиране на номера с интервали за по-добра четимост
        StringBuilder masked = new StringBuilder();
        for (int i = 0; i < cardLength - 4; i++) {
            masked.append('*');
            if ((i + 1) % 4 == 0 && i < cardLength - 5) {
                masked.append(' ');
            }
        }
        
        if (masked.length() > 0 && masked.charAt(masked.length() - 1) != ' ') {
            masked.append(' ');
        }
        
        // Добавяне на последните 4 цифри
        String lastFour = sanitized.substring(cardLength - 4);
        masked.append(lastFour);
        
        return masked.toString();
    }
    
    /**
     * Връща типа на картата (Visa, Mastercard, Maestro и т.н.)
     * на базата на номера на картата
     * @return Типа на картата или "Unknown" ако не може да бъде определен
     */
    public String getCardType() {
        if (cardNumber == null || cardNumber.isEmpty()) {
            return "Unknown";
        }
        
        String sanitized = cardNumber.replaceAll("\\s", "");
        
        // Visa
        if (sanitized.startsWith("4")) {
            return "Visa";
        }
        
        // Mastercard
        if (sanitized.startsWith("5") && 
            Integer.parseInt(sanitized.substring(1, 2)) >= 1 && 
            Integer.parseInt(sanitized.substring(1, 2)) <= 5) {
            return "Mastercard";
        }
        
        // Новите серии Mastercard
        if (sanitized.startsWith("2") && 
            Integer.parseInt(sanitized.substring(1, 4)) >= 221 && 
            Integer.parseInt(sanitized.substring(1, 4)) <= 720) {
            return "Mastercard";
        }
        
        // American Express
        if (sanitized.startsWith("34") || sanitized.startsWith("37")) {
            return "American Express";
        }
        
        // Discover
        if (sanitized.startsWith("6011") || 
            sanitized.startsWith("65") || 
            (sanitized.length() >= 3 && 
             Integer.parseInt(sanitized.substring(0, 3)) >= 644 && 
             Integer.parseInt(sanitized.substring(0, 3)) <= 649)) {
            return "Discover";
        }
        
        // Maestro
        if (sanitized.startsWith("5018") || 
            sanitized.startsWith("5020") || 
            sanitized.startsWith("5038") || 
            sanitized.startsWith("5893") || 
            sanitized.startsWith("6304") || 
            sanitized.startsWith("6759") || 
            sanitized.startsWith("6761") || 
            sanitized.startsWith("6762") || 
            sanitized.startsWith("6763")) {
            return "Maestro";
        }
        
        return "Unknown";
    }
}