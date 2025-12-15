package com.rental.manager.web.controller;


import com.rental.manager.dto.requestdto.ApartmentPatchRequestDto;
import com.rental.manager.dto.requestdto.ApartmentRequestDto;
import com.rental.manager.dto.responsedto.ApartmentResponseDto;
import com.rental.manager.service.ApartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/apartments")
@RequiredArgsConstructor
public class ApartmentController {
    private final ApartmentService apartmentService;

    @GetMapping
    public ResponseEntity<List<ApartmentResponseDto>> getAllApartments() {
        return ResponseEntity.ok(apartmentService.getAllApartments());
    }
    
    @PostMapping
    public ResponseEntity<ApartmentResponseDto> createApartment(@RequestBody @Validated ApartmentRequestDto request) {
        return ResponseEntity.ok(apartmentService.createApartment(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApartmentResponseDto> updateApartment(@PathVariable UUID id, @RequestBody @Validated ApartmentRequestDto request) {
        return ResponseEntity.ok(apartmentService.updateApartment(id, request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApartmentResponseDto> patchApartment(@PathVariable UUID id,
                                                               @RequestBody ApartmentPatchRequestDto request) {
        return ResponseEntity.ok(apartmentService.patchApartment(id,
                request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApartment(@PathVariable UUID id) {
        apartmentService.deleteApartment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApartmentResponseDto> getApartmentById(@PathVariable UUID id) {
        return ResponseEntity.ok(apartmentService.getApartmentById(id));
    }

    @GetMapping("/search/by-owner-id/{ownerId}")
    public ResponseEntity<List<ApartmentResponseDto>> getApartmentsByOwnerId(@PathVariable UUID ownerId) {
        return ResponseEntity.ok(apartmentService.getApartmentsByOwnerId(ownerId));
    }

    @GetMapping("/search/by-owner-name/{ownerName}")
    public ResponseEntity<List<ApartmentResponseDto>> getApartmentsByOwnerName(@PathVariable String ownerName) {
        return ResponseEntity.ok(apartmentService.getApartmentsByOwnerName(ownerName));
    }

    @GetMapping("/search/by-owner-email/{ownerEmail}")
    public ResponseEntity<List<ApartmentResponseDto>> getApartmentsByOwnerEmail(@PathVariable String ownerEmail) {
        return ResponseEntity.ok(apartmentService.getApartmentsByOwnerEmail(ownerEmail));
    }

    @GetMapping("/search/by-owner-phone/{ownerPhoneNumber}")
    public ResponseEntity<List<ApartmentResponseDto>> getApartmentsByOwnerPhoneNumber(@PathVariable String ownerPhoneNumber) {
        return ResponseEntity.ok(apartmentService.getApartmentsByOwnerPhoneNumber(ownerPhoneNumber));
    }

    @GetMapping("/search/by-title/{title}")
    public ResponseEntity<List<ApartmentResponseDto>> getApartmentsByTitle(@PathVariable String title) {
        return ResponseEntity.ok(apartmentService.getApartmentsByTitle(title));
    }

    @GetMapping("/search/by-accommodation-type/{accommodationType}")
    public ResponseEntity<List<ApartmentResponseDto>> getApartmentsByAccommodationType(@PathVariable String accommodationType) {
        return ResponseEntity.ok(apartmentService.getApartmentsByAccommodationType(accommodationType));
    }

    @GetMapping("/search/by-postal-code/{postalCode}")
    public ResponseEntity<List<ApartmentResponseDto>> getApartmentsByPostalCode(@PathVariable String postalCode) {
        return ResponseEntity.ok(apartmentService.getApartmentsByPostalCode(postalCode));
    }

    @GetMapping("/search/by-country/{country}")
    public ResponseEntity<List<ApartmentResponseDto>> getApartmentsByCountry(@PathVariable String country) {
        return ResponseEntity.ok(apartmentService.getApartmentsByCountry(country));
    }

    @GetMapping("/search/by-city/{city}")
    public ResponseEntity<List<ApartmentResponseDto>> getApartmentsByCity(@PathVariable String city) {
        return ResponseEntity.ok(apartmentService.getApartmentsByCity(city));
    }

    @GetMapping("/search/by-district/{district}")
    public ResponseEntity<List<ApartmentResponseDto>> getApartmentsByDistrict(@PathVariable String district) {
        return ResponseEntity.ok(apartmentService.getApartmentsByDistrict(district));
    }

    @GetMapping("/search/by-street/{street}")
    public ResponseEntity<List<ApartmentResponseDto>> getApartmentsByStreet(@PathVariable String street) {
        return ResponseEntity.ok(apartmentService.getApartmentsByStreet(street));
    }

    @GetMapping("/search/by-price-per-night/{minPrice}/{maxPrice}")
    public ResponseEntity<List<ApartmentResponseDto>> getApartmentsByPricePerNightRange(@PathVariable BigDecimal minPrice, @PathVariable BigDecimal maxPrice) {
        return ResponseEntity.ok(apartmentService.getApartmentsByPricePerNightRange(minPrice, maxPrice));
    }

    @GetMapping("/search/by-area/{minArea}/{maxArea}")
    public ResponseEntity<List<ApartmentResponseDto>> getApartmentsByAreaRange(@PathVariable Double minArea, @PathVariable Double maxArea) {
        return ResponseEntity.ok(apartmentService.getApartmentsByAreaRange(minArea, maxArea));
    }

    @GetMapping("/search/by-number-of-rooms/{numberOfRooms}")
    public ResponseEntity<List<ApartmentResponseDto>> getApartmentsByNumberOfRooms(@PathVariable Integer numberOfRooms) {
        return ResponseEntity.ok(apartmentService.getApartmentsByNumberOfRooms(numberOfRooms));
    }
    @GetMapping("/search/by-number-of-bathrooms/{numberOfBathrooms}")
    public ResponseEntity<List<ApartmentResponseDto>> getApartmentsByNumberOfBathrooms(@PathVariable Integer numberOfBathrooms) {
        return ResponseEntity.ok(apartmentService.getApartmentsByNumberOfBathrooms(numberOfBathrooms));
    }
}
