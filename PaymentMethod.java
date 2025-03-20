package com.sixstringmarket.util;

import java.math.BigDecimal;

/**
 * Абстрактен клас, представляващ метод на плащане.
 * Служи като основа за различните видове методи на плащане
 * като кредитна карта, наложен платеж и т.н.
 */
public abstract class PaymentMethod {
    private String name;
    private String description;
    private BigDecimal processingFee;
    private boolean isEnabled;
    
    /**
     * Конструктор с всички параметри
     */
    public PaymentMethod(String name, String description, BigDecimal processingFee, boolean isEnabled) {
        this.name = name;
        this.description = description;
        this.processingFee = processingFee;
        this.isEnabled = isEnabled;
    }
    
    /**
     * Изчислява общата сума на поръчката, включително таксата за обработка
     * @param subtotal Първоначалната сума на поръчката
     * @return Общата сума за плащане
     */
    public BigDecimal calculateTotal(BigDecimal subtotal) {
        if (processingFee == null) {
            return subtotal;
        }
        return subtotal.add(processingFee);
    }
    
    /**
     * Проверява дали въведените данни за плащане са валидни
     * @return true ако данните са валидни, false в противен случай
     */
    public abstract boolean validatePaymentData();
    
    /**
     * Обработва плащането
     * @param amount Сума за плащане
     * @return true ако плащането е успешно, false в противен случай
     */
    public abstract boolean processPayment(BigDecimal amount);
    
    // Getters and setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public BigDecimal getProcessingFee() {
        return processingFee;
    }
    
    public void setProcessingFee(BigDecimal processingFee) {
        this.processingFee = processingFee;
    }
    
    public boolean isEnabled() {
        return isEnabled;
    }
    
    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
    
    @Override
    public String toString() {
        return name;
    }
}