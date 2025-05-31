package com.ecommerce.fashionbackend.user.service.impl;

import com.ecommerce.fashionbackend.common.constant.Role;
import com.ecommerce.fashionbackend.common.constant.UserStatus;
import com.ecommerce.fashionbackend.user.dto.request.AddressRequest;
import com.ecommerce.fashionbackend.user.dto.response.AddressResponse;
import com.ecommerce.fashionbackend.user.mapper.AddressMapper;
import com.ecommerce.fashionbackend.user.repository.AddressRepository;
import com.ecommerce.fashionbackend.user.dto.request.ChangePasswordRequest;
import com.ecommerce.fashionbackend.user.dto.request.CreateUserRequest;
import com.ecommerce.fashionbackend.user.dto.request.UpdateUserRequest;
import com.ecommerce.fashionbackend.common.dto.response.UserResponse;
import com.ecommerce.fashionbackend.user.entity.Address;
import com.ecommerce.fashionbackend.user.entity.User;
import com.ecommerce.fashionbackend.user.mapper.UserMapper;
import com.ecommerce.fashionbackend.user.repository.UserRepository;
import com.ecommerce.fashionbackend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final AddressRepository addressRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    private final AddressMapper addressMapper;

    private static final int MAX_ADDRESS_PER_USER = 3;

    @Override
    public UserResponse saveUser(CreateUserRequest createUserRequest) {
        if (userRepository.existsByEmail(createUserRequest.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        User user = userMapper.toUser(createUserRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(UserStatus.ACTIVE);
        user.setRole(Role.USER);
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse updateUser(UpdateUserRequest updateUserRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        userMapper.updateUser(user, updateUserRequest);
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse getProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return userMapper.toUserResponse(user);
    }

    @Override
    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public AddressResponse saveAddress(AddressRequest addressRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (user.getAddresses().size() >= MAX_ADDRESS_PER_USER) {
            throw new IllegalArgumentException("Maximum 3 addresses allowed");
        }
        Address address = addressMapper.toAddress(addressRequest);
        address.setUser(user);
        user.getAddresses().add(address);
        userRepository.save(user);
        return addressMapper.toAddressResponse(address);
    }

    @Override
    public AddressResponse updateAddress(Long addressId, AddressRequest addressRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Address address = user.getAddresses().stream()
                .filter(a -> a.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Address not found"));
        addressMapper.updateAddress(address, addressRequest);
        userRepository.save(user);
        return addressMapper.toAddressResponse(address);
    }

    @Override
    public void deleteAddress(Long addressId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Address address = user.getAddresses().stream()
                .filter(a -> a.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Address not found"));
        user.getAddresses().remove(address);
        userRepository.save(user);
    }

    @Override
    public List<AddressResponse> getAllAddress() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return user.getAddresses().stream()
                .map(addressMapper::toAddressResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void setDefaultAddress(Long addressId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("Address not found"));
        if (!address.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Unauthorized to access this address");
        }
        user.getAddresses().forEach(a -> a.setDefault(false) );
        address.setDefault(true);
        userRepository.save(user);
    }
}
