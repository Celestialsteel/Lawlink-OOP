package com.lawlink.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String showHome() {
        return "home"; // This looks for templates/home.html
    }
    @GetMapping("/about")
    public String showAbout() {
        return "about"; // This looks for templates/home.html
    }
    @GetMapping("/account")
    public String showAccount() {
        return "account"; // This looks for templates/home.html
    }
    @GetMapping("/contacts")
    public String showContacts() {
        return "contacts"; // This looks for templates/home.html
    }
    @GetMapping("/services")
    public String showServices() {
        return "services"; // This looks for templates/home.html
    }
    @GetMapping("/team")
    public String showTeam() {
        return "team"; // This looks for templates/home.html
    }
    
}
