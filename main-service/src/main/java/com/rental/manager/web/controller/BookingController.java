package com.rental.manager.web.controller;

import com.rental.manager.dto.requestdto.BookingPatchRequestDto;
import com.rental.manager.dto.requestdto.BookingPaymentStatusPatchRequestDto;
import com.rental.manager.dto.requestdto.BookingRequestDto;
import com.rental.manager.dto.requestdto.BookingStatusPatchRequestDto;
import com.rental.manager.dto.responsedto.BookingResponseDto;
import com.rental.manager.entities.enums.BookingStatus;
import com.rental.manager.entities.enums.PaymentStatus;
import com.rental.manager.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping

    public ResponseEntity<BookingResponseDto> createBooking(@RequestBody @Validated BookingRequestDto request) {
        return ResponseEntity.ok(bookingService.createBooking(request));
    }

    @GetMapping
    public ResponseEntity<List<BookingResponseDto>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingResponseDto> updateBooking(@PathVariable UUID id, @RequestBody @Validated BookingRequestDto request) {
        return ResponseEntity.ok(bookingService.updateBooking(id, request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookingResponseDto> patchBooking(@PathVariable UUID id, @RequestBody BookingPatchRequestDto request) {
        return ResponseEntity.ok(bookingService.patchBooking(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable UUID id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDto> getBookingById(@PathVariable UUID id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping("/search/by-booking-code/{bookingCode}")
    public ResponseEntity<BookingResponseDto> getBookingByBookingCode(@PathVariable String bookingCode) {
        return ResponseEntity.ok(bookingService.getBookingByBookingCode(bookingCode));
    }

    @GetMapping("/search/by-guest-name/{guestName}")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByGuestName(@PathVariable String guestName) {
        return ResponseEntity.ok(bookingService.getBookingsByGuestName(guestName));
    }

    @GetMapping("/search/by-guest-email/{guestEmail}")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByGuestEmail(@PathVariable String guestEmail) {
        return ResponseEntity.ok(bookingService.getBookingsByGuestEmail(guestEmail));
    }

    @GetMapping("/search/by-guest-phone-number/{guestPhoneNumber}")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByGuestPhoneNumber(@PathVariable String guestPhoneNumber) {
        return ResponseEntity.ok(bookingService.getBookingsByGuestPhoneNumber(guestPhoneNumber));
    }

    @GetMapping("/search/by-booking-status/{bookingStatus}")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByBookingStatus(@PathVariable BookingStatus bookingStatus) {
        return ResponseEntity.ok(bookingService.getBookingsByBookingStatus(bookingStatus));
    }

    @GetMapping("/search/by-payment-status/{paymentStatus}")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByPaymentStatus(@PathVariable PaymentStatus paymentStatus) {
        return ResponseEntity.ok(bookingService.getBookingsByPaymentStatus(paymentStatus));
    }

    @GetMapping("/search/by-apartment-id/{apartmentId}")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByApartmentId(@PathVariable UUID apartmentId) {
        return ResponseEntity.ok(bookingService.getBookingsByApartmentId(apartmentId));
    }

    @GetMapping("/search/by-apartment-title/{apartmentTitle}")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByApartmentTitle(@PathVariable String apartmentTitle) {
        return ResponseEntity.ok(bookingService.getBookingsByApartmentTitle(apartmentTitle));
    }

    @GetMapping("/search/by-apartment-owner-id/{ownerId}")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByApartmentOwnerId(@PathVariable UUID ownerId) {
        return ResponseEntity.ok(bookingService.getBookingsByApartmentOwnerId(ownerId));
    }

    @GetMapping("/search/by-apartment-owner-name/{ownerName}")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByApartmentOwnerName(@PathVariable String ownerName) {
        return ResponseEntity.ok(bookingService.getBookingsByApartmentOwnerName(ownerName));
    }

    @GetMapping("/search/by-apartment-owner-email/{ownerEmail}")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByApartmentOwnerEmail(@PathVariable String ownerEmail) {
        return ResponseEntity.ok(bookingService.getBookingsByApartmentOwnerEmail(ownerEmail));
    }

    @GetMapping("/search/by-apartment-owner-phone-number/{ownerPhoneNumber}")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByApartmentOwnerPhoneNumber(@PathVariable String ownerPhoneNumber) {
        return ResponseEntity.ok(bookingService.getBookingsByApartmentOwnerPhoneNumber(ownerPhoneNumber));
    }

    @PatchMapping("/booking-status/{id}")
    public ResponseEntity<BookingResponseDto> patchBookingStatus(@PathVariable UUID id,
                                                                 @RequestBody BookingStatusPatchRequestDto request) {
        return ResponseEntity.ok(bookingService.patchBookingStatus(id, request));
    }

    @PatchMapping("/payment-status/{id}")
    public ResponseEntity<BookingResponseDto> patchPaymentStatus(@PathVariable UUID id,
                                                                 @RequestBody BookingPaymentStatusPatchRequestDto request) {
        return ResponseEntity.ok(bookingService.patchPaymentStatus(id, request));
    }

}
