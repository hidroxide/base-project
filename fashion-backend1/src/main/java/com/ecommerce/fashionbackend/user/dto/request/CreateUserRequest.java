package com.ecommerce.fashionbackend.user.dto.request;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String fullName;

    private String email;

    private String password;

    private String phone;

    private String avatar;
}
