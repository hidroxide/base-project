package com.ecommerce.fashionbackend.admin.controller;

import com.ecommerce.fashionbackend.admin.service.AdminService;
import com.ecommerce.fashionbackend.common.constant.Role;
import com.ecommerce.fashionbackend.common.constant.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUser(@PathVariable String userId) {
        return ResponseEntity.ok(adminService.getUser(userId));
    }

    @GetMapping("/delete-user")
    public ResponseEntity<?> deleteUser(String userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@RequestParam(required = false) Role role,
                                         @RequestParam(required = false) String keyword,
                                         @RequestParam(required = false) UserStatus userStatus,
                                         @RequestParam(defaultValue = "0") int pageNo,
                                         @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(adminService.getAllUsers(role, keyword, userStatus, pageNo, pageSize));
    }

    @PutMapping("/users/{userId}/activate")
    public ResponseEntity<?> activateUser(@PathVariable String userId) {
        return ResponseEntity.ok(adminService.changeUserStatus(userId, UserStatus.ACTIVE));
    }

    @PutMapping("/users/{userId}/block")
    public ResponseEntity<?> blockUser(@PathVariable String userId) {
        return ResponseEntity.ok(adminService.changeUserStatus(userId, UserStatus.BLOCKED));
    }
}