package com.ecommerce.fashionbackend.service.impl;

import com.ecommerce.fashionbackend.dto.request.ContactRequest;
import com.ecommerce.fashionbackend.service.ContactService;
import com.ecommerce.fashionbackend.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {
    private final EmailService emailService;

    @Override
    public void handleContactForm(ContactRequest contactRequest) throws MessagingException {
        emailService.sendContactEmail(contactRequest);
    }
}
