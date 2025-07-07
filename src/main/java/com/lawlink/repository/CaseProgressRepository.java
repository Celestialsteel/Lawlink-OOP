package com.lawlink.repository;

import com.lawlink.entity.CaseProgress;
import com.lawlink.entity.CaseStatus;
import com.lawlink.entity.UpdateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CaseProgressRepository extends JpaRepository<CaseProgress, Long> {

    // Find progress updates by case ID
    List<CaseProgress> findByCaseEntityIdOrderByTimestampDesc(Long caseId);
    
    // Find progress updates by case ID that are visible to client
    List<CaseProgress> findByCaseEntityIdAndIsClientVisibleTrueOrderByTimestampDesc(Long caseId);
    
    // Find progress updates by case ID and update type
    List<CaseProgress> findByCaseEntityIdAndUpdateTypeOrderByTimestampDesc(Long caseId, UpdateType updateType);
    
    // Find progress updates that require client action
    @Query("SELECT p FROM CaseProgress p WHERE p.caseEntity.id = :caseId AND p.requiresClientAction = true AND p.isClientVisible = true ORDER BY p.timestamp DESC")
    List<CaseProgress> findClientActionRequiredByCaseId(@Param("caseId") Long caseId);
    
    // Find progress updates with upcoming next actions
    @Query("SELECT p FROM CaseProgress p WHERE p.nextActionDate BETWEEN :startDate AND :endDate ORDER BY p.nextActionDate ASC")
    List<CaseProgress> findUpcomingActions(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Find overdue actions
    @Query("SELECT p FROM CaseProgress p WHERE p.nextActionDate < :currentDate AND p.caseEntity.status NOT IN ('COMPLETED', 'CLOSED', 'CANCELLED') ORDER BY p.nextActionDate ASC")
    List<CaseProgress> findOverdueActions(@Param("currentDate") LocalDateTime currentDate);
    
    // Find progress updates by lawyer (updated by)
    @Query("SELECT p FROM CaseProgress p WHERE p.updatedBy = :lawyerEmail ORDER BY p.timestamp DESC")
    List<CaseProgress> findByUpdatedBy(@Param("lawyerEmail") String lawyerEmail);
    
    // Find recent progress updates for a lawyer's cases
    @Query("SELECT p FROM CaseProgress p WHERE p.caseEntity.lawyer.user.email = :lawyerEmail AND p.timestamp >= :sinceDate ORDER BY p.timestamp DESC")
    List<CaseProgress> findRecentUpdatesByLawyer(@Param("lawyerEmail") String lawyerEmail, @Param("sinceDate") LocalDateTime sinceDate);
    
    // Find recent progress updates for a client's cases
    @Query("SELECT p FROM CaseProgress p WHERE p.caseEntity.client.user.email = :clientEmail AND p.isClientVisible = true AND p.timestamp >= :sinceDate ORDER BY p.timestamp DESC")
    List<CaseProgress> findRecentUpdatesByClient(@Param("clientEmail") String clientEmail, @Param("sinceDate") LocalDateTime sinceDate);
    
    // Find progress updates by status change
    List<CaseProgress> findByNewStatusOrderByTimestampDesc(CaseStatus status);
    
    // Find progress updates within date range
    @Query("SELECT p FROM CaseProgress p WHERE p.timestamp BETWEEN :startDate AND :endDate ORDER BY p.timestamp DESC")
    List<CaseProgress> findProgressUpdatesBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Find urgent updates
    @Query("SELECT p FROM CaseProgress p WHERE p.updateType = 'URGENT_NOTICE' OR p.updateType = 'CLIENT_ACTION_REQUIRED' ORDER BY p.timestamp DESC")
    List<CaseProgress> findUrgentUpdates();
    
    // Find latest progress update for each case
    @Query("SELECT p FROM CaseProgress p WHERE p.timestamp = (SELECT MAX(p2.timestamp) FROM CaseProgress p2 WHERE p2.caseEntity.id = p.caseEntity.id)")
    List<CaseProgress> findLatestProgressForAllCases();
    
    // Count progress updates by case
    Long countByCaseEntityId(Long caseId);
    
    // Count progress updates by update type
    Long countByUpdateType(UpdateType updateType);
    
    // Find progress updates that mention specific keywords
    @Query("SELECT p FROM CaseProgress p WHERE LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.notes) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY p.timestamp DESC")
    List<CaseProgress> findByKeyword(@Param("keyword") String keyword);
    
    // Get progress statistics by update type
    @Query("SELECT p.updateType, COUNT(p) FROM CaseProgress p GROUP BY p.updateType")
    List<Object[]> getProgressStatisticsByType();
    
    // Get daily progress activity
    @Query("SELECT DATE(p.timestamp), COUNT(p) FROM CaseProgress p WHERE p.timestamp >= :sinceDate GROUP BY DATE(p.timestamp) ORDER BY DATE(p.timestamp)")
    List<Object[]> getDailyProgressActivity(@Param("sinceDate") LocalDateTime sinceDate);
}