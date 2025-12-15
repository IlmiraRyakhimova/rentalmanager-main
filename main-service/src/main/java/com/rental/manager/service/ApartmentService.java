package com.rental.manager.service;

import com.rental.manager.dto.requestdto.ApartmentPatchRequestDto;
import com.rental.manager.dto.requestdto.ApartmentRequestDto;
import com.rental.manager.dto.responsedto.ApartmentResponseDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface ApartmentService {

    ApartmentResponseDto createApartment(ApartmentRequestDto request);
    ApartmentResponseDto updateApartment(UUID id, ApartmentRequestDto request);
    ApartmentResponseDto patchApartment(UUID id, ApartmentPatchRequestDto request);
    void deleteApartment(UUID id);

    ApartmentResponseDto getApartmentById(UUID id);
    List<ApartmentResponseDto> getApartmentsByOwnerId(UUID ownerId);
    List<ApartmentResponseDto> getApartmentsByOwnerName(String ownerName);
    List <ApartmentResponseDto> getApartmentsByOwnerEmail(String ownerEmail);
    List<ApartmentResponseDto> getApartmentsByOwnerPhoneNumber(String ownerPhoneNumber);


    List<ApartmentResponseDto> getApartmentsByTitle(String title);
    List<ApartmentResponseDto> getApartmentsByAccommodationType(String accommodationType);

    List<ApartmentResponseDto> getApartmentsByPostalCode(String postalCode);
    List<ApartmentResponseDto> getApartmentsByCountry(String country);
    List<ApartmentResponseDto> getApartmentsByCity(String city);
    List<ApartmentResponseDto> getApartmentsByDistrict(String district);
    List<ApartmentResponseDto> getApartmentsByStreet(String street);

    List<ApartmentResponseDto> getApartmentsByPricePerNightRange(BigDecimal minPrice, BigDecimal maxPrice);
    List<ApartmentResponseDto> getApartmentsByAreaRange(Double minArea, Double maxArea);
    List<ApartmentResponseDto> getApartmentsByNumberOfRooms(Integer numberOfRooms);
    List<ApartmentResponseDto> getApartmentsByNumberOfBathrooms(Integer numberOfBathrooms);

    List<ApartmentResponseDto> getAllApartments();
}
