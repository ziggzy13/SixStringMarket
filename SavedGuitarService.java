package com.sixstringmarket.service;

import com.sixstringmarket.dao.SavedGuitarDAO;
import com.sixstringmarket.dao.GuitarDAO;
import com.sixstringmarket.model.SavedGuitar;
import com.sixstringmarket.model.Guitar;

import java.util.List;

/**
 * Сервизен клас за работа със запазени китари
 */
public class SavedGuitarService {
    private SavedGuitarDAO savedGuitarDAO;
    private GuitarDAO guitarDAO;
    
    /**
     * Конструктор
     */
    public SavedGuitarService() {
        this.savedGuitarDAO = new SavedGuitarDAO();
        this.guitarDAO = new GuitarDAO();
    }
    
    /**
     * Запазване на китара в списъка с желани
     * @param userId ID на потребителя
     * @param guitarId ID на китарата
     * @return true ако операцията е успешна, false в противен случай
     * @throws IllegalArgumentException ако китарата не съществува
     */
    public boolean saveGuitar(int userId, int guitarId) {
        // Проверка дали китарата съществува
        Guitar guitar = guitarDAO.getGuitarById(guitarId);
        if (guitar == null) {
            throw new IllegalArgumentException("Китарата не съществува");
        }
        
        // Проверка дали китарата вече е запазена
        if (savedGuitarDAO.isGuitarSaved(userId, guitarId)) {
            return true; // Вече е запазена
        }
        
        return savedGuitarDAO.saveGuitar(userId, guitarId);
    }
    
    /**
     * Премахване на китара от списъка с желани
     * @param userId ID на потребителя
     * @param guitarId ID на китарата
     * @return true ако операцията е успешна, false в противен случай
     */
    public boolean removeFromSaved(int userId, int guitarId) {
        return savedGuitarDAO.removeFromSaved(userId, guitarId);
    }
    
    /**
     * Проверка дали китара е в списъка с желани
     * @param userId ID на потребителя
     * @param guitarId ID на китарата
     * @return true ако китарата е запазена, false в противен случай
     */
    public boolean isGuitarSaved(int userId, int guitarId) {
        return savedGuitarDAO.isGuitarSaved(userId, guitarId);
    }
    
    /**
     * Получаване на всички запазени китари на потребител
     * @param userId ID на потребителя
     * @return Списък със запазени китари
     */
    public List<SavedGuitar> getSavedGuitarsByUser(int userId) {
        List<SavedGuitar> savedGuitars = savedGuitarDAO.getSavedGuitarsByUser(userId);
        
        // Зареждане на пълната информация за всяка китара
        for (SavedGuitar savedGuitar : savedGuitars) {
            Guitar guitar = guitarDAO.getGuitarById(savedGuitar.getGuitarId());
            savedGuitar.setGuitar(guitar);
        }
        
        return savedGuitars;
    }
}