package com.ecommerce.fashionbackend.auth.dto.request;

import lombok.Data;

@Data
public class RefreshTokenRequest {
    private String refreshToken;
}