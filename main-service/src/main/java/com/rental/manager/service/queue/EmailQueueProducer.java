package com.rental.manager.service.queue;

import com.rental.manager.dto.requestdto.EmailTaskRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailQueueProducer {
    private final RedisTemplate<String, Object> redisTemplate;

    public void enqueueEmailTask(EmailTaskRequestDto dto) {
        redisTemplate.opsForList().rightPush("emailQueue", dto);
    }
}
