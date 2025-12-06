package com.rental.manager.service;

public interface EmailService {
    void sendVerificationEmail(String to, String verificationToken);
    void sendPasswordResetEmail(String to, String resetToken);
    void sendEmail(String to, String token, String subject, String path, String message);
    void createAndSendVerificationToken(String to);
    String verifyEmail(String verificationToken);
}
