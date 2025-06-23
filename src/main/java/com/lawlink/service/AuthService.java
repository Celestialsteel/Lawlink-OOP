/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lawlink.service;

/**
 *
 * @author barra
 */
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    public String getGoogleAuthUrl() {
        return "/oauth2/authorization/google";
    }

    public String getGoogleClientId() {
        return googleClientId;
    }
}
