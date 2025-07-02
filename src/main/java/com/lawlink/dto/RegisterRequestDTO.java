/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lawlink.dto;

/**
 *
 * @author barra
 */
import java.util.Map;

public class RegisterRequestDTO {
    private String username;
    private String email;
    private String password;
    private String role;
    private Map<String, String> extraDetails;

    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Map<String, String> getExtraDetails() { return extraDetails; }
    public void setExtraDetails(Map<String, String> extraDetails) { this.extraDetails = extraDetails; }
}

