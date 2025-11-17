package com.rental.manager.mappers;

import com.rental.manager.dto.requestdto.ApartmentRequestDTO;
import com.rental.manager.dto.responsedto.ApartmentResponseDTO;
import com.rental.manager.entities.apartment.Apartment;
import org.springframework.stereotype.Component;

@Component
public class ApartmentMapper {
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;

    public ApartmentMapper(UserMapper userMapper, AddressMapper addressMapper) {
        this.userMapper = userMapper;
        this.addressMapper = addressMapper;
    }

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
        entity.setPricePerNight(dto.getPricePerNight());
        entity.setArea(dto.getArea());
        entity.setNumberOfRooms(dto.getNumberOfRooms());
        entity.setNumberOfBathrooms(dto.getNumberOfBathrooms());
        return entity;
    }
}
