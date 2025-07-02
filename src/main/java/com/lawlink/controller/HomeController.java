package com.lawlink.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String showHome() {
        return "home"; 
    }
    @GetMapping("/about")
    public String showAbout() {
        return "about"; 
    }
    @GetMapping("/account")
    public String showAccount() {
        return "account"; 
    }
    @GetMapping("/contacts")
    public String showContacts() {
        return "contacts"; 
    }
    @GetMapping("/services")
    public String showServices() {
        return "services"; 
    }
    @GetMapping("/team")
    public String showTeam() {
        return "team"; 
    }
    
}
