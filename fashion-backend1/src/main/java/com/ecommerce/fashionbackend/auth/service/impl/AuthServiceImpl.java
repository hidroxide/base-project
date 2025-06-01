package com.ecommerce.fashionbackend.auth.service.impl;

import com.ecommerce.fashionbackend.auth.dto.request.AccessTokenRequest;
import com.ecommerce.fashionbackend.auth.dto.request.ForgotPasswordRequest;
import com.ecommerce.fashionbackend.auth.dto.request.LoginRequest;
import com.ecommerce.fashionbackend.auth.dto.request.RefreshTokenRequest;
import com.ecommerce.fashionbackend.auth.dto.response.AuthResponse;
import com.ecommerce.fashionbackend.auth.dto.response.IntrospectResponse;
import com.ecommerce.fashionbackend.auth.service.AuthService;
import com.ecommerce.fashionbackend.common.constant.TokenType;
import com.ecommerce.fashionbackend.common.service.MailService;
import com.ecommerce.fashionbackend.security.service.JwtService;
import com.ecommerce.fashionbackend.security.service.RedisTokenService;
import com.ecommerce.fashionbackend.user.entity.User;
import com.ecommerce.fashionbackend.user.repository.UserRepository;
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

    private final MailService mailService;

    private final UserRepository userRepository;

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword())
        );
        User user = (User) authentication.getPrincipal();
        return generateToken(user);
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        if (StringUtils.isBlank(refreshToken)) {
            throw new IllegalArgumentException("Refresh token is blank");
        }
        String email = jwtService.extractEmail(refreshToken, TokenType.REFRESH_TOKEN);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!jwtService.validateToken(refreshToken, TokenType.REFRESH_TOKEN, user)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
        String redisRefreshToken = redisTokenService.getRefreshToken(user.getId());
        if (!redisRefreshToken.equals(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
        String newAccessToken = jwtService.generateAccessToken(user);
        redisTokenService.updateAccessToken(user.getId(), newAccessToken);
        return AuthResponse.builder()
                .userId(user.getId())
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void logout(AccessTokenRequest accessTokenRequest) {
        String accessToken = accessTokenRequest.getAccessToken();
        if (StringUtils.isBlank(accessToken)) {
            throw new IllegalArgumentException("Access token is blank");
        }
        String email = jwtService.extractEmail(accessToken, TokenType.ACCESS_TOKEN);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!jwtService.validateToken(accessToken, TokenType.ACCESS_TOKEN, user)) {
            throw new IllegalArgumentException("Invalid access token");
        }
        redisTokenService.deleteToken(user.getId());
        SecurityContextHolder.clearContext();
    }

    @Override
    public IntrospectResponse introspectToken(AccessTokenRequest accessTokenRequest) {
        String accessToken = accessTokenRequest.getAccessToken();
        if (StringUtils.isBlank(accessToken)) {
            throw new IllegalArgumentException("Access token is blank");
        }
        boolean isValid = true;
        String email = jwtService.extractEmail(accessToken, TokenType.ACCESS_TOKEN);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!jwtService.validateToken(accessToken, TokenType.ACCESS_TOKEN, user)) {
            isValid = false;
        }
        return IntrospectResponse.builder()
                .isValid(isValid)
                .build();
    }


    @Override
    public void forgotPassword(ForgotPasswordRequest forgotPasswordRequest) throws MessagingException {
        String email = forgotPasswordRequest.getEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        String newPassword = generateRandomPassword();
        user.setPassword(newPassword);
        userRepository.save(user);
        mailService.sendEmail(email, "Forgot Password", "Your new password is " + newPassword);
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