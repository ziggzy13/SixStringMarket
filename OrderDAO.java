package com.sixstringmarket.dao;

import com.sixstringmarket.model.Order;
import com.sixstringmarket.model.Order.OrderStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object за работа с поръчки в базата данни
 */
public class OrderDAO {
    
    /**
     * Създаване на нова поръчка
     * @param order Обект с данните за поръчката
     * @return true ако операцията е успешна, false в противен случай
     */
    public boolean createOrder(Order order) {
        String sql = "INSERT INTO orders (guitar_id, buyer_id, seller_id, price, status) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, order.getGuitarId());
            stmt.setInt(2, order.getBuyerId());
            stmt.setInt(3, order.getSellerId());
            stmt.setBigDecimal(4, order.getPrice());
            stmt.setString(5, order.getStatus().name());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    order.setOrderId(rs.getInt(1));
                }
                return true;
            }
            
            return false;
        } catch (SQLException e) {
            System.err.println("Грешка при създаване на поръчка: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Получаване на поръчка по ID
     * @param orderId ID на поръчката
     * @return Order обект или null ако няма такава поръчка
     */
    public Order getOrderById(int orderId) {
        String sql = "SELECT * FROM orders WHERE order_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractOrderFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Грешка при получаване на поръчка по ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Обновяване на статуса на поръчка
     * @param orderId ID на поръчката
     * @param status Новият статус
     * @return true ако операцията е успешна, false в противен случай
     */
    public boolean updateOrderStatus(int orderId, OrderStatus status) {
        String sql = "UPDATE orders SET status = ? WHERE order_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status.name());
            stmt.setInt(2, orderId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Грешка при обновяване на статуса на поръчка: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Получаване на всички поръчки на купувач
     * @param buyerId ID на купувача
     * @return Списък с поръчки
     */
    public List<Order> getOrdersByBuyer(int buyerId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE buyer_id = ? ORDER BY order_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, buyerId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                orders.add(extractOrderFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Грешка при получаване на поръчки на купувач: " + e.getMessage());
        }
        
        return orders;
    }
    
    /**
     * Получаване на всички поръчки на продавач
     * @param sellerId ID на продавача
     * @return Списък с поръчки
     */
    public List<Order> getOrdersBySeller(int sellerId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE seller_id = ? ORDER BY order_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, sellerId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                orders.add(extractOrderFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Грешка при получаване на поръчки на продавач: " + e.getMessage());
        }
        
        return orders;
    }
    
    /**
     * Получаване на всички поръчки (админ функция)
     * @return Списък с всички поръчки
     */
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders ORDER BY order_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                orders.add(extractOrderFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Грешка при получаване на всички поръчки: " + e.getMessage());
        }
        
        return orders;
    }
    
    /**
     * Помощен метод за извличане на поръчка от ResultSet
     */
    private Order extractOrderFromResultSet(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getInt("order_id"));
        order.setGuitarId(rs.getInt("guitar_id"));
        order.setBuyerId(rs.getInt("buyer_id"));
        order.setSellerId(rs.getInt("seller_id"));
        order.setOrderDate(rs.getTimestamp("order_date"));
        order.setPrice(rs.getBigDecimal("price"));
        order.setStatus(OrderStatus.valueOf(rs.getString("status")));
        return order;
    }
}