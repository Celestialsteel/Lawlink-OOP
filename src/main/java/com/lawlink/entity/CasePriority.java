package com.lawlink.entity;

public enum CasePriority {
    LOW("Low", 1),
    MEDIUM("Medium", 2),
    HIGH("High", 3),
    URGENT("Urgent", 4),
    CRITICAL("Critical", 5);

    private final String displayName;
    private final int level;

    CasePriority(String displayName, int level) {
        this.displayName = displayName;
        this.level = level;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getLevel() {
        return level;
    }

    // Helper method to get color coding for UI
    public String getColorCode() {
        switch (this) {
            case LOW:
                return "#28a745"; // Green
            case MEDIUM:
                return "#ffc107"; // Yellow
            case HIGH:
                return "#fd7e14"; // Orange
            case URGENT:
                return "#dc3545"; // Red
            case CRITICAL:
                return "#6f42c1"; // Purple
            default:
                return "#6c757d"; // Gray
        }
    }

    // Helper method to determine if priority requires immediate attention
    public boolean requiresImmediateAttention() {
        return this == URGENT || this == CRITICAL;
    }

    // Helper method to get recommended response time in hours
    public int getRecommendedResponseTimeHours() {
        switch (this) {
            case CRITICAL:
                return 1;   // 1 hour
            case URGENT:
                return 4;   // 4 hours
            case HIGH:
                return 24;  // 1 day
            case MEDIUM:
                return 72;  // 3 days
            case LOW:
            default:
                return 168; // 1 week
        }
    }
}