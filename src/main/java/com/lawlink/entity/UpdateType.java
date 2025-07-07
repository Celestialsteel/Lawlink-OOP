package com.lawlink.entity;

public enum UpdateType {
    STATUS_CHANGE("Status Change"),
    DOCUMENT_ADDED("Document Added"),
    MEETING_SCHEDULED("Meeting Scheduled"),
    COURT_DATE_SET("Court Date Set"),
    DEADLINE_SET("Deadline Set"),
    CLIENT_ACTION_REQUIRED("Client Action Required"),
    PAYMENT_RECEIVED("Payment Received"),
    MILESTONE_REACHED("Milestone Reached"),
    GENERAL_UPDATE("General Update"),
    URGENT_NOTICE("Urgent Notice");

    private final String displayName;

    UpdateType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    // Helper method to get icon for UI
    public String getIcon() {
        switch (this) {
            case STATUS_CHANGE:
                return "🔄";
            case DOCUMENT_ADDED:
                return "📄";
            case MEETING_SCHEDULED:
                return "📅";
            case COURT_DATE_SET:
                return "⚖️";
            case DEADLINE_SET:
                return "⏰";
            case CLIENT_ACTION_REQUIRED:
                return "❗";
            case PAYMENT_RECEIVED:
                return "💰";
            case MILESTONE_REACHED:
                return "🎯";
            case GENERAL_UPDATE:
                return "ℹ️";
            case URGENT_NOTICE:
                return "🚨";
            default:
                return "📝";
        }
    }

    // Helper method to determine if update type requires immediate attention
    public boolean requiresImmediateAttention() {
        return this == URGENT_NOTICE || this == CLIENT_ACTION_REQUIRED || this == COURT_DATE_SET;
    }

    // Helper method to get color coding for UI
    public String getColorCode() {
        switch (this) {
            case URGENT_NOTICE:
                return "#dc3545"; // Red
            case CLIENT_ACTION_REQUIRED:
                return "#fd7e14"; // Orange
            case COURT_DATE_SET:
            case DEADLINE_SET:
                return "#ffc107"; // Yellow
            case MILESTONE_REACHED:
            case PAYMENT_RECEIVED:
                return "#28a745"; // Green
            case STATUS_CHANGE:
                return "#007bff"; // Blue
            case DOCUMENT_ADDED:
            case MEETING_SCHEDULED:
            case GENERAL_UPDATE:
            default:
                return "#6c757d"; // Gray
        }
    }
}