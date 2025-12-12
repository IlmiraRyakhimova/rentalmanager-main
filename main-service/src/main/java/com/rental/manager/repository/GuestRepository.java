package com.rental.manager.repository;

import com.rental.manager.entities.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GuestRepository extends JpaRepository<Guest, UUID> {
    List<Guest> findByName(String name);
    Guest findByEmail(String email);
    Guest findByPhoneNumber(String phoneNumber);
}
