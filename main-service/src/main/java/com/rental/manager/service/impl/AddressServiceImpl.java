package com.rental.manager.service.impl;

import com.rental.manager.dto.requestdto.AddressPatchRequestDto;
import com.rental.manager.dto.requestdto.AddressRequestDto;
import com.rental.manager.dto.responsedto.AddressResponseDto;
import com.rental.manager.entities.Address;
import com.rental.manager.mappers.AddressMapper;
import com.rental.manager.repository.AddressRepository;
import com.rental.manager.service.AddressService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private static final String ENTITY_NOT_FOUND_MSG = "Address not found with id: ";

    private final AddressRepository addressRepository;
    private final AddressMapper mapper;
    @Override
    public AddressResponseDto createAddress(AddressRequestDto request) {
        Address address = mapper.toEntity(request);
        return mapper.toDto(addressRepository.save(address));
    }

    @Override
    public AddressResponseDto updateAddress(UUID id, AddressRequestDto request) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND_MSG + id));
        address.setStreet(request.getStreet());
        address.setDistrict(request.getDistrict());
        address.setCity(request.getCity());
        address.setCountry(request.getCountry());
        address.setPostalCode(request.getPostalCode());
        return mapper.toDto(addressRepository.save(address));
    }

    @Override
    public AddressResponseDto patchAddress(UUID id, AddressPatchRequestDto request) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND_MSG + id));
        mapper.updateEntity(address, request);
        return mapper.toDto(addressRepository.save(address));
    }




    @Override
    public void deleteAddress(UUID id) {
        addressRepository.deleteById(id);
    }

    @Override
    public AddressResponseDto getAddressById(UUID id) {
        return mapper.toDto(addressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND_MSG + id)));
    }

    @Override
    public List<AddressResponseDto> getAddressesByPostalCode(String postalCode) {
        List<Address> addresses = addressRepository.findByPostalCode(postalCode);
        return addresses.stream().map(mapper::toDto).toList();
    }

    @Override
    public List<AddressResponseDto> getAddressesByCountry(String country) {
        List<Address> addresses = addressRepository.findByCountryContainingIgnoreCase(country);
        return addresses.stream().map(mapper::toDto).toList();
    }

    @Override
    public List<AddressResponseDto> getAddressesByCity(String city) {
        List<Address> addresses = addressRepository.findByCityContainingIgnoreCase(city);
        return addresses.stream().map(mapper::toDto).toList();
    }

    @Override
    public List<AddressResponseDto> getAddressesByDistrict(String district) {
        List<Address> addresses = addressRepository.findByDistrictContainingIgnoreCase(district);
        return addresses.stream().map(mapper::toDto).toList();
    }

    @Override
    public List<AddressResponseDto> getAddressesByStreet(String street) {
        List<Address> addresses = addressRepository.findByStreetContainingIgnoreCase(street);
        return addresses.stream().map(mapper::toDto).toList();
    }


}
