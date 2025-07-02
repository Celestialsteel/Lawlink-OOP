package com.lawlink.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
     @JoinColumn(name = "lawyer_id")
    private Lawyer lawyer;
    
  @Column(name = "lawyer_response_message", length = 1000)
private String lawyerResponseMessage;


     @Column(name = "status")
    private String status; 

    
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Client getClient() { return client; }

    public void setClient(Client client) { this.client = client; }

    public Lawyer getLawyer() { return lawyer; }

    public void setLawyer(Lawyer lawyer) { this.lawyer = lawyer; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }
   
public String getLawyerResponseMessage() {
    return lawyerResponseMessage;
}

public void setLawyerResponseMessage(String lawyerResponseMessage) {
    this.lawyerResponseMessage = lawyerResponseMessage;
}

}
