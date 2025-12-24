package com.rental.manager.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface EmailService {
    void sendVerificationEmail(String to, String verificationToken);
    void createAndSendVerificationToken(String to);
    String verifyEmail(String verificationToken);
    void sendPasswordResetEmail(String to, String resetToken);
    void createAndSendPasswordResetToken(String to);
    String resetPassword(String resetToken, String newPassword);
    void sendEmail(String to, String token, String subject, String path, String message);
    void sendOwnerCredentialsEmail(String to, String name, String password);
    void resendOwnerCredentialsEmail(String ownerEmail);
    void sendBookingInfoToGuest(String to, String name, String bookingCode, LocalDate checkIn, LocalDate checkOut);
    void sendBookingInfoToOwner(String to, String ownerName, String guestName, String bookingCode, LocalDate checkIn, LocalDate checkOut);
}
