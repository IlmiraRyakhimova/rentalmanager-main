package com.rental.manager.service.impl;

import com.rental.manager.dto.requestdto.AddressPatchRequestDTO;
import com.rental.manager.dto.requestdto.AddressRequestDTO;
import com.rental.manager.dto.responsedto.AddressResponseDTO;
import com.rental.manager.entities.apartment.Address;
import com.rental.manager.mappers.AddressMapper;
import com.rental.manager.repository.AddressRepository;
import com.rental.manager.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper mapper;
    @Override
    public AddressResponseDTO createAddress(AddressRequestDTO request) {
        Address address = mapper.toEntity(request);
        return mapper.toDTO(addressRepository.save(address));
    }

    @Override
    public AddressResponseDTO updateAddress(UUID id, AddressRequestDTO request) {
        Address address = addressRepository.findById(id).orElseThrow();
        address.setStreet(request.getStreet());
        address.setDistrict(request.getDistrict());
        address.setCity(request.getCity());
        address.setCountry(request.getCountry());
        address.setPostalCode(request.getPostalCode());
        return mapper.toDTO(address);
    }

    @Override
    public AddressResponseDTO patchAddress(UUID id, AddressPatchRequestDTO request) {
        Address address = addressRepository.findById(id).orElseThrow();
        if (request.getApartmentNumber() != null) {
            address.setApartmentNumber(request.getApartmentNumber());
        }
        if (request.getFloorNumber() != null) {
            address.setFloorNumber(request.getFloorNumber());
        }
        if (request.getBuildingNumber() != null) {
            address.setBuildingNumber(request.getBuildingNumber());
        }
        if (request.getStreet() != null) {
            address.setStreet(request.getStreet());
        }
        if (request.getDistrict() != null) {
            address.setDistrict(request.getDistrict());
        }
        if (request.getCity() != null) {
            address.setCity(request.getCity());
        }
        if (request.getCountry() != null) {
            address.setCountry(request.getCountry());
        }
        if (request.getPostalCode() != null) {
            address.setPostalCode(request.getPostalCode());
        }
        return mapper.toDTO(address);
    }




    @Override
    public void deleteAddress(UUID id) {
        addressRepository.deleteById(id);
    }

    @Override
    public AddressResponseDTO getAddressById(UUID id) {
        return mapper.toDTO(addressRepository.findById(id).orElseThrow());
    }

    @Override
    public List<AddressResponseDTO> getAddressesByPostalCode(String postalCode) {
        List<Address> addresses = addressRepository.findByPostalCode(postalCode);
        return addresses.stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<AddressResponseDTO> getAddressesByCountry(String country) {
        List<Address> addresses = addressRepository.findByCountryContainingIgnoreCase(country);
        return addresses.stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<AddressResponseDTO> getAddressesByCity(String city) {
        List<Address> addresses = addressRepository.findByCityContainingIgnoreCase(city);
        return addresses.stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<AddressResponseDTO> getAddressesByDistrict(String district) {
        List<Address> addresses = addressRepository.findByDistrictContainingIgnoreCase(district);
        return addresses.stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<AddressResponseDTO> getAddressesByStreet(String street) {
        List<Address> addresses = addressRepository.findByStreetContainingIgnoreCase(street);
        return addresses.stream().map(mapper::toDTO).toList();
    }


}
