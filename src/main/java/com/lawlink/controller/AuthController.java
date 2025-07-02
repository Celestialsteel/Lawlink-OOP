package com.lawlink.controller;

import com.lawlink.entity.UserEntity;
import com.lawlink.service.AuthService;
import com.lawlink.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String email = request.get("email");
            String password = request.get("password");
            String role = request.get("role");

            if (username == null || email == null || password == null || role == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Missing required fields."));
            }

            UserEntity user = new UserEntity();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);

            Map<String, String> extraDetails = new HashMap<>();
            if (role.equals("ROLE_LAWYER")) {
                extraDetails.put("speciality", request.getOrDefault("speciality", ""));
                extraDetails.put("location", request.getOrDefault("location", ""));
                extraDetails.put("university", request.getOrDefault("university", ""));
                extraDetails.put("price", request.getOrDefault("price", "0"));
            } else if (role.equals("ROLE_CLIENT")) {
                extraDetails.put("location", request.getOrDefault("location", ""));
                extraDetails.put("speciality", request.getOrDefault("speciality", ""));
            }

            UserEntity registeredUser = userService.registerUser(user, role, extraDetails);

            return ResponseEntity.ok(Map.of(
                "message", "User registered successfully",
                "username", registeredUser.getUsername()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        try {
            String identifier = credentials.get("identifier");
            String password = credentials.get("password");

            if (identifier == null || password == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Email and password are required."));
            }

            String token = userService.authenticate(identifier, password);
            return ResponseEntity.ok(Map.of("token", token));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            if (email != null) {
                userService.initiatePasswordReset(email);
            }
        } catch (RuntimeException ignored) {}
        return ResponseEntity.ok(Map.of(
            "message", "If an account exists with this email, you will receive password reset instructions."
        ));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        try {
            String token = request.get("token");
            String newPassword = request.get("newPassword");

            if (token == null || newPassword == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Missing token or new password."));
            }

            userService.resetPassword(token, newPassword);
            return ResponseEntity.ok(Map.of("message", "Password reset successful!"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/oauth2/config")
    public ResponseEntity<?> getOAuth2Config() {
        return ResponseEntity.ok(Map.of(
            "googleAuthUrl", authService.getGoogleAuthUrl(),
            "googleClientId", authService.getGoogleClientId()
        ));
    }
}
