package com.sixstringmarket.model;

import java.util.Date;

public class GuitarReview {
    private int reviewId;
    private int guitarId;
    private int userId;
    private int rating;  // 1-5 stars
    private String comment;
    private Date reviewDate;
    
    // For convenience
    private String username;
    private String guitarTitle;
    
    public GuitarReview() {
    }
    
    public GuitarReview(int guitarId, int userId, int rating, String comment) {
        this.guitarId = guitarId;
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
    }
    
    // Getters and Setters
    public int getReviewId() {
        return reviewId;
    }
    
    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }
    
    public int getGuitarId() {
        return guitarId;
    }
    
    public void setGuitarId(int guitarId) {
        this.guitarId = guitarId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getRating() {
        return rating;
    }
    
    public void setRating(int rating) {
        if (rating < 1) rating = 1;
        if (rating > 5) rating = 5;
        this.rating = rating;
    }
    
    public String getComment() {
        return comment;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public Date getReviewDate() {
        return reviewDate;
    }
    
    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getGuitarTitle() {
        return guitarTitle;
    }
    
    public void setGuitarTitle(String guitarTitle) {
        this.guitarTitle = guitarTitle;
    }
    
    @Override
    public String toString() {
        return "GuitarReview{" +
                "reviewId=" + reviewId +
                ", guitarId=" + guitarId +
                ", userId=" + userId +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", reviewDate=" + reviewDate +
                '}';
    }
}