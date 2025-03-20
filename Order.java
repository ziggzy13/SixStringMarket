package com.sixstringmarket.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Модел на поръчка в системата SixStringMarket
 */
public class Order {
    private int orderId;
    private int guitarId;
    private int buyerId;
    private int sellerId;
    private Date orderDate;
    private BigDecimal price;
    private OrderStatus status;
    
    /**
     * Възможни статуси на поръчка
     */
    public enum OrderStatus {
        COMPLETED,   // Завършена (потвърдена от двете страни)
        PROCESSING,  // В процес на обработка
        CANCELLED    // Отказана (от купувача или продавача)
    }
    
    /**
     * Конструктор по подразбиране
     */
    public Order() {
    }
    
    /**
     * Конструктор с основни параметри
     */
    public Order(int orderId, int guitarId, int buyerId, int sellerId, BigDecimal price) {
        this.orderId = orderId;
        this.guitarId = guitarId;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.price = price;
        this.status = OrderStatus.PROCESSING;
    }
    
    // Getters и Setters
    
    public int getOrderId() {
        return orderId;
    }
    
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    
    public int getGuitarId() {
        return guitarId;
    }
    
    public void setGuitarId(int guitarId) {
        this.guitarId = guitarId;
    }
    
    public int getBuyerId() {
        return buyerId;
    }
    
    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }
    
    public int getSellerId() {
        return sellerId;
    }
    
    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }
    
    public Date getOrderDate() {
        return orderDate;
    }
    
    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public OrderStatus getStatus() {
        return status;
    }
    
    public void setStatus(OrderStatus status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", guitarId=" + guitarId +
                ", buyerId=" + buyerId +
                ", sellerId=" + sellerId +
                ", orderDate=" + orderDate +
                ", price=" + price +
                ", status=" + status +
                '}';
    }
}