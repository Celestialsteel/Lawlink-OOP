package com.lawlink.dto;

import com.lawlink.entity.CaseStatus;

public class UpdateCaseStatusRequestDTO {
    private CaseStatus newStatus;
    private String description;
    private String notes;
    private boolean notifyClient = true;

    // Constructors
    public UpdateCaseStatusRequestDTO() {}

    public UpdateCaseStatusRequestDTO(CaseStatus newStatus, String description) {
        this.newStatus = newStatus;
        this.description = description;
    }

    // Getters and Setters
    public CaseStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(CaseStatus newStatus) {
        this.newStatus = newStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isNotifyClient() {
        return notifyClient;
    }

    public void setNotifyClient(boolean notifyClient) {
        this.notifyClient = notifyClient;
    }

    // Validation
    public boolean isValid() {
        return newStatus != null && 
               description != null && !description.trim().isEmpty();
    }

    public String getValidationError() {
        if (newStatus == null) {
            return "New status is required";
        }
        if (description == null || description.trim().isEmpty()) {
            return "Description is required";
        }
        return null;
    }
}