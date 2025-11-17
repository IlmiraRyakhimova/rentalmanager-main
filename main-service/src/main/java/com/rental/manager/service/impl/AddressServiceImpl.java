package com.rental.manager.service.impl;

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
    public AddressResponseDTO createAddress(Address address) {
        return mapper.toDTO(addressRepository.save(address));
    }

    @Override
    public AddressResponseDTO updateAddress(UUID id, Address newAddressInfo) {
        Address address = addressRepository.findById(id).orElseThrow();
        address.setStreet(newAddressInfo.getStreet());
        address.setDistrict(newAddressInfo.getDistrict());
        address.setCity(newAddressInfo.getCity());
        address.setCountry(newAddressInfo.getCountry());
        address.setPostalCode(newAddressInfo.getPostalCode());
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
