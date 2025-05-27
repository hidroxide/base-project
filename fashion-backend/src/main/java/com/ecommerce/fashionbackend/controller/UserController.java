package com.ecommerce.fashionbackend.controller;

import com.ecommerce.fashionbackend.dto.request.AddressRequest;
import com.ecommerce.fashionbackend.dto.request.UpdateUserRequest;
import com.ecommerce.fashionbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        return ResponseEntity.ok(userService.getProfile());
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateUserRequest updateUserRequest) {
        return ResponseEntity.ok(userService.updateUser(updateUserRequest));
    }

    @GetMapping("/address")
    public ResponseEntity<?> getAddress() {
        return ResponseEntity.ok(userService.getAddress());
    }

    @PutMapping("/address")
    public ResponseEntity<?> updateAddress(@RequestBody AddressRequest addressRequest) {
        return ResponseEntity.ok(userService.saveAddress(addressRequest));
    }

    @DeleteMapping("/address")
    public ResponseEntity<String> deleteAddress() {
        userService.deleteAddress();
        return ResponseEntity.ok("Address deleted successfully");
    }
}
