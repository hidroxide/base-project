package com.ecommerce.fashionbackend.security.service;

import com.ecommerce.fashionbackend.common.constant.TokenType;
import com.ecommerce.fashionbackend.user.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateAccessToken(User user);
    String generateRefreshToken(User user);
    String extractEmail(String token, TokenType tokenType);
    boolean validateToken(String token, TokenType tokenType, UserDetails userDetails);
}