package com.lawlink.service;

import com.lawlink.entity.UserEntity;
import com.lawlink.repository.UserRepository;
import com.lawlink.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.UUID;
import java.util.HashSet;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    @Value("${app.security.verification-token-expiration}")
    private long verificationTokenExpiration;

    @Value("${app.security.reset-token-expiration}")
    private long resetTokenExpiration;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public UserEntity registerUser(UserEntity user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Set default role if none provided
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(new HashSet<>());
            user.getRoles().add("ROLE_USER");
        }

        // Generate verification token
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        user.setVerificationTokenExpiry(new Date(System.currentTimeMillis() + verificationTokenExpiration));
        user.setEnabled(false);

        UserEntity savedUser = userRepository.save(user);

        // Send verification email
        try {
            String verificationUrl = frontendUrl + "/verify?token=" + token;
            String emailContent = String.format(
                "Hello %s,<br><br>" +
                "Please verify your email by clicking on the link below:<br>" +
                "<a href='%s'>Verify Email</a><br><br>" +
                "This link will expire in 1 hour.<br><br>" +
                "Best regards,<br>Lawlink Team",
                user.getUsername(), verificationUrl);
            
            emailService.sendEmail(user.getEmail(), "Verify your email", emailContent);
        } catch (Exception e) {
            // Log the error but don't prevent user registration
            System.err.println("Failed to send verification email: " + e.getMessage());
        }

        return savedUser;
    }

    public void verifyEmail(String token) {
        UserEntity user = userRepository.findByVerificationToken(token)
            .orElseThrow(() -> new RuntimeException("Invalid verification token"));

        if (user.getVerificationTokenExpiry().before(new Date())) {
            throw new RuntimeException("Verification token has expired");
        }

        user.setEnabled(true);
        user.setVerificationToken(null);
        user.setVerificationTokenExpiry(null);
        userRepository.save(user);
    }

    public void initiatePasswordReset(String email) {
        UserEntity user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        String token = UUID.randomUUID().toString();
        user.setPasswordResetToken(token);
        user.setPasswordResetTokenExpiry(new Date(System.currentTimeMillis() + resetTokenExpiration));
        userRepository.save(user);

        try {
            String resetUrl = frontendUrl + "/reset-password?token=" + token;
            String emailContent = String.format(
                "Hello %s,<br><br>" +
                "You have requested to reset your password. Click the link below to proceed:<br>" +
                "<a href='%s'>Reset Password</a><br><br>" +
                "This link will expire in 15 minutes.<br>" +
                "If you didn't request this, please ignore this email.<br><br>" +
                "Best regards,<br>Lawlink Team",
                user.getUsername(), resetUrl);
            
            emailService.sendEmail(user.getEmail(), "Reset your password", emailContent);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send password reset email");
        }
    }

    public void resetPassword(String token, String newPassword) {
        UserEntity user = userRepository.findByPasswordResetToken(token)
            .orElseThrow(() -> new RuntimeException("Invalid password reset token"));

        if (user.getPasswordResetTokenExpiry().before(new Date())) {
            throw new RuntimeException("Password reset token has expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null);
        user.setPasswordResetTokenExpiry(null);
        userRepository.save(user);
    }

    public String authenticate(String username, String password) {
        UserEntity user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return jwtUtil.generateToken(username);
    }

    public UserEntity getCurrentUser(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserEntity registerOAuth2User(UserEntity user) {
        // For OAuth2 users, we skip password and email verification
        user.setEnabled(true);
        return userRepository.save(user);
    }
}
