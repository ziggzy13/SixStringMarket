package com.sixstringmarket.service;

import com.sixstringmarket.dao.OrderDAO;
import com.sixstringmarket.dao.GuitarDAO;
import com.sixstringmarket.dao.UserDAO;
import com.sixstringmarket.model.Order;
import com.sixstringmarket.model.Order.OrderStatus;
import com.sixstringmarket.model.Guitar;
import com.sixstringmarket.model.Guitar.Status;
import com.sixstringmarket.model.User;

import java.util.List;

/**
 * Сервизен клас за работа с поръчки
 */
public class OrderService {
    private OrderDAO orderDAO;
    private GuitarDAO guitarDAO;
    private UserDAO userDAO;
    
    /**
     * Конструктор
     */
    public OrderService() {
        this.orderDAO = new OrderDAO();
        this.guitarDAO = new GuitarDAO();
        this.userDAO = new UserDAO();
    }
    
    /**
     * Създаване на нова поръчка
     * @param guitarId ID на китарата
     * @param buyerId ID на купувача
     * @return true ако създаването е успешно, false в противен случай
     * @throws IllegalArgumentException ако данните са невалидни
     */
    public boolean createOrder(int guitarId, int buyerId) {
        // Проверка дали китарата съществува
        Guitar guitar = guitarDAO.getGuitarById(guitarId);
        if (guitar == null) {
            throw new IllegalArgumentException("Китарата не съществува");
        }
        
        // Проверка дали китарата е активна
        if (guitar.getStatus() != Status.ACTIVE) {
            throw new IllegalArgumentException("Китарата не е налична за покупка");
        }
        
        // Проверка дали купувачът съществува
        User buyer = userDAO.getUserById(buyerId);
        if (buyer == null) {
            throw new IllegalArgumentException("Купувачът не съществува");
        }
        
        // Проверка дали купувачът не е продавачът
        if (buyerId == guitar.getSellerId()) {
            throw new IllegalArgumentException("Не можете да закупите собствената си китара");
        }
        
        // Създаване на поръчка
        Order order = new Order();
        order.setGuitarId(guitarId);
        order.setBuyerId(buyerId);
        order.setSellerId(guitar.getSellerId());
        order.setPrice(guitar.getPrice());
        order.setStatus(OrderStatus.PROCESSING);
        
        // Запазване на поръчката
        boolean result = orderDAO.createOrder(order);
        
        // Ако поръчката е създадена успешно, маркираме китарата като резервирана
        if (result) {
            guitarDAO.updateGuitarStatus(guitarId, Status.RESERVED);
        }
        
        return result;
    }
    
    /**
     * Получаване на поръчка по ID
     * @param orderId ID на поръчката
     * @return Обект с данните за поръчката или null ако не е намерена
     */
    public Order getOrderById(int orderId) {
        return orderDAO.getOrderById(orderId);
    }
    
    /**
     * Потвърждаване на поръчка от продавача
     * @param orderId ID на поръчката
     * @return true ако операцията е успешна, false в противен случай
     * @throws IllegalArgumentException ако поръчката не съществува
     */
    public boolean confirmOrder(int orderId) {
        Order order = orderDAO.getOrderById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Поръчката не съществува");
        }
        
        // Маркиране на поръчката като завършена
        boolean result = orderDAO.updateOrderStatus(orderId, OrderStatus.COMPLETED);
        
        // Маркиране на китарата като продадена
        if (result) {
            guitarDAO.updateGuitarStatus(order.getGuitarId(), Status.SOLD);
        }
        
        return result;
    }
    
    /**
     * Отказване на поръчка
     * @param orderId ID на поръчката
     * @return true ако операцията е успешна, false в противен случай
     * @throws IllegalArgumentException ако поръчката не съществува
     */
    public boolean cancelOrder(int orderId) {
        Order order = orderDAO.getOrderById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Поръчката не съществува");
        }
        
        // Маркиране на поръчката като отказана
        boolean result = orderDAO.updateOrderStatus(orderId, OrderStatus.CANCELLED);
        
        // Връщане на китарата в активен статус
        if (result) {
            guitarDAO.updateGuitarStatus(order.getGuitarId(), Status.ACTIVE);
        }
        
        return result;
    }
    
    /**
     * Получаване на всички поръчки на купувач
     * @param buyerId ID на купувача
     * @return Списък с поръчки
     */
    public List<Order> getOrdersByBuyer(int buyerId) {
        return orderDAO.getOrdersByBuyer(buyerId);
    }
    
    /**
     * Получаване на всички поръчки на продавач
     * @param sellerId ID на продавача
     * @return Списък с поръчки
     */
    public List<Order> getOrdersBySeller(int sellerId) {
        return orderDAO.getOrdersBySeller(sellerId);
    }
    
    /**
     * Получаване на всички поръчки (админ функция)
     * @return Списък с всички поръчки
     */
    public List<Order> getAllOrders() {
        return orderDAO.getAllOrders();
    }
}