package com.rental.manager.mappers;

import com.rental.manager.dto.requestdto.AddressPatchRequestDto;
import com.rental.manager.dto.requestdto.AddressRequestDto;
import com.rental.manager.dto.responsedto.AddressResponseDto;
import com.rental.manager.entities.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public AddressResponseDto toDto(Address entity) {
        return AddressResponseDto.builder()
                .id(entity.getId())
                .postalCode(entity.getPostalCode())
                .country(entity.getCountry())
                .city(entity.getCity())
                .district(entity.getDistrict())
                .street(entity.getStreet())
                .buildingNumber(entity.getBuildingNumber())
                .floorNumber(entity.getFloorNumber())
                .apartmentNumber(entity.getApartmentNumber())
                .build();
    }

    public Address toEntity(AddressRequestDto dto) {
        return Address.builder()
                .postalCode(dto.getPostalCode())
                .country(dto.getCountry())
                .city(dto.getCity())
                .district(dto.getDistrict())
                .street(dto.getStreet())
                .buildingNumber(dto.getBuildingNumber())
                .floorNumber(dto.getFloorNumber())
                .apartmentNumber(dto.getApartmentNumber())
                .build();
    }

    public void updateEntity(Address entity, AddressPatchRequestDto dto) {
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
    }


}
