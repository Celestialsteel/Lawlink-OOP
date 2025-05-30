package com.lawlink.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinColumn;
import java.util.List;

@Entity
public class Lawyer extends User {
    private String specialization;
    private boolean available;

    @OneToMany(mappedBy = "lawyer")
    private List<CaseFile> assignedCases;

    // Getters and Setters
}
