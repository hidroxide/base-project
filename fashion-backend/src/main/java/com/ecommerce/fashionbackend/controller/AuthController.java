package com.ecommerce.fashionbackend.controller;

import com.ecommerce.fashionbackend.dto.request.AccessTokenRequest;
import com.ecommerce.fashionbackend.dto.request.LoginRequest;
import com.ecommerce.fashionbackend.dto.request.RefreshTokenRequest;
import com.ecommerce.fashionbackend.dto.request.RegisterRequest;
import com.ecommerce.fashionbackend.dto.response.AuthResponse;
import com.ecommerce.fashionbackend.dto.response.UserResponse;
import com.ecommerce.fashionbackend.service.AuthService;
import com.ecommerce.fashionbackend.service.UserService;
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
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
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
}
