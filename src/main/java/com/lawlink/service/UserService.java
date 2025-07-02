package com.lawlink.service;

import com.lawlink.entity.*;
import com.lawlink.repository.*;
import com.lawlink.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LawyerRepository lawyerRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

  
    public UserEntity registerUser(UserEntity user, String role, Map<String, String> extraDetails) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.setRoles(Set.of(role));
        UserEntity savedUser = userRepository.save(user);

        if ("ROLE_LAWYER".equals(role)) {
            Lawyer lawyer = new Lawyer();
            lawyer.setUser(savedUser);
            lawyer.setFullName(user.getUsername());
            lawyer.setEmail(user.getEmail());
            lawyer.setSpecialization(extraDetails.get("speciality"));
            lawyer.setLocation(extraDetails.get("location"));
            lawyer.setUniversity(extraDetails.get("university"));
            lawyer.setPrice(Double.parseDouble(extraDetails.get("price")));
            lawyerRepository.save(lawyer);
        } else if ("ROLE_CLIENT".equals(role)) {
            Client client = new Client();
            client.setUser(savedUser);
            client.setFullName(user.getUsername());
            client.setEmail(user.getEmail());
            client.setLocation(extraDetails.get("location"));
            client.setSpecializationNeed(extraDetails.get("speciality"));
            clientRepository.save(client);
        }

        return savedUser;
    }

 
    public String authenticate(String identifier, String password) {
        UserEntity user = userRepository.findByEmail(identifier)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return jwtUtil.generateToken(user); 
    }

    public void initiatePasswordReset(String email) {
        Optional<UserEntity> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            String resetToken = UUID.randomUUID().toString();
            user.setPasswordResetToken(resetToken);
            user.setPasswordResetTokenExpiry(new Date(System.currentTimeMillis() + 15 * 60 * 1000)); 
            userRepository.save(user);

            System.out.println("Password reset token (mock): " + resetToken);
        }
    }

    public void resetPassword(String token, String newPassword) {
        UserEntity user = userRepository.findByPasswordResetToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired reset token"));

        if (user.getPasswordResetTokenExpiry() == null || user.getPasswordResetTokenExpiry().before(new Date())) {
            throw new RuntimeException("Reset token has expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null);
        user.setPasswordResetTokenExpiry(null);
        userRepository.save(user);
    }

    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public UserEntity registerOAuth2User(UserEntity user) {
        user.setEnabled(true);
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(Set.of("ROLE_USER"));
        }
        return userRepository.save(user);
    }

    public Optional<UserEntity> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<UserEntity> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
