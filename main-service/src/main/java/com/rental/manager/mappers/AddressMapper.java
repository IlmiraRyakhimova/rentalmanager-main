package com.rental.manager.mappers;

import com.rental.manager.dto.requestdto.AddressPatchRequestDTO;
import com.rental.manager.dto.requestdto.AddressRequestDTO;
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
        dto.setBuildingNumber(entity.getBuildingNumber());
        dto.setFloorNumber(entity.getFloorNumber());
        dto.setApartmentNumber(entity.getApartmentNumber());
        return dto;
    }

    public Address toEntity(AddressRequestDTO dto) {
        Address entity = new Address();
        entity.setPostalCode(dto.getPostalCode());
        entity.setCountry(dto.getCountry());
        entity.setCity(dto.getCity());
        entity.setDistrict(dto.getDistrict());
        entity.setStreet(dto.getStreet());
        entity.setBuildingNumber(dto.getBuildingNumber());
        entity.setFloorNumber(dto.getFloorNumber());
        entity.setApartmentNumber(dto.getApartmentNumber());
        return entity;
    }

    public Address toEntity(AddressPatchRequestDTO dto) {
        Address entity = new Address();
        if (dto.getPostalCode() != null) {
            entity.setPostalCode(dto.getPostalCode());
        }
        if (dto.getCountry() != null) {
            entity.setCountry(dto.getCountry());
        }
        if (dto.getCity() != null) {
            entity.setCity(dto.getCity());
        }
        if (dto.getDistrict() != null) {
            entity.setDistrict(dto.getDistrict());
        }
        if (dto.getStreet() != null) {
            entity.setStreet(dto.getStreet());
        }
        if (dto.getBuildingNumber() != null) {
            entity.setBuildingNumber(dto.getBuildingNumber());
        }
        if (dto.getFloorNumber() != null) {
            entity.setFloorNumber(dto.getFloorNumber());
        }
        if (dto.getApartmentNumber() != null) {
            entity.setApartmentNumber(dto.getApartmentNumber());
        }
        return entity;
    }


}
