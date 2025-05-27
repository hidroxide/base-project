package com.ecommerce.fashionbackend.service.impl;

import com.ecommerce.fashionbackend.service.RedisTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisTokenServiceImpl implements RedisTokenService {
    private final StringRedisTemplate redisTemplate;

    private static final long ACCESS_TOKEN_TTL = 60 * 24;

    private static final long REFRESH_TOKEN_TTL = 60 * 24 * 7;

    @Override
    public void saveToken(String userId, String accessToken, String refreshToken) {
        redisTemplate.opsForValue().set(getAccessTokenKey(userId), accessToken, ACCESS_TOKEN_TTL, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(getRefreshTokenKey(userId), refreshToken, REFRESH_TOKEN_TTL, TimeUnit.MINUTES);
    }

    @Override
    public void updateAccessToken(String userId, String newAccessToken) {
        redisTemplate.opsForValue().set(getAccessTokenKey(userId), newAccessToken, ACCESS_TOKEN_TTL, TimeUnit.MINUTES);
    }

    @Override
    public String getAccessToken(String userId) {
        return redisTemplate.opsForValue().get(getAccessTokenKey(userId));
    }

    @Override
    public String getRefreshToken(String userId) {
        return redisTemplate.opsForValue().get(getRefreshTokenKey(userId));
    }

    @Override
    public void deleteToken(String userId) {
        redisTemplate.delete(getAccessTokenKey(userId));
        redisTemplate.delete(getRefreshTokenKey(userId));
    }

//    other methods
    private String getAccessTokenKey(String userId) {
        return "user:" + userId + ":access_token";
    }

    private String getRefreshTokenKey(String userId) {
        return "user:" + userId + ":refresh_token";
    }
}
