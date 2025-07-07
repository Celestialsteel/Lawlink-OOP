package com.lawlink.controller;

import com.lawlink.dto.CaseDTO;
import com.lawlink.dto.CaseProgressDTO;
import com.lawlink.entity.CaseStatus;
import com.lawlink.entity.CaseType;
import com.lawlink.entity.CasePriority;
import com.lawlink.service.CaseService;
import com.lawlink.service.CaseProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cases")
@CrossOrigin(origins = "*")
public class CaseController {

    @Autowired
    private CaseService caseService;

    @Autowired
    private CaseProgressService progressService;

    // Create a new case
    @PostMapping
    public ResponseEntity<?> createCase(@RequestBody CaseDTO caseDTO, Authentication auth) {
        try {
            CaseDTO createdCase = caseService.createCase(caseDTO);
            return ResponseEntity.ok(createdCase);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Create case from appointment
    @PostMapping("/from-appointment/{appointmentId}")
    public ResponseEntity<?> createCaseFromAppointment(@PathVariable Long appointmentId, 
                                                      @RequestBody CaseDTO caseDTO, 
                                                      Authentication auth) {
        try {
            CaseDTO createdCase = caseService.createCaseFromAppointment(appointmentId, caseDTO);
            return ResponseEntity.ok(createdCase);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Get case by ID
    @GetMapping("/{caseId}")
    public ResponseEntity<?> getCaseById(@PathVariable Long caseId) {
        try {
            CaseDTO caseDTO = caseService.getCaseById(caseId);
            return ResponseEntity.ok(caseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Get case by case number
    @GetMapping("/number/{caseNumber}")
    public ResponseEntity<?> getCaseByCaseNumber(@PathVariable String caseNumber) {
        try {
            CaseDTO caseDTO = caseService.getCaseByCaseNumber(caseNumber);
            return ResponseEntity.ok(caseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Get cases by client
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<CaseDTO>> getCasesByClient(@PathVariable Long clientId) {
        List<CaseDTO> cases = caseService.getCasesByClient(clientId);
        return ResponseEntity.ok(cases);
    }

    // Get cases by lawyer
    @GetMapping("/lawyer/{lawyerId}")
    public ResponseEntity<List<CaseDTO>> getCasesByLawyer(@PathVariable Long lawyerId) {
        List<CaseDTO> cases = caseService.getCasesByLawyer(lawyerId);
        return ResponseEntity.ok(cases);
    }

    // Get active cases by lawyer
    @GetMapping("/lawyer/{lawyerId}/active")
    public ResponseEntity<List<CaseDTO>> getActiveCasesByLawyer(@PathVariable Long lawyerId) {
        List<CaseDTO> cases = caseService.getActiveCasesByLawyer(lawyerId);
        return ResponseEntity.ok(cases);
    }

    // Get active cases by client
    @GetMapping("/client/{clientId}/active")
    public ResponseEntity<List<CaseDTO>> getActiveCasesByClient(@PathVariable Long clientId) {
        List<CaseDTO> cases = caseService.getActiveCasesByClient(clientId);
        return ResponseEntity.ok(cases);
    }

    // Update case status
    @PutMapping("/{caseId}/status")
    public ResponseEntity<?> updateCaseStatus(@PathVariable Long caseId, 
                                             @RequestBody Map<String, Object> request,
                                             Authentication auth) {
        try {
            CaseStatus newStatus = CaseStatus.valueOf((String) request.get("status"));
            String description = (String) request.get("description");
            String updatedBy = auth.getName();
            
            CaseDTO updatedCase = caseService.updateCaseStatus(caseId, newStatus, description, updatedBy);
            return ResponseEntity.ok(updatedCase);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Update case details
    @PutMapping("/{caseId}")
    public ResponseEntity<?> updateCase(@PathVariable Long caseId, @RequestBody CaseDTO caseDTO) {
        try {
            CaseDTO updatedCase = caseService.updateCase(caseId, caseDTO);
            return ResponseEntity.ok(updatedCase);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Delete case (soft delete)
    @DeleteMapping("/{caseId}")
    public ResponseEntity<?> deleteCase(@PathVariable Long caseId, Authentication auth) {
        try {
            caseService.deleteCase(caseId, auth.getName());
            return ResponseEntity.ok(Map.of("message", "Case deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Search cases
    @GetMapping("/search")
    public ResponseEntity<List<CaseDTO>> searchCases(@RequestParam String query) {
        List<CaseDTO> cases = caseService.searchCases(query);
        return ResponseEntity.ok(cases);
    }

    // Get cases by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<CaseDTO>> getCasesByStatus(@PathVariable String status) {
        try {
            CaseStatus caseStatus = CaseStatus.valueOf(status.toUpperCase());
            List<CaseDTO> cases = caseService.getCasesByStatus(caseStatus);
            return ResponseEntity.ok(cases);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get overdue cases
    @GetMapping("/overdue")
    public ResponseEntity<List<CaseDTO>> getOverdueCases() {
        List<CaseDTO> cases = caseService.getOverdueCases();
        return ResponseEntity.ok(cases);
    }

    // Get cases due soon
    @GetMapping("/due-soon")
    public ResponseEntity<List<CaseDTO>> getCasesDueSoon() {
        List<CaseDTO> cases = caseService.getCasesDueSoon();
        return ResponseEntity.ok(cases);
    }

    // Get high priority cases
    @GetMapping("/high-priority")
    public ResponseEntity<List<CaseDTO>> getHighPriorityCases() {
        List<CaseDTO> cases = caseService.getHighPriorityCases();
        return ResponseEntity.ok(cases);
    }

    // Get case statistics
    @GetMapping("/statistics")
    public ResponseEntity<CaseService.CaseStatisticsDTO> getCaseStatistics() {
        CaseService.CaseStatisticsDTO stats = caseService.getCaseStatistics();
        return ResponseEntity.ok(stats);
    }

    // Get case progress updates
    @GetMapping("/{caseId}/progress")
    public ResponseEntity<List<CaseProgressDTO>> getCaseProgress(@PathVariable Long caseId) {
        List<CaseProgressDTO> progress = progressService.getProgressUpdatesByCase(caseId);
        return ResponseEntity.ok(progress);
    }

    // Get client-visible progress updates
    @GetMapping("/{caseId}/progress/client-visible")
    public ResponseEntity<List<CaseProgressDTO>> getClientVisibleProgress(@PathVariable Long caseId) {
        List<CaseProgressDTO> progress = progressService.getClientVisibleProgressUpdates(caseId);
        return ResponseEntity.ok(progress);
    }

    // Add progress update
    @PostMapping("/{caseId}/progress")
    public ResponseEntity<?> addProgressUpdate(@PathVariable Long caseId, 
                                              @RequestBody CaseProgressDTO progressDTO,
                                              Authentication auth) {
        try {
            progressDTO.setCaseId(caseId);
            progressDTO.setUpdatedBy(auth.getName());
            CaseProgressDTO createdProgress = progressService.addProgressUpdate(progressDTO);
            return ResponseEntity.ok(createdProgress);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Add client action required
    @PostMapping("/{caseId}/progress/client-action")
    public ResponseEntity<?> addClientActionRequired(@PathVariable Long caseId,
                                                    @RequestBody Map<String, Object> request,
                                                    Authentication auth) {
        try {
            String description = (String) request.get("description");
            String actionRequired = (String) request.get("actionRequired");
            // You might want to parse actionDate from request if provided
            
            CaseProgressDTO progress = progressService.addClientActionRequired(
                caseId, description, actionRequired, null, auth.getName());
            return ResponseEntity.ok(progress);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Get upcoming actions
    @GetMapping("/progress/upcoming-actions")
    public ResponseEntity<List<CaseProgressDTO>> getUpcomingActions() {
        List<CaseProgressDTO> actions = progressService.getUpcomingActions();
        return ResponseEntity.ok(actions);
    }

    // Get overdue actions
    @GetMapping("/progress/overdue-actions")
    public ResponseEntity<List<CaseProgressDTO>> getOverdueActions() {
        List<CaseProgressDTO> actions = progressService.getOverdueActions();
        return ResponseEntity.ok(actions);
    }

    // Get urgent updates
    @GetMapping("/progress/urgent")
    public ResponseEntity<List<CaseProgressDTO>> getUrgentUpdates() {
        List<CaseProgressDTO> updates = progressService.getUrgentUpdates();
        return ResponseEntity.ok(updates);
    }

    // Get available case types
    @GetMapping("/types")
    public ResponseEntity<CaseType[]> getCaseTypes() {
        return ResponseEntity.ok(CaseType.values());
    }

    // Get available case statuses
    @GetMapping("/statuses")
    public ResponseEntity<CaseStatus[]> getCaseStatuses() {
        return ResponseEntity.ok(CaseStatus.values());
    }

    // Get available case priorities
    @GetMapping("/priorities")
    public ResponseEntity<CasePriority[]> getCasePriorities() {
        return ResponseEntity.ok(CasePriority.values());
    }
}