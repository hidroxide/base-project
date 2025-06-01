package com.ecommerce.fashionbackend.contact.service;

import com.ecommerce.fashionbackend.contact.dto.ContactRequest;
import jakarta.mail.MessagingException;

public interface ContactService {
    void handleContactForm(ContactRequest contactRequest) throws MessagingException;
}