package com.rental.manager.service.impl;

import com.rental.manager.dto.requestdto.AddressPatchRequestDTO;
import com.rental.manager.dto.requestdto.ApartmentPatchRequestDTO;
import com.rental.manager.dto.requestdto.ApartmentRequestDTO;
import com.rental.manager.dto.requestdto.UserPatchRequestDTO;
import com.rental.manager.dto.responsedto.ApartmentResponseDTO;
import com.rental.manager.entities.User;
import com.rental.manager.entities.apartment.Address;
import com.rental.manager.entities.apartment.Apartment;
import com.rental.manager.mappers.AddressMapper;
import com.rental.manager.mappers.ApartmentMapper;
import com.rental.manager.mappers.UserMapper;
import com.rental.manager.repository.AddressRepository;
import com.rental.manager.repository.ApartmentRepository;
import com.rental.manager.repository.UserRepository;
import com.rental.manager.service.AddressService;
import com.rental.manager.service.ApartmentService;
import com.rental.manager.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ApartmentServiceImpl implements ApartmentService {

    private static final String ENTITY_NOT_FOUND_MSG = "Apartment not found with id: ";

    private final ApartmentRepository apartmentRepository;
    private final ApartmentMapper mapper;
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final AddressService addressService;
    private final UserService userService;

    @Override
    @Transactional
    public ApartmentResponseDTO createApartment(ApartmentRequestDTO request) {
        Apartment apartment = mapper.toEntity(request);

        resolveOwner(apartment);
        resolveAddress(apartment);

        Apartment saved = apartmentRepository.save(apartment);
        return mapper.toDTO(saved);
    }

    private void resolveOwner(Apartment apartment) {
        User owner = apartment.getOwner();
        if (owner == null) {
            return;
        }

        User existingOwner = findExistingOwner(owner);

        if (existingOwner != null) {
            apartment.setOwner(existingOwner);
        } else {
            User savedOwner = userRepository.save(owner);
            apartment.setOwner(savedOwner);
        }
    }

    private User findExistingOwner(User owner) {
        if (owner.getPhoneNumber() != null && !owner.getPhoneNumber().isBlank()) {
            User existingOwner = userRepository.findByPhoneNumber(owner.getPhoneNumber());
            if (existingOwner != null) {
                return existingOwner;
            }
        }

        if (owner.getEmail() != null && !owner.getEmail().isBlank()) {
            return userRepository.findByEmail(owner.getEmail());
        }

        return null;
    }

    private void resolveAddress(Apartment apartment) {
        Address addr = apartment.getAddress();
        if (addr == null) {
            return;
        }

        Address existingAddress = findExistingAddress(addr);

        if (existingAddress != null) {
            apartment.setAddress(existingAddress);
        } else {
            Address savedAddress = addressRepository.save(addr);
            apartment.setAddress(savedAddress);
        }
    }

    private Address findExistingAddress(Address addr) {
        String city = addr.getCity() != null ? addr.getCity() : "";

        return addressRepository.findByCityContainingIgnoreCase(city).stream()
                .filter(a -> isAddressMatching(a, addr))
                .findFirst()
                .orElse(null);
    }

    private boolean isAddressMatching(Address existing, Address requested) {
        return eq(existing.getDistrict(), requested.getDistrict()) &&
                eq(existing.getStreet(), requested.getStreet()) &&
                eq(existing.getBuildingNumber(), requested.getBuildingNumber()) &&
                eq(existing.getApartmentNumber(), requested.getApartmentNumber());
    }



    private boolean eq(Object a, Object b) {
        return (a == null && b == null) || (a != null && a.equals(b));
    }


    @Override
    @Transactional
    public ApartmentResponseDTO updateApartment(UUID id, ApartmentRequestDTO request) {
        Apartment apartment = apartmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND_MSG + id));
        apartment.setOwner(userMapper.toEntity(request.getOwner()));
        apartment.setTitle(request.getTitle());
        apartment.setAddress(addressMapper.toEntity(request.getAddress()));
        apartment.setAccommodationType(request.getAccommodationType());
        apartment.setPricePerNight(request.getPricePerNight());
        apartment.setArea(request.getArea());
        apartment.setNumberOfRooms(request.getNumberOfRooms());
        apartment.setNumberOfBathrooms(request.getNumberOfBathrooms());
        return mapper.toDTO(apartmentRepository.save(apartment));
    }

    @Override
    @Transactional
    public ApartmentResponseDTO patchApartment(UUID id,
                                                ApartmentPatchRequestDTO request) {
        Apartment apartment = apartmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND_MSG + id));

        if (request.getTitle() != null) apartment.setTitle(request.getTitle());
        if (request.getAccommodationType() != null) apartment.setAccommodationType(request.getAccommodationType());
        if (request.getPricePerNight() != null) apartment.setPricePerNight(request.getPricePerNight());
        if (request.getArea() != null) apartment.setArea(request.getArea());
        if (request.getNumberOfRooms() != null) apartment.setNumberOfRooms(request.getNumberOfRooms());
        if (request.getNumberOfBathrooms() != null) apartment.setNumberOfBathrooms(request.getNumberOfBathrooms());

        AddressPatchRequestDTO address = request.getAddress();

        if (address != null && (address.getPostalCode() != null || address.getCountry() != null || address.getCity() != null ||
                address.getDistrict() != null || address.getStreet() != null || address.getBuildingNumber() != null ||
                address.getFloorNumber() != null || address.getApartmentNumber() != null)) {
            addressService.patchAddress(apartment.getAddress().getId(), address);
        }

        UserPatchRequestDTO owner = request.getOwner();

        if (owner != null && (owner.getName() != null || owner.getEmail() != null || owner.getPhoneNumber() != null)) {
            userService.patchUser(apartment.getOwner().getId(), owner);
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
        return mapper.toDTO(apartmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND_MSG + id)));

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
