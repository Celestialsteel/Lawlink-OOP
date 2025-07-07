package com.lawlink.dto;

import com.lawlink.entity.CaseStatus;
import com.lawlink.entity.UpdateType;

import java.time.LocalDateTime;

public class CaseProgressDTO {
    private Long id;
    private Long caseId;
    private String caseNumber;
    private CaseStatus previousStatus;
    private CaseStatus newStatus;
    private String description;
    private String notes;
    private String updatedBy;
    private String updatedByName;
    private UpdateType updateType;
    private LocalDateTime timestamp;
    private LocalDateTime nextActionDate;
    private String nextActionDescription;
    private Boolean isClientVisible;
    private Boolean requiresClientAction;
    private String clientActionRequired;

    // Constructors
    public CaseProgressDTO() {}

    public CaseProgressDTO(Long id, Long caseId, CaseStatus previousStatus, CaseStatus newStatus,
                          String description, UpdateType updateType, LocalDateTime timestamp) {
        this.id = id;
        this.caseId = caseId;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.description = description;
        this.updateType = updateType;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public CaseStatus getPreviousStatus() {
        return previousStatus;
    }

    public void setPreviousStatus(CaseStatus previousStatus) {
        this.previousStatus = previousStatus;
    }

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

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedByName() {
        return updatedByName;
    }

    public void setUpdatedByName(String updatedByName) {
        this.updatedByName = updatedByName;
    }

    public UpdateType getUpdateType() {
        return updateType;
    }

    public void setUpdateType(UpdateType updateType) {
        this.updateType = updateType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public LocalDateTime getNextActionDate() {
        return nextActionDate;
    }

    public void setNextActionDate(LocalDateTime nextActionDate) {
        this.nextActionDate = nextActionDate;
    }

    public String getNextActionDescription() {
        return nextActionDescription;
    }

    public void setNextActionDescription(String nextActionDescription) {
        this.nextActionDescription = nextActionDescription;
    }

    public Boolean getIsClientVisible() {
        return isClientVisible;
    }

    public void setIsClientVisible(Boolean isClientVisible) {
        this.isClientVisible = isClientVisible;
    }

    public Boolean getRequiresClientAction() {
        return requiresClientAction;
    }

    public void setRequiresClientAction(Boolean requiresClientAction) {
        this.requiresClientAction = requiresClientAction;
    }

    public String getClientActionRequired() {
        return clientActionRequired;
    }

    public void setClientActionRequired(String clientActionRequired) {
        this.clientActionRequired = clientActionRequired;
    }

    // Helper methods
    public String getUpdateTypeDisplayName() {
        return updateType != null ? updateType.getDisplayName() : "";
    }

    public String getUpdateTypeIcon() {
        return updateType != null ? updateType.getIcon() : "📝";
    }

    public String getUpdateTypeColorCode() {
        return updateType != null ? updateType.getColorCode() : "#6c757d";
    }

    public String getPreviousStatusDisplayName() {
        return previousStatus != null ? previousStatus.getDisplayName() : "";
    }

    public String getNewStatusDisplayName() {
        return newStatus != null ? newStatus.getDisplayName() : "";
    }

    public boolean isStatusChange() {
        return updateType == UpdateType.STATUS_CHANGE;
    }

    public boolean hasNextAction() {
        return nextActionDate != null && nextActionDescription != null && !nextActionDescription.trim().isEmpty();
    }

    public boolean isOverdue() {
        return nextActionDate != null && nextActionDate.isBefore(LocalDateTime.now());
    }

    public boolean requiresImmediateAttention() {
        return updateType != null && updateType.requiresImmediateAttention();
    }

    public String getFormattedTimestamp() {
        if (timestamp == null) return "";
        return timestamp.toString(); // You can format this as needed
    }

    public String getFormattedNextActionDate() {
        if (nextActionDate == null) return "";
        return nextActionDate.toString(); // You can format this as needed
    }
}