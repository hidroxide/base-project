package com.ecommerce.fashionbackend.contact.service.impl;

import com.ecommerce.fashionbackend.common.service.MailService;
import com.ecommerce.fashionbackend.contact.dto.ContactRequest;
import com.ecommerce.fashionbackend.contact.service.ContactService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {
    private final MailService mailService;

    @Override
    public void handleContactForm(ContactRequest contactRequest) throws MessagingException {
        mailService.sendContactEmail(contactRequest);
    }
}