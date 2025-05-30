package com.ecommerce.fashionbackend.controller;

import com.ecommerce.fashionbackend.dto.request.ContactRequest;
import com.ecommerce.fashionbackend.service.ContactService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contact")
@RequiredArgsConstructor
public class ContactController {
    private final ContactService contactService;

    @PostMapping
    public ResponseEntity<String> contact(@RequestBody ContactRequest contactRequest) {
        try {
            contactService.handleContactForm(contactRequest);
            return ResponseEntity.ok("Contact form sent successfully");
        } catch (MessagingException e) {
            return ResponseEntity.badRequest().body("Error occurred while sending contact form");
        }
    }
}
