/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lawlink.model;
import jakarta.persistence.*;
import java.util.*;

/**
 *
 * @author barra
 */
@Entity
public class LegalCase {
      // Encapsulation
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String caseId;

    private String title;
    private String status = "Open";

    @ManyToOne
    private Lawyer assignedLawyer;

    @ManyToOne
    private Client client;

    @OneToMany(mappedBy = "relatedCase", cascade = CascadeType.ALL)
    private List<Document> documents = new ArrayList<>();

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Lawyer getAssignedLawyer() {
        return assignedLawyer;
    }

    public void setAssignedLawyer(Lawyer assignedLawyer) {
        this.assignedLawyer = assignedLawyer;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }
}
