package com.lawlink.dto;

public class AppointmentRequestDTO {
    private Long lawyerId;
 private String clientMessage; 
    public AppointmentRequestDTO() {
    }

    public AppointmentRequestDTO(Long lawyerId) {
        this.lawyerId = lawyerId;
    }

    public Long getLawyerId() {
        return lawyerId;
    }

    public void setLawyerId(Long lawyerId) {
        this.lawyerId = lawyerId;
    }
   

    public String getClientMessage() {
        return clientMessage;
    }

    public void setClientMessage(String clientMessage) {
        this.clientMessage = clientMessage;
    }
}
