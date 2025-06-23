package com.lawlink.service;

import com.lawlink.entity.UserEntity;
import com.lawlink.repository.UserRepository;
import com.lawlink.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;

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

    @Value("${app.security.reset-token-expiration:3600}")
    private long resetTokenExpiration;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public UserEntity registerUser(UserEntity user) {
        System.out.println(">>> REGISTERING: " + user.getUsername()); // ADD THIS

    // rest of your logic
    System.out.println("Registering user: " + user.getUsername() + ", email: " + user.getEmail());
    System.out.println("Saving user: " + user.getUsername() + ", " + user.getEmail());


    if (userRepository.existsByUsername(user.getUsername())) {
        System.out.println("Username already exists.");
        throw new RuntimeException("Username already exists");
    }

    if (userRepository.existsByEmail(user.getEmail())) {
        System.out.println("Email already exists.");
        throw new RuntimeException("Email already exists");
    }

    user.setPassword(passwordEncoder.encode(user.getPassword()));
    if (user.getRoles() == null || user.getRoles().isEmpty()) {
        user.setRoles(new HashSet<>());
        user.getRoles().add("ROLE_USER");
    }

    String token = UUID.randomUUID().toString();
    user.setVerificationToken(token);
    user.setVerificationTokenExpiry(new Date(System.currentTimeMillis() + verificationTokenExpiration));
    user.setEnabled(false);

    UserEntity saved = userRepository.save(user);
    System.out.println("User saved with ID: " + saved.getId());

    return saved;
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
        user.setPasswordResetTokenExpiry(new Date(System.currentTimeMillis() + resetTokenExpiration * 1000L));
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
                user.getUsername(), resetUrl
            );

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

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", new ArrayList<>(user.getRoles()));

        return jwtUtil.generateTokenWithClaims(username, claims);
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
        user.setEnabled(true);
        return userRepository.save(user);
    }
}
