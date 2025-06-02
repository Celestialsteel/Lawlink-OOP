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

// Inheritance: Lawyer inherits from User
@Entity
public class Lawyer extends User {
    // Encapsulation
    private String specialization;
    private boolean available = true;
    private boolean verified = false;

    @OneToMany(mappedBy = "assignedLawyer")
    private List<LegalCase> assignedCases;

    // Polymorphism: Provides specific implementation of getRole()
    @Override
    public String getRole() {
        return "Lawyer";
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public List<LegalCase> getAssignedCases() {
        return assignedCases;
    }

    public void setAssignedCases(List<LegalCase> assignedCases) {
        this.assignedCases = assignedCases;
    }
}
