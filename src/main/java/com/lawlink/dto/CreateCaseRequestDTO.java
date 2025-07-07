package com.lawlink.dto;

import com.lawlink.entity.CaseType;
import com.lawlink.entity.CasePriority;

import java.time.LocalDateTime;

public class CreateCaseRequestDTO {
    private String title;
    private String description;
    private CaseType caseType;
    private CasePriority priority;
    private Long clientId;
    private Long lawyerId;
    private Long appointmentId;
    private LocalDateTime expectedCompletionDate;
    private String notes;

    // Constructors
    public CreateCaseRequestDTO() {}

    public CreateCaseRequestDTO(String title, String description, CaseType caseType, 
                               Long clientId, Long lawyerId) {
        this.title = title;
        this.description = description;
        this.caseType = caseType;
        this.clientId = clientId;
        this.lawyerId = lawyerId;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CaseType getCaseType() {
        return caseType;
    }

    public void setCaseType(CaseType caseType) {
        this.caseType = caseType;
    }

    public CasePriority getPriority() {
        return priority;
    }

    public void setPriority(CasePriority priority) {
        this.priority = priority;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getLawyerId() {
        return lawyerId;
    }

    public void setLawyerId(Long lawyerId) {
        this.lawyerId = lawyerId;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public LocalDateTime getExpectedCompletionDate() {
        return expectedCompletionDate;
    }

    public void setExpectedCompletionDate(LocalDateTime expectedCompletionDate) {
        this.expectedCompletionDate = expectedCompletionDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // Validation helper methods
    public boolean isValid() {
        return title != null && !title.trim().isEmpty() &&
               description != null && !description.trim().isEmpty() &&
               caseType != null &&
               clientId != null &&
               lawyerId != null;
    }

    public String getValidationError() {
        if (title == null || title.trim().isEmpty()) {
            return "Title is required";
        }
        if (description == null || description.trim().isEmpty()) {
            return "Description is required";
        }
        if (caseType == null) {
            return "Case type is required";
        }
        if (clientId == null) {
            return "Client ID is required";
        }
        if (lawyerId == null) {
            return "Lawyer ID is required";
        }
        return null;
    }
}