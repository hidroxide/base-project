package com.ecommerce.fashionbackend.auth.service;

import com.ecommerce.fashionbackend.auth.dto.request.AccessTokenRequest;
import com.ecommerce.fashionbackend.auth.dto.request.ForgotPasswordRequest;
import com.ecommerce.fashionbackend.auth.dto.request.LoginRequest;
import com.ecommerce.fashionbackend.auth.dto.request.RefreshTokenRequest;
import com.ecommerce.fashionbackend.auth.dto.response.AuthResponse;
import com.ecommerce.fashionbackend.auth.dto.response.IntrospectResponse;
import jakarta.mail.MessagingException;

public interface AuthService {
    AuthResponse login(LoginRequest loginRequest);
    AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
    void logout(AccessTokenRequest accessTokenRequest);
    IntrospectResponse introspectToken(AccessTokenRequest accessTokenRequest);
    void forgotPassword(ForgotPasswordRequest forgotPasswordRequest) throws MessagingException;
}