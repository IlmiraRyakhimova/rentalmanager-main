package com.rental.manager.repository;

import com.rental.manager.entities.User;
import com.rental.manager.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByAuthServiceUserId(String authServiceUserId);

    List<User> findByRole(UserRole role);
    List<User> findByNameContainingIgnoreCase(String name);


    boolean existsByEmail(String email);
    boolean existsByAuthServiceUserId(String authServiceUserId);


}