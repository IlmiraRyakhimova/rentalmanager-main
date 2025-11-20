package com.rental.manager.repository;

import com.rental.manager.entities.Booking;
import com.rental.manager.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
    
}
