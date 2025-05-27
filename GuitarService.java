package com.sixstringmarket.service;

import com.sixstringmarket.dao.GuitarDAO;
import com.sixstringmarket.model.Guitar;
import com.sixstringmarket.model.Guitar.GuitarType;
import com.sixstringmarket.model.Guitar.Condition;
import com.sixstringmarket.model.Guitar.Status;
import com.sixstringmarket.util.ImageHandler;
import com.sixstringmarket.util.ValidationUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Сервизен клас за работа с китари
 */
public class GuitarService {
    private GuitarDAO guitarDAO;
    
    /**
     * Конструктор
     */
    public GuitarService() {
        this.guitarDAO = new GuitarDAO();
    }
    
    /**
     * Добавяне на нова китара
     * @param guitar Обект с данните за китарата
     * @param imagePath Път до изображението
     * @return true ако добавянето е успешно, false в противен случай
     * @throws IllegalArgumentException ако данните са невалидни
     */
    public boolean addGuitar(Guitar guitar, String imagePath) {
        // Валидация на входните данни
        if (guitar.getTitle() == null || guitar.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Заглавието е задължително");
        }
        
        if (guitar.getBrand() == null || guitar.getBrand().trim().isEmpty()) {
            throw new IllegalArgumentException("Марката е задължителна");
        }
        
        if (guitar.getPrice() == null || guitar.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Цената трябва да бъде положително число");
        }
        
        // Ако има изображение, обработваме го
        if (imagePath != null && !imagePath.trim().isEmpty()) {
            String savedImagePath = ImageHandler.saveImage(imagePath, "guitars");
            guitar.setImagePath(savedImagePath);
        }
        
        // Задаваме статус по подразбиране като ACTIVE
        guitar.setStatus(Status.ACTIVE);
        
        return guitarDAO.createGuitar(guitar);
    }
    
    /**
     * Получаване на китара по ID
     * @param guitarId ID на китарата
     * @return Обект с данните за китарата или null ако не е намерена
     */
    public Guitar getGuitarById(int guitarId) {
        return guitarDAO.getGuitarById(guitarId);
    }
    
    /**
     * Обновяване на информация за китара
     * @param guitar Обект с обновените данни
     * @param newImagePath Път до новото изображение (ако има промяна)
     * @return true ако обновяването е успешно, false в противен случай
     * @throws IllegalArgumentException ако данните са невалидни
     */
    public boolean updateGuitar(Guitar guitar, String newImagePath) {
        if (guitar.getTitle() == null || guitar.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Заглавието е задължително");
        }
        
        if (guitar.getBrand() == null || guitar.getBrand().trim().isEmpty()) {
            throw new IllegalArgumentException("Марката е задължителна");
        }
        
        if (guitar.getPrice() == null || guitar.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Цената трябва да бъде положително число");
        }
        
       
        if (newImagePath != null && !newImagePath.trim().isEmpty()) {
            String savedImagePath = ImageHandler.saveImage(newImagePath, "guitars");
            
          
            Guitar existingGuitar = guitarDAO.getGuitarById(guitar.getGuitarId());
            if (existingGuitar != null && existingGuitar.getImagePath() != null) {
                ImageHandler.deleteImage(existingGuitar.getImagePath());
            }
            
            guitar.setImagePath(savedImagePath);
        }
        
        return guitarDAO.updateGuitar(guitar);
    }
    
    /**
     * Маркиране на китара като продадена
     * @param guitarId ID на китарата
     * @return true ако операцията е успешна, false в противен случай
     */
    public boolean markGuitarAsSold(int guitarId) {
        return guitarDAO.updateGuitarStatus(guitarId, Status.SOLD);
    }
    
    /**
     * Маркиране на китара като резервирана
     * @param guitarId ID на китарата
     * @return true ако операцията е успешна, false в противен случай
     */
    public boolean markGuitarAsReserved(int guitarId) {
        return guitarDAO.updateGuitarStatus(guitarId, Status.RESERVED);
    }
    
    /**
     * Маркиране на китара като активна (връщане в активен статус)
     * @param guitarId ID на китарата
     * @return true ако операцията е успешна, false в противен случай
     */
    public boolean markGuitarAsActive(int guitarId) {
        return guitarDAO.updateGuitarStatus(guitarId, Status.ACTIVE);
    }
    
    /**
     * Премахване на обява за китара
     * @param guitarId ID на китарата
     * @return true ако операцията е успешна, false в противен случай
     */
    public boolean removeGuitar(int guitarId) {
        Guitar guitar = guitarDAO.getGuitarById(guitarId);
        if (guitar == null) {
            return false;
        }
        
        try {
            if (guitar.getImagePath() != null && !guitar.getImagePath().isEmpty()) {
                ImageHandler.deleteImage(guitar.getImagePath());
            }
            return guitarDAO.deleteGuitar(guitarId);
        } catch (Exception e) {
            System.err.println("Грешка при изтриване на китара: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Получаване на всички активни китари
     * @return Списък с активни китари
     */
    public List<Guitar> getAllActiveGuitars() {
        return guitarDAO.getAllActiveGuitars();
    }
    
    /**
     * Получаване на китари по продавач
     * @param sellerId ID на продавача
     * @return Списък с китари на този продавач
     */
    public List<Guitar> getGuitarsBySeller(int sellerId) {
        return guitarDAO.getGuitarsBySeller(sellerId);
    }
    
    /**
     * Търсене и филтриране на китари
     * @param keyword Ключова дума за търсене
     * @param type Тип на китарата
     * @param brand Марка на китарата
     * @param minPrice Минимална цена
     * @param maxPrice Максимална цена
     * @param condition Състояние на китарата
     * @return Списък с намерени китари
     */
    public List<Guitar> searchGuitars(String keyword, GuitarType type, String brand, 
                               BigDecimal minPrice, BigDecimal maxPrice, Condition condition) {
        
        Map<String, Object> criteria = new HashMap<>();
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            criteria.put("keyword", keyword);
        }
        
        if (type != null) {
            criteria.put("type", type);
        }
        
        if (brand != null && !brand.trim().isEmpty()) {
            criteria.put("brand", brand);
        }
        
        if (minPrice != null) {
            criteria.put("minPrice", minPrice);
        }
        
        if (maxPrice != null) {
            criteria.put("maxPrice", maxPrice);
        }
        
        if (condition != null) {
            criteria.put("condition", condition);
        }
        
        return guitarDAO.searchGuitars(criteria);
    }
}