package com.lawlink.controller;

import com.lawlink.dto.AppointmentDTO;
import com.lawlink.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    // Book a new appointment
    @PostMapping("/book")
    public ResponseEntity<?> bookAppointment(@Valid @RequestBody AppointmentDTO appointmentDTO) {
        try {
            AppointmentDTO bookedAppointment = appointmentService.bookAppointment(appointmentDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(bookedAppointment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred while booking the appointment"));
        }
    }

    // Get all appointments for a client
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<AppointmentDTO>> getClientAppointments(@PathVariable Long clientId) {
        try {
            List<AppointmentDTO> appointments = appointmentService.getClientAppointments(clientId);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get all appointments for a lawyer
    @GetMapping("/lawyer/{lawyerId}")
    public ResponseEntity<List<AppointmentDTO>> getLawyerAppointments(@PathVariable Long lawyerId) {
        try {
            List<AppointmentDTO> appointments = appointmentService.getLawyerAppointments(lawyerId);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get upcoming appointments for a client
    @GetMapping("/client/{clientId}/upcoming")
    public ResponseEntity<List<AppointmentDTO>> getUpcomingClientAppointments(@PathVariable Long clientId) {
        try {
            List<AppointmentDTO> appointments = appointmentService.getUpcomingClientAppointments(clientId);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get upcoming appointments for a lawyer
    @GetMapping("/lawyer/{lawyerId}/upcoming")
    public ResponseEntity<List<AppointmentDTO>> getUpcomingLawyerAppointments(@PathVariable Long lawyerId) {
        try {
            List<AppointmentDTO> appointments = appointmentService.getUpcomingLawyerAppointments(lawyerId);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get appointment by ID
    @GetMapping("/{appointmentId}")
    public ResponseEntity<?> getAppointment(@PathVariable Long appointmentId) {
        try {
            AppointmentDTO appointment = appointmentService.getAppointmentById(appointmentId);
            return ResponseEntity.ok(appointment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Confirm an appointment (lawyer action)
    @PutMapping("/{appointmentId}/confirm")
    public ResponseEntity<?> confirmAppointment(@PathVariable Long appointmentId) {
        try {
            AppointmentDTO confirmedAppointment = appointmentService.confirmAppointment(appointmentId);
            return ResponseEntity.ok(confirmedAppointment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred while confirming the appointment"));
        }
    }

    // Cancel an appointment
    @PutMapping("/{appointmentId}/cancel")
    public ResponseEntity<?> cancelAppointment(@PathVariable Long appointmentId, 
                                             @RequestBody(required = false) Map<String, String> requestBody) {
        try {
            String reason = requestBody != null ? requestBody.get("reason") : null;
            AppointmentDTO cancelledAppointment = appointmentService.cancelAppointment(appointmentId, reason);
            return ResponseEntity.ok(cancelledAppointment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred while cancelling the appointment"));
        }
    }

    // Complete an appointment (lawyer action)
    @PutMapping("/{appointmentId}/complete")
    public ResponseEntity<?> completeAppointment(@PathVariable Long appointmentId,
                                               @RequestBody(required = false) Map<String, String> requestBody) {
        try {
            String lawyerNotes = requestBody != null ? requestBody.get("lawyerNotes") : null;
            AppointmentDTO completedAppointment = appointmentService.completeAppointment(appointmentId, lawyerNotes);
            return ResponseEntity.ok(completedAppointment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred while completing the appointment"));
        }
    }

    // Check lawyer availability
    @GetMapping("/lawyer/{lawyerId}/availability")
    public ResponseEntity<?> checkLawyerAvailability(
            @PathVariable Long lawyerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam Integer durationMinutes) {
        try {
            boolean isAvailable = appointmentService.isLawyerAvailable(lawyerId, startTime, durationMinutes);
            return ResponseEntity.ok(Map.of(
                "available", isAvailable,
                "lawyerId", lawyerId,
                "startTime", startTime,
                "endTime", startTime.plusMinutes(durationMinutes),
                "durationMinutes", durationMinutes
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An error occurred while checking availability"));
        }
    }

    // Get lawyer's schedule for a specific date
    @GetMapping("/lawyer/{lawyerId}/schedule")
    public ResponseEntity<?> getLawyerSchedule(
            @PathVariable Long lawyerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime date) {
        try {
            List<AppointmentDTO> schedule = appointmentService.getLawyerSchedule(lawyerId, date);
            return ResponseEntity.ok(Map.of(
                "lawyerId", lawyerId,
                "date", date.toLocalDate(),
                "appointments", schedule
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An error occurred while retrieving the schedule"));
        }
    }

    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "AppointmentService",
            "timestamp", LocalDateTime.now().toString()
        ));
    }
}