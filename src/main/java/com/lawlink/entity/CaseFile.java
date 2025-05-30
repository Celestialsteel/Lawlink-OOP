package com.lawlink.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinColumn;
import java.time.LocalDate;

@Entity
public class CaseFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String status; // Open, In-Progress, Closed
    private LocalDate creationDate;

    @ManyToOne
    private Client client;

    @ManyToOne
    private Lawyer lawyer;

    // Getters and Setters
}
