package com.rental.manager.service.impl;

import com.rental.manager.dto.responsedto.ApartmentResponseDTO;
import com.rental.manager.entities.User;
import com.rental.manager.entities.apartment.Address;
import com.rental.manager.entities.apartment.Apartment;
import com.rental.manager.mappers.ApartmentMapper;
import com.rental.manager.repository.AddressRepository;
import com.rental.manager.repository.ApartmentRepository;
import com.rental.manager.repository.UserRepository;
import com.rental.manager.service.AddressService;
import com.rental.manager.service.ApartmentService;
import com.rental.manager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ApartmentServiceImpl implements ApartmentService {

    private final ApartmentRepository apartmentRepository;
    private final ApartmentMapper mapper;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final AddressService addressService;
    private final UserService userService;


    @Override
    @Transactional
    public ApartmentResponseDTO createApartment(Apartment apartment) {
        User owner = apartment.getOwner();
        if (owner != null) {
            User existingOwner = null;

            if (owner.getPhoneNumber() != null && !owner.getPhoneNumber().isBlank()) {
                existingOwner = userRepository.findByPhoneNumber(owner.getPhoneNumber());
            }

            if (existingOwner == null && owner.getEmail() != null && !owner.getEmail().isBlank()) {
                existingOwner = userRepository.findByEmail(owner.getEmail());
            }

            if (existingOwner != null) {
                apartment.setOwner(existingOwner);
            } else {
                User savedOwner = userRepository.save(owner);
                apartment.setOwner(savedOwner);
            }
        }

        Address addr = apartment.getAddress();
        if (addr != null) {
            Address existingAddress = addressRepository.findByCityContainingIgnoreCase(addr.getCity() != null ? addr.getCity() : "").stream()
                    .filter(a ->
                            eq(a.getDistrict(), addr.getDistrict()) &&
                                    eq(a.getStreet(), addr.getStreet()) &&
                                    eq(a.getBuildingNumber(), addr.getBuildingNumber()) &&
                                    eq(a.getApartmentNumber(), addr.getApartmentNumber()))
                    .findFirst()
                    .orElse(null);
            if (existingAddress != null) {
                apartment.setAddress(existingAddress);
            } else {
                Address savedAddress = addressRepository.save(addr);
                apartment.setAddress(savedAddress);
            }
        }

        Apartment saved = apartmentRepository.save(apartment);
        return mapper.toDTO(saved);
    }


    private boolean eq(Object a, Object b) {
        return (a == null && b == null) || (a != null && a.equals(b));
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
    @Transactional
    public ApartmentResponseDTO patchApartment(UUID id,
                                                String title,
                                                String accommodationType,
                                                BigDecimal pricePerNight,
                                                Double area,
                                                Integer numberOfRooms,
                                                Integer numberOfBathrooms,
                                                String postalCode,
                                                String country,
                                                String city,
                                                String district,
                                                String street,
                                                Integer buildingNumber,
                                                Integer floorNumber,
                                                Integer apartmentNumber,
                                                String ownerName,
                                                String ownerEmail,
                                                String ownerPhoneNumber) {
        Apartment apartment = apartmentRepository.findById(id).orElseThrow();

        if (title != null) apartment.setTitle(title);
        if (accommodationType != null) apartment.setAccommodationType(accommodationType);
        if (pricePerNight != null) apartment.setPricePerNight(pricePerNight);
        if (area != null) apartment.setArea(area);
        if (numberOfRooms != null) apartment.setNumberOfRooms(numberOfRooms);
        if (numberOfBathrooms != null) apartment.setNumberOfBathrooms(numberOfBathrooms);

        if (apartment.getAddress() != null && (postalCode != null || country != null || city != null ||
                district != null || street != null || buildingNumber != null ||
                floorNumber != null || apartmentNumber != null)) {
            addressService.patchAddress(apartment.getAddress().getId(), apartmentNumber, floorNumber, buildingNumber, street, district, city, country, postalCode);
        }

        if (apartment.getOwner() != null && (ownerName != null || ownerEmail != null || ownerPhoneNumber != null)) {
            userService.patchUser(apartment.getOwner().getId(), ownerName, ownerEmail, ownerPhoneNumber);
        }

        Apartment saved = apartmentRepository.save(apartment);
        return mapper.toDTO(saved);
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
    public List<ApartmentResponseDTO> getApartmentsByOwnerName(String ownerName) {
        List<Apartment> apartments = apartmentRepository.findByOwnerNameContainingIgnoreCase(ownerName);
        return apartments.stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<ApartmentResponseDTO> getApartmentsByOwnerEmail(String ownerEmail) {
        List<Apartment> apartments = apartmentRepository.findByOwnerEmailContainingIgnoreCase(ownerEmail);
        return apartments.stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<ApartmentResponseDTO> getApartmentsByOwnerPhoneNumber(String ownerPhoneNumber) {
        List<Apartment> apartments = apartmentRepository.findByOwnerPhoneNumberContainingIgnoreCase(ownerPhoneNumber);
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

    @Override
    public List<ApartmentResponseDTO> getAllApartments() {
        List<Apartment> apartments = apartmentRepository.findAll();
        return apartments.stream().map(mapper::toDTO).toList();
    }
}
