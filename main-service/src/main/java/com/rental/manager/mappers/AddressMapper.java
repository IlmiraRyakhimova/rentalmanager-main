package com.rental.manager.mappers;

import com.rental.manager.dto.responsedto.AddressResponseDTO;
import com.rental.manager.entities.apartment.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public AddressResponseDTO toDTO(Address entity) {
        AddressResponseDTO dto = new AddressResponseDTO();
        dto.setId(entity.getId());
        dto.setPostalCode(entity.getPostalCode());
        dto.setCountry(entity.getCountry());
        dto.setCity(entity.getCity());
        dto.setDistrict(entity.getDistrict());
        dto.setStreet(entity.getStreet());
        dto.setHouseNumber(entity.getHouseNumber());
        dto.setFloorNumber(entity.getFloorNumber());
        dto.setApartmentNumber(entity.getApartmentNumber());
        return dto;
    }

    public Address toEntity(AddressResponseDTO dto) {
        Address entity = new Address();
        entity.setPostalCode(dto.getPostalCode());
        entity.setCountry(dto.getCountry());
        entity.setCity(dto.getCity());
        entity.setDistrict(dto.getDistrict());
        entity.setStreet(dto.getStreet());
        entity.setHouseNumber(dto.getHouseNumber());
        entity.setFloorNumber(dto.getFloorNumber());
        entity.setApartmentNumber(dto.getApartmentNumber());
        return entity;
    }


}
