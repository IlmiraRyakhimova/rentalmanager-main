package com.rental.manager.repository;

import com.rental.manager.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findById(UUID id);
    List<User> findByNameContainingIgnoreCase(String name);
    User findByEmail(String email);
    User findByPhoneNumber(String phoneNumber);
    void deleteById(UUID id);

}