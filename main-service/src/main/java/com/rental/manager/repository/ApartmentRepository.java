package com.rental.manager.repository;

import com.rental.manager.entities.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface ApartmentRepository extends JpaRepository<Apartment, UUID> {

    List<Apartment> findByOwnerId(UUID ownerId);
    List<Apartment> findByAgentId(UUID agentId);
    List<Apartment> findByOwnerNameContainingIgnoreCase(String ownerName);
    List<Apartment> findByOwnerEmailContainingIgnoreCase(String ownerEmail);
    List<Apartment> findByOwnerPhoneNumberContainingIgnoreCase(String ownerPhoneNumber);
    List<Apartment> findByAccommodationTypeContainingIgnoreCase(String accommodationType);

    List<Apartment> findByTitleContainingIgnoreCase(String title);
    List<Apartment> findByAddressPostalCode(String postalCode);
    List<Apartment> findByAddressCountryContainingIgnoreCase(String country);
    List<Apartment> findByAddressCityContainingIgnoreCase(String city);
    List<Apartment> findByAddressDistrictContainingIgnoreCase(String district);
    List<Apartment> findByAddressStreetContainingIgnoreCase(String street);

    List<Apartment> findByAreaBetween(Double minArea, Double maxArea);
    List<Apartment> findByNumberOfRooms(Integer numberOfRooms);
    List<Apartment> findByNumberOfBathrooms(Integer numberOfBathrooms);
    List<Apartment> findByPricePerNightBetween(BigDecimal minPrice, BigDecimal maxPrice);



}
