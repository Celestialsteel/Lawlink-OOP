package com.lawlink.dto;

public class RatingDTO {
    private Long id;
    private int stars;
    private String review;
    private String submittedAt; // Use String for date formatting
    private Long clientId;
    private Long lawyerId;

    public RatingDTO() {}

    public RatingDTO(Long id, int stars, String review, String submittedAt, Long clientId, Long lawyerId) {
        this.id = id;
        this.stars = stars;
        this.review = review;
        this.submittedAt = submittedAt;
        this.clientId = clientId;
        this.lawyerId = lawyerId;
    }

    // Getters and Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getStars() { return stars; }
    public void setStars(int stars) { this.stars = stars; }

    public String getReview() { return review; }
    public void setReview(String review) { this.review = review; }

    public String getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(String submittedAt) { this.submittedAt = submittedAt; }

    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }

    public Long getLawyerId() { return lawyerId; }
    public void setLawyerId(Long lawyerId) { this.lawyerId = lawyerId; }
    
}
