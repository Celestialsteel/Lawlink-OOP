package com.lawlink.controller;

import com.lawlink.entity.UserEntity;
import com.lawlink.service.AuthService;
import com.lawlink.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")  // You may want to restrict this in production
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    // Registration Endpoint
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserEntity user) {
        try {
            UserEntity registeredUser = userService.registerUser(user);
            return ResponseEntity.ok(Map.of(
                "message", "User registered successfully",
                "username", registeredUser.getUsername()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Login Endpoint
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        try {
            String token = userService.authenticate(
                credentials.get("username"),
                credentials.get("password")
            );
            return ResponseEntity.ok(Map.of("token", token));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Email Verification Endpoint
    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        try {
            userService.verifyEmail(token);
            return ResponseEntity.ok(Map.of("message", "Email verified successfully!"));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Forgot Password Endpoint
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        try {
            userService.initiatePasswordReset(request.get("email"));
        } catch (RuntimeException ignored) {
            // Silent failure is intentional for security
        }
        return ResponseEntity.ok(Map.of(
            "message", "If an account exists with this email, you will receive password reset instructions."
        ));
    }

    // Reset Password Endpoint
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        try {
            userService.resetPassword(
                request.get("token"),
                request.get("newPassword")
            );
            return ResponseEntity.ok(Map.of("message", "Password reset successful!"));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Google OAuth2 Configuration (for frontend use)
    @GetMapping("/oauth2/config")
    public ResponseEntity<?> getOAuth2Config() {
        return ResponseEntity.ok(Map.of(
            "googleAuthUrl", authService.getGoogleAuthUrl(),
            "googleClientId", authService.getGoogleClientId()
        ));
    }
}
