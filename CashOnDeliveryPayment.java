package com.sixstringmarket.util;

import java.math.BigDecimal;

/**
 * Клас, представляващ метод на плащане с наложен платеж.
 * При този метод на плащане, клиентът плаща при доставката на стоката.
 */
public class CashOnDeliveryPayment extends PaymentMethod {
    private static final BigDecimal DEFAULT_FEE = new BigDecimal("5.00");
    
    private String deliveryAddress;
    private String recipientName;
    private String recipientPhone;
    
    /**
     * Конструктор по подразбиране
     */
    public CashOnDeliveryPayment() {
        super("Наложен платеж", "Плащане при доставка - такса " + DEFAULT_FEE + " лв.", 
              DEFAULT_FEE, true);
    }
    
    /**
     * Конструктор с всички параметри
     */
    public CashOnDeliveryPayment(String deliveryAddress, String recipientName, String recipientPhone) {
        super("Наложен платеж", "Плащане при доставка - такса " + DEFAULT_FEE + " лв.", 
              DEFAULT_FEE, true);
        this.deliveryAddress = deliveryAddress;
        this.recipientName = recipientName;
        this.recipientPhone = recipientPhone;
    }
    
    @Override
    public boolean validatePaymentData() {
        return isValidDeliveryAddress() && 
               isValidRecipientName() && 
               isValidRecipientPhone();
    }
    
    @Override
    public boolean processPayment(BigDecimal amount) {
        // При наложен платеж няма реална обработка на плащането предварително
        // Плащането се извършва при доставката
        return true;
    }
    
    /**
     * Проверява дали адресът за доставка е валиден
     * @return true ако адресът е валиден, false в противен случай
     */
    public boolean isValidDeliveryAddress() {
        return deliveryAddress != null && 
               !deliveryAddress.trim().isEmpty() && 
               deliveryAddress.trim().length() >= 10;
    }
    
    /**
     * Проверява дали името на получателя е валидно
     * @return true ако името е валидно, false в противен случай
     */
    public boolean isValidRecipientName() {
        return recipientName != null && 
               !recipientName.trim().isEmpty() && 
               recipientName.trim().length() >= 3;
    }
    
    /**
     * Проверява дали телефонният номер на получателя е валиден
     * @return true ако телефонният номер е валиден, false в противен случай
     */
    public boolean isValidRecipientPhone() {
        return recipientPhone != null && 
               recipientPhone.matches("0[0-9]{9}"); // Формат: 0xxxxxxxxx (10 цифри)
    }
    
    @Override
    public BigDecimal calculateTotal(BigDecimal subtotal) {
        // Добавяне на таксата за наложен платеж към крайната сума
        return subtotal.add(DEFAULT_FEE);
    }
    
    // Getters and setters
    public String getDeliveryAddress() {
        return deliveryAddress;
    }
    
    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
    
    public String getRecipientName() {
        return recipientName;
    }
    
    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }
    
    public String getRecipientPhone() {
        return recipientPhone;
    }
    
    public void setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
    }
}