package com.lawlink.security;

import com.lawlink.entity.UserEntity;
import com.lawlink.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {
        
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        
        // Extract user information from OAuth2User
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        
        // Check if user exists, if not create new user
        UserEntity user;
        try {
            user = userService.findByEmail(email);
        } catch (RuntimeException e) {
            // Create new user
            user = new UserEntity();
            user.setEmail(email);
            user.setUsername(email); // Use email as username
            user.setEnabled(true); // OAuth2 users are pre-verified
            user.setRoles(new HashSet<>());
            user.getRoles().add("ROLE_USER");
            user = userService.registerOAuth2User(user);
        }
        
        // Generate JWT token
        String token = jwtUtil.generateToken(user.getUsername());
        
        // Redirect to frontend with token
        getRedirectStrategy().sendRedirect(request, response, 
            String.format("%s/oauth2/redirect?token=%s", getDefaultTargetUrl(), token));
    }
}
