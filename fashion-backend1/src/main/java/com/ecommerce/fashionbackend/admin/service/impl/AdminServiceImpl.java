package com.ecommerce.fashionbackend.admin.service.impl;

import com.ecommerce.fashionbackend.admin.service.AdminService;
import com.ecommerce.fashionbackend.common.constant.Role;
import com.ecommerce.fashionbackend.common.constant.UserStatus;
import com.ecommerce.fashionbackend.common.dto.response.PageResponse;
import com.ecommerce.fashionbackend.common.dto.response.UserResponse;
import com.ecommerce.fashionbackend.user.entity.User;
import com.ecommerce.fashionbackend.user.mapper.UserMapper;
import com.ecommerce.fashionbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public UserResponse getUser(String userId) {
        User user = userRepository.findById(userId)
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
    public PageResponse<UserResponse> getAllUsers(
            Role role,
            String keyword,
            UserStatus userStatus,
            int pageNo,
            int pageSize
    ) {
        Specification<User> spec = null;

        if (role != null) {
            spec = hasRole(role);
        }
        if (keyword != null && !keyword.isBlank()) {
            spec = addCondition(spec, hasKeyword(keyword));
        }
        if (userStatus != null) {
            spec = addCondition(spec, hasStatus(userStatus));
        }

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
            String lowerCaseKeyword = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("fullName")), lowerCaseKeyword),
                    cb.like(cb.lower(root.get("email")), lowerCaseKeyword),
                    cb.like(cb.lower(root.get("phone")), lowerCaseKeyword)
            );
        };
    }

    public static Specification<User> addCondition(Specification<User> spec, Specification<User> condition) {
        return (spec == null) ? condition : spec.and(condition);
    }

}
