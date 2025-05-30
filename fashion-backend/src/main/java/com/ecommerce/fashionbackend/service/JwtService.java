package com.ecommerce.fashionbackend.service;

import com.ecommerce.fashionbackend.constant.TokenType;
import com.ecommerce.fashionbackend.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateAccessToken(User user);
    String generateRefreshToken(User user);
    String extractEmail(String token, TokenType tokenType);
    boolean validateToken(String token, TokenType tokenType, UserDetails userDetails);
}
