package com.lawlink.service;

import com.lawlink.dto.AppointmentDTO;
import com.lawlink.entity.Appointment;
import com.lawlink.entity.Client;
import com.lawlink.entity.Lawyer;
import com.lawlink.repository.AppointmentRepository;
import com.lawlink.repository.ClientRepository;
import com.lawlink.repository.LawyerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private LawyerRepository lawyerRepository;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Book a new appointment
     */
    public AppointmentDTO bookAppointment(AppointmentDTO appointmentDTO) {
        // Validate input
        validateAppointmentRequest(appointmentDTO);

        // Get client and lawyer entities
        Client client = clientRepository.findById(appointmentDTO.getClientId())
                .orElseThrow(() -> new IllegalArgumentException("Client not found with ID: " + appointmentDTO.getClientId()));

        Lawyer lawyer = lawyerRepository.findById(appointmentDTO.getLawyerId())
                .orElseThrow(() -> new IllegalArgumentException("Lawyer not found with ID: " + appointmentDTO.getLawyerId()));

        // Check lawyer availability
        if (!isLawyerAvailable(appointmentDTO.getLawyerId(), 
                              appointmentDTO.getAppointmentDateTime(), 
                              appointmentDTO.getDurationMinutes())) {
            throw new IllegalStateException("Lawyer is not available at the requested time");
        }

        // Check business hours
        if (!isWithinBusinessHours(appointmentDTO.getAppointmentDateTime())) {
            throw new IllegalArgumentException("Appointments can only be scheduled during business hours (9 AM - 6 PM, Monday-Friday)");
        }

        // Check minimum advance booking (at least 2 hours in advance)
        if (appointmentDTO.getAppointmentDateTime().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new IllegalArgumentException("Appointments must be booked at least 2 hours in advance");
        }

        // Create appointment entity
        Appointment appointment = new Appointment();
        appointment.setClient(client);
        appointment.setLawyer(lawyer);
        appointment.setAppointmentDateTime(appointmentDTO.getAppointmentDateTime());
        appointment.setDurationMinutes(appointmentDTO.getDurationMinutes());
        appointment.setDescription(appointmentDTO.getDescription());
        appointment.setClientNotes(appointmentDTO.getClientNotes());
        appointment.setStatus(Appointment.AppointmentStatus.PENDING);

        // Save appointment
        Appointment savedAppointment = appointmentRepository.save(appointment);

        return convertToDTO(savedAppointment);
    }

    /**
     * Get appointments for a client
     */
    @Transactional(readOnly = true)
    public List<AppointmentDTO> getClientAppointments(Long clientId) {
        List<Appointment> appointments = appointmentRepository.findByClientIdOrderByAppointmentDateTimeDesc(clientId);
        return appointments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get appointments for a lawyer
     */
    @Transactional(readOnly = true)
    public List<AppointmentDTO> getLawyerAppointments(Long lawyerId) {
        List<Appointment> appointments = appointmentRepository.findByLawyerIdOrderByAppointmentDateTimeDesc(lawyerId);
        return appointments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get upcoming appointments for a client
     */
    @Transactional(readOnly = true)
    public List<AppointmentDTO> getUpcomingClientAppointments(Long clientId) {
        List<Appointment> appointments = appointmentRepository.findUpcomingAppointmentsByClientId(clientId, LocalDateTime.now());
        return appointments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get upcoming appointments for a lawyer
     */
    @Transactional(readOnly = true)
    public List<AppointmentDTO> getUpcomingLawyerAppointments(Long lawyerId) {
        List<Appointment> appointments = appointmentRepository.findUpcomingAppointmentsByLawyerId(lawyerId, LocalDateTime.now());
        return appointments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Confirm an appointment (lawyer action)
     */
    public AppointmentDTO confirmAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with ID: " + appointmentId));

        if (appointment.getStatus() != Appointment.AppointmentStatus.PENDING) {
            throw new IllegalStateException("Only pending appointments can be confirmed");
        }

        if (appointment.getAppointmentDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Cannot confirm past appointments");
        }

        appointment.setStatus(Appointment.AppointmentStatus.CONFIRMED);
        Appointment savedAppointment = appointmentRepository.save(appointment);

        return convertToDTO(savedAppointment);
    }

    /**
     * Cancel an appointment
     */
    public AppointmentDTO cancelAppointment(Long appointmentId, String reason) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with ID: " + appointmentId));

        if (!appointment.canBeCancelled()) {
            throw new IllegalStateException("Appointment cannot be cancelled (must be at least 24 hours in advance)");
        }

        appointment.setStatus(Appointment.AppointmentStatus.CANCELLED);
        if (reason != null && !reason.trim().isEmpty()) {
            appointment.setLawyerNotes(appointment.getLawyerNotes() != null ? 
                appointment.getLawyerNotes() + "\nCancellation reason: " + reason : 
                "Cancellation reason: " + reason);
        }

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return convertToDTO(savedAppointment);
    }

    /**
     * Complete an appointment
     */
    public AppointmentDTO completeAppointment(Long appointmentId, String lawyerNotes) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with ID: " + appointmentId));

        if (appointment.getStatus() != Appointment.AppointmentStatus.CONFIRMED) {
            throw new IllegalStateException("Only confirmed appointments can be marked as completed");
        }

        appointment.setStatus(Appointment.AppointmentStatus.COMPLETED);
        if (lawyerNotes != null && !lawyerNotes.trim().isEmpty()) {
            appointment.setLawyerNotes(lawyerNotes);
        }

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return convertToDTO(savedAppointment);
    }

    /**
     * Get appointment by ID
     */
    @Transactional(readOnly = true)
    public AppointmentDTO getAppointmentById(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with ID: " + appointmentId));
        return convertToDTO(appointment);
    }

    /**
     * Check if lawyer is available at a specific time
     */
    @Transactional(readOnly = true)
    public boolean isLawyerAvailable(Long lawyerId, LocalDateTime startTime, Integer durationMinutes) {
        LocalDateTime endTime = startTime.plusMinutes(durationMinutes);
        List<Appointment> conflictingAppointments = appointmentRepository.findConflictingAppointments(
                lawyerId, startTime, endTime);
        return conflictingAppointments.isEmpty();
    }

    /**
     * Get lawyer's schedule for a specific date
     */
    @Transactional(readOnly = true)
    public List<AppointmentDTO> getLawyerSchedule(Long lawyerId, LocalDateTime date) {
        List<Appointment> appointments = appointmentRepository.findAppointmentsByLawyerAndDate(lawyerId, date);
        return appointments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update appointment status automatically (scheduled task)
     */
    public void updateAppointmentStatuses() {
        LocalDateTime now = LocalDateTime.now();
        
        // Mark confirmed appointments as completed if they're past their end time
        List<Appointment> toComplete = appointmentRepository.findAppointmentsToMarkCompleted(now.minusMinutes(30));
        for (Appointment appointment : toComplete) {
            appointment.setStatus(Appointment.AppointmentStatus.COMPLETED);
            appointmentRepository.save(appointment);
        }

        // Mark pending appointments as no-show if they're past their start time
        List<Appointment> toMarkNoShow = appointmentRepository.findAppointmentsToMarkNoShow(now.minusMinutes(15));
        for (Appointment appointment : toMarkNoShow) {
            appointment.setStatus(Appointment.AppointmentStatus.NO_SHOW);
            appointmentRepository.save(appointment);
        }
    }

    // Private helper methods

    private void validateAppointmentRequest(AppointmentDTO appointmentDTO) {
        if (appointmentDTO.getClientId() == null) {
            throw new IllegalArgumentException("Client ID is required");
        }
        if (appointmentDTO.getLawyerId() == null) {
            throw new IllegalArgumentException("Lawyer ID is required");
        }
        if (appointmentDTO.getAppointmentDateTime() == null) {
            throw new IllegalArgumentException("Appointment date and time is required");
        }
        if (appointmentDTO.getDurationMinutes() == null || appointmentDTO.getDurationMinutes() < 15) {
            throw new IllegalArgumentException("Duration must be at least 15 minutes");
        }
        if (appointmentDTO.getDurationMinutes() > 480) {
            throw new IllegalArgumentException("Duration cannot exceed 8 hours");
        }
        if (appointmentDTO.getAppointmentDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot schedule appointments in the past");
        }
    }

    private boolean isWithinBusinessHours(LocalDateTime dateTime) {
        // Business hours: Monday-Friday, 9 AM - 6 PM
        int dayOfWeek = dateTime.getDayOfWeek().getValue();
        if (dayOfWeek > 5) { // Saturday = 6, Sunday = 7
            return false;
        }

        LocalTime time = dateTime.toLocalTime();
        LocalTime businessStart = LocalTime.of(9, 0);
        LocalTime businessEnd = LocalTime.of(18, 0);

        return !time.isBefore(businessStart) && !time.isAfter(businessEnd);
    }

    private AppointmentDTO convertToDTO(Appointment appointment) {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(appointment.getId());
        dto.setClientId(appointment.getClient().getId());
        dto.setLawyerId(appointment.getLawyer().getId());
        dto.setAppointmentDateTime(appointment.getAppointmentDateTime());
        dto.setDurationMinutes(appointment.getDurationMinutes());
        dto.setStatus(appointment.getStatus().name());
        dto.setDescription(appointment.getDescription());
        dto.setClientNotes(appointment.getClientNotes());
        dto.setLawyerNotes(appointment.getLawyerNotes());
        dto.setCreatedAt(appointment.getCreatedAt() != null ? appointment.getCreatedAt().format(DATE_TIME_FORMATTER) : null);
        dto.setUpdatedAt(appointment.getUpdatedAt() != null ? appointment.getUpdatedAt().format(DATE_TIME_FORMATTER) : null);

        // Set additional fields
        dto.setClientName(appointment.getClient().getFullName());
        dto.setClientEmail(appointment.getClient().getEmail());
        dto.setLawyerName(appointment.getLawyer().getFullName());
        dto.setLawyerEmail(appointment.getLawyer().getEmail());
        dto.setLawyerSpecialization(appointment.getLawyer().getSpecialization());

        return dto;
    }
}