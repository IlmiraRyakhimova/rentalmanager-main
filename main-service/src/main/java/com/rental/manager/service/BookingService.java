package com.rental.manager.service;

import com.rental.manager.dto.requestdto.BookingPatchRequestDto;
import com.rental.manager.dto.requestdto.BookingPaymentStatusPatchRequestDto;
import com.rental.manager.dto.requestdto.BookingRequestDto;
import com.rental.manager.dto.requestdto.BookingStatusPatchRequestDto;
import com.rental.manager.dto.responsedto.BookingResponseDto;
import com.rental.manager.entities.enums.BookingStatus;
import com.rental.manager.entities.enums.PaymentStatus;

import java.util.List;
import java.util.UUID;

public interface BookingService {
    BookingResponseDto createBooking(BookingRequestDto request);
    BookingResponseDto updateBooking(UUID id, BookingRequestDto request);
    BookingResponseDto patchBooking(UUID id, BookingPatchRequestDto request);

    BookingResponseDto patchBookingStatus(UUID id, BookingStatusPatchRequestDto request);
    BookingResponseDto patchPaymentStatus(UUID id, BookingPaymentStatusPatchRequestDto request);
    void deleteBooking(UUID id);

    List<BookingResponseDto> getAllBookings();
    BookingResponseDto getBookingById(UUID id);
    BookingResponseDto getBookingByBookingCode(String bookingCode);
    List<BookingResponseDto>  getBookingsByGuestName(String guestName);
    List<BookingResponseDto> getBookingsByGuestEmail(String guestEmail);
    List<BookingResponseDto> getBookingsByGuestPhoneNumber(String guestPhoneNumber);
    List<BookingResponseDto> getBookingsByBookingStatus(BookingStatus bookingStatus);
    List<BookingResponseDto> getBookingsByPaymentStatus(PaymentStatus paymentStatus);

    List<BookingResponseDto> getBookingsByApartmentId(UUID apartmentId);
    List<BookingResponseDto> getBookingsByApartmentTitle(String apartmentTitle);

    List<BookingResponseDto> getBookingsByApartmentOwnerId(UUID ownerId);
    List<BookingResponseDto> getBookingsByApartmentOwnerName(String ownerName);
    List<BookingResponseDto> getBookingsByApartmentOwnerEmail(String ownerEmail);
    List<BookingResponseDto> getBookingsByApartmentOwnerPhoneNumber(String ownerPhoneNumber);

}
