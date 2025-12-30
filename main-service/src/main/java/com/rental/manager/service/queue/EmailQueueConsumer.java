package com.rental.manager.service.queue;

import com.rental.manager.dto.requestdto.EmailTaskRequestDto;
import com.rental.manager.service.EmailService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailQueueConsumer {
    private final RedisTemplate<String, Object> redisTemplate;
    private final EmailService emailService;

    @PostConstruct
    public void init() {
        System.out.println("✅ EmailQueueConsumer запущен!");
    }

    @Scheduled(fixedDelay = 1000)
    public void consume() {
            EmailTaskRequestDto dto = (EmailTaskRequestDto) redisTemplate.opsForList().leftPop("emailQueue");
            if (dto == null) return;
            switch (dto.getTaskType()) {
                case EMAIL_VERIFICATION -> emailService.createAndSendVerificationToken(dto.getTo());
                case OWNER_CREDENTIALS -> {
                    emailService.sendOwnerCredentialsEmail(dto.getTo(), dto.getOwnerName(), dto.getOwnerPassword());
                    emailService.createAndSendVerificationToken(dto.getTo());
                }

                case RESEND_OWNER_CREDENTIALS -> emailService.resendOwnerCredentialsEmail(dto.getTo());
                case FORGOT_PASSWORD -> emailService.createAndSendPasswordResetToken(dto.getTo());
                case BOOKING_INFO_TO_GUEST -> emailService.sendBookingInfoToGuest(
                        dto.getTo(),
                        dto.getGuestName(),
                        dto.getBookingCode(),
                        dto.getCheckIn(),
                        dto.getCheckOut(),
                        dto. getPrice()
                );
                case BOOKING_INFO_TO_OWNER -> emailService.sendBookingInfoToOwner(
                        dto.getTo(),
                        dto.getOwnerName(),
                        dto.getGuestName(),
                        dto.getBookingCode(),
                        dto.getCheckIn(),
                        dto.getCheckOut(),
                        dto.getPrice()
                );
            }
    }
}
