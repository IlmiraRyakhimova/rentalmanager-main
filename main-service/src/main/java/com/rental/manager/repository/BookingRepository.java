package com.rental.manager.repository;

import com.rental.manager.entities.Booking;
import com.rental.manager.entities.enums.BookingStatus;
import com.rental.manager.entities.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {

    Booking findByBookingCode(String bookingCode);
    List<Booking> findByMainGuestNameContainingIgnoreCase(String guestName);
    List<Booking> findByGuestPhoneNumberContainingIgnoreCase(String guestPhoneNumber);
    List<Booking>  findByGuestEmailContainingIgnoreCase(String guestEmail);
    List<Booking> findByBookingStatus(BookingStatus bookingStatus);
    List<Booking> findByPaymentStatus(PaymentStatus paymentStatus);

    List<Booking> findByApartmentId(UUID apartmentId);
    List<Booking> findByApartmentTitleContainingIgnoreCase(String apartmentTitle);

    List<Booking> findByApartmentOwnerId(UUID ownerId);
    List<Booking> findByApartmentOwnerNameContainingIgnoreCase(String ownerName);
    List<Booking> findByApartmentOwnerEmailContainingIgnoreCase(String ownerEmail);
    List<Booking> findByApartmentOwnerPhoneNumberContainingIgnoreCase(String ownerPhoneNumber);







}
