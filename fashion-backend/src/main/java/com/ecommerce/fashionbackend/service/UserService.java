package com.ecommerce.fashionbackend.service;

import com.ecommerce.fashionbackend.constant.Role;
import com.ecommerce.fashionbackend.constant.UserStatus;
import com.ecommerce.fashionbackend.dto.request.AddressRequest;
import com.ecommerce.fashionbackend.dto.request.ChangePasswordRequest;
import com.ecommerce.fashionbackend.dto.request.RegisterRequest;
import com.ecommerce.fashionbackend.dto.request.UpdateUserRequest;
import com.ecommerce.fashionbackend.dto.response.AddressResponse;
import com.ecommerce.fashionbackend.dto.response.PageResponse;
import com.ecommerce.fashionbackend.dto.response.UserResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {
//    user
    UserResponse saveUser(RegisterRequest registerRequest);
    UserResponse updateUser(UpdateUserRequest updateUserRequest);
    UserResponse getUser(String userId);
    UserResponse getProfile();
    void deleteUser(String userId);
    UserStatus changeUserStatus(String userId, UserStatus userStatus);
    void changePassword(ChangePasswordRequest changePasswordRequest);
    PageResponse<?> getAllUsersByRole(Role role, int pageNo, int pageSize);
//    address
    AddressResponse saveAddress(AddressRequest addressRequest);
    AddressResponse getAddress();
    void deleteAddress();
}
