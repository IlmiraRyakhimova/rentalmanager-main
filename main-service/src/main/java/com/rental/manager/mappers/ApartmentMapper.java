package com.rental.manager.mappers;

import com.rental.manager.dto.requestdto.ApartmentPatchRequestDto;
import com.rental.manager.dto.requestdto.ApartmentRequestDto;
import com.rental.manager.dto.responsedto.ApartmentResponseDto;
import com.rental.manager.entities.Apartment;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ApartmentMapper {
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;

    public ApartmentResponseDto toDto(Apartment entity) {
        return ApartmentResponseDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .owner(userMapper.toDto(entity.getOwner()))
                .address(addressMapper.toDto(entity.getAddress()))
                .accommodationType(entity.getAccommodationType())
                .pricePerNight(entity.getPricePerNight())
                .area(entity.getArea())
                .numberOfRooms(entity.getNumberOfRooms())
                .numberOfBathrooms(entity.getNumberOfBathrooms())
                .build();
    }

    public Apartment toEntity(ApartmentRequestDto dto)  {
        return Apartment.builder()
                .title(dto.getTitle())
                .accommodationType(dto.getAccommodationType())
                .owner(userMapper.toEntity(dto.getOwner()))
                .address(addressMapper.toEntity(dto.getAddress()))
                .pricePerNight(dto.getPricePerNight())
                .area(dto.getArea())
                .numberOfRooms(dto.getNumberOfRooms())
                .numberOfBathrooms(dto.getNumberOfBathrooms())
                .build();


    }

    public void updateEntity(Apartment entity, ApartmentPatchRequestDto dto) {
        if (dto.getTitle() != null) {
            entity.setTitle(dto.getTitle());
        }
        if (dto.getAccommodationType() != null) {
            entity.setAccommodationType(dto.getAccommodationType());
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
    }
}
