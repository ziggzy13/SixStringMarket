package com.sixstringmarket.service;

import com.sixstringmarket.model.Guitar;
import com.sixstringmarket.model.Guitar.GuitarType;
import com.sixstringmarket.model.Guitar.Condition;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

/**
 * Сервизен клас за търсене и филтриране на китари
 */
public class SearchService {
    private GuitarService guitarService;
    
    /**
     * Конструктор
     */
    public SearchService() {
        this.guitarService = new GuitarService();
    }
    
    /**
     * Търсене по ключова дума
     * @param keyword Ключова дума за търсене
     * @return Списък с намерени китари
     */
    public List<Guitar> searchByKeyword(String keyword) {
        return guitarService.searchGuitars(keyword, null, null, null, null, null);
    }
    
    /**
     * Филтриране по тип
     * @param type Тип на китарата
     * @return Списък с филтрирани китари
     */
    public List<Guitar> filterByType(GuitarType type) {
        return guitarService.searchGuitars(null, type, null, null, null, null);
    }
    
    /**
     * Филтриране по марка
     * @param brand Марка на китарата
     * @return Списък с филтрирани китари
     */
    public List<Guitar> filterByBrand(String brand) {
        return guitarService.searchGuitars(null, null, brand, null, null, null);
    }
    
    /**
     * Филтриране по ценови диапазон
     * @param minPrice Минимална цена
     * @param maxPrice Максимална цена
     * @return Списък с филтрирани китари
     */
    public List<Guitar> filterByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return guitarService.searchGuitars(null, null, null, minPrice, maxPrice, null);
    }
    
    /**
     * Филтриране по състояние
     * @param condition Състояние на китарата
     * @return Списък с филтрирани китари
     */
    public List<Guitar> filterByCondition(Condition condition) {
        return guitarService.searchGuitars(null, null, null, null, null, condition);
    }
    
    /**
     * Комбинирано търсене и филтриране
     * @param keyword Ключова дума за търсене
     * @param type Тип на китарата
     * @param brand Марка на китарата
     * @param minPrice Минимална цена
     * @param maxPrice Максимална цена
     * @param condition Състояние на китарата
     * @return Списък с намерени китари
     */
    public List<Guitar> search(String keyword, GuitarType type, String brand, 
                         BigDecimal minPrice, BigDecimal maxPrice, Condition condition) {
        
        return guitarService.searchGuitars(keyword, type, brand, minPrice, maxPrice, condition);
    }
    
    /**
     * Получаване на уникални марки от наличните китари (за филтри)
     * @return Множество с уникални марки
     */
    public Set<String> getAvailableBrands() {
        List<Guitar> allGuitars = guitarService.getAllActiveGuitars();
        Set<String> brands = new HashSet<>();
        
        for (Guitar guitar : allGuitars) {
            if (guitar.getBrand() != null && !guitar.getBrand().isEmpty()) {
                brands.add(guitar.getBrand());
            }
        }
        
        return brands;
    }
}