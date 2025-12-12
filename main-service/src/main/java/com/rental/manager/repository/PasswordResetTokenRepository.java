package com.rental.manager.repository;

import com.rental.manager.entities.PasswordResetToken;
import com.rental.manager.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {
    PasswordResetToken findByToken(String token);

    PasswordResetToken findByUser(User user);

    void deleteByUser(User user);

    void deleteByExpiryDateBefore(LocalDateTime dateTime);
}
