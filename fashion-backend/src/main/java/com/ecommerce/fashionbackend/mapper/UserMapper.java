package com.ecommerce.fashionbackend.mapper;

import com.ecommerce.fashionbackend.dto.request.RegisterRequest;
import com.ecommerce.fashionbackend.dto.request.UpdateUserRequest;
import com.ecommerce.fashionbackend.dto.response.UserResponse;
import com.ecommerce.fashionbackend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(RegisterRequest registerRequest);

    void updateUser(@MappingTarget User user, UpdateUserRequest updateUserRequest);

    UserResponse toUserResponse(User user);
}
