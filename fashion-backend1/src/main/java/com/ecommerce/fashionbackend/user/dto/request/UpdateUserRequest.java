package com.ecommerce.fashionbackend.user.dto.request;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String fullName;

    private String email;

    private String phone;

    private String avatar;
}