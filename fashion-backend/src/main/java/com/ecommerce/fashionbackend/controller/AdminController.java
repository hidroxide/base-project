package com.ecommerce.fashionbackend.controller;

import com.ecommerce.fashionbackend.constant.Role;
import com.ecommerce.fashionbackend.constant.UserStatus;
import com.ecommerce.fashionbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@RequestParam(required = false) Role role,
                                         @RequestParam(required = false) String keyword,
                                         @RequestParam(required = false) UserStatus userStatus,
                                         @RequestParam(defaultValue = "0") int pageNo,
                                         @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(userService.getAllUsers(role, keyword, userStatus, pageNo, pageSize));
    }

    @PutMapping("/users/{userId}/ban")
    public ResponseEntity<?> getBannedUser(@PathVariable String userId) {
        return ResponseEntity.ok(userService.changeUserStatus(userId, UserStatus.BLOCKED));
    }
}
