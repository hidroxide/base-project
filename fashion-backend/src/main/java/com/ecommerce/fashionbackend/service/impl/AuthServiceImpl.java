package com.ecommerce.fashionbackend.service.impl;

import com.ecommerce.fashionbackend.constant.TokenType;
import com.ecommerce.fashionbackend.dto.request.*;
import com.ecommerce.fashionbackend.dto.response.AuthResponse;
import com.ecommerce.fashionbackend.dto.response.IntrospectResponse;
import com.ecommerce.fashionbackend.entity.User;
import com.ecommerce.fashionbackend.repository.UserRepository;
import com.ecommerce.fashionbackend.service.*;
import io.micrometer.common.util.StringUtils;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final RedisTokenService redisTokenService;

    private final EmailService emailService;

private final UserRepository userRepository;
    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()));
        User user = (User) authentication.getPrincipal();
        return generateToken(user);
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        if (StringUtils.isBlank(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
        String username = jwtService.extractUsername(refreshToken, TokenType.REFRESH);
        User user = userRepository.findByEmail(username).orElseThrow(() -> new IllegalArgumentException("Invalid username"));
        if (!jwtService.validateToken(refreshToken, TokenType.REFRESH, user)) {
            throw new IllegalArgumentException("Refresh token is invalid or expired");
        }

        String redisRefreshToken = redisTokenService.getRefreshToken(user.getId());
        if (!redisRefreshToken.equals(refreshToken)) {
            throw new IllegalArgumentException("Refresh token is invalid or expired");
        }

        String accessToken = jwtService.generateAccessToken(user);
        redisTokenService.updateAccessToken(user.getId(), accessToken);
        return AuthResponse
                .builder()
                .userId(user.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void logout(LogoutRequest logoutRequest) {
        String accessToken = logoutRequest.getAccessToken();;
        if (StringUtils.isBlank(accessToken)) {
            throw new IllegalArgumentException("Invalid token");
        }
        String username = jwtService.extractUsername(accessToken, TokenType.ACCESS);
        User user = userRepository.findByEmail(username).orElseThrow(() -> new IllegalArgumentException("Invalid username"));
        if (!jwtService.validateToken(accessToken, TokenType.ACCESS, user)) {
            throw new IllegalArgumentException("Refresh token is invalid or expired");
        }
        redisTokenService.deleteToken(user.getId());
        SecurityContextHolder.clearContext();
    }

    @Override
    public IntrospectResponse introspectToken(IntrospectRequest introspectRequest) {
        String accessToken = introspectRequest.getAccessToken();
        if (StringUtils.isBlank(accessToken)) {
            throw new IllegalArgumentException("Invalid token");
        }
        boolean isValid = true;
        try {
            String username = jwtService.extractUsername(accessToken, TokenType.ACCESS);
            User user = userRepository.findByEmail(username).orElseThrow(() -> new IllegalArgumentException("Invalid username"));
            isValid = jwtService.validateToken(accessToken, TokenType.ACCESS, user);
        } catch (Exception e) {
            isValid = false;
        }
        return IntrospectResponse
                .builder()
                .isValid(isValid)
                .build();
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest forgotPasswordRequest) throws MessagingException {
        User user = userRepository.findByEmail(forgotPasswordRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email"));
        String newPassword = generateRandomPassword();
        user.setPassword(newPassword);
        userRepository.save(user);

        emailService.sendEmail(user.getEmail(), "New password", "Your new password is: " + newPassword);
    }

//    other methods
    private AuthResponse generateToken(User user) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        redisTokenService.saveToken(user.getId(), accessToken, refreshToken);
        return AuthResponse.builder()
                .userId(user.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String generateRandomPassword() {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        SecureRandom random = new SecureRandom();
        int LENGTH_PASSWORD = 12;

        for (int i = 0; i < LENGTH_PASSWORD; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(randomIndex));
        }
        return password.toString();
    }
}
