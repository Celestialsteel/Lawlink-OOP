/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lawlink.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author barra
 */
@RestController  // 🔴 This was missing
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "Lawlink Backend is Running!";
    }

    @GetMapping("/api/test")
    public String testApi() {
        return "API is working!";
    } 
}
