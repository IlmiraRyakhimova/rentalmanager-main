package com.rental.manager.service.impl;

import com.rental.manager.entities.EmailVerificationToken;
import com.rental.manager.entities.PasswordResetToken;
import com.rental.manager.entities.User;
import com.rental.manager.repository.EmailVerificationTokenRepository;
import com.rental.manager.repository.PasswordResetTokenRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;

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
        String actionUrl = frontendUrl + "/auth/verify?token=" + verificationToken;
        sendEmail(to, subject,message , actionUrl, "Подтвердить email");
    }

    @Override
    public void sendPasswordResetEmail(String to, String resetToken) {
        String subject = "Сброс пароля";
        String message = "Нажмите на кнопку ниже, чтобы сбросить пароль:";
        String actionUrl = frontendUrl + "/reset-password?token=" + resetToken;
        sendEmail(to, subject, message, actionUrl, "Сбросить пароль");
    }

    @Override
    @Transactional
    public void createAndSendPasswordResetToken(String to) {
        User user = userRepository.findByEmail(to);
        if (user == null) {
            throw new EntityNotFoundException("Пользователь не найден с email: " + to);
        }

        if (!user.isEmailVerified()) {
            throw new IllegalArgumentException("Email не подтвержден. Пожалуйста, подтвердите email" + to);
        }

        passwordResetTokenRepository.deleteByUser(user);
        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setUser(user);
        passwordResetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        passwordResetToken.setCreatedAt(LocalDateTime.now());
        passwordResetTokenRepository.save(passwordResetToken);
        sendPasswordResetEmail(user.getEmail(), token);
    }

    @Override
    public String resetPassword(String resetToken, String newPassword) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(resetToken);
        if (passwordResetToken == null) {
            throw new IllegalArgumentException("Токен сброса пароля уже использован");
        }

        if(passwordResetToken.isExpired()) {
            throw new IllegalArgumentException("Токен сброса пароля истек");
        }

        User user = passwordResetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);
        passwordResetTokenRepository.delete(passwordResetToken);

        return String.format("%s/auth/reset-success?email=%s&name=%s",
                frontendUrl,
                user.getEmail(),
                user.getName());
    }


    @Override
    public void sendEmail(String to, String subject, String message, String actionUrl, String buttonText) {
        try {
            String content = """
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border-radius: 8px; background-color: #f9f9f9; text-align: center;">
                <h2 style="color: #333;">%s</h2>
                <div style="font-size: 16px; color: #555; text-align: left; margin: 20px 0;">%s</div>
                <a href="%s" style="display: inline-block; margin: 20px 0; padding: 10px 20px; font-size: 16px; color: #fff; background-color: #007bff; text-decoration: none; border-radius: 5px;">%s</a>
                <p style="font-size: 14px; color: #777;">Или скопируйте и вставьте эту ссылку в браузер:</p>
                <p style="font-size: 14px; color: #007bff; word-break: break-all;">%s</p>
                <p style="font-size: 12px; color: #aaa; margin-top: 30px;">С уважением,<br>Елена Лозовая</p>
            </div>
        """.formatted(subject, message, actionUrl, buttonText, actionUrl);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
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
    public void sendOwnerCredentialsEmail(String to, String name, String password) {
        String subject = "Для вас создан аккаунт собственника апартаментов";
        String message = """
        Здравствуйте, %s!<br><br>
        Для вас создан аккаунт собственника апартаментов.<br><br>
        <strong>Ваши учетные данные:</strong><br>
        Email: %s<br>
        Пароль: %s<br><br>
        Пожалуйста, при входе в систему подтвердите свой email и измените пароль после первого входа.
        """.formatted(name, to, password);

        String loginUrl = frontendUrl + "/owner-login";
        sendEmail(to, subject, message, loginUrl, "Войти в систему");

    }

    @Override
    @Transactional
    public void createAndSendVerificationToken(String to) {
        User user = userRepository.findByEmail(to);
        if (user == null) {
            throw new EntityNotFoundException("Пользователь не найден с email: " + to);
        }

        if (user.isEmailVerified()) {
            throw new IllegalArgumentException("Email уже подтвержден для: " + to);
        }

        emailVerificationTokenRepository.deleteByUser(user);
        String token = UUID.randomUUID().toString();
        EmailVerificationToken verificationToken = new EmailVerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        verificationToken.setCreatedAt(LocalDateTime.now());
        emailVerificationTokenRepository.save(verificationToken);
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
        
        return String.format("%s/auth/verified?accessToken=%s&refreshToken=%s&email=%s&name=%s&role=%s",
                frontendUrl,
                jwtDto.getToken(),
                jwtDto.getRefreshToken(),
                user.getEmail(),
                user.getName(),
                user.getRole().name());
    }

    @Override
    @Transactional
    public void resendOwnerCredentialsEmail(String ownerEmail) {
        User user = userRepository.findByEmail(ownerEmail);
        if (user == null) {
            throw new EntityNotFoundException("Собственник не найден с email: " + ownerEmail);
        }

        if (user.getRole() != com.rental.manager.entities.enums.UserRole.OWNER) {
            throw new IllegalArgumentException("Пользователь не является собственником");
        }

        // Генерируем новый пароль
        String newPassword = UUID.randomUUID().toString().substring(0, 8);
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setEmailVerified(false); // Сбрасываем верификацию
        userRepository.save(user);

        // Отправляем письмо с новыми учетными данными
        sendOwnerCredentialsEmail(ownerEmail, user.getName(), newPassword);

        // Создаём токен верификации
        createAndSendVerificationToken(ownerEmail);
    }
}
