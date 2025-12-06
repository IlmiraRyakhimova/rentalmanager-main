package com.rental.manager.mappers;

import com.rental.manager.dto.requestdto.ApartmentPatchRequestDTO;
import com.rental.manager.dto.requestdto.ApartmentRequestDTO;
import com.rental.manager.dto.responsedto.ApartmentResponseDTO;
import com.rental.manager.entities.Apartment;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ApartmentMapper {
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;

    public ApartmentResponseDTO toDTO(Apartment entity) {
        ApartmentResponseDTO dto = new ApartmentResponseDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setOwner(userMapper.toDTO(entity.getOwner()));
        dto.setAddress(addressMapper.toDTO(entity.getAddress()));
        dto.setAccommodationType(entity.getAccommodationType());
        dto.setPricePerNight(entity.getPricePerNight());
        dto.setArea(entity.getArea());
        dto.setNumberOfRooms(entity.getNumberOfRooms());
        dto.setNumberOfBathrooms(entity.getNumberOfBathrooms());
        return dto;
    }

    public Apartment toEntity(ApartmentRequestDTO dto)  {
        Apartment entity = new Apartment();
        entity.setTitle(dto.getTitle());
        entity.setAccommodationType(dto.getAccommodationType());
        entity.setOwner(userMapper.toEntity(dto.getOwner()));
        entity.setAddress(addressMapper.toEntity(dto.getAddress()));
        entity.setPricePerNight(dto.getPricePerNight());
        entity.setArea(dto.getArea());
        entity.setNumberOfRooms(dto.getNumberOfRooms());
        entity.setNumberOfBathrooms(dto.getNumberOfBathrooms());

        return entity;
    }

    public Apartment toEntity(ApartmentPatchRequestDTO dto) {
        Apartment entity = new Apartment();
        if (dto.getTitle() != null) {
            entity.setTitle(dto.getTitle());
        }
        if (dto.getAccommodationType() != null) {
            entity.setAccommodationType(dto.getAccommodationType());
        }
        if (dto.getOwner() != null) {
            entity.setOwner(userMapper.toEntity(dto.getOwner()));
        }
        if (dto.getAddress() != null) {
            entity.setAddress(addressMapper.toEntity(dto.getAddress()));
        }
        if (dto.getPricePerNight() != null) {
            entity.setPricePerNight(dto.getPricePerNight());
        }
        if (dto.getArea() != null) {
            entity.setArea(dto.getArea());
        }
        if (dto.getNumberOfRooms() != null) {
            entity.setNumberOfRooms(dto.getNumberOfRooms());
        }
        if (dto.getNumberOfBathrooms() != null) {
            entity.setNumberOfBathrooms(dto.getNumberOfBathrooms());
        }
        return entity;
    }
}
