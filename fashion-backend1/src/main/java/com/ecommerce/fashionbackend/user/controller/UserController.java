package com.ecommerce.fashionbackend.user.controller;

import com.ecommerce.fashionbackend.user.dto.request.AddressRequest;
import com.ecommerce.fashionbackend.user.dto.request.ChangePasswordRequest;
import com.ecommerce.fashionbackend.user.dto.request.UpdateUserRequest;
import com.ecommerce.fashionbackend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequest updateUserRequest) {
        return ResponseEntity.ok(userService.updateUser(updateUserRequest));
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        return ResponseEntity.ok(userService.getProfile());
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        userService.changePassword(changePasswordRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/address")
    public ResponseEntity<?> saveAddress(@RequestBody AddressRequest addressRequest) {
        return ResponseEntity.ok(userService.saveAddress(addressRequest));
    }

    @PutMapping("/address/{addressId}")
    public ResponseEntity<?> updateAddress(@PathVariable Long addressId, @RequestBody AddressRequest addressRequest) {
        return ResponseEntity.ok(userService.updateAddress(addressId, addressRequest));
    }

    @DeleteMapping("/address/{addressId}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long addressId) {
        userService.deleteAddress(addressId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/address")
    public ResponseEntity<?> getAllAddress() {
        return ResponseEntity.ok(userService.getAllAddress());
    }

    @PutMapping("/address/default/{addressId}")
    public ResponseEntity<?> setDefaultAddress(@PathVariable Long addressId) {
        userService.setDefaultAddress(addressId);
        return ResponseEntity.ok().build();
    }
}