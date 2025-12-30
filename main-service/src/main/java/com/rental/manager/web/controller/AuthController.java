package com.rental.manager.web.controller;


import com.rental.manager.dto.requestdto.*;
import com.rental.manager.dto.responsedto.AuthResponseDto;
import com.rental.manager.security.auth.AuthService;
import com.rental.manager.security.jwt.dto.RefreshTokenDTO;
import com.rental.manager.service.EmailService;
import com.rental.manager.service.queue.EmailTaskType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.rental.manager.service.queue.EmailQueueProducer;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final EmailService emailService;
    private final EmailQueueProducer emailQueueProducer;

    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponseDto> signUp(@RequestBody @Validated SignUpRequestDto request) {
        return ResponseEntity.ok(authService.signUp(request));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponseDto> signIn(@RequestBody @Validated SignInRequestDto request) {
        return ResponseEntity.ok(authService.signIn(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponseDto> refreshAccessToken(@RequestBody @Validated RefreshTokenDTO refreshTokenDTO) {
        return ResponseEntity.ok(authService.refreshAccessToken(refreshTokenDTO));
    }

    @PostMapping("/log-out")
    public ResponseEntity<Void> logOut() {
        authService.logOut();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        String redirectUrl = emailService.verifyEmail(token);
        return ResponseEntity.ok(redirectUrl);
    }

    @PostMapping("/resend-verification-email")
    public ResponseEntity<String> resendVerificationEmail(@RequestBody EmailVerificationRequestDto request) {
        emailQueueProducer.enqueueEmailTask(
                EmailTaskRequestDto.builder()
                        .to(request.getEmail())
                        .taskType(EmailTaskType.EMAIL_VERIFICATION)
                        .build());
        return ResponseEntity.ok("Письмо с подтверждением отправлено повторно.");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody EmailVerificationRequestDto request) {
        emailQueueProducer.enqueueEmailTask(
                EmailTaskRequestDto.builder()
                        .to(request.getEmail())
                        .taskType(EmailTaskType.FORGOT_PASSWORD)
                        .build());
        return ResponseEntity.ok("Письмо для сброса пароля отправлено.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetRequestDto request) {
        emailService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok("Пароль успешно изменен");
    }

    @PostMapping("/resend-owner-credentials")
    public ResponseEntity<String> resendOwnerCredentials(@RequestBody EmailVerificationRequestDto request) {
        emailQueueProducer.enqueueEmailTask(
                EmailTaskRequestDto.builder()
                        .to(request.getEmail())
                        .taskType(EmailTaskType.RESEND_OWNER_CREDENTIALS)
                        .build());
        return ResponseEntity.ok("Письмо с новым паролем отправлено собственнику.");
    }


}

