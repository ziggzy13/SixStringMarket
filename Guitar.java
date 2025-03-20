package com.sixstringmarket.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Модел на китара в системата SixStringMarket
 */
public class Guitar {
    private int guitarId;
    private int sellerId;
    private String title;
    private String brand;
    private String model;
    private GuitarType type;
    private Condition condition;
    private Integer manufacturingYear;
    private BigDecimal price;
    private String description;
    private String imagePath;
    private Date dateAdded;
    private Status status;
    
    /**
     * Типове китари
     */
    public enum GuitarType {
        ACOUSTIC,   // Акустична
        ELECTRIC,   // Електрическа
        CLASSICAL,  // Класическа
        BASS,       // Бас
        OTHER       // Друго (укулеле, банджо и т.н.)
    }
    
    /**
     * Състояния на китари
     */
    public enum Condition {
        NEW,      // Нова
        USED,     // Употребявана
        VINTAGE   // Винтидж (по-стара от 30 години)
    }
    
    /**
     * Статуси на обявите
     */
    public enum Status {
        ACTIVE,    // Активна обява
        SOLD,      // Продадена
        RESERVED,  // Резервирана
        REMOVED    // Премахната от продавача
    }
    
    /**
     * Конструктор по подразбиране
     */
    public Guitar() {
    }
    
    /**
     * Конструктор с основни параметри
     */
    public Guitar(int guitarId, int sellerId, String title, String brand, GuitarType type, BigDecimal price) {
        this.guitarId = guitarId;
        this.sellerId = sellerId;
        this.title = title;
        this.brand = brand;
        this.type = type;
        this.price = price;
        this.status = Status.ACTIVE;
    }
    
    // Getters и Setters
    
    public int getGuitarId() {
        return guitarId;
    }
    
    public void setGuitarId(int guitarId) {
        this.guitarId = guitarId;
    }
    
    public int getSellerId() {
        return sellerId;
    }
    
    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getBrand() {
        return brand;
    }
    
    public void setBrand(String brand) {
        this.brand = brand;
    }
    
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public GuitarType getType() {
        return type;
    }
    
    public void setType(GuitarType type) {
        this.type = type;
    }
    
    public Condition getCondition() {
        return condition;
    }
    
    public void setCondition(Condition condition) {
        this.condition = condition;
    }
    
    public Integer getManufacturingYear() {
        return manufacturingYear;
    }
    
    public void setManufacturingYear(Integer manufacturingYear) {
        this.manufacturingYear = manufacturingYear;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getImagePath() {
        return imagePath;
    }
    
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    
    public Date getDateAdded() {
        return dateAdded;
    }
    
    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }
    
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "Guitar{" +
                "guitarId=" + guitarId +
                ", title='" + title + '\'' +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", type=" + type +
                ", price=" + price +
                ", status=" + status +
                '}';
    }
}