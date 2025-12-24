package com.rental.manager.service.impl;

import com.rental.manager.dto.requestdto.ApartmentPatchRequestDto;
import com.rental.manager.dto.requestdto.ApartmentRequestDto;
import com.rental.manager.dto.responsedto.ApartmentResponseDto;
import com.rental.manager.entities.User;
import com.rental.manager.entities.Address;
import com.rental.manager.entities.Apartment;
import com.rental.manager.entities.enums.UserRole;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ApartmentServiceImpl implements ApartmentService {

    private static final String ENTITY_NOT_FOUND_MSG = "Apartment not found with id: ";
    private final PasswordEncoder passwordEncoder;

    private final ApartmentRepository apartmentRepository;
    private final ApartmentMapper mapper;
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final AddressService addressService;
    private final UserService userService;
    private final EmailServiceImpl emailService;

    @Override
    @Transactional
    public ApartmentResponseDto createApartment(ApartmentRequestDto request) {
        Apartment apartment = mapper.toEntity(request);

        resolveOwner(apartment);
        resolveAddress(apartment);
        String currentUserEmail = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        User agent = userRepository.findByEmail(currentUserEmail);
        apartment.setAgent(agent);

        Apartment saved = apartmentRepository.save(apartment);
        return mapper.toDto(saved);
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
            String ownerPassword = generateTemporaryPassword();
            owner.setPassword(passwordEncoder.encode(ownerPassword));
            owner.setRole(UserRole.OWNER);
            owner.setEmailVerified(false);
            apartment.setOwner(userRepository.save(owner));
            emailService.sendOwnerCredentialsEmail(owner.getEmail(), owner.getName(), ownerPassword);
            emailService.createAndSendVerificationToken(owner.getEmail());
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

        private String generateTemporaryPassword() {
            return UUID.randomUUID().toString().substring(0, 12);
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
    public ApartmentResponseDto updateApartment(UUID id, ApartmentRequestDto request) {
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
        return mapper.toDto(apartmentRepository.save(apartment));
    }

    @Override
    @Transactional
    public ApartmentResponseDto patchApartment(UUID id,
                                               ApartmentPatchRequestDto request) {
        Apartment apartment = apartmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND_MSG + id));

       mapper.updateEntity(apartment, request);

        if (request.getAddress() != null) {
            addressService.patchAddress(apartment.getAddress().getId(), request.getAddress());
        }

        if (request.getOwner() != null) {
            userService.patchUser(apartment.getOwner().getId(), request.getOwner());
        }
        return mapper.toDto(apartmentRepository.save(apartment));
    }


    @Override
    public void deleteApartment(UUID id) {
        apartmentRepository.deleteById(id);
    }

    @Override
    public ApartmentResponseDto getApartmentById(UUID id) {
        return mapper.toDto(apartmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND_MSG + id)));

    }

    @Override
    public List<ApartmentResponseDto> getApartmentsByOwnerId(UUID ownerId) {
        List<Apartment> apartments = apartmentRepository.findByOwnerId(ownerId);
        return apartments.stream().map(mapper::toDto).toList();
    }

    @Override
    public List<ApartmentResponseDto> getApartmentsByOwnerName(String ownerName) {
        List<Apartment> apartments = apartmentRepository.findByOwnerNameContainingIgnoreCase(ownerName);
        return apartments.stream().map(mapper::toDto).toList();
    }

    @Override
    public List<ApartmentResponseDto> getApartmentsByOwnerEmail(String ownerEmail) {
        List<Apartment> apartments = apartmentRepository.findByOwnerEmailContainingIgnoreCase(ownerEmail);
        return apartments.stream().map(mapper::toDto).toList();
    }

    @Override
    public List<ApartmentResponseDto> getApartmentsByOwnerPhoneNumber(String ownerPhoneNumber) {
        List<Apartment> apartments = apartmentRepository.findByOwnerPhoneNumberContainingIgnoreCase(ownerPhoneNumber);
        return apartments.stream().map(mapper::toDto).toList();
    }

    @Override
    public List<ApartmentResponseDto> getApartmentsByTitle(String title) {
        List<Apartment> apartments = apartmentRepository.findByTitleContainingIgnoreCase(title);
        return apartments.stream().map(mapper::toDto).toList();
    }

    @Override
    public List<ApartmentResponseDto> getApartmentsByAccommodationType(String accommodationType) {
        List<Apartment> apartments = apartmentRepository.findByAccommodationTypeContainingIgnoreCase(accommodationType);
        return apartments.stream().map(mapper::toDto).toList();
    }

    @Override
    public List<ApartmentResponseDto> getApartmentsByPostalCode(String postalCode) {
        List<Apartment> apartments = apartmentRepository.findByAddressPostalCode(postalCode);
        return apartments.stream().map(mapper::toDto).toList();
    }


    @Override
    public List<ApartmentResponseDto> getApartmentsByCountry(String country) {
        List<Apartment> apartments = apartmentRepository.findByAddressCountryContainingIgnoreCase(country);
        return apartments.stream().map(mapper::toDto).toList();
    }

    @Override
    public List<ApartmentResponseDto> getApartmentsByCity(String city) {
        List<Apartment> apartments = apartmentRepository.findByAddressCityContainingIgnoreCase(city);
        return apartments.stream().map(mapper::toDto).toList();
    }

    @Override
    public List<ApartmentResponseDto> getApartmentsByDistrict(String district) {
        List<Apartment> apartments = apartmentRepository.findByAddressDistrictContainingIgnoreCase(district);
        return apartments.stream().map(mapper::toDto).toList();
    }

    @Override
    public List<ApartmentResponseDto> getApartmentsByStreet(String street) {
        List<Apartment> apartments = apartmentRepository.findByAddressStreetContainingIgnoreCase(street);
        return apartments.stream().map(mapper::toDto).toList();
    }

    @Override
    public List<ApartmentResponseDto> getApartmentsByPricePerNightRange(BigDecimal minPrice, BigDecimal maxPrice) {
        List<Apartment> apartments = apartmentRepository.findByPricePerNightBetween(minPrice, maxPrice);
        return apartments.stream().map(mapper::toDto).toList();
    }

    @Override
    public List<ApartmentResponseDto> getApartmentsByAreaRange(Double minArea, Double maxArea) {
        List<Apartment> apartments = apartmentRepository.findByAreaBetween(minArea, maxArea);
        return apartments.stream().map(mapper::toDto).toList();
    }

    @Override
    public List<ApartmentResponseDto> getApartmentsByNumberOfRooms(Integer numberOfRooms) {
        List<Apartment> apartments = apartmentRepository.findByNumberOfRooms(numberOfRooms);
        return apartments.stream().map(mapper::toDto).toList();
    }

    @Override
    public List<ApartmentResponseDto> getApartmentsByNumberOfBathrooms(Integer numberOfBathrooms) {
        List<Apartment> apartments = apartmentRepository.findByNumberOfBathrooms(numberOfBathrooms);
        return apartments.stream().map(mapper::toDto).toList();
    }

    @Override
    public List<ApartmentResponseDto> getAllApartments() {
        String agentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User agent = userRepository.findByEmail(agentEmail);
        List<Apartment> apartments = apartmentRepository.findByAgentId(agent.getId());
        return apartments.stream().map(mapper::toDto).toList();
    }
}
