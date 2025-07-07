package com.lawlink.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "case_progress")
public class CaseProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", nullable = false)
    private Case caseEntity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CaseStatus previousStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CaseStatus newStatus;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(length = 2000)
    private String notes;

    @Column(nullable = false)
    private String updatedBy; // Email or username of who made the update

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UpdateType updateType;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column
    private LocalDateTime nextActionDate;

    @Column(length = 500)
    private String nextActionDescription;

    @Column
    private Boolean isClientVisible = true; // Whether client can see this update

    @Column
    private Boolean requiresClientAction = false; // Whether client needs to take action

    @Column(length = 500)
    private String clientActionRequired; // Description of what client needs to do

    // Constructors
    public CaseProgress() {
        this.timestamp = LocalDateTime.now();
        this.updateType = UpdateType.STATUS_CHANGE;
        this.isClientVisible = true;
        this.requiresClientAction = false;
    }

    public CaseProgress(Case caseEntity, CaseStatus previousStatus, CaseStatus newStatus, 
                       String description, String updatedBy) {
        this();
        this.caseEntity = caseEntity;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.description = description;
        this.updatedBy = updatedBy;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Case getCaseEntity() {
        return caseEntity;
    }

    public void setCaseEntity(Case caseEntity) {
        this.caseEntity = caseEntity;
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
    public boolean isStatusChange() {
        return updateType == UpdateType.STATUS_CHANGE;
    }

    public boolean hasNextAction() {
        return nextActionDate != null && nextActionDescription != null;
    }

    public boolean isOverdue() {
        return nextActionDate != null && nextActionDate.isBefore(LocalDateTime.now());
    }
}