package com.lawlink.repository;

import com.lawlink.entity.CaseDocument;
import com.lawlink.entity.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CaseDocumentRepository extends JpaRepository<CaseDocument, Long> {

    // Find documents by case ID
    List<CaseDocument> findByCaseEntityIdOrderByUploadedAtDesc(Long caseId);
    
    // Find documents by case ID that are visible to client
    List<CaseDocument> findByCaseEntityIdAndIsClientVisibleTrueOrderByUploadedAtDesc(Long caseId);
    
    // Find documents by case ID and document type
    List<CaseDocument> findByCaseEntityIdAndDocumentTypeOrderByUploadedAtDesc(Long caseId, DocumentType documentType);
    
    // Find documents by document type
    List<CaseDocument> findByDocumentTypeOrderByUploadedAtDesc(DocumentType documentType);
    
    // Find confidential documents
    List<CaseDocument> findByIsConfidentialTrueOrderByUploadedAtDesc();
    
    // Find documents by uploader
    List<CaseDocument> findByUploadedByOrderByUploadedAtDesc(String uploadedBy);
    
    // Find documents uploaded within date range
    @Query("SELECT d FROM CaseDocument d WHERE d.uploadedAt BETWEEN :startDate AND :endDate ORDER BY d.uploadedAt DESC")
    List<CaseDocument> findDocumentsUploadedBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Find documents by file type
    List<CaseDocument> findByFileTypeOrderByUploadedAtDesc(String fileType);
    
    // Find documents by original file name (partial match)
    @Query("SELECT d FROM CaseDocument d WHERE LOWER(d.originalFileName) LIKE LOWER(CONCAT('%', :fileName, '%')) ORDER BY d.uploadedAt DESC")
    List<CaseDocument> findByOriginalFileNameContaining(@Param("fileName") String fileName);
    
    // Find documents by description (partial match)
    @Query("SELECT d FROM CaseDocument d WHERE LOWER(d.description) LIKE LOWER(CONCAT('%', :description, '%')) ORDER BY d.uploadedAt DESC")
    List<CaseDocument> findByDescriptionContaining(@Param("description") String description);
    
    // Find documents by tags
    @Query("SELECT d FROM CaseDocument d WHERE LOWER(d.tags) LIKE LOWER(CONCAT('%', :tag, '%')) ORDER BY d.uploadedAt DESC")
    List<CaseDocument> findByTagsContaining(@Param("tag") String tag);
    
    // Find large documents (above certain size)
    @Query("SELECT d FROM CaseDocument d WHERE d.fileSize > :sizeThreshold ORDER BY d.fileSize DESC")
    List<CaseDocument> findLargeDocuments(@Param("sizeThreshold") Long sizeThreshold);
    
    // Find recent documents for a lawyer's cases
    @Query("SELECT d FROM CaseDocument d WHERE d.caseEntity.lawyer.user.email = :lawyerEmail AND d.uploadedAt >= :sinceDate ORDER BY d.uploadedAt DESC")
    List<CaseDocument> findRecentDocumentsByLawyer(@Param("lawyerEmail") String lawyerEmail, @Param("sinceDate") LocalDateTime sinceDate);
    
    // Find recent documents for a client's cases (visible only)
    @Query("SELECT d FROM CaseDocument d WHERE d.caseEntity.client.user.email = :clientEmail AND d.isClientVisible = true AND d.uploadedAt >= :sinceDate ORDER BY d.uploadedAt DESC")
    List<CaseDocument> findRecentDocumentsByClient(@Param("clientEmail") String clientEmail, @Param("sinceDate") LocalDateTime sinceDate);
    
    // Count documents by case
    Long countByCaseEntityId(Long caseId);
    
    // Count documents by document type
    Long countByDocumentType(DocumentType documentType);
    
    // Count confidential documents by case
    Long countByCaseEntityIdAndIsConfidentialTrue(Long caseId);
    
    // Get total file size for a case
    @Query("SELECT COALESCE(SUM(d.fileSize), 0) FROM CaseDocument d WHERE d.caseEntity.id = :caseId")
    Long getTotalFileSizeByCase(@Param("caseId") Long caseId);
    
    // Get document statistics by type
    @Query("SELECT d.documentType, COUNT(d) FROM CaseDocument d GROUP BY d.documentType")
    List<Object[]> getDocumentStatisticsByType();
    
    // Get document upload activity by date
    @Query("SELECT DATE(d.uploadedAt), COUNT(d) FROM CaseDocument d WHERE d.uploadedAt >= :sinceDate GROUP BY DATE(d.uploadedAt) ORDER BY DATE(d.uploadedAt)")
    List<Object[]> getDocumentUploadActivity(@Param("sinceDate") LocalDateTime sinceDate);
    
    // Search documents across all fields
    @Query("SELECT d FROM CaseDocument d WHERE " +
           "LOWER(d.originalFileName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(d.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(d.tags) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "ORDER BY d.uploadedAt DESC")
    List<CaseDocument> searchDocuments(@Param("searchTerm") String searchTerm);
    
    // Find documents by version
    List<CaseDocument> findByVersionOrderByUploadedAtDesc(String version);
    
    // Find latest version of documents with same original name in a case
    @Query("SELECT d FROM CaseDocument d WHERE d.caseEntity.id = :caseId AND d.uploadedAt = " +
           "(SELECT MAX(d2.uploadedAt) FROM CaseDocument d2 WHERE d2.caseEntity.id = :caseId AND d2.originalFileName = d.originalFileName)")
    List<CaseDocument> findLatestVersionsByCase(@Param("caseId") Long caseId);
}