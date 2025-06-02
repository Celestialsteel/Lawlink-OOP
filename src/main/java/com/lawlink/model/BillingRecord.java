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
public class BillingRecord {
      // Encapsulation
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    private LegalCase legalCase;

    private double totalBilled;

    public void addBillableHours(double hours, double ratePerHour) {
        this.totalBilled += hours * ratePerHour;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LegalCase getLegalCase() {
        return legalCase;
    }

    public void setLegalCase(LegalCase legalCase) {
        this.legalCase = legalCase;
    }

    public double getTotalBilled() {
        return totalBilled;
    }

    public void setTotalBilled(double totalBilled) {
        this.totalBilled = totalBilled;
    }
}
