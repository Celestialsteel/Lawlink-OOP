package com.lawlink.entity;

public enum DocumentType {
    CONTRACT("Contract"),
    LEGAL_BRIEF("Legal Brief"),
    COURT_FILING("Court Filing"),
    EVIDENCE("Evidence"),
    CORRESPONDENCE("Correspondence"),
    INVOICE("Invoice"),
    RECEIPT("Receipt"),
    IDENTIFICATION("Identification"),
    MEDICAL_RECORD("Medical Record"),
    FINANCIAL_STATEMENT("Financial Statement"),
    WITNESS_STATEMENT("Witness Statement"),
    EXPERT_REPORT("Expert Report"),
    SETTLEMENT_AGREEMENT("Settlement Agreement"),
    POWER_OF_ATTORNEY("Power of Attorney"),
    AFFIDAVIT("Affidavit"),
    MOTION("Motion"),
    DISCOVERY_DOCUMENT("Discovery Document"),
    DEPOSITION("Deposition"),
    POLICE_REPORT("Police Report"),
    INSURANCE_DOCUMENT("Insurance Document"),
    PROPERTY_DEED("Property Deed"),
    WILL_TESTAMENT("Will/Testament"),
    DIVORCE_DECREE("Divorce Decree"),
    CUSTODY_AGREEMENT("Custody Agreement"),
    EMPLOYMENT_CONTRACT("Employment Contract"),
    NON_DISCLOSURE_AGREEMENT("Non-Disclosure Agreement"),
    PATENT_APPLICATION("Patent Application"),
    TRADEMARK_FILING("Trademark Filing"),
    TAX_DOCUMENT("Tax Document"),
    BANKRUPTCY_FILING("Bankruptcy Filing"),
    OTHER("Other");

    private final String displayName;

    DocumentType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    // Helper method to get icon for UI
    public String getIcon() {
        switch (this) {
            case CONTRACT:
            case SETTLEMENT_AGREEMENT:
            case EMPLOYMENT_CONTRACT:
            case NON_DISCLOSURE_AGREEMENT:
                return "📋";
            case LEGAL_BRIEF:
            case MOTION:
                return "📄";
            case COURT_FILING:
            case AFFIDAVIT:
            case DISCOVERY_DOCUMENT:
                return "⚖️";
            case EVIDENCE:
            case WITNESS_STATEMENT:
            case EXPERT_REPORT:
                return "🔍";
            case CORRESPONDENCE:
                return "✉️";
            case INVOICE:
            case RECEIPT:
            case FINANCIAL_STATEMENT:
                return "💰";
            case IDENTIFICATION:
                return "🆔";
            case MEDICAL_RECORD:
                return "🏥";
            case DEPOSITION:
                return "🎤";
            case POLICE_REPORT:
                return "👮";
            case INSURANCE_DOCUMENT:
                return "🛡️";
            case PROPERTY_DEED:
                return "🏠";
            case WILL_TESTAMENT:
                return "📜";
            case DIVORCE_DECREE:
            case CUSTODY_AGREEMENT:
                return "👨‍👩‍👧‍👦";
            case POWER_OF_ATTORNEY:
                return "🔐";
            case PATENT_APPLICATION:
            case TRADEMARK_FILING:
                return "💡";
            case TAX_DOCUMENT:
                return "📊";
            case BANKRUPTCY_FILING:
                return "💸";
            case OTHER:
            default:
                return "📁";
        }
    }

    // Helper method to determine if document type is typically confidential
    public boolean isTypicallyConfidential() {
        switch (this) {
            case MEDICAL_RECORD:
            case FINANCIAL_STATEMENT:
            case TAX_DOCUMENT:
            case WITNESS_STATEMENT:
            case EXPERT_REPORT:
            case SETTLEMENT_AGREEMENT:
            case NON_DISCLOSURE_AGREEMENT:
            case BANKRUPTCY_FILING:
                return true;
            default:
                return false;
        }
    }

    // Helper method to determine if document type requires special handling
    public boolean requiresSpecialHandling() {
        switch (this) {
            case EVIDENCE:
            case COURT_FILING:
            case AFFIDAVIT:
            case DEPOSITION:
            case POLICE_REPORT:
            case WILL_TESTAMENT:
            case POWER_OF_ATTORNEY:
                return true;
            default:
                return false;
        }
    }

    // Helper method to get category for grouping
    public String getCategory() {
        switch (this) {
            case CONTRACT:
            case SETTLEMENT_AGREEMENT:
            case EMPLOYMENT_CONTRACT:
            case NON_DISCLOSURE_AGREEMENT:
            case CUSTODY_AGREEMENT:
                return "Contracts & Agreements";
            case LEGAL_BRIEF:
            case COURT_FILING:
            case MOTION:
            case AFFIDAVIT:
            case DISCOVERY_DOCUMENT:
                return "Legal Documents";
            case EVIDENCE:
            case WITNESS_STATEMENT:
            case EXPERT_REPORT:
            case DEPOSITION:
            case POLICE_REPORT:
                return "Evidence & Testimony";
            case INVOICE:
            case RECEIPT:
            case FINANCIAL_STATEMENT:
            case TAX_DOCUMENT:
                return "Financial Documents";
            case IDENTIFICATION:
            case MEDICAL_RECORD:
            case INSURANCE_DOCUMENT:
                return "Personal Documents";
            case PROPERTY_DEED:
            case WILL_TESTAMENT:
            case DIVORCE_DECREE:
                return "Legal Records";
            case PATENT_APPLICATION:
            case TRADEMARK_FILING:
                return "Intellectual Property";
            case CORRESPONDENCE:
                return "Communications";
            case POWER_OF_ATTORNEY:
                return "Authorization Documents";
            case BANKRUPTCY_FILING:
                return "Bankruptcy Documents";
            case OTHER:
            default:
                return "Other Documents";
        }
    }
}