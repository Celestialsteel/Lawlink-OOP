package com.lawlink.entity;

public enum CaseStatus {
    INITIATED("Case Initiated"),
    UNDER_REVIEW("Under Review"),
    INVESTIGATION("Investigation Phase"),
    DOCUMENTATION("Documentation Phase"),
    NEGOTIATION("Negotiation Phase"),
    COURT_PROCEEDINGS("Court Proceedings"),
    AWAITING_DECISION("Awaiting Decision"),
    COMPLETED("Completed"),
    CLOSED("Closed"),
    CANCELLED("Cancelled"),
    ON_HOLD("On Hold");

    private final String displayName;

    CaseStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    // Helper methods for status transitions
    public boolean canTransitionTo(CaseStatus newStatus) {
        switch (this) {
            case INITIATED:
                return newStatus == UNDER_REVIEW || newStatus == CANCELLED || newStatus == ON_HOLD;
            case UNDER_REVIEW:
                return newStatus == INVESTIGATION || newStatus == DOCUMENTATION || 
                       newStatus == CANCELLED || newStatus == ON_HOLD;
            case INVESTIGATION:
                return newStatus == DOCUMENTATION || newStatus == NEGOTIATION || 
                       newStatus == COURT_PROCEEDINGS || newStatus == CANCELLED || newStatus == ON_HOLD;
            case DOCUMENTATION:
                return newStatus == NEGOTIATION || newStatus == COURT_PROCEEDINGS || 
                       newStatus == COMPLETED || newStatus == CANCELLED || newStatus == ON_HOLD;
            case NEGOTIATION:
                return newStatus == COURT_PROCEEDINGS || newStatus == COMPLETED || 
                       newStatus == CANCELLED || newStatus == ON_HOLD;
            case COURT_PROCEEDINGS:
                return newStatus == AWAITING_DECISION || newStatus == COMPLETED || 
                       newStatus == CANCELLED || newStatus == ON_HOLD;
            case AWAITING_DECISION:
                return newStatus == COMPLETED || newStatus == COURT_PROCEEDINGS || 
                       newStatus == CANCELLED || newStatus == ON_HOLD;
            case COMPLETED:
                return newStatus == CLOSED;
            case ON_HOLD:
                return true; // Can transition to any status from on hold
            case CLOSED:
            case CANCELLED:
                return false; // Terminal states
            default:
                return false;
        }
    }

    public boolean isTerminal() {
        return this == COMPLETED || this == CLOSED || this == CANCELLED;
    }

    public boolean isActive() {
        return !isTerminal() && this != ON_HOLD;
    }
}