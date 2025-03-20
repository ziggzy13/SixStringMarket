package com.sixstringmarket.dao;

import com.sixstringmarket.model.Guitar;
import com.sixstringmarket.model.Guitar.GuitarType;
import com.sixstringmarket.model.Guitar.Condition;
import com.sixstringmarket.model.Guitar.Status;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object за работа с китари в базата данни
 */
public class GuitarDAO {
    
    /**
     * Създаване на нова обява за китара
     * @param guitar Обект с данните за китарата
     * @return true ако операцията е успешна, false в противен случай
     */
    public boolean createGuitar(Guitar guitar) {
        String sql = "INSERT INTO guitars (seller_id, title, brand, model, type, guitar_condition, " +
                "manufacturing_year, price, description, image_path, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, guitar.getSellerId());
            stmt.setString(2, guitar.getTitle());
            stmt.setString(3, guitar.getBrand());
            stmt.setString(4, guitar.getModel());
            stmt.setString(5, guitar.getType().name());
            stmt.setString(6, guitar.getCondition().name());
            
            if (guitar.getManufacturingYear() != null) {
                stmt.setInt(7, guitar.getManufacturingYear());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }
            
            stmt.setBigDecimal(8, guitar.getPrice());
            stmt.setString(9, guitar.getDescription());
            stmt.setString(10, guitar.getImagePath());
            stmt.setString(11, guitar.getStatus().name());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    guitar.setGuitarId(rs.getInt(1));
                }
                return true;
            }
            
            return false;
        } catch (SQLException e) {
            System.err.println("Грешка при създаване на обява за китара: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Получаване на китара по ID
     * @param guitarId ID на китарата
     * @return Guitar обект или null ако няма такава китара
     */
    public Guitar getGuitarById(int guitarId) {
        String sql = "SELECT * FROM guitars WHERE guitar_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, guitarId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractGuitarFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Грешка при получаване на китара по ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Обновяване на информацията за китара
     * @param guitar Обект с обновените данни
     * @return true ако операцията е успешна, false в противен случай
     */
    public boolean updateGuitar(Guitar guitar) {
        String sql = "UPDATE guitars SET title = ?, brand = ?, model = ?, type = ?, " +
                "guitar_condition = ?, manufacturing_year = ?, price = ?, description = ?, " +
                "image_path = ?, status = ? WHERE guitar_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, guitar.getTitle());
            stmt.setString(2, guitar.getBrand());
            stmt.setString(3, guitar.getModel());
            stmt.setString(4, guitar.getType().name());
            stmt.setString(5, guitar.getCondition().name());
            
            if (guitar.getManufacturingYear() != null) {
                stmt.setInt(6, guitar.getManufacturingYear());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }
            
            stmt.setBigDecimal(7, guitar.getPrice());
            stmt.setString(8, guitar.getDescription());
            stmt.setString(9, guitar.getImagePath());
            stmt.setString(10, guitar.getStatus().name());
            stmt.setInt(11, guitar.getGuitarId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Грешка при обновяване на китара: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Обновяване на статуса на китара
     * @param guitarId ID на китарата
     * @param status Новият статус
     * @return true ако операцията е успешна, false в противен случай
     */
    public boolean updateGuitarStatus(int guitarId, Status status) {
        String sql = "UPDATE guitars SET status = ? WHERE guitar_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status.name());
            stmt.setInt(2, guitarId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Грешка при обновяване на статуса на китара: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Изтриване на обява за китара
     * @param guitarId ID на китарата
     * @return true ако операцията е успешна, false в противен случай
     */
    public boolean deleteGuitar(int guitarId) {
        String sql = "DELETE FROM guitars WHERE guitar_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, guitarId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Грешка при изтриване на китара: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Получаване на всички активни китари
     * @return Списък с активни китари
     */
    public List<Guitar> getAllActiveGuitars() {
        List<Guitar> guitars = new ArrayList<>();
        String sql = "SELECT * FROM guitars WHERE status = 'ACTIVE' ORDER BY date_added DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                guitars.add(extractGuitarFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Грешка при получаване на активни китари: " + e.getMessage());
        }
        
        return guitars;
    }
    
    /**
     * Получаване на китари по продавач
     * @param sellerId ID на продавача
     * @return Списък с китари
     */
    public List<Guitar> getGuitarsBySeller(int sellerId) {
        List<Guitar> guitars = new ArrayList<>();
        String sql = "SELECT * FROM guitars WHERE seller_id = ? ORDER BY date_added DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, sellerId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                guitars.add(extractGuitarFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Грешка при получаване на китари по продавач: " + e.getMessage());
        }
        
        return guitars;
    }
    
    /**
     * Търсене на китари със сложни критерии
     * @param criteria Карта с критерии за търсене
     * @return Списък с намерени китари
     */
    public List<Guitar> searchGuitars(Map<String, Object> criteria) {
        List<Guitar> guitars = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM guitars WHERE status = 'ACTIVE'");
        
        // Построяване на SQL заявката според критериите
        if (criteria.containsKey("keyword")) {
            String keyword = "%" + criteria.get("keyword") + "%";
            sqlBuilder.append(" AND (title LIKE ? OR brand LIKE ? OR model LIKE ? OR description LIKE ?)");
        }
        
        if (criteria.containsKey("type")) {
            sqlBuilder.append(" AND type = ?");
        }
        
        if (criteria.containsKey("brand")) {
            sqlBuilder.append(" AND brand = ?");
        }
        
        if (criteria.containsKey("minPrice")) {
            sqlBuilder.append(" AND price >= ?");
        }
        
        if (criteria.containsKey("maxPrice")) {
            sqlBuilder.append(" AND price <= ?");
        }
        
        if (criteria.containsKey("condition")) {
            sqlBuilder.append(" AND guitar_condition = ?");
        }
        
        sqlBuilder.append(" ORDER BY date_added DESC");
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString())) {
            
            int paramIndex = 1;
            
            // Добавяне на параметрите към заявката
            if (criteria.containsKey("keyword")) {
                String keyword = "%" + criteria.get("keyword") + "%";
                stmt.setString(paramIndex++, keyword);
                stmt.setString(paramIndex++, keyword);
                stmt.setString(paramIndex++, keyword);
                stmt.setString(paramIndex++, keyword);
            }
            
            if (criteria.containsKey("type")) {
                stmt.setString(paramIndex++, ((GuitarType) criteria.get("type")).name());
            }
            
            if (criteria.containsKey("brand")) {
                stmt.setString(paramIndex++, (String) criteria.get("brand"));
            }
            
            if (criteria.containsKey("minPrice")) {
                stmt.setBigDecimal(paramIndex++, (java.math.BigDecimal) criteria.get("minPrice"));
            }
            
            if (criteria.containsKey("maxPrice")) {
                stmt.setBigDecimal(paramIndex++, (java.math.BigDecimal) criteria.get("maxPrice"));
            }
            
            if (criteria.containsKey("condition")) {
                stmt.setString(paramIndex++, ((Condition) criteria.get("condition")).name());
            }
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                guitars.add(extractGuitarFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Грешка при търсене на китари: " + e.getMessage());
        }
        
        return guitars;
    }
    
    /**
     * Помощен метод за извличане на китара от ResultSet
     */
    private Guitar extractGuitarFromResultSet(ResultSet rs) throws SQLException {
        Guitar guitar = new Guitar();
        guitar.setGuitarId(rs.getInt("guitar_id"));
        guitar.setSellerId(rs.getInt("seller_id"));
        guitar.setTitle(rs.getString("title"));
        guitar.setBrand(rs.getString("brand"));
        guitar.setModel(rs.getString("model"));
        guitar.setType(GuitarType.valueOf(rs.getString("type")));
        guitar.setCondition(Condition.valueOf(rs.getString("guitar_condition")));
        
        // Проверка за NULL при YEAR
        int manufacturingYear = rs.getInt("manufacturing_year");
        if (!rs.wasNull()) {
            guitar.setManufacturingYear(manufacturingYear);
        }
        
        guitar.setPrice(rs.getBigDecimal("price"));
        guitar.setDescription(rs.getString("description"));
        guitar.setImagePath(rs.getString("image_path"));
        guitar.setDateAdded(rs.getTimestamp("date_added"));
        guitar.setStatus(Status.valueOf(rs.getString("status")));
        
        return guitar;
    }
}