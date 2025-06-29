package com.lawlink.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class AppointmentDTO {
    private Long id;

    @NotNull(message = "Client ID is required")
    private Long clientId;

    @NotNull(message = "Lawyer ID is required")
    private Long lawyerId;

    @NotNull(message = "Appointment date and time is required")
    @Future(message = "Appointment must be scheduled for a future date and time")
    private LocalDateTime appointmentDateTime;

    @NotNull(message = "Duration is required")
    @Min(value = 15, message = "Minimum appointment duration is 15 minutes")
    @Max(value = 480, message = "Maximum appointment duration is 8 hours")
    private Integer durationMinutes;

    private String status;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @Size(max = 500, message = "Client notes cannot exceed 500 characters")
    private String clientNotes;

    @Size(max = 500, message = "Lawyer notes cannot exceed 500 characters")
    private String lawyerNotes;

    private String createdAt;
    private String updatedAt;

    // Additional fields for response
    private String clientName;
    private String clientEmail;
    private String lawyerName;
    private String lawyerEmail;
    private String lawyerSpecialization;

    // Constructors
    public AppointmentDTO() {}

    public AppointmentDTO(Long clientId, Long lawyerId, LocalDateTime appointmentDateTime, 
                         Integer durationMinutes, String description) {
        this.clientId = clientId;
        this.lawyerId = lawyerId;
        this.appointmentDateTime = appointmentDateTime;
        this.durationMinutes = durationMinutes;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getLawyerId() {
        return lawyerId;
    }

    public void setLawyerId(Long lawyerId) {
        this.lawyerId = lawyerId;
    }

    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClientNotes() {
        return clientNotes;
    }

    public void setClientNotes(String clientNotes) {
        this.clientNotes = clientNotes;
    }

    public String getLawyerNotes() {
        return lawyerNotes;
    }

    public void setLawyerNotes(String lawyerNotes) {
        this.lawyerNotes = lawyerNotes;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getLawyerName() {
        return lawyerName;
    }

    public void setLawyerName(String lawyerName) {
        this.lawyerName = lawyerName;
    }

    public String getLawyerEmail() {
        return lawyerEmail;
    }

    public void setLawyerEmail(String lawyerEmail) {
        this.lawyerEmail = lawyerEmail;
    }

    public String getLawyerSpecialization() {
        return lawyerSpecialization;
    }

    public void setLawyerSpecialization(String lawyerSpecialization) {
        this.lawyerSpecialization = lawyerSpecialization;
    }

    // Helper methods
    public LocalDateTime getAppointmentEndTime() {
        if (appointmentDateTime != null && durationMinutes != null) {
            return appointmentDateTime.plusMinutes(durationMinutes);
        }
        return null;
    }

    public boolean isUpcoming() {
        return appointmentDateTime != null && 
               appointmentDateTime.isAfter(LocalDateTime.now()) &&
               ("PENDING".equals(status) || "CONFIRMED".equals(status));
    }

    public boolean isPast() {
        return appointmentDateTime != null && 
               appointmentDateTime.isBefore(LocalDateTime.now());
    }
}