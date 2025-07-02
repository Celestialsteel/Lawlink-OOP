package com.lawlink.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String email;

    private String location;               
    private String specializationNeed;  

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

   
    

    public Client() {}

    public Client(String fullName, String email, String location, String specializationNeed) {
        this.fullName = fullName;
        this.email = email;
        this.location = location;
        this.specializationNeed = specializationNeed;
    }


    public Long getId() { return id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getSpecializationNeed() { return specializationNeed; }
    public void setSpecializationNeed(String specializationNeed) { this.specializationNeed = specializationNeed; }

    public UserEntity getUser() { return user; }
    public void setUser(UserEntity user) { this.user = user; }

   
}
