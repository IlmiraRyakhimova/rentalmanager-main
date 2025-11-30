package com.rental.manager.service;

import com.rental.manager.dto.requestdto.ApartmentPatchRequestDTO;
import com.rental.manager.dto.requestdto.ApartmentRequestDTO;
import com.rental.manager.dto.responsedto.ApartmentResponseDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface ApartmentService {

    ApartmentResponseDTO createApartment(ApartmentRequestDTO request);
    ApartmentResponseDTO updateApartment(UUID id, ApartmentRequestDTO request);
    ApartmentResponseDTO patchApartment(UUID id, ApartmentPatchRequestDTO request);
    void deleteApartment(UUID id);

    ApartmentResponseDTO getApartmentById(UUID id);
    List<ApartmentResponseDTO> getApartmentsByOwnerId(UUID ownerId);
    List<ApartmentResponseDTO> getApartmentsByOwnerName(String ownerName);
    List <ApartmentResponseDTO> getApartmentsByOwnerEmail(String ownerEmail);
    List<ApartmentResponseDTO> getApartmentsByOwnerPhoneNumber(String ownerPhoneNumber);


    List<ApartmentResponseDTO> getApartmentsByTitle(String title);
    List<ApartmentResponseDTO> getApartmentsByAccommodationType(String accommodationType);

    List<ApartmentResponseDTO> getApartmentsByPostalCode(String postalCode);
    List<ApartmentResponseDTO> getApartmentsByCountry(String country);
    List<ApartmentResponseDTO> getApartmentsByCity(String city);
    List<ApartmentResponseDTO> getApartmentsByDistrict(String district);
    List<ApartmentResponseDTO> getApartmentsByStreet(String street);

    List<ApartmentResponseDTO> getApartmentsByPricePerNightRange(BigDecimal minPrice, BigDecimal maxPrice);
    List<ApartmentResponseDTO> getApartmentsByAreaRange(Double minArea, Double maxArea);
    List<ApartmentResponseDTO> getApartmentsByNumberOfRooms(Integer numberOfRooms);
    List<ApartmentResponseDTO> getApartmentsByNumberOfBathrooms(Integer numberOfBathrooms);

    List<ApartmentResponseDTO> getAllApartments();
}
