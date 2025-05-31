package com.ecommerce.fashionbackend.admin.service;

import com.ecommerce.fashionbackend.common.constant.Role;
import com.ecommerce.fashionbackend.common.constant.UserStatus;
import com.ecommerce.fashionbackend.common.dto.response.PageResponse;
import com.ecommerce.fashionbackend.common.dto.response.UserResponse;

public interface AdminService {
    UserResponse getUser(String userId);
    void deleteUser(String userId);
    UserStatus changeUserStatus(String userId, UserStatus userStatus);
    PageResponse<UserResponse> getAllUsers(Role role, String keyword, UserStatus userStatus,
                                int pageNo, int pageSize);
}
