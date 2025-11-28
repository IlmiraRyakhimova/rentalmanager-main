package com.rental.manager.web.controller;

import com.rental.manager.dto.requestdto.BookingPatchRequestDTO;
import com.rental.manager.dto.requestdto.BookingPaymentStatusPatchRequestDTO;
import com.rental.manager.dto.requestdto.BookingRequestDTO;
import com.rental.manager.dto.requestdto.BookingStatusPatchRequestDTO;
import com.rental.manager.dto.responsedto.BookingResponseDTO;
import com.rental.manager.entities.Booking;
import com.rental.manager.entities.enums.BookingStatus;
import com.rental.manager.entities.enums.PaymentStatus;
import com.rental.manager.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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

    public ResponseEntity<BookingResponseDTO> createBooking(@RequestBody @Validated BookingRequestDTO request) {
        return ResponseEntity.ok(bookingService.createBooking(request));
    }

    @GetMapping
    public ResponseEntity<List<BookingResponseDTO>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> updateBooking(@PathVariable UUID id, @RequestBody @Validated BookingRequestDTO request) {
        return ResponseEntity.ok(bookingService.updateBooking(id, request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> patchBooking(@PathVariable UUID id, @RequestBody BookingPatchRequestDTO request) {
        return ResponseEntity.ok(bookingService.patchBooking(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable UUID id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> getBookingById(@PathVariable UUID id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping("/search/by-booking-code/{bookingCode}")
    public ResponseEntity<BookingResponseDTO> getBookingByBookingCode(@PathVariable String bookingCode) {
        return ResponseEntity.ok(bookingService.getBookingByBookingCode(bookingCode));
    }

    @GetMapping("/search/by-guest-name/{guestName}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByGuestName(@PathVariable String guestName) {
        return ResponseEntity.ok(bookingService.getBookingsByGuestName(guestName));
    }

    @GetMapping("/search/by-guest-email/{guestEmail}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByGuestEmail(@PathVariable String guestEmail) {
        return ResponseEntity.ok(bookingService.getBookingsByGuestEmail(guestEmail));
    }

    @GetMapping("/search/by-guest-phone-number/{guestPhoneNumber}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByGuestPhoneNumber(@PathVariable String guestPhoneNumber) {
        return ResponseEntity.ok(bookingService.getBookingsByGuestPhoneNumber(guestPhoneNumber));
    }

    @GetMapping("/search/by-booking-status/{bookingStatus}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByBookingStatus(@PathVariable BookingStatus bookingStatus) {
        return ResponseEntity.ok(bookingService.getBookingsByBookingStatus(bookingStatus));
    }

    @GetMapping("/search/by-payment-status/{paymentStatus}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByPaymentStatus(@PathVariable PaymentStatus paymentStatus) {
        return ResponseEntity.ok(bookingService.getBookingsByPaymentStatus(paymentStatus));
    }

    @GetMapping("/search/by-apartment-id/{apartmentId}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByApartmentId(@PathVariable UUID apartmentId) {
        return ResponseEntity.ok(bookingService.getBookingsByApartmentId(apartmentId));
    }

    @GetMapping("/search/by-apartment-title/{apartmentTitle}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByApartmentTitle(@PathVariable String apartmentTitle) {
        return ResponseEntity.ok(bookingService.getBookingsByApartmentTitle(apartmentTitle));
    }

    @GetMapping("/search/by-apartment-owner-id/{ownerId}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByApartmentOwnerId(@PathVariable UUID ownerId) {
        return ResponseEntity.ok(bookingService.getBookingsByApartmentOwnerId(ownerId));
    }

    @GetMapping("/search/by-apartment-owner-name/{ownerName}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByApartmentOwnerName(@PathVariable String ownerName) {
        return ResponseEntity.ok(bookingService.getBookingsByApartmentOwnerName(ownerName));
    }

    @GetMapping("/search/by-apartment-owner-email/{ownerEmail}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByApartmentOwnerEmail(@PathVariable String ownerEmail) {
        return ResponseEntity.ok(bookingService.getBookingsByApartmentOwnerEmail(ownerEmail));
    }

    @GetMapping("/search/by-apartment-owner-phone-number/{ownerPhoneNumber}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByApartmentOwnerPhoneNumber(@PathVariable String ownerPhoneNumber) {
        return ResponseEntity.ok(bookingService.getBookingsByApartmentOwnerPhoneNumber(ownerPhoneNumber));
    }

    @PatchMapping("/booking-status/{id}")
    public ResponseEntity<BookingResponseDTO> patchBookingStatus(@PathVariable UUID id,
                                                                 @RequestBody BookingStatusPatchRequestDTO request) {
        return ResponseEntity.ok(bookingService.patchBookingStatus(id, request));
    }

    @PatchMapping("/payment-status/{id}")
    public ResponseEntity<BookingResponseDTO> patchPaymentStatus(@PathVariable UUID id,
                                                                 @RequestBody BookingPaymentStatusPatchRequestDTO request) {
        return ResponseEntity.ok(bookingService.patchPaymentStatus(id, request));
    }

}
