package com.ecommerce.fashionbackend.user.service;

import com.ecommerce.fashionbackend.user.dto.request.AddressRequest;
import com.ecommerce.fashionbackend.user.dto.response.AddressResponse;
import com.ecommerce.fashionbackend.user.dto.request.ChangePasswordRequest;
import com.ecommerce.fashionbackend.user.dto.request.CreateUserRequest;
import com.ecommerce.fashionbackend.user.dto.request.UpdateUserRequest;
import com.ecommerce.fashionbackend.common.dto.response.UserResponse;

import java.util.List;

public interface UserService {
//    user
    UserResponse saveUser(CreateUserRequest createUserRequest);
    UserResponse updateUser(UpdateUserRequest updateUserRequest);
    UserResponse getProfile();
//    void updateAvatar(String avatar);
    void changePassword(ChangePasswordRequest changePasswordRequest);

//    address
    AddressResponse saveAddress(AddressRequest addressRequest);
    AddressResponse updateAddress(Long addressId, AddressRequest addressRequest);
    void deleteAddress(Long addressId);
    List<AddressResponse> getAllAddress();
    void setDefaultAddress(Long addressId);
}
