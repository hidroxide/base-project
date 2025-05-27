package com.ecommerce.fashionbackend.service;

public interface RedisTokenService {
    void saveToken(String userId, String accessToken, String refreshToken);
    void updateAccessToken(String userId, String newAccessToken);
    String getAccessToken(String userId);
    String getRefreshToken(String userId);
    void deleteToken(String userId);
}
