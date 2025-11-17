package com.rental.manager.service;

import com.rental.manager.dto.responsedto.AddressResponseDTO;
import com.rental.manager.entities.apartment.Address;

import java.util.List;
import java.util.UUID;

public interface AddressService {
    AddressResponseDTO createAddress(Address address);
    AddressResponseDTO updateAddress(UUID id, Address newAddressInfo);
    void deleteAddress(UUID id);

    AddressResponseDTO getAddressById(UUID id);
    List<AddressResponseDTO> getAddressesByPostalCode(String postalCode);
    List<AddressResponseDTO> getAddressesByCountry(String country);
    List<AddressResponseDTO> getAddressesByCity(String city);
    List<AddressResponseDTO> getAddressesByDistrict(String district);
    List<AddressResponseDTO> getAddressesByStreet(String street);


}
