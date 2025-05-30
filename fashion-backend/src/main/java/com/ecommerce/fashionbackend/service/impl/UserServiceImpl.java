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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    private final AddressMapper addressMapper;

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
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        userMapper.updateUser(user, updateUserRequest);
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse getUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
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
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        userRepository.delete(user);
    }

    @Override
    public UserStatus changeUserStatus(String userId, UserStatus userStatus) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setStatus(userStatus);
        userRepository.save(user);
        return user.getStatus();
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
    public PageResponse<?> getAllUsers(Role role, String keyword, UserStatus userStatus,
                                             int pageNo, int pageSize) {
        Specification<User> spec = Stream.of(
                hasRole(role),
                hasKeyword(keyword),
                hasStatus(userStatus))
                .reduce(Specification::and)
                .orElse(null);
        Page<User> result = userRepository.findAll(spec, PageRequest.of(pageNo, pageSize));
        List<UserResponse> userResponses = result.getContent()
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(result.getTotalPages())
                .items(userResponses)
                .build();
    }

    @Override
    public AddressResponse saveAddress(AddressRequest addressRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (user.getAddress() == null) {
            user.setAddress(addressMapper.toAddress(addressRequest));
        } else {
            addressMapper.updateAddress(user.getAddress(), addressRequest);
        }
        return addressMapper.toAddressResponse(user.getAddress());
    }

    @Override
    public AddressResponse getAddress() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return addressMapper.toAddressResponse(user.getAddress());
    }

    @Override
    public void deleteAddress() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (user.getAddress() != null) {
            user.setAddress(null);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Address not found");
        }
    }

//    other methods
    private Specification<User> hasRole(Role role) {
        return (root, query, cb)
                -> role == null ? null : cb.equal(root.get("role"), role);
    }

    private Specification<User> hasStatus(UserStatus userStatus) {
        return (root, query, cb)
                -> userStatus == null ? null : cb.equal(root.get("status"), userStatus);
    }

    private Specification<User> hasKeyword(String keyword) {
        return (root, query, cb)
                -> {
            if (keyword == null || keyword.isBlank()) {
                return null;
            } else {
                String lowerCaseKeyword = "%" + keyword.toLowerCase() + "%";
                return cb.or(
                        cb.like(cb.lower(root.get("fullName")), lowerCaseKeyword),
                        cb.like(cb.lower(root.get("email")), lowerCaseKeyword),
                        cb.like(cb.lower(root.get("phone")), lowerCaseKeyword)
                );
            }
        };
    }
}
