package com.rental.manager.service;

import com.rental.manager.dto.requestdto.AddressPatchRequestDTO;
import com.rental.manager.dto.requestdto.AddressRequestDTO;
import com.rental.manager.dto.responsedto.AddressResponseDTO;


import java.util.List;
import java.util.UUID;

public interface AddressService {
    AddressResponseDTO createAddress(AddressRequestDTO request);
    AddressResponseDTO updateAddress(UUID id, AddressRequestDTO request);
    AddressResponseDTO patchAddress(UUID id, AddressPatchRequestDTO request);
    void deleteAddress(UUID id);

    AddressResponseDTO getAddressById(UUID id);
    List<AddressResponseDTO> getAddressesByPostalCode(String postalCode);
    List<AddressResponseDTO> getAddressesByCountry(String country);
    List<AddressResponseDTO> getAddressesByCity(String city);
    List<AddressResponseDTO> getAddressesByDistrict(String district);
    List<AddressResponseDTO> getAddressesByStreet(String street);


}
