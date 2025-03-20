package com.sixstringmarket.model;

import java.util.Date;

/**
 * Модел на запазена китара в системата SixStringMarket
 */
public class SavedGuitar {
    private int saveId;
    private int userId;
    private int guitarId;
    private Date dateSaved;
    
    // Референция към обекта Guitar за удобство при извличане
    private Guitar guitar;
    
    /**
     * Конструктор по подразбиране
     */
    public SavedGuitar() {
    }
    
    /**
     * Конструктор с основни параметри
     */
    public SavedGuitar(int userId, int guitarId) {
        this.userId = userId;
        this.guitarId = guitarId;
    }
    
    // Getters и Setters
    
    public int getSaveId() {
        return saveId;
    }
    
    public void setSaveId(int saveId) {
        this.saveId = saveId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getGuitarId() {
        return guitarId;
    }
    
    public void setGuitarId(int guitarId) {
        this.guitarId = guitarId;
    }
    
    public Date getDateSaved() {
        return dateSaved;
    }
    
    public void setDateSaved(Date dateSaved) {
        this.dateSaved = dateSaved;
    }
    
    public Guitar getGuitar() {
        return guitar;
    }
    
    public void setGuitar(Guitar guitar) {
        this.guitar = guitar;
    }
    
    @Override
    public String toString() {
        return "SavedGuitar{" +
                "saveId=" + saveId +
                ", userId=" + userId +
                ", guitarId=" + guitarId +
                ", dateSaved=" + dateSaved +
                '}';
    }
}