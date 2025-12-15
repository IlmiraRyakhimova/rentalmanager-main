package com.rental.manager.service;

import com.rental.manager.dto.requestdto.AddressPatchRequestDto;
import com.rental.manager.dto.requestdto.AddressRequestDto;
import com.rental.manager.dto.responsedto.AddressResponseDto;


import java.util.List;
import java.util.UUID;

public interface AddressService {
    AddressResponseDto createAddress(AddressRequestDto request);
    AddressResponseDto updateAddress(UUID id, AddressRequestDto request);
    AddressResponseDto patchAddress(UUID id, AddressPatchRequestDto request);
    void deleteAddress(UUID id);

    AddressResponseDto getAddressById(UUID id);
    List<AddressResponseDto> getAddressesByPostalCode(String postalCode);
    List<AddressResponseDto> getAddressesByCountry(String country);
    List<AddressResponseDto> getAddressesByCity(String city);
    List<AddressResponseDto> getAddressesByDistrict(String district);
    List<AddressResponseDto> getAddressesByStreet(String street);


}
