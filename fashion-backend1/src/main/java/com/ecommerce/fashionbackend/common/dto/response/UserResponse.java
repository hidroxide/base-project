package com.ecommerce.fashionbackend.common.dto.response;

import lombok.Data;

@Data
public class UserResponse {
    private String id;

    private String fullName;

    private String email;

    private String phone;

    private String avatar;
}
