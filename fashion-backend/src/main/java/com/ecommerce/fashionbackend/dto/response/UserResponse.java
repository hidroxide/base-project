package com.ecommerce.fashionbackend.dto.response;

import com.ecommerce.fashionbackend.constant.UserStatus;
import lombok.Data;

@Data
public class UserResponse {
    private String id;

    private String fullName;

    private String email;

    private String phone;

    private String avatar;

    private UserStatus userStatus;
}