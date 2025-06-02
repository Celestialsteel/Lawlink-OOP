package com.lawlink.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class CaseNote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String note;
    private LocalDateTime createdAt;

    @ManyToOne
    private CaseEntity caseEntity;

    public CaseNote() {}

    public CaseNote(String note, LocalDateTime createdAt, CaseEntity caseEntity) {
        this.note = note;
        this.createdAt = createdAt;
        this.caseEntity = caseEntity;
    }

    // Getters and Setters

    public Long getId() { return id; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public CaseEntity getCaseEntity() { return caseEntity; }
    public void setCaseEntity(CaseEntity caseEntity) { this.caseEntity = caseEntity; }
}
