package com.rental.manager.service;

import com.rental.manager.dto.requestdto.BookingPatchRequestDTO;
import com.rental.manager.dto.requestdto.BookingPaymentStatusPatchRequestDTO;
import com.rental.manager.dto.requestdto.BookingRequestDTO;
import com.rental.manager.dto.requestdto.BookingStatusPatchRequestDTO;
import com.rental.manager.dto.responsedto.BookingResponseDTO;
import com.rental.manager.entities.enums.BookingStatus;
import com.rental.manager.entities.enums.PaymentStatus;

import java.util.List;
import java.util.UUID;

public interface BookingService {
    BookingResponseDTO createBooking(BookingRequestDTO request);
    BookingResponseDTO updateBooking(UUID id, BookingRequestDTO request);
    BookingResponseDTO patchBooking(UUID id, BookingPatchRequestDTO request);

    BookingResponseDTO patchBookingStatus(UUID id, BookingStatusPatchRequestDTO request);
    BookingResponseDTO patchPaymentStatus(UUID id, BookingPaymentStatusPatchRequestDTO request);
    void deleteBooking(UUID id);

    List<BookingResponseDTO> getAllBookings();
    BookingResponseDTO getBookingById(UUID id);
    BookingResponseDTO getBookingByBookingCode(String bookingCode);
    List<BookingResponseDTO>  getBookingsByGuestName(String guestName);
    List<BookingResponseDTO> getBookingsByGuestEmail(String guestEmail);
    List<BookingResponseDTO> getBookingsByGuestPhoneNumber(String guestPhoneNumber);
    List<BookingResponseDTO> getBookingsByBookingStatus(BookingStatus bookingStatus);
    List<BookingResponseDTO> getBookingsByPaymentStatus(PaymentStatus paymentStatus);

    List<BookingResponseDTO> getBookingsByApartmentId(UUID apartmentId);
    List<BookingResponseDTO> getBookingsByApartmentTitle(String apartmentTitle);

    List<BookingResponseDTO> getBookingsByApartmentOwnerId(UUID ownerId);
    List<BookingResponseDTO> getBookingsByApartmentOwnerName(String ownerName);
    List<BookingResponseDTO> getBookingsByApartmentOwnerEmail(String ownerEmail);
    List<BookingResponseDTO> getBookingsByApartmentOwnerPhoneNumber(String ownerPhoneNumber);

}
