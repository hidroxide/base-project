package com.ecommerce.fashionbackend.service;

import com.ecommerce.fashionbackend.dto.request.ContactRequest;
import jakarta.mail.MessagingException;

public interface ContactService {
    void handleContactForm(ContactRequest contactRequest) throws MessagingException;
}
