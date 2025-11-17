package com.rental.manager.service.impl;

import com.rental.manager.dto.responsedto.ApartmentResponseDTO;
import com.rental.manager.dto.responsedto.UserResponseDTO;
import com.rental.manager.entities.User;
import com.rental.manager.entities.apartment.Address;
import com.rental.manager.entities.apartment.Apartment;
import com.rental.manager.mappers.ApartmentMapper;
import com.rental.manager.repository.ApartmentRepository;
import com.rental.manager.repository.UserRepository;
import com.rental.manager.service.AddressService;
import com.rental.manager.service.ApartmentService;
import com.rental.manager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ApartmentServiceImpl implements ApartmentService {

    private final ApartmentRepository apartmentRepository;
    private final ApartmentMapper mapper;
    private final UserRepository userRepository;
    private final AddressService addressService;
    private final UserService userService;

    @Override
    public ApartmentResponseDTO createApartment(Apartment apartment) {
        User owner = apartment.getOwner();
        User existingOwner = userRepository.findByPhoneNumber(owner.getPhoneNumber());
        apartment.setOwner(existingOwner != null ? existingOwner : userRepository.save(owner));
        User agent = userRepository.findById(apartment.getAgent().getId()).orElseThrow();
        apartment.setAgent(agent);
        return mapper.toDTO(apartmentRepository.save(apartment));
    }

    @Override
    public ApartmentResponseDTO updateApartment(UUID id, Apartment newApartmentInfo) {
        Apartment apartment = apartmentRepository.findById(id).orElseThrow();
        apartment.setOwner(newApartmentInfo.getOwner());
        apartment.setTitle(newApartmentInfo.getTitle());
        apartment.setAddress(newApartmentInfo.getAddress());
        apartment.setAccommodationType(newApartmentInfo.getAccommodationType());
        apartment.setPricePerNight(newApartmentInfo.getPricePerNight());
        apartment.setArea(newApartmentInfo.getArea());
        apartment.setNumberOfRooms(newApartmentInfo.getNumberOfRooms());
        apartment.setNumberOfBathrooms(newApartmentInfo.getNumberOfBathrooms());
        return mapper.toDTO(apartment);
    }

    @Override
    public void deleteApartment(UUID id) {
        apartmentRepository.deleteById(id);
    }

    @Override
    public ApartmentResponseDTO getApartmentById(UUID id) {
        return mapper.toDTO(apartmentRepository.findById(id).orElseThrow());
    }

    @Override
    public List<ApartmentResponseDTO> getApartmentsByOwnerId(UUID ownerId) {
        List<Apartment> apartments = apartmentRepository.findByOwnerId(ownerId);
        return apartments.stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<ApartmentResponseDTO> getApartmentsByTitle(String title) {
        List<Apartment> apartments = apartmentRepository.findByTitleContainingIgnoreCase(title);
        return apartments.stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<ApartmentResponseDTO> getApartmentsByAccommodationType(String accommodationType) {
        List<Apartment> apartments = apartmentRepository.findByAccommodationTypeContainingIgnoreCase(accommodationType);
        return apartments.stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<ApartmentResponseDTO> getApartmentsByPostalCode(String postalCode) {
        List<Apartment> apartments = apartmentRepository.findByAddressPostalCode(postalCode);
        return apartments.stream().map(mapper::toDTO).toList();
    }


    @Override
    public List<ApartmentResponseDTO> getApartmentsByCountry(String country) {
        List<Apartment> apartments = apartmentRepository.findByAddressCountryContainingIgnoreCase(country);
        return apartments.stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<ApartmentResponseDTO> getApartmentsByCity(String city) {
        List<Apartment> apartments = apartmentRepository.findByAddressCityContainingIgnoreCase(city);
        return apartments.stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<ApartmentResponseDTO> getApartmentsByDistrict(String district) {
        List<Apartment> apartments = apartmentRepository.findByAddressDistrictContainingIgnoreCase(district);
        return apartments.stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<ApartmentResponseDTO> getApartmentsByStreet(String street) {
        List<Apartment> apartments = apartmentRepository.findByAddressStreetContainingIgnoreCase(street);
        return apartments.stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<ApartmentResponseDTO> getApartmentsByPricePerNightRange(BigDecimal minPrice, BigDecimal maxPrice) {
        List<Apartment> apartments = apartmentRepository.findByPricePerNightBetween(minPrice, maxPrice);
        return apartments.stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<ApartmentResponseDTO> getApartmentsByAreaRange(Double minArea, Double maxArea) {
        List<Apartment> apartments = apartmentRepository.findByAreaBetween(minArea, maxArea);
        return apartments.stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<ApartmentResponseDTO> getApartmentsByNumberOfRooms(Integer numberOfRooms) {
        List<Apartment> apartments = apartmentRepository.findByNumberOfRooms(numberOfRooms);
        return apartments.stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<ApartmentResponseDTO> getApartmentsByNumberOfBathrooms(Integer numberOfBathrooms) {
        List<Apartment> apartments = apartmentRepository.findByNumberOfBathrooms(numberOfBathrooms);
        return apartments.stream().map(mapper::toDTO).toList();
    }
}
