package com.lawlink.service;

import com.lawlink.dto.CaseProgressDTO;
import com.lawlink.entity.*;
import com.lawlink.repository.CaseProgressRepository;
import com.lawlink.repository.CaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CaseProgressService {

    @Autowired
    private CaseProgressRepository progressRepository;

    @Autowired
    private CaseRepository caseRepository;

    // Add progress update
    public CaseProgressDTO addProgressUpdate(CaseProgressDTO progressDTO) {
        Case caseEntity = caseRepository.findById(progressDTO.getCaseId())
                .orElseThrow(() -> new RuntimeException("Case not found"));

        CaseProgress progress = new CaseProgress();
        progress.setCaseEntity(caseEntity);
        progress.setPreviousStatus(caseEntity.getStatus());
        progress.setNewStatus(progressDTO.getNewStatus() != null ? progressDTO.getNewStatus() : caseEntity.getStatus());
        progress.setDescription(progressDTO.getDescription());
        progress.setNotes(progressDTO.getNotes());
        progress.setUpdatedBy(progressDTO.getUpdatedBy());
        progress.setUpdateType(progressDTO.getUpdateType() != null ? progressDTO.getUpdateType() : UpdateType.GENERAL_UPDATE);
        progress.setNextActionDate(progressDTO.getNextActionDate());
        progress.setNextActionDescription(progressDTO.getNextActionDescription());
        progress.setIsClientVisible(progressDTO.getIsClientVisible() != null ? progressDTO.getIsClientVisible() : true);
        progress.setRequiresClientAction(progressDTO.getRequiresClientAction() != null ? progressDTO.getRequiresClientAction() : false);
        progress.setClientActionRequired(progressDTO.getClientActionRequired());

        // Update case status if it's a status change
        if (progressDTO.getNewStatus() != null && !progressDTO.getNewStatus().equals(caseEntity.getStatus())) {
            if (!caseEntity.getStatus().canTransitionTo(progressDTO.getNewStatus())) {
                throw new RuntimeException("Invalid status transition from " + caseEntity.getStatus() + " to " + progressDTO.getNewStatus());
            }
            caseEntity.setStatus(progressDTO.getNewStatus());
            progress.setUpdateType(UpdateType.STATUS_CHANGE);
            
            // Set completion date if case is completed
            if (progressDTO.getNewStatus() == CaseStatus.COMPLETED && caseEntity.getActualCompletionDate() == null) {
                caseEntity.setActualCompletionDate(LocalDateTime.now());
            }
            
            caseRepository.save(caseEntity);
        }

        CaseProgress savedProgress = progressRepository.save(progress);
        return convertToDTO(savedProgress);
    }

    // Add general update (non-status change)
    public CaseProgressDTO addGeneralUpdate(Long caseId, String description, String notes, 
                                           String updatedBy, UpdateType updateType) {
        CaseProgressDTO progressDTO = new CaseProgressDTO();
        progressDTO.setCaseId(caseId);
        progressDTO.setDescription(description);
        progressDTO.setNotes(notes);
        progressDTO.setUpdatedBy(updatedBy);
        progressDTO.setUpdateType(updateType != null ? updateType : UpdateType.GENERAL_UPDATE);
        
        return addProgressUpdate(progressDTO);
    }

    // Add client action required update
    public CaseProgressDTO addClientActionRequired(Long caseId, String description, String actionRequired, 
                                                  LocalDateTime actionDate, String updatedBy) {
        CaseProgressDTO progressDTO = new CaseProgressDTO();
        progressDTO.setCaseId(caseId);
        progressDTO.setDescription(description);
        progressDTO.setUpdatedBy(updatedBy);
        progressDTO.setUpdateType(UpdateType.CLIENT_ACTION_REQUIRED);
        progressDTO.setRequiresClientAction(true);
        progressDTO.setClientActionRequired(actionRequired);
        progressDTO.setNextActionDate(actionDate);
        progressDTO.setNextActionDescription(actionRequired);
        
        return addProgressUpdate(progressDTO);
    }

    // Add deadline/next action
    public CaseProgressDTO addDeadline(Long caseId, String description, LocalDateTime deadlineDate, 
                                      String deadlineDescription, String updatedBy) {
        CaseProgressDTO progressDTO = new CaseProgressDTO();
        progressDTO.setCaseId(caseId);
        progressDTO.setDescription(description);
        progressDTO.setUpdatedBy(updatedBy);
        progressDTO.setUpdateType(UpdateType.DEADLINE_SET);
        progressDTO.setNextActionDate(deadlineDate);
        progressDTO.setNextActionDescription(deadlineDescription);
        
        return addProgressUpdate(progressDTO);
    }

    // Add urgent notice
    public CaseProgressDTO addUrgentNotice(Long caseId, String description, String notes, String updatedBy) {
        CaseProgressDTO progressDTO = new CaseProgressDTO();
        progressDTO.setCaseId(caseId);
        progressDTO.setDescription(description);
        progressDTO.setNotes(notes);
        progressDTO.setUpdatedBy(updatedBy);
        progressDTO.setUpdateType(UpdateType.URGENT_NOTICE);
        
        return addProgressUpdate(progressDTO);
    }

    // Get progress updates by case
    public List<CaseProgressDTO> getProgressUpdatesByCase(Long caseId) {
        List<CaseProgress> progressList = progressRepository.findByCaseEntityIdOrderByTimestampDesc(caseId);
        return progressList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get client-visible progress updates by case
    public List<CaseProgressDTO> getClientVisibleProgressUpdates(Long caseId) {
        List<CaseProgress> progressList = progressRepository.findByCaseEntityIdAndIsClientVisibleTrueOrderByTimestampDesc(caseId);
        return progressList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get progress updates that require client action
    public List<CaseProgressDTO> getClientActionRequired(Long caseId) {
        List<CaseProgress> progressList = progressRepository.findClientActionRequiredByCaseId(caseId);
        return progressList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get upcoming actions (next 7 days)
    public List<CaseProgressDTO> getUpcomingActions() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekFromNow = now.plusDays(7);
        List<CaseProgress> progressList = progressRepository.findUpcomingActions(now, weekFromNow);
        return progressList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get overdue actions
    public List<CaseProgressDTO> getOverdueActions() {
        List<CaseProgress> progressList = progressRepository.findOverdueActions(LocalDateTime.now());
        return progressList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get recent updates by lawyer
    public List<CaseProgressDTO> getRecentUpdatesByLawyer(String lawyerEmail, int days) {
        LocalDateTime sinceDate = LocalDateTime.now().minusDays(days);
        List<CaseProgress> progressList = progressRepository.findRecentUpdatesByLawyer(lawyerEmail, sinceDate);
        return progressList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get recent updates by client
    public List<CaseProgressDTO> getRecentUpdatesByClient(String clientEmail, int days) {
        LocalDateTime sinceDate = LocalDateTime.now().minusDays(days);
        List<CaseProgress> progressList = progressRepository.findRecentUpdatesByClient(clientEmail, sinceDate);
        return progressList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get urgent updates
    public List<CaseProgressDTO> getUrgentUpdates() {
        List<CaseProgress> progressList = progressRepository.findUrgentUpdates();
        return progressList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Search progress updates
    public List<CaseProgressDTO> searchProgressUpdates(String keyword) {
        List<CaseProgress> progressList = progressRepository.findByKeyword(keyword);
        return progressList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Update progress visibility
    public CaseProgressDTO updateProgressVisibility(Long progressId, Boolean isClientVisible) {
        CaseProgress progress = progressRepository.findById(progressId)
                .orElseThrow(() -> new RuntimeException("Progress update not found"));
        
        progress.setIsClientVisible(isClientVisible);
        CaseProgress savedProgress = progressRepository.save(progress);
        return convertToDTO(savedProgress);
    }

    // Mark client action as completed
    public CaseProgressDTO markClientActionCompleted(Long progressId, String completionNotes, String updatedBy) {
        CaseProgress progress = progressRepository.findById(progressId)
                .orElseThrow(() -> new RuntimeException("Progress update not found"));
        
        if (!progress.getRequiresClientAction()) {
            throw new RuntimeException("This progress update does not require client action");
        }

        // Create a new progress update to mark completion
        CaseProgressDTO completionUpdate = new CaseProgressDTO();
        completionUpdate.setCaseId(progress.getCaseEntity().getId());
        completionUpdate.setDescription("Client action completed: " + progress.getClientActionRequired());
        completionUpdate.setNotes(completionNotes);
        completionUpdate.setUpdatedBy(updatedBy);
        completionUpdate.setUpdateType(UpdateType.GENERAL_UPDATE);
        
        return addProgressUpdate(completionUpdate);
    }

    // Get progress statistics
    public ProgressStatisticsDTO getProgressStatistics() {
        ProgressStatisticsDTO stats = new ProgressStatisticsDTO();
        
        List<Object[]> typeStats = progressRepository.getProgressStatisticsByType();
        for (Object[] stat : typeStats) {
            UpdateType type = (UpdateType) stat[0];
            Long count = (Long) stat[1];
            stats.addTypeCount(type, count);
        }
        
        return stats;
    }

    // Convert entity to DTO
    private CaseProgressDTO convertToDTO(CaseProgress progress) {
        CaseProgressDTO dto = new CaseProgressDTO();
        dto.setId(progress.getId());
        dto.setCaseId(progress.getCaseEntity().getId());
        dto.setCaseNumber(progress.getCaseEntity().getCaseNumber());
        dto.setPreviousStatus(progress.getPreviousStatus());
        dto.setNewStatus(progress.getNewStatus());
        dto.setDescription(progress.getDescription());
        dto.setNotes(progress.getNotes());
        dto.setUpdatedBy(progress.getUpdatedBy());
        dto.setUpdateType(progress.getUpdateType());
        dto.setTimestamp(progress.getTimestamp());
        dto.setNextActionDate(progress.getNextActionDate());
        dto.setNextActionDescription(progress.getNextActionDescription());
        dto.setIsClientVisible(progress.getIsClientVisible());
        dto.setRequiresClientAction(progress.getRequiresClientAction());
        dto.setClientActionRequired(progress.getClientActionRequired());
        
        return dto;
    }

    // Inner class for progress statistics
    public static class ProgressStatisticsDTO {
        private java.util.Map<UpdateType, Long> typeCounts = new java.util.HashMap<>();

        public void addTypeCount(UpdateType type, Long count) {
            typeCounts.put(type, count);
        }

        public java.util.Map<UpdateType, Long> getTypeCounts() {
            return typeCounts;
        }
    }
}