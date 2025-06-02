package com.lawlink.model;

import jakarta.persistence.*;
import java.util.*;

// Abstraction: User is an abstract class defining common user behavior
@MappedSuperclass
public abstract class User {
    // Encapsulation: Fields are private and accessed via public getters/setters
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String id;

    protected String name;
    protected String email;
    protected String password;

    public abstract String getRole(); // Abstraction: Subclasses must implement this

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
}
