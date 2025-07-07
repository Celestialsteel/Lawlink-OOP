package com.lawlink.dto;

import com.lawlink.entity.CaseStatus;
import com.lawlink.entity.CaseType;
import com.lawlink.entity.CasePriority;

import java.time.LocalDateTime;
import java.util.List;

public class CaseDTO {
    private Long id;
    private String caseNumber;
    private String title;
    private String description;
    private CaseType caseType;
    private CaseStatus status;
    private CasePriority priority;
    private Long clientId;
    private String clientName;
    private String clientEmail;
    private Long lawyerId;
    private String lawyerName;
    private String lawyerEmail;
    private Long appointmentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime expectedCompletionDate;
    private LocalDateTime actualCompletionDate;
    private String notes;
    private List<CaseProgressDTO> progressUpdates;
    private List<CaseDocumentDTO> documents;
    private int totalDocuments;
    private int totalProgressUpdates;
    private boolean isOverdue;
    private boolean hasUpcomingDeadlines;
    private boolean requiresClientAction;

    // Constructors
    public CaseDTO() {}

    public CaseDTO(Long id, String caseNumber, String title, CaseStatus status, 
                   CaseType caseType, CasePriority priority, String clientName, String lawyerName) {
        this.id = id;
        this.caseNumber = caseNumber;
        this.title = title;
        this.status = status;
        this.caseType = caseType;
        this.priority = priority;
        this.clientName = clientName;
        this.lawyerName = lawyerName;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

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

    public CaseStatus getStatus() {
        return status;
    }

    public void setStatus(CaseStatus status) {
        this.status = status;
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

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public Long getLawyerId() {
        return lawyerId;
    }

    public void setLawyerId(Long lawyerId) {
        this.lawyerId = lawyerId;
    }

    public String getLawyerName() {
        return lawyerName;
    }

    public void setLawyerName(String lawyerName) {
        this.lawyerName = lawyerName;
    }

    public String getLawyerEmail() {
        return lawyerEmail;
    }

    public void setLawyerEmail(String lawyerEmail) {
        this.lawyerEmail = lawyerEmail;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getExpectedCompletionDate() {
        return expectedCompletionDate;
    }

    public void setExpectedCompletionDate(LocalDateTime expectedCompletionDate) {
        this.expectedCompletionDate = expectedCompletionDate;
    }

    public LocalDateTime getActualCompletionDate() {
        return actualCompletionDate;
    }

    public void setActualCompletionDate(LocalDateTime actualCompletionDate) {
        this.actualCompletionDate = actualCompletionDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<CaseProgressDTO> getProgressUpdates() {
        return progressUpdates;
    }

    public void setProgressUpdates(List<CaseProgressDTO> progressUpdates) {
        this.progressUpdates = progressUpdates;
    }

    public List<CaseDocumentDTO> getDocuments() {
        return documents;
    }

    public void setDocuments(List<CaseDocumentDTO> documents) {
        this.documents = documents;
    }

    public int getTotalDocuments() {
        return totalDocuments;
    }

    public void setTotalDocuments(int totalDocuments) {
        this.totalDocuments = totalDocuments;
    }

    public int getTotalProgressUpdates() {
        return totalProgressUpdates;
    }

    public void setTotalProgressUpdates(int totalProgressUpdates) {
        this.totalProgressUpdates = totalProgressUpdates;
    }

    public boolean isOverdue() {
        return isOverdue;
    }

    public void setOverdue(boolean overdue) {
        isOverdue = overdue;
    }

    public boolean isHasUpcomingDeadlines() {
        return hasUpcomingDeadlines;
    }

    public void setHasUpcomingDeadlines(boolean hasUpcomingDeadlines) {
        this.hasUpcomingDeadlines = hasUpcomingDeadlines;
    }

    public boolean isRequiresClientAction() {
        return requiresClientAction;
    }

    public void setRequiresClientAction(boolean requiresClientAction) {
        this.requiresClientAction = requiresClientAction;
    }

    // Helper methods
    public String getStatusDisplayName() {
        return status != null ? status.getDisplayName() : "";
    }

    public String getCaseTypeDisplayName() {
        return caseType != null ? caseType.getDisplayName() : "";
    }

    public String getPriorityDisplayName() {
        return priority != null ? priority.getDisplayName() : "";
    }

    public String getPriorityColorCode() {
        return priority != null ? priority.getColorCode() : "#6c757d";
    }

    public boolean isActive() {
        return status != null && status.isActive();
    }

    public boolean isCompleted() {
        return status == CaseStatus.COMPLETED || status == CaseStatus.CLOSED;
    }
}