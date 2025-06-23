/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lawlink.model;

/**
 *
 * @author barra
 */
import jakarta.persistence.*;
import java.util.*;
@Entity
@Table(name = "user_roles")
public class UserRole {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // ✅ Ensure this matches `User`'s PK type
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false) // ✅ Ensure this matches `Role`'s PK type
    private Role role;
     public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
