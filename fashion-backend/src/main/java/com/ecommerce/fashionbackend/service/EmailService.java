package com.ecommerce.fashionbackend.service;

import com.ecommerce.fashionbackend.dto.request.ContactRequest;
import jakarta.mail.MessagingException;

public interface EmailService {
    void sendEmail(String to, String subject, String content) throws MessagingException;
    void sendContactEmail(ContactRequest contactRequest) throws MessagingException;
}
