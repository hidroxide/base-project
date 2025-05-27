package com.ecommerce.fashionbackend.service.impl;

import com.ecommerce.fashionbackend.constant.Role;
import com.ecommerce.fashionbackend.constant.UserStatus;
import com.ecommerce.fashionbackend.dto.request.AddressRequest;
import com.ecommerce.fashionbackend.dto.request.ChangePasswordRequest;
import com.ecommerce.fashionbackend.dto.request.RegisterRequest;
import com.ecommerce.fashionbackend.dto.request.UpdateUserRequest;
import com.ecommerce.fashionbackend.dto.response.AddressResponse;
import com.ecommerce.fashionbackend.dto.response.PageResponse;
import com.ecommerce.fashionbackend.dto.response.UserResponse;
import com.ecommerce.fashionbackend.entity.User;
import com.ecommerce.fashionbackend.mapper.AddressMapper;
import com.ecommerce.fashionbackend.mapper.UserMapper;
import com.ecommerce.fashionbackend.repository.UserRepository;
import com.ecommerce.fashionbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final AddressMapper addressMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse saveUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        User user = userMapper.toUser(registerRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(UserStatus.ACTIVE);
        user.setRole(Role.USER);
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse updateUser(UpdateUserRequest updateUserRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email"));
        userMapper.updateUser(user, updateUserRequest);
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse getUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid userId"));
        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email"));
        return userMapper.toUserResponse(user);
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid userId"));
        userRepository.delete(user);
    }

    @Override
    public UserStatus changeUserStatus(String userId, UserStatus userStatus) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid userId"));
        user.setStatus(userStatus);
        userRepository.save(user);
        return user.getStatus();
    }

    @Override
    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email"));
        if (passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Invalid old password");
        }
    }

    public Specification<User> hasRole(Role role) {
        return (root, query, cb)
                -> role == null ? null : cb.equal(root.get("role"), role);
    }

    @Override
    public PageResponse<UserResponse> getAllUsersByRole(Role role, int pageNo, int pageSize) {
        Specification<User> spec = hasRole(role);
        Page<User> result = userRepository.findAll(spec, PageRequest.of(pageNo, pageSize));
        List<UserResponse> userResponses = result.getContent().stream()
                .map(userMapper::toUserResponse)
                .toList();
        return PageResponse.<UserResponse>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(result.getTotalPages())
                .items(userResponses)
                .build();
    }

    @Override
    public AddressResponse saveAddress(AddressRequest addressRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email"));

        if ( user.getAddress() == null) {
            user.setAddress(addressMapper.toAddress(addressRequest));
        } else {
            addressMapper.updateAddress(user.getAddress(), addressRequest);
        }
        return addressMapper.toAddressResponse(user.getAddress());
    }

    @Override
    public AddressResponse getAddress() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email"));
        return addressMapper.toAddressResponse(user.getAddress());
    }

    @Override
    public void deleteAddress() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email"));
        if (user.getAddress() == null) {
            throw new IllegalArgumentException("Address not found");
        }
        user.setAddress(null);
        userRepository.save(user);
    }
}
