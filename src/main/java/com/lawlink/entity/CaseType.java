package com.lawlink.entity;

public enum CaseType {
    CRIMINAL_DEFENSE("Criminal Defense"),
    CIVIL_LITIGATION("Civil Litigation"),
    FAMILY_LAW("Family Law"),
    CORPORATE_LAW("Corporate Law"),
    REAL_ESTATE("Real Estate"),
    IMMIGRATION("Immigration"),
    PERSONAL_INJURY("Personal Injury"),
    EMPLOYMENT_LAW("Employment Law"),
    INTELLECTUAL_PROPERTY("Intellectual Property"),
    TAX_LAW("Tax Law"),
    BANKRUPTCY("Bankruptcy"),
    ESTATE_PLANNING("Estate Planning"),
    ENVIRONMENTAL_LAW("Environmental Law"),
    HEALTHCARE_LAW("Healthcare Law"),
    CONSTITUTIONAL_LAW("Constitutional Law"),
    OTHER("Other");

    private final String displayName;

    CaseType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    // Helper method to get typical duration in days for each case type
    public int getTypicalDurationDays() {
        switch (this) {
            case CRIMINAL_DEFENSE:
                return 180; // 6 months
            case CIVIL_LITIGATION:
                return 365; // 1 year
            case FAMILY_LAW:
                return 120; // 4 months
            case CORPORATE_LAW:
                return 90;  // 3 months
            case REAL_ESTATE:
                return 60;  // 2 months
            case IMMIGRATION:
                return 270; // 9 months
            case PERSONAL_INJURY:
                return 300; // 10 months
            case EMPLOYMENT_LAW:
                return 150; // 5 months
            case INTELLECTUAL_PROPERTY:
                return 240; // 8 months
            case TAX_LAW:
                return 120; // 4 months
            case BANKRUPTCY:
                return 180; // 6 months
            case ESTATE_PLANNING:
                return 45;  // 1.5 months
            case ENVIRONMENTAL_LAW:
                return 365; // 1 year
            case HEALTHCARE_LAW:
                return 180; // 6 months
            case CONSTITUTIONAL_LAW:
                return 730; // 2 years
            case OTHER:
            default:
                return 120; // 4 months default
        }
    }

    // Helper method to determine if case type typically requires court proceedings
    public boolean typicallyRequiresCourt() {
        switch (this) {
            case CRIMINAL_DEFENSE:
            case CIVIL_LITIGATION:
            case FAMILY_LAW:
            case PERSONAL_INJURY:
            case BANKRUPTCY:
            case CONSTITUTIONAL_LAW:
                return true;
            case CORPORATE_LAW:
            case REAL_ESTATE:
            case IMMIGRATION:
            case EMPLOYMENT_LAW:
            case INTELLECTUAL_PROPERTY:
            case TAX_LAW:
            case ESTATE_PLANNING:
            case ENVIRONMENTAL_LAW:
            case HEALTHCARE_LAW:
            case OTHER:
            default:
                return false;
        }
    }
}