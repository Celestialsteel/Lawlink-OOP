/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lawlink.controller;

/**
 *
 * @author barra
 */

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class RoleBasedViewController {
    
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/client")
    public String clientDashboard() {
        return "Welcome to the Client Dashboard";
    }

    @PreAuthorize("hasRole('LAWYER')")
    @GetMapping("/lawyer")
    public String lawyerDashboard() {
        return "Welcome to the Lawyer Dashboard";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String adminDashboard() {
        return "Welcome to the Admin Dashboard";
    }
}
