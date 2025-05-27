package com.ecommerce.fashionbackend.service;

import com.ecommerce.fashionbackend.dto.request.*;
import com.ecommerce.fashionbackend.dto.response.AuthResponse;
import com.ecommerce.fashionbackend.dto.response.IntrospectResponse;
import jakarta.mail.MessagingException;

public interface AuthService {
    AuthResponse login(LoginRequest loginRequest);
    AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
    void logout(LogoutRequest logoutRequest);
    IntrospectResponse introspectToken(IntrospectRequest introspectRequest);
    void forgotPassword(ForgotPasswordRequest forgotPasswordRequest) throws MessagingException;
}
