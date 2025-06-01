package com.ecommerce.fashionbackend.user.mapper;

import com.ecommerce.fashionbackend.user.dto.response.UserResponse;
import com.ecommerce.fashionbackend.user.dto.request.CreateUserRequest;
import com.ecommerce.fashionbackend.user.dto.request.UpdateUserRequest;
import com.ecommerce.fashionbackend.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(CreateUserRequest createUserRequest);

    void updateUser(@MappingTarget User user, UpdateUserRequest updateUserRequest);

    UserResponse toUserResponse(User user);
}