package com.lawlink.service;

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
