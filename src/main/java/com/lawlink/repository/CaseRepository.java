package com.lawlink.repository;

import com.lawlink.entity.Case;
import com.lawlink.entity.CaseStatus;
import com.lawlink.entity.CaseType;
import com.lawlink.entity.CasePriority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CaseRepository extends JpaRepository<Case, Long> {

    // Find cases by client
    List<Case> findByClientId(Long clientId);
    
    // Find cases by lawyer
    List<Case> findByLawyerId(Long lawyerId);
    
    // Find case by case number
    Optional<Case> findByCaseNumber(String caseNumber);
    
    // Find cases by status
    List<Case> findByStatus(CaseStatus status);
    
    // Find cases by type
    List<Case> findByCaseType(CaseType caseType);
    
    // Find cases by priority
    List<Case> findByPriority(CasePriority priority);
    
    // Find cases by client and status
    List<Case> findByClientIdAndStatus(Long clientId, CaseStatus status);
    
    // Find cases by lawyer and status
    List<Case> findByLawyerIdAndStatus(Long lawyerId, CaseStatus status);
    
    // Find active cases (not completed, closed, or cancelled)
    @Query("SELECT c FROM Case c WHERE c.status NOT IN ('COMPLETED', 'CLOSED', 'CANCELLED')")
    List<Case> findActiveCases();
    
    // Find active cases by lawyer
    @Query("SELECT c FROM Case c WHERE c.lawyer.id = :lawyerId AND c.status NOT IN ('COMPLETED', 'CLOSED', 'CANCELLED')")
    List<Case> findActiveCasesByLawyer(@Param("lawyerId") Long lawyerId);
    
    // Find active cases by client
    @Query("SELECT c FROM Case c WHERE c.client.id = :clientId AND c.status NOT IN ('COMPLETED', 'CLOSED', 'CANCELLED')")
    List<Case> findActiveCasesByClient(@Param("clientId") Long clientId);
    
    // Find overdue cases (expected completion date passed)
    @Query("SELECT c FROM Case c WHERE c.expectedCompletionDate < :currentDate AND c.status NOT IN ('COMPLETED', 'CLOSED', 'CANCELLED')")
    List<Case> findOverdueCases(@Param("currentDate") LocalDateTime currentDate);
    
    // Find cases due soon (within specified days)
    @Query("SELECT c FROM Case c WHERE c.expectedCompletionDate BETWEEN :startDate AND :endDate AND c.status NOT IN ('COMPLETED', 'CLOSED', 'CANCELLED')")
    List<Case> findCasesDueSoon(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Find high priority active cases
    @Query("SELECT c FROM Case c WHERE c.priority IN ('HIGH', 'URGENT', 'CRITICAL') AND c.status NOT IN ('COMPLETED', 'CLOSED', 'CANCELLED')")
    List<Case> findHighPriorityActiveCases();
    
    // Find cases by lawyer and priority
    List<Case> findByLawyerIdAndPriority(Long lawyerId, CasePriority priority);
    
    // Find cases created within date range
    @Query("SELECT c FROM Case c WHERE c.createdAt BETWEEN :startDate AND :endDate")
    List<Case> findCasesCreatedBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Find cases updated within date range
    @Query("SELECT c FROM Case c WHERE c.updatedAt BETWEEN :startDate AND :endDate")
    List<Case> findCasesUpdatedBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Count cases by status
    @Query("SELECT COUNT(c) FROM Case c WHERE c.status = :status")
    Long countByStatus(@Param("status") CaseStatus status);
    
    // Count active cases by lawyer
    @Query("SELECT COUNT(c) FROM Case c WHERE c.lawyer.id = :lawyerId AND c.status NOT IN ('COMPLETED', 'CLOSED', 'CANCELLED')")
    Long countActiveCasesByLawyer(@Param("lawyerId") Long lawyerId);
    
    // Count completed cases by lawyer
    @Query("SELECT COUNT(c) FROM Case c WHERE c.lawyer.id = :lawyerId AND c.status = 'COMPLETED'")
    Long countCompletedCasesByLawyer(@Param("lawyerId") Long lawyerId);
    
    // Find cases with upcoming deadlines (next action dates)
    @Query("SELECT DISTINCT c FROM Case c JOIN c.progressUpdates p WHERE p.nextActionDate BETWEEN :startDate AND :endDate AND c.status NOT IN ('COMPLETED', 'CLOSED', 'CANCELLED')")
    List<Case> findCasesWithUpcomingDeadlines(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Search cases by title or description
    @Query("SELECT c FROM Case c WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(c.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Case> searchCases(@Param("searchTerm") String searchTerm);
    
    // Find cases by appointment
    Optional<Case> findByAppointmentId(Long appointmentId);
    
    // Get case statistics for dashboard
    @Query("SELECT c.status, COUNT(c) FROM Case c GROUP BY c.status")
    List<Object[]> getCaseStatusStatistics();
    
    // Get case type statistics
    @Query("SELECT c.caseType, COUNT(c) FROM Case c GROUP BY c.caseType")
    List<Object[]> getCaseTypeStatistics();
    
    // Get priority statistics
    @Query("SELECT c.priority, COUNT(c) FROM Case c GROUP BY c.priority")
    List<Object[]> getCasePriorityStatistics();
}