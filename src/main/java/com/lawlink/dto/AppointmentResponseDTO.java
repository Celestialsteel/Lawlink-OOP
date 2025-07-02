package com.lawlink.dto;

public class AppointmentResponseDTO {
    private Long id;
    private String status;
    private String lawyerResponseMessage;

    public AppointmentResponseDTO() {
    }

    public AppointmentResponseDTO(Long id, String status, String lawyerResponseMessage) {
        this.id = id;
        this.status = status;
        this.lawyerResponseMessage = lawyerResponseMessage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLawyerResponseMessage() {
        return lawyerResponseMessage;
    }

    public void setLawyerResponseMessage(String lawyerResponseMessage) {
        this.lawyerResponseMessage = lawyerResponseMessage;
    }
}
