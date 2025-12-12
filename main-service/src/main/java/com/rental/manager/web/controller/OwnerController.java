package com.rental.manager.web.controller;

import com.rental.manager.dto.responsedto.ApartmentResponseDTO;
import com.rental.manager.dto.responsedto.BookingResponseDTO;
import com.rental.manager.service.ApartmentService;
import com.rental.manager.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/owner")
@RequiredArgsConstructor
public class OwnerController {
    private final ApartmentService apartmentService;
    private final BookingService bookingService;

    @GetMapping("/my-apartments")
    public ResponseEntity<List<ApartmentResponseDTO>> getMyApartments(Principal principal) {
        String ownerEmail = principal.getName();
        return ResponseEntity.ok(apartmentService.getApartmentsByOwnerEmail(ownerEmail));
    }

    @GetMapping("/my-bookings")
    public ResponseEntity<List<BookingResponseDTO>> getMyBookings(Principal principal) {
        String ownerEmail = principal.getName();
        return ResponseEntity.ok(bookingService.getBookingsByApartmentOwnerEmail(ownerEmail));
    }
}
