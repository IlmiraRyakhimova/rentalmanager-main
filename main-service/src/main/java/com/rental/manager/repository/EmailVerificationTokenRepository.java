package com.rental.manager.repository;

import com.rental.manager.entities.EmailVerificationToken;
import com.rental.manager.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, UUID> {

    EmailVerificationToken findByToken(String token);

    EmailVerificationToken findByUser(User user);

    void deleteByUser(User user);

    void deleteByExpiryDateBefore(LocalDateTime dateTime);
}
