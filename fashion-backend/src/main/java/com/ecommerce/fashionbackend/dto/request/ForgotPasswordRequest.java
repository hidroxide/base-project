package com.ecommerce.fashionbackend.dto.request;

import lombok.Data;

@Data
public class ForgotPasswordRequest {
    private String email;

    private String phone;
}