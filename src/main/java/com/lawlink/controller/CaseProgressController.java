package com.lawlink.controller;

import com.lawlink.dto.CaseProgressDTO;
import com.lawlink.entity.UpdateType;
import com.lawlink.service.CaseProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/case-progress")
@CrossOrigin(origins = "*")
public class CaseProgressController {

    @Autowired
    private CaseProgressService progressService;

    // Add general progress update
    @PostMapping("/general")
    public ResponseEntity<?> addGeneralUpdate(@RequestBody Map<String, Object> request, Authentication auth) {
        try {
            Long caseId = Long.valueOf(request.get("caseId").toString());
            String description = (String) request.get("description");
            String notes = (String) request.get("notes");
            String updateTypeStr = (String) request.get("updateType");
            
            UpdateType updateType = updateTypeStr != null ? 
                UpdateType.valueOf(updateTypeStr) : UpdateType.GENERAL_UPDATE;
            
            CaseProgressDTO progress = progressService.addGeneralUpdate(
                caseId, description, notes, auth.getName(), updateType);
            return ResponseEntity.ok(progress);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Add client action required
    @PostMapping("/client-action")
    public ResponseEntity<?> addClientActionRequired(@RequestBody Map<String, Object> request, Authentication auth) {
        try {
            Long caseId = Long.valueOf(request.get("caseId").toString());
            String description = (String) request.get("description");
            String actionRequired = (String) request.get("actionRequired");
            
            LocalDateTime actionDate = null;
            if (request.get("actionDate") != null) {
                actionDate = LocalDateTime.parse((String) request.get("actionDate"));
            }
            
            CaseProgressDTO progress = progressService.addClientActionRequired(
                caseId, description, actionRequired, actionDate, auth.getName());
            return ResponseEntity.ok(progress);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Add deadline
    @PostMapping("/deadline")
    public ResponseEntity<?> addDeadline(@RequestBody Map<String, Object> request, Authentication auth) {
        try {
            Long caseId = Long.valueOf(request.get("caseId").toString());
            String description = (String) request.get("description");
            String deadlineDescription = (String) request.get("deadlineDescription");
            LocalDateTime deadlineDate = LocalDateTime.parse((String) request.get("deadlineDate"));
            
            CaseProgressDTO progress = progressService.addDeadline(
                caseId, description, deadlineDate, deadlineDescription, auth.getName());
            return ResponseEntity.ok(progress);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Add urgent notice
    @PostMapping("/urgent")
    public ResponseEntity<?> addUrgentNotice(@RequestBody Map<String, Object> request, Authentication auth) {
        try {
            Long caseId = Long.valueOf(request.get("caseId").toString());
            String description = (String) request.get("description");
            String notes = (String) request.get("notes");
            
            CaseProgressDTO progress = progressService.addUrgentNotice(
                caseId, description, notes, auth.getName());
            return ResponseEntity.ok(progress);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Get progress updates by case
    @GetMapping("/case/{caseId}")
    public ResponseEntity<List<CaseProgressDTO>> getProgressByCase(@PathVariable Long caseId) {
        List<CaseProgressDTO> progress = progressService.getProgressUpdatesByCase(caseId);
        return ResponseEntity.ok(progress);
    }

    // Get client-visible progress updates
    @GetMapping("/case/{caseId}/client-visible")
    public ResponseEntity<List<CaseProgressDTO>> getClientVisibleProgress(@PathVariable Long caseId) {
        List<CaseProgressDTO> progress = progressService.getClientVisibleProgressUpdates(caseId);
        return ResponseEntity.ok(progress);
    }

    // Get client action required items
    @GetMapping("/case/{caseId}/client-actions")
    public ResponseEntity<List<CaseProgressDTO>> getClientActionRequired(@PathVariable Long caseId) {
        List<CaseProgressDTO> actions = progressService.getClientActionRequired(caseId);
        return ResponseEntity.ok(actions);
    }

    // Get upcoming actions (next 7 days)
    @GetMapping("/upcoming-actions")
    public ResponseEntity<List<CaseProgressDTO>> getUpcomingActions() {
        List<CaseProgressDTO> actions = progressService.getUpcomingActions();
        return ResponseEntity.ok(actions);
    }

    // Get overdue actions
    @GetMapping("/overdue-actions")
    public ResponseEntity<List<CaseProgressDTO>> getOverdueActions() {
        List<CaseProgressDTO> actions = progressService.getOverdueActions();
        return ResponseEntity.ok(actions);
    }

    // Get recent updates by lawyer
    @GetMapping("/lawyer/recent")
    public ResponseEntity<List<CaseProgressDTO>> getRecentUpdatesByLawyer(
            @RequestParam(defaultValue = "7") int days, Authentication auth) {
        List<CaseProgressDTO> updates = progressService.getRecentUpdatesByLawyer(auth.getName(), days);
        return ResponseEntity.ok(updates);
    }

    // Get recent updates by client
    @GetMapping("/client/recent")
    public ResponseEntity<List<CaseProgressDTO>> getRecentUpdatesByClient(
            @RequestParam(defaultValue = "7") int days, Authentication auth) {
        List<CaseProgressDTO> updates = progressService.getRecentUpdatesByClient(auth.getName(), days);
        return ResponseEntity.ok(updates);
    }

    // Get urgent updates
    @GetMapping("/urgent")
    public ResponseEntity<List<CaseProgressDTO>> getUrgentUpdates() {
        List<CaseProgressDTO> updates = progressService.getUrgentUpdates();
        return ResponseEntity.ok(updates);
    }

    // Search progress updates
    @GetMapping("/search")
    public ResponseEntity<List<CaseProgressDTO>> searchProgressUpdates(@RequestParam String keyword) {
        List<CaseProgressDTO> updates = progressService.searchProgressUpdates(keyword);
        return ResponseEntity.ok(updates);
    }

    // Update progress visibility
    @PutMapping("/{progressId}/visibility")
    public ResponseEntity<?> updateProgressVisibility(@PathVariable Long progressId,
                                                     @RequestBody Map<String, Boolean> request) {
        try {
            Boolean isClientVisible = request.get("isClientVisible");
            CaseProgressDTO progress = progressService.updateProgressVisibility(progressId, isClientVisible);
            return ResponseEntity.ok(progress);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Mark client action as completed
    @PostMapping("/{progressId}/complete-action")
    public ResponseEntity<?> markClientActionCompleted(@PathVariable Long progressId,
                                                      @RequestBody Map<String, String> request,
                                                      Authentication auth) {
        try {
            String completionNotes = request.get("completionNotes");
            CaseProgressDTO progress = progressService.markClientActionCompleted(
                progressId, completionNotes, auth.getName());
            return ResponseEntity.ok(progress);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Get progress statistics
    @GetMapping("/statistics")
    public ResponseEntity<CaseProgressService.ProgressStatisticsDTO> getProgressStatistics() {
        CaseProgressService.ProgressStatisticsDTO stats = progressService.getProgressStatistics();
        return ResponseEntity.ok(stats);
    }

    // Get available update types
    @GetMapping("/update-types")
    public ResponseEntity<UpdateType[]> getUpdateTypes() {
        return ResponseEntity.ok(UpdateType.values());
    }

    // Bulk update progress visibility
    @PutMapping("/bulk-visibility")
    public ResponseEntity<?> bulkUpdateVisibility(@RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<Long> progressIds = (List<Long>) request.get("progressIds");
            Boolean isClientVisible = (Boolean) request.get("isClientVisible");
            
            for (Long progressId : progressIds) {
                progressService.updateProgressVisibility(progressId, isClientVisible);
            }
            
            return ResponseEntity.ok(Map.of("message", "Visibility updated for " + progressIds.size() + " items"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Get progress timeline for dashboard
    @GetMapping("/timeline")
    public ResponseEntity<List<CaseProgressDTO>> getProgressTimeline(
            @RequestParam(required = false) Long caseId,
            @RequestParam(defaultValue = "30") int days) {
        
        if (caseId != null) {
            List<CaseProgressDTO> timeline = progressService.getProgressUpdatesByCase(caseId);
            return ResponseEntity.ok(timeline);
        } else {
            // Return recent updates across all cases (you might want to implement this)
            List<CaseProgressDTO> timeline = progressService.getUpcomingActions();
            return ResponseEntity.ok(timeline);
        }
    }

    // Get action items dashboard
    @GetMapping("/action-items")
    public ResponseEntity<Map<String, Object>> getActionItemsDashboard() {
        List<CaseProgressDTO> upcomingActions = progressService.getUpcomingActions();
        List<CaseProgressDTO> overdueActions = progressService.getOverdueActions();
        List<CaseProgressDTO> urgentUpdates = progressService.getUrgentUpdates();
        
        Map<String, Object> dashboard = Map.of(
            "upcomingActions", upcomingActions,
            "overdueActions", overdueActions,
            "urgentUpdates", urgentUpdates,
            "totalActionItems", upcomingActions.size() + overdueActions.size() + urgentUpdates.size()
        );
        
        return ResponseEntity.ok(dashboard);
    }
}