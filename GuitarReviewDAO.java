package com.sixstringmarket.dao;

import com.sixstringmarket.model.GuitarReview;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuitarReviewDAO {
    
    public boolean addReview(GuitarReview review) {
        String sql = "INSERT INTO guitar_reviews (guitar_id, user_id, rating, comment) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, review.getGuitarId());
            stmt.setInt(2, review.getUserId());
            stmt.setInt(3, review.getRating());
            stmt.setString(4, review.getComment());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    review.setReviewId(rs.getInt(1));
                }
                return true;
            }
            
            return false;
        } catch (SQLException e) {
            System.err.println("Error adding review: " + e.getMessage());
            return false;
        }
    }
    
    public boolean updateReview(GuitarReview review) {
        String sql = "UPDATE guitar_reviews SET rating = ?, comment = ? WHERE review_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, review.getRating());
            stmt.setString(2, review.getComment());
            stmt.setInt(3, review.getReviewId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating review: " + e.getMessage());
            return false;
        }
    }
    
    public boolean deleteReview(int reviewId) {
        String sql = "DELETE FROM guitar_reviews WHERE review_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, reviewId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting review: " + e.getMessage());
            return false;
        }
    }
    
    public List<GuitarReview> getReviewsByGuitarId(int guitarId) {
        List<GuitarReview> reviews = new ArrayList<>();
        String sql = "SELECT * FROM guitar_reviews WHERE guitar_id = ? ORDER BY review_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, guitarId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                reviews.add(extractReviewFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting reviews by guitar ID: " + e.getMessage());
        }
        
        return reviews;
    }
    
    public double getAverageRatingForGuitar(int guitarId) {
        String sql = "SELECT AVG(rating) as avg_rating FROM guitar_reviews WHERE guitar_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, guitarId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("avg_rating");
            }
        } catch (SQLException e) {
            System.err.println("Error getting average rating: " + e.getMessage());
        }
        
        return 0.0;
    }
    
    private GuitarReview extractReviewFromResultSet(ResultSet rs) throws SQLException {
        GuitarReview review = new GuitarReview();
        review.setReviewId(rs.getInt("review_id"));
        review.setGuitarId(rs.getInt("guitar_id"));
        review.setUserId(rs.getInt("user_id"));
        review.setRating(rs.getInt("rating"));
        review.setComment(rs.getString("comment"));
        review.setReviewDate(rs.getTimestamp("review_date"));
        return review;
    }
}