/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lawlink.controller;

/**
 *
 * @author barra
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class Dashboards {
    
    @GetMapping("/lawyerdashboard")
    public String lawyerdashboard() {
        return "lawyerdashboard"; 
    }

    @GetMapping("/clientdashboard")
    public String clientdashboard() {
        return "clientdashboard";
    }
    @GetMapping("/admindashboard")
    public String admindashboard() {
        return "admindashboard";
    }
}
