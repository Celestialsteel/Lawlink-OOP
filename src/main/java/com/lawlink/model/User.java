package com.lawlink.model;

import jakarta.persistence.*;
import java.util.*;

// Abstraction: User is an abstract class defining common user behavior
@Entity
@Inheritance(strategy = InheritanceType.JOINED) // optional but clean for subclassing
@Table(name = "users")
public abstract class User {
    // Encapsulation: Fields are protected and accessed via public getters/setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    protected String name;
    protected String email;
    protected String password;

    public abstract String getRole(); // Abstraction: Subclasses must implement this

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    @ManyToMany(fetch = FetchType.EAGER)
@JoinTable(
  name = "user_roles",
  joinColumns = @JoinColumn(name = "user_id"),
  inverseJoinColumns = @JoinColumn(name = "role_id"))
private Set<Role> roles = new HashSet<>();

}
