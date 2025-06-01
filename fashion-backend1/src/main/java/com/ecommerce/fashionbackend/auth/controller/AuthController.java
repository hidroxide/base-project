package com.ecommerce.fashionbackend.auth.controller;

import com.ecommerce.fashionbackend.auth.dto.request.AccessTokenRequest;
import com.ecommerce.fashionbackend.auth.dto.request.ForgotPasswordRequest;
import com.ecommerce.fashionbackend.auth.dto.request.LoginRequest;
import com.ecommerce.fashionbackend.auth.dto.request.RefreshTokenRequest;
import com.ecommerce.fashionbackend.auth.dto.response.AuthResponse;
import com.ecommerce.fashionbackend.auth.service.AuthService;
import com.ecommerce.fashionbackend.user.dto.request.CreateUserRequest;
import com.ecommerce.fashionbackend.user.dto.response.UserResponse;
import com.ecommerce.fashionbackend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CreateUserRequest registerRequest) {
        UserResponse userResponse = userService.saveUser(registerRequest);
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        AuthResponse authResponse = authService.login(loginRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        AuthResponse authResponse = authService.refreshToken(refreshTokenRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/introspect")
    public ResponseEntity<?> introspect(@RequestBody AccessTokenRequest accessTokenRequest) {
        return ResponseEntity.ok(authService.introspectToken(accessTokenRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody AccessTokenRequest accessTokenRequest) {
        authService.logout(accessTokenRequest);
        return ResponseEntity.ok("Logout successful");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        try {
            authService.forgotPassword(forgotPasswordRequest);
            return ResponseEntity.ok("Password reset email sent successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error occurred while sending password reset email");
        }
    }
}