package com.ecommerce.fashionbackend.service;

import com.ecommerce.fashionbackend.dto.request.AccessTokenRequest;
import com.ecommerce.fashionbackend.dto.request.ForgotPasswordRequest;
import com.ecommerce.fashionbackend.dto.request.LoginRequest;
import com.ecommerce.fashionbackend.dto.request.RefreshTokenRequest;
import com.ecommerce.fashionbackend.dto.response.AuthResponse;
import com.ecommerce.fashionbackend.dto.response.IntrospectResponse;
import jakarta.mail.MessagingException;

public interface AuthService {
    AuthResponse login(LoginRequest loginRequest);
    AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
    void logout(AccessTokenRequest accessTokenRequest);
    IntrospectResponse introspectToken(AccessTokenRequest accessTokenRequest);
    void forgotPassword(ForgotPasswordRequest forgotPasswordRequest) throws MessagingException;
}
