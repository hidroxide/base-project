package com.ecommerce.fashionbackend.security.service.impl;

import com.ecommerce.fashionbackend.common.constant.TokenType;
import com.ecommerce.fashionbackend.security.service.JwtService;
import com.ecommerce.fashionbackend.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.access.secret-key}")
    private String accessSecretKey;

    @Value("${jwt.refresh.secret-key}")
    private String refreshSecretKey;

    @Value("${jwt.access.expiration}")
    private long accessExpiration;

    @Value("${jwt.refresh.expiration}")
    private long refreshExpiration;

    @Override
    public String generateAccessToken(User user) {
        return generateToken(new HashMap<>(), user, TokenType.ACCESS_TOKEN, accessExpiration);
    }

    @Override
    public String generateRefreshToken(User user) {
        return generateToken(new HashMap<>(), user, TokenType.REFRESH_TOKEN, refreshExpiration);
    }

    @Override
    public String extractEmail(String token, TokenType tokenType) {
        return extractClaim(token, tokenType, Claims::getSubject);
    }

    @Override
    public boolean validateToken(String token, TokenType tokenType, UserDetails userDetails) {
        final String username = extractEmail(token, tokenType);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token, tokenType));
    }

    //    other methods
    private String generateToken(Map<String, Object> claims, User user, TokenType tokenType, long expiration) {
        assert user != null;
        List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        claims.put("roles", roles);
        claims.put("id", user.getId());
        return Jwts
                .builder()
                .claims(claims)
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(tokenType))
                .compact();
    }

    private SecretKey getSignInKey(TokenType tokenType) {
        if (TokenType.ACCESS_TOKEN.equals(tokenType)) {
            return Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessSecretKey));
        } else {
            return Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecretKey));
        }
    }

    private Claims extractAllClaims(String token, TokenType tokenType) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey(tokenType))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private <T> T extractClaim(String token, TokenType tokenType, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token, tokenType);
        return claimsResolver.apply(claims);
    }

    private Date extractExpiration(String token, TokenType tokenType) {
        return extractClaim(token, tokenType, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token, TokenType tokenType) {
        return extractExpiration(token, tokenType).before(new Date());
    }
}
