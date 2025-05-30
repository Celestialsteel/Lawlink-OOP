// FILE: Client.java
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
public class Client extends User {
    private String contactInfo;

    @OneToMany(mappedBy = "client")
    private List<CaseFile> cases;

    // Getters and Setters
}