package com.lawlink.service;

import com.lawlink.dto.CaseDTO;
import com.lawlink.dto.CaseProgressDTO;
import com.lawlink.entity.*;
import com.lawlink.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CaseService {

    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    private CaseProgressRepository progressRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private LawyerRepository lawyerRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    // Create a new case
    public CaseDTO createCase(CaseDTO caseDTO) {
        Client client = clientRepository.findById(caseDTO.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found"));
        
        Lawyer lawyer = lawyerRepository.findById(caseDTO.getLawyerId())
                .orElseThrow(() -> new RuntimeException("Lawyer not found"));

        Case caseEntity = new Case();
        caseEntity.setTitle(caseDTO.getTitle());
        caseEntity.setDescription(caseDTO.getDescription());
        caseEntity.setCaseType(caseDTO.getCaseType());
        caseEntity.setPriority(caseDTO.getPriority() != null ? caseDTO.getPriority() : CasePriority.MEDIUM);
        caseEntity.setClient(client);
        caseEntity.setLawyer(lawyer);
        caseEntity.setNotes(caseDTO.getNotes());
        
        // Set expected completion date based on case type if not provided
        if (caseDTO.getExpectedCompletionDate() != null) {
            caseEntity.setExpectedCompletionDate(caseDTO.getExpectedCompletionDate());
        } else {
            int typicalDays = caseDTO.getCaseType().getTypicalDurationDays();
            caseEntity.setExpectedCompletionDate(LocalDateTime.now().plusDays(typicalDays));
        }

        // Link to appointment if provided
        if (caseDTO.getAppointmentId() != null) {
            Appointment appointment = appointmentRepository.findById(caseDTO.getAppointmentId())
                    .orElseThrow(() -> new RuntimeException("Appointment not found"));
            caseEntity.setAppointment(appointment);
        }

        Case savedCase = caseRepository.save(caseEntity);

        // Create initial progress update
        CaseProgress initialProgress = new CaseProgress(
                savedCase, 
                null, 
                CaseStatus.INITIATED, 
                "Case has been initiated and assigned to " + lawyer.getFullName(),
                lawyer.getUser().getEmail()
        );
        initialProgress.setUpdateType(UpdateType.STATUS_CHANGE);
        initialProgress.setIsClientVisible(true);
        progressRepository.save(initialProgress);

        return convertToDTO(savedCase);
    }

    // Create case from appointment
    public CaseDTO createCaseFromAppointment(Long appointmentId, CaseDTO caseDTO) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        // Check if case already exists for this appointment
        Optional<Case> existingCase = caseRepository.findByAppointmentId(appointmentId);
        if (existingCase.isPresent()) {
            throw new RuntimeException("Case already exists for this appointment");
        }

        caseDTO.setClientId(appointment.getClient().getId());
        caseDTO.setLawyerId(appointment.getLawyer().getId());
        caseDTO.setAppointmentId(appointmentId);

        return createCase(caseDTO);
    }

    // Update case status
    public CaseDTO updateCaseStatus(Long caseId, CaseStatus newStatus, String description, String updatedBy) {
        Case caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() -> new RuntimeException("Case not found"));

        CaseStatus previousStatus = caseEntity.getStatus();

        // Validate status transition
        if (!previousStatus.canTransitionTo(newStatus)) {
            throw new RuntimeException("Invalid status transition from " + previousStatus + " to " + newStatus);
        }

        caseEntity.setStatus(newStatus);
        
        // Set completion date if case is completed
        if (newStatus == CaseStatus.COMPLETED && caseEntity.getActualCompletionDate() == null) {
            caseEntity.setActualCompletionDate(LocalDateTime.now());
        }

        Case savedCase = caseRepository.save(caseEntity);

        // Create progress update
        CaseProgress progress = new CaseProgress(
                savedCase, 
                previousStatus, 
                newStatus, 
                description != null ? description : "Status updated to " + newStatus.getDisplayName(),
                updatedBy
        );
        progress.setUpdateType(UpdateType.STATUS_CHANGE);
        progress.setIsClientVisible(true);
        progressRepository.save(progress);

        return convertToDTO(savedCase);
    }

    // Update case details
    public CaseDTO updateCase(Long caseId, CaseDTO caseDTO) {
        Case caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() -> new RuntimeException("Case not found"));

        if (caseDTO.getTitle() != null) {
            caseEntity.setTitle(caseDTO.getTitle());
        }
        if (caseDTO.getDescription() != null) {
            caseEntity.setDescription(caseDTO.getDescription());
        }
        if (caseDTO.getPriority() != null) {
            caseEntity.setPriority(caseDTO.getPriority());
        }
        if (caseDTO.getExpectedCompletionDate() != null) {
            caseEntity.setExpectedCompletionDate(caseDTO.getExpectedCompletionDate());
        }
        if (caseDTO.getNotes() != null) {
            caseEntity.setNotes(caseDTO.getNotes());
        }

        Case savedCase = caseRepository.save(caseEntity);
        return convertToDTO(savedCase);
    }

    // Get case by ID
    public CaseDTO getCaseById(Long caseId) {
        Case caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() -> new RuntimeException("Case not found"));
        return convertToDTO(caseEntity);
    }

    // Get case by case number
    public CaseDTO getCaseByCaseNumber(String caseNumber) {
        Case caseEntity = caseRepository.findByCaseNumber(caseNumber)
                .orElseThrow(() -> new RuntimeException("Case not found"));
        return convertToDTO(caseEntity);
    }

    // Get cases by client
    public List<CaseDTO> getCasesByClient(Long clientId) {
        List<Case> cases = caseRepository.findByClientId(clientId);
        return cases.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get cases by lawyer
    public List<CaseDTO> getCasesByLawyer(Long lawyerId) {
        List<Case> cases = caseRepository.findByLawyerId(lawyerId);
        return cases.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get active cases by lawyer
    public List<CaseDTO> getActiveCasesByLawyer(Long lawyerId) {
        List<Case> cases = caseRepository.findActiveCasesByLawyer(lawyerId);
        return cases.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get active cases by client
    public List<CaseDTO> getActiveCasesByClient(Long clientId) {
        List<Case> cases = caseRepository.findActiveCasesByClient(clientId);
        return cases.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get overdue cases
    public List<CaseDTO> getOverdueCases() {
        List<Case> cases = caseRepository.findOverdueCases(LocalDateTime.now());
        return cases.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get cases due soon (within next 7 days)
    public List<CaseDTO> getCasesDueSoon() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekFromNow = now.plusDays(7);
        List<Case> cases = caseRepository.findCasesDueSoon(now, weekFromNow);
        return cases.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get high priority cases
    public List<CaseDTO> getHighPriorityCases() {
        List<Case> cases = caseRepository.findHighPriorityActiveCases();
        return cases.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Search cases
    public List<CaseDTO> searchCases(String searchTerm) {
        List<Case> cases = caseRepository.searchCases(searchTerm);
        return cases.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get cases by status
    public List<CaseDTO> getCasesByStatus(CaseStatus status) {
        List<Case> cases = caseRepository.findByStatus(status);
        return cases.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get case statistics
    public CaseStatisticsDTO getCaseStatistics() {
        CaseStatisticsDTO stats = new CaseStatisticsDTO();
        
        List<Object[]> statusStats = caseRepository.getCaseStatusStatistics();
        for (Object[] stat : statusStats) {
            CaseStatus status = (CaseStatus) stat[0];
            Long count = (Long) stat[1];
            stats.addStatusCount(status, count);
        }
        
        List<Object[]> typeStats = caseRepository.getCaseTypeStatistics();
        for (Object[] stat : typeStats) {
            CaseType type = (CaseType) stat[0];
            Long count = (Long) stat[1];
            stats.addTypeCount(type, count);
        }
        
        List<Object[]> priorityStats = caseRepository.getCasePriorityStatistics();
        for (Object[] stat : priorityStats) {
            CasePriority priority = (CasePriority) stat[0];
            Long count = (Long) stat[1];
            stats.addPriorityCount(priority, count);
        }
        
        return stats;
    }

    // Delete case (soft delete by setting status to CANCELLED)
    public void deleteCase(Long caseId, String deletedBy) {
        Case caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() -> new RuntimeException("Case not found"));

        CaseStatus previousStatus = caseEntity.getStatus();
        caseEntity.setStatus(CaseStatus.CANCELLED);
        caseRepository.save(caseEntity);

        // Create progress update for cancellation
        CaseProgress progress = new CaseProgress(
                caseEntity, 
                previousStatus, 
                CaseStatus.CANCELLED, 
                "Case has been cancelled",
                deletedBy
        );
        progress.setUpdateType(UpdateType.STATUS_CHANGE);
        progress.setIsClientVisible(true);
        progressRepository.save(progress);
    }

    // Convert entity to DTO
    private CaseDTO convertToDTO(Case caseEntity) {
        CaseDTO dto = new CaseDTO();
        dto.setId(caseEntity.getId());
        dto.setCaseNumber(caseEntity.getCaseNumber());
        dto.setTitle(caseEntity.getTitle());
        dto.setDescription(caseEntity.getDescription());
        dto.setCaseType(caseEntity.getCaseType());
        dto.setStatus(caseEntity.getStatus());
        dto.setPriority(caseEntity.getPriority());
        dto.setClientId(caseEntity.getClient().getId());
        dto.setClientName(caseEntity.getClient().getFullName());
        dto.setClientEmail(caseEntity.getClient().getEmail());
        dto.setLawyerId(caseEntity.getLawyer().getId());
        dto.setLawyerName(caseEntity.getLawyer().getFullName());
        dto.setLawyerEmail(caseEntity.getLawyer().getEmail());
        dto.setCreatedAt(caseEntity.getCreatedAt());
        dto.setUpdatedAt(caseEntity.getUpdatedAt());
        dto.setExpectedCompletionDate(caseEntity.getExpectedCompletionDate());
        dto.setActualCompletionDate(caseEntity.getActualCompletionDate());
        dto.setNotes(caseEntity.getNotes());
        
        if (caseEntity.getAppointment() != null) {
            dto.setAppointmentId(caseEntity.getAppointment().getId());
        }

        // Set additional flags
        dto.setOverdue(caseEntity.getExpectedCompletionDate() != null && 
                      caseEntity.getExpectedCompletionDate().isBefore(LocalDateTime.now()) && 
                      caseEntity.isActive());

        // Count related entities
        dto.setTotalDocuments(caseEntity.getDocuments().size());
        dto.setTotalProgressUpdates(caseEntity.getProgressUpdates().size());

        return dto;
    }

    // Inner class for case statistics
    public static class CaseStatisticsDTO {
        private java.util.Map<CaseStatus, Long> statusCounts = new java.util.HashMap<>();
        private java.util.Map<CaseType, Long> typeCounts = new java.util.HashMap<>();
        private java.util.Map<CasePriority, Long> priorityCounts = new java.util.HashMap<>();

        public void addStatusCount(CaseStatus status, Long count) {
            statusCounts.put(status, count);
        }

        public void addTypeCount(CaseType type, Long count) {
            typeCounts.put(type, count);
        }

        public void addPriorityCount(CasePriority priority, Long count) {
            priorityCounts.put(priority, count);
        }

        // Getters
        public java.util.Map<CaseStatus, Long> getStatusCounts() { return statusCounts; }
        public java.util.Map<CaseType, Long> getTypeCounts() { return typeCounts; }
        public java.util.Map<CasePriority, Long> getPriorityCounts() { return priorityCounts; }
    }
}