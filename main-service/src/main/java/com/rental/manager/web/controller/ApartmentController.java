package com.rental.manager.web.controller;


import com.rental.manager.dto.requestdto.ApartmentPatchRequestDTO;
import com.rental.manager.dto.requestdto.ApartmentRequestDTO;
import com.rental.manager.dto.responsedto.ApartmentResponseDTO;
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
    public ResponseEntity<List<ApartmentResponseDTO>> getAllApartments() {
        return ResponseEntity.ok(apartmentService.getAllApartments());
    }
    
    @PostMapping
    public ResponseEntity<ApartmentResponseDTO> createApartment(@RequestBody @Validated ApartmentRequestDTO request) {
        return ResponseEntity.ok(apartmentService.createApartment(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApartmentResponseDTO> updateApartment(@PathVariable UUID id, @RequestBody @Validated ApartmentRequestDTO request) {
        return ResponseEntity.ok(apartmentService.updateApartment(id, request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApartmentResponseDTO> patchApartment(@PathVariable UUID id,
                                                              @RequestBody ApartmentPatchRequestDTO request) {
        return ResponseEntity.ok(apartmentService.patchApartment(id,
                request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApartment(@PathVariable UUID id) {
        apartmentService.deleteApartment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApartmentResponseDTO> getApartmentById(@PathVariable UUID id) {
        return ResponseEntity.ok(apartmentService.getApartmentById(id));
    }

    @GetMapping("/search/by-owner-id/{ownerId}")
    public ResponseEntity<List<ApartmentResponseDTO>> getApartmentsByOwnerId(@PathVariable UUID ownerId) {
        return ResponseEntity.ok(apartmentService.getApartmentsByOwnerId(ownerId));
    }

    @GetMapping("/search/by-owner-name/{ownerName}")
    public ResponseEntity<List<ApartmentResponseDTO>> getApartmentsByOwnerName(@PathVariable String ownerName) {
        return ResponseEntity.ok(apartmentService.getApartmentsByOwnerName(ownerName));
    }

    @GetMapping("/search/by-owner-email/{ownerEmail}")
    public ResponseEntity<List<ApartmentResponseDTO>> getApartmentsByOwnerEmail(@PathVariable String ownerEmail) {
        return ResponseEntity.ok(apartmentService.getApartmentsByOwnerEmail(ownerEmail));
    }

    @GetMapping("/search/by-owner-phone/{ownerPhoneNumber}")
    public ResponseEntity<List<ApartmentResponseDTO>> getApartmentsByOwnerPhoneNumber(@PathVariable String ownerPhoneNumber) {
        return ResponseEntity.ok(apartmentService.getApartmentsByOwnerPhoneNumber(ownerPhoneNumber));
    }

    @GetMapping("/search/by-title/{title}")
    public ResponseEntity<List<ApartmentResponseDTO>> getApartmentsByTitle(@PathVariable String title) {
        return ResponseEntity.ok(apartmentService.getApartmentsByTitle(title));
    }

    @GetMapping("/search/by-accommodation-type/{accommodationType}")
    public ResponseEntity<List<ApartmentResponseDTO>> getApartmentsByAccommodationType(@PathVariable String accommodationType) {
        return ResponseEntity.ok(apartmentService.getApartmentsByAccommodationType(accommodationType));
    }

    @GetMapping("/search/by-postal-code/{postalCode}")
    public ResponseEntity<List<ApartmentResponseDTO>> getApartmentsByPostalCode(@PathVariable String postalCode) {
        return ResponseEntity.ok(apartmentService.getApartmentsByPostalCode(postalCode));
    }

    @GetMapping("/search/by-country/{country}")
    public ResponseEntity<List<ApartmentResponseDTO>> getApartmentsByCountry(@PathVariable String country) {
        return ResponseEntity.ok(apartmentService.getApartmentsByCountry(country));
    }

    @GetMapping("/search/by-city/{city}")
    public ResponseEntity<List<ApartmentResponseDTO>> getApartmentsByCity(@PathVariable String city) {
        return ResponseEntity.ok(apartmentService.getApartmentsByCity(city));
    }

    @GetMapping("/search/by-district/{district}")
    public ResponseEntity<List<ApartmentResponseDTO>> getApartmentsByDistrict(@PathVariable String district) {
        return ResponseEntity.ok(apartmentService.getApartmentsByDistrict(district));
    }

    @GetMapping("/search/by-street/{street}")
    public ResponseEntity<List<ApartmentResponseDTO>> getApartmentsByStreet(@PathVariable String street) {
        return ResponseEntity.ok(apartmentService.getApartmentsByStreet(street));
    }

    @GetMapping("/search/by-price-per-night/{minPrice}/{maxPrice}")
    public ResponseEntity<List<ApartmentResponseDTO>> getApartmentsByPricePerNightRange(@PathVariable BigDecimal minPrice, @PathVariable BigDecimal maxPrice) {
        return ResponseEntity.ok(apartmentService.getApartmentsByPricePerNightRange(minPrice, maxPrice));
    }

    @GetMapping("/search/by-area/{minArea}/{maxArea}")
    public ResponseEntity<List<ApartmentResponseDTO>> getApartmentsByAreaRange(@PathVariable Double minArea, @PathVariable Double maxArea) {
        return ResponseEntity.ok(apartmentService.getApartmentsByAreaRange(minArea, maxArea));
    }

    @GetMapping("/search/by-number-of-rooms/{numberOfRooms}")
    public ResponseEntity<List<ApartmentResponseDTO>> getApartmentsByNumberOfRooms(@PathVariable Integer numberOfRooms) {
        return ResponseEntity.ok(apartmentService.getApartmentsByNumberOfRooms(numberOfRooms));
    }
    @GetMapping("/search/by-number-of-bathrooms/{numberOfBathrooms}")
    public ResponseEntity<List<ApartmentResponseDTO>> getApartmentsByNumberOfBathrooms(@PathVariable Integer numberOfBathrooms) {
        return ResponseEntity.ok(apartmentService.getApartmentsByNumberOfBathrooms(numberOfBathrooms));
    }
}
