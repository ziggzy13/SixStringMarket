package com.sixstringmarket.util;

import java.math.BigDecimal;

/**
 * Клас, представляващ метод на плащане с банков превод.
 * При този метод на плащане, клиентът заплаща сумата чрез банков превод.
 */
public class BankTransferPayment extends PaymentMethod {
    // Информация за банковия превод
    private static final String BANK_NAME = "БългарскаБанка АД";
    private static final String ACCOUNT_HOLDER = "SixStringMarket ЕООД";
    private static final String IBAN = "BG80BNBG96611020345678";
    private static final String BIC = "BNBGBGSD";
    
    private String transferReference; // Референция за плащането
    private String customerName; // Име на клиента, извършващ превода
    
    /**
     * Конструктор по подразбиране
     */
    public BankTransferPayment() {
        super("Банков превод", "Плащане чрез банков превод", new BigDecimal("0.00"), true);
    }
    
    /**
     * Конструктор с всички параметри
     */
    public BankTransferPayment(String customerName) {
        super("Банков превод", "Плащане чрез банков превод", new BigDecimal("0.00"), true);
        this.customerName = customerName;
        // Генериране на уникална референция за плащането
        this.transferReference = generateTransferReference();
    }
    
    @Override
    public boolean validatePaymentData() {
        return isValidCustomerName();
    }
    
    @Override
    public boolean processPayment(BigDecimal amount) {
        // При банков превод няма предварителна обработка на плащането
        // Плащането се проверява след извършване на превода
        return true;
    }
    
    /**
     * Проверява дали името на клиента е валидно
     * @return true ако името е валидно, false в противен случай
     */
    public boolean isValidCustomerName() {
        return customerName != null && 
               !customerName.trim().isEmpty() && 
               customerName.trim().length() >= 3;
    }
    
    /**
     * Генерира уникална референция за плащането
     * @return Уникална референция
     */
    private String generateTransferReference() {
        // Формат: SSM-TIMESTAMP-RANDOM
        long timestamp = System.currentTimeMillis();
        int random = (int) (Math.random() * 1000);
        return "SSM-" + timestamp + "-" + random;
    }
    
    /**
     * Генерира инструкции за плащане
     * @param amount Сума за плащане
     * @return Инструкции за плащане
     */
    public String generatePaymentInstructions(BigDecimal amount) {
        StringBuilder instructions = new StringBuilder();
        instructions.append("ИНСТРУКЦИИ ЗА БАНКОВ ПРЕВОД:\n\n");
        instructions.append("Банка: ").append(BANK_NAME).append("\n");
        instructions.append("Получател: ").append(ACCOUNT_HOLDER).append("\n");
        instructions.append("IBAN: ").append(IBAN).append("\n");
        instructions.append("BIC: ").append(BIC).append("\n");
        instructions.append("Сума: ").append(amount).append(" лв.\n");
        instructions.append("Основание за плащане: ").append(transferReference).append("\n\n");
        instructions.append("ВАЖНО: Моля, напишете референтния номер в основанието за плащане, ");
        instructions.append("за да можем да обработим поръчката Ви.\n");
        instructions.append("Поръчката ще бъде обработена след потвърждение на плащането.");
        
        return instructions.toString();
    }
    
    // Getters and setters
    public String getTransferReference() {
        return transferReference;
    }
    
    public void setTransferReference(String transferReference) {
        this.transferReference = transferReference;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    // Статични getters за банковите данни
    public static String getBankName() {
        return BANK_NAME;
    }
    
    public static String getAccountHolder() {
        return ACCOUNT_HOLDER;
    }
    
    public static String getIban() {
        return IBAN;
    }
    
    public static String getBic() {
        return BIC;
    }
}