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
// Inheritance: Client inherits from User
@Entity
public class Client extends User {
    @OneToMany(mappedBy = "client")
    private List<LegalCase> cases;

    // Polymorphism: Provides specific implementation of getRole()
    @Override
    public String getRole() {
        return "Client";
    }

    public List<LegalCase> getCases() {
        return cases;
    }

    public void setCases(List<LegalCase> cases) {
        this.cases = cases;
    }
}
