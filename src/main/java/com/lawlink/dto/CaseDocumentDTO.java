package com.lawlink.dto;

import com.lawlink.entity.DocumentType;

import java.time.LocalDateTime;

public class CaseDocumentDTO {
    private Long id;
    private Long caseId;
    private String caseNumber;
    private String fileName;
    private String originalFileName;
    private String filePath;
    private String fileType;
    private Long fileSize;
    private String formattedFileSize;
    private DocumentType documentType;
    private String description;
    private String uploadedBy;
    private String uploadedByName;
    private LocalDateTime uploadedAt;
    private Boolean isClientVisible;
    private Boolean isConfidential;
    private String version;
    private String tags;

    // Constructors
    public CaseDocumentDTO() {}

    public CaseDocumentDTO(Long id, Long caseId, String fileName, String originalFileName,
                          DocumentType documentType, String uploadedBy, LocalDateTime uploadedAt) {
        this.id = id;
        this.caseId = caseId;
        this.fileName = fileName;
        this.originalFileName = originalFileName;
        this.documentType = documentType;
        this.uploadedBy = uploadedBy;
        this.uploadedAt = uploadedAt;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
        this.formattedFileSize = formatFileSize(fileSize);
    }

    public String getFormattedFileSize() {
        return formattedFileSize;
    }

    public void setFormattedFileSize(String formattedFileSize) {
        this.formattedFileSize = formattedFileSize;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public String getUploadedByName() {
        return uploadedByName;
    }

    public void setUploadedByName(String uploadedByName) {
        this.uploadedByName = uploadedByName;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public Boolean getIsClientVisible() {
        return isClientVisible;
    }

    public void setIsClientVisible(Boolean isClientVisible) {
        this.isClientVisible = isClientVisible;
    }

    public Boolean getIsConfidential() {
        return isConfidential;
    }

    public void setIsConfidential(Boolean isConfidential) {
        this.isConfidential = isConfidential;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    // Helper methods
    public String getDocumentTypeDisplayName() {
        return documentType != null ? documentType.getDisplayName() : "";
    }

    public String getDocumentTypeIcon() {
        return documentType != null ? documentType.getIcon() : "📁";
    }

    public String getDocumentCategory() {
        return documentType != null ? documentType.getCategory() : "Other Documents";
    }

    public boolean isPdf() {
        return "application/pdf".equals(fileType);
    }

    public boolean isImage() {
        return fileType != null && fileType.startsWith("image/");
    }

    public boolean isDocument() {
        return fileType != null && (
            fileType.contains("word") || 
            fileType.contains("document") || 
            fileType.contains("text") ||
            isPdf()
        );
    }

    public boolean isTypicallyConfidential() {
        return documentType != null && documentType.isTypicallyConfidential();
    }

    public boolean requiresSpecialHandling() {
        return documentType != null && documentType.requiresSpecialHandling();
    }

    public String getFormattedUploadedAt() {
        if (uploadedAt == null) return "";
        return uploadedAt.toString(); // You can format this as needed
    }

    public String[] getTagsArray() {
        if (tags == null || tags.trim().isEmpty()) {
            return new String[0];
        }
        return tags.split(",");
    }

    private String formatFileSize(Long size) {
        if (size == null) return "Unknown";
        
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return String.format("%.1f KB", size / 1024.0);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.1f MB", size / (1024.0 * 1024.0));
        } else {
            return String.format("%.1f GB", size / (1024.0 * 1024.0 * 1024.0));
        }
    }

    public String getFileExtension() {
        if (originalFileName == null) return "";
        int lastDot = originalFileName.lastIndexOf('.');
        return lastDot > 0 ? originalFileName.substring(lastDot + 1).toLowerCase() : "";
    }
}