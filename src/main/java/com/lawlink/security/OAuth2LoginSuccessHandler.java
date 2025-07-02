package com.lawlink.security;

import com.lawlink.entity.UserEntity;
import com.lawlink.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

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

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

     
        UserEntity user;
        try {
            user = userService.findByEmail(email);
        } catch (RuntimeException e) {
            user = new UserEntity();
            user.setEmail(email);
            user.setUsername(email); 
            user.setEnabled(true);
            user.setRoles(new HashSet<>());
            user.getRoles().add("ROLE_USER");
            user = userService.registerOAuth2User(user);
        }

     
        String token = jwtUtil.generateToken(user);

       
        getRedirectStrategy().sendRedirect(
            request,
            response,
            String.format("%s/oauth2/redirect?token=%s", getDefaultTargetUrl(), token)
        );
    }
}
