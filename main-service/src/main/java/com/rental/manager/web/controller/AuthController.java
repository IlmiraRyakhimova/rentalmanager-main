package com.rental.manager.web.controller;


import com.rental.manager.dto.requestdto.EmailVerificationRequestDTO;
import com.rental.manager.dto.requestdto.PasswordResetRequestDTO;
import com.rental.manager.dto.requestdto.SignInRequestDTO;
import com.rental.manager.dto.requestdto.SignUpRequestDTO;
import com.rental.manager.dto.responsedto.AuthResponseDTO;
import com.rental.manager.security.auth.AuthService;
import com.rental.manager.security.jwt.dto.RefreshTokenDTO;
import com.rental.manager.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final EmailService emailService;

    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponseDTO> signUp(@RequestBody @Validated SignUpRequestDTO request) {
        return ResponseEntity.ok(authService.signUp(request));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponseDTO> signIn(@RequestBody @Validated SignInRequestDTO request) {
        return ResponseEntity.ok(authService.signIn(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponseDTO> refreshAccessToken(@RequestBody @Validated RefreshTokenDTO refreshTokenDTO) {
        return ResponseEntity.ok(authService.refreshAccessToken(refreshTokenDTO));
    }

    @PostMapping("/log-out")
    public ResponseEntity<Void> logOut() {
        authService.logOut();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        emailService.verifyEmail(token);
        return ResponseEntity.ok("Email успешно подтвержден");
    }

    @PostMapping("/resend-verification-email")
    public ResponseEntity<String> resendVerificationEmail(@RequestBody EmailVerificationRequestDTO request) {
        emailService.createAndSendVerificationToken(request.getEmail());
        return ResponseEntity.ok("Письмо с подтверждением отправлено повторно.");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody EmailVerificationRequestDTO request) {
        emailService.createAndSendPasswordResetToken(request.getEmail());
        return ResponseEntity.ok("Письмо для сброса пароля отправлено.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetRequestDTO request) {
        emailService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok("Пароль успешно изменен");
    }


}

