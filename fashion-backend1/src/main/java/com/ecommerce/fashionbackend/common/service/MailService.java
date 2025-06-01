package com.ecommerce.fashionbackend.common.service;

import com.ecommerce.fashionbackend.contact.dto.ContactRequest;
import jakarta.mail.MessagingException;

public interface MailService {
    void sendEmail(String to, String subject, String content) throws MessagingException;
    void sendContactEmail(ContactRequest contactRequest) throws MessagingException;
}