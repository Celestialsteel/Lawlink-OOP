package com.lawlink.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Lawyer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String email;
    private String specialization;

    @OneToMany(mappedBy = "lawyer", cascade = CascadeType.ALL)
    private List<CaseEntity> cases;

    public Lawyer() {}

    public Lawyer(String fullName, String email, String specialization) {
        this.fullName = fullName;
        this.email = email;
        this.specialization = specialization;
    }

    // Getters and Setters

    public Long getId() { return id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public List<CaseEntity> getCases() { return cases; }
    public void setCases(List<CaseEntity> cases) { this.cases = cases; }
}
