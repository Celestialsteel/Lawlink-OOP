package com.lawlink.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class CaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String caseNumber;
    private String title;
    private String status;
    private LocalDate openDate;
    private LocalDate closeDate;

    @ManyToOne
    private Client client;

    @ManyToOne
    private Lawyer lawyer;

    @OneToMany(mappedBy = "caseEntity", cascade = CascadeType.ALL)
    private List<Document> documents;

    @OneToMany(mappedBy = "caseEntity", cascade = CascadeType.ALL)
    private List<CaseNote> notes;

    public CaseEntity() {}

    public CaseEntity(String caseNumber, String title, String status, LocalDate openDate, LocalDate closeDate, Client client, Lawyer lawyer) {
        this.caseNumber = caseNumber;
        this.title = title;
        this.status = status;
        this.openDate = openDate;
        this.closeDate = closeDate;
        this.client = client;
        this.lawyer = lawyer;
    }

    // Getters and Setters

    public Long getId() { return id; }
    public String getCaseNumber() { return caseNumber; }
    public void setCaseNumber(String caseNumber) { this.caseNumber = caseNumber; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getOpenDate() { return openDate; }
    public void setOpenDate(LocalDate openDate) { this.openDate = openDate; }

    public LocalDate getCloseDate() { return closeDate; }
    public void setCloseDate(LocalDate closeDate) { this.closeDate = closeDate; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public Lawyer getLawyer() { return lawyer; }
    public void setLawyer(Lawyer lawyer) { this.lawyer = lawyer; }

    public List<Document> getDocuments() { return documents; }
    public void setDocuments(List<Document> documents) { this.documents = documents; }

    public List<CaseNote> getNotes() { return notes; }
    public void setNotes(List<CaseNote> notes) { this.notes = notes; }
}
