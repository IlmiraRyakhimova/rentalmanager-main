package com.rental.manager.service.impl;

import com.rental.manager.entities.EmailVerificationToken;
import com.rental.manager.entities.User;
import com.rental.manager.repository.EmailVerificationTokenRepository;
import com.rental.manager.repository.UserRepository;
import com.rental.manager.security.jwt.JwtService;
import com.rental.manager.security.jwt.dto.JwtDTO;
import com.rental.manager.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final JwtService jwtService;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${app.backend.url}")
    private String backendUrl;

    @Value("${app.frontend.url}")
    private String frontendUrl;


    @Override
    public void sendVerificationEmail(String to, String verificationToken) {
        String subject = "Подтверждение email";
        String message = "Нажмите на кнопку ниже, чтобы подтвердить email:";
        String path = "/api/auth/verify-email";
        sendEmail(to, verificationToken, subject, path, message);
    }

    @Override
    public void sendPasswordResetEmail(String to, String resetToken) {
        String subject = "Сброс пароля";
        String message = "Нажмите на кнопку ниже, чтобы сбросить пароль:";
        String path = "/api/auth/reset-password";
        sendEmail(to, resetToken, subject, path, message);
    }

    @Override
    public void sendEmail(String to, String token, String subject, String path, String message) {
        try {
            String actionUrl = backendUrl + path + "?token=" + token;

            String content = """
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border-radius: 8px; background-color: #f9f9f9; text-align: center;">
                    <h2 style="color: #333;">%s</h2>
                    <p style="font-size: 16px; color: #555;">%s</p>
                    <a href="%s" style="display: inline-block; margin: 20px 0; padding: 10px 20px; font-size: 16px; color: #fff; background-color: #007bff; text-decoration: none; border-radius: 5px;">Proceed</a>
                    <p style="font-size: 14px; color: #777;">Или скопируйте и вставьте эту ссылку в браузер:</p>
                    <p style="font-size: 14px; color: #007bff;">%s</p>
                    <p style="font-size: 12px; color: #aaa;">This is an automated message. Please do not reply.</p>
                </div>
            """.formatted(subject, message, actionUrl, actionUrl);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(from);
            helper.setText(content, true);
            mailSender.send(mimeMessage);

        } catch (Exception e) {
            throw new RuntimeException("Ошибка отправки email: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void createAndSendVerificationToken(String to) {
        User user = userRepository.findByEmail(to);
        if (user == null) {
            throw new EntityNotFoundException("User not found with email: " + to);
        }

        if (user.isEmailVerified()) {
            throw new IllegalArgumentException("Email is already verified for: " + to);
        }

        emailVerificationTokenRepository.deleteByUser(user);
        String token = UUID.randomUUID().toString();
        EmailVerificationToken verificationToken = new EmailVerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        verificationToken.setCreatedAt(LocalDateTime.now());
        EmailVerificationToken saved = emailVerificationTokenRepository.save(verificationToken);
        sendVerificationEmail(user.getEmail(), token);
    }

    @Override
    public String verifyEmail(String token) {
        EmailVerificationToken verificationToken = emailVerificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            throw new IllegalArgumentException("Токен подтверждения email уже использован");
        }


        if(verificationToken.isExpired()) {
            throw new IllegalArgumentException("Токен подтверждения email истек");
        }

        User user = verificationToken.getUser();
        user.setEmailVerified(true);

        JwtDTO jwtDto = jwtService.generateAuthToken(user.getEmail());
        user.setRefreshTokenHash(jwtService.hashRefreshToken(jwtDto.getRefreshToken()));

        userRepository.save(user);
        emailVerificationTokenRepository.delete(verificationToken);
//        return String.format("%s/auth/verified?accessToken=%s&refreshToken=%s&email=%s&name=%s",
//                frontendUrl,
//                jwtDto.getToken(),
//                jwtDto.getRefreshToken(),
//                user.getEmail(),
//                user.getName());
        return String.format(
                "Email verified!\nAccess Token: %s\nRefresh Token: %s",
                jwtDto.getToken(),
                jwtDto.getRefreshToken()
        );

    }
}
