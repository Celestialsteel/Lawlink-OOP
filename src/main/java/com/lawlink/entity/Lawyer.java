package com.lawlink.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "lawyer")
public class Lawyer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    private String fullName;
     private String email;
    private String specialization;
    private String location;
    private String university;
    private Double price;
    

    

    public Lawyer() {}

    public Lawyer(UserEntity user, String fullName, String specialization, String location, String university, Double price) {
        this.user = user;
        this.fullName = fullName;
        this.specialization = specialization;
        this.location = location;
        this.university = university;
        this.price = price;
    }

   
    public Long getId() { return id; }

    public UserEntity getUser() { return user; }
    public void setUser(UserEntity user) { this.user = user; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getUniversity() { return university; }
    public void setUniversity(String university) { this.university = university; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    
    
    public String getEmail() { return email; }
public void setEmail(String email) { this.email = email; }

}
