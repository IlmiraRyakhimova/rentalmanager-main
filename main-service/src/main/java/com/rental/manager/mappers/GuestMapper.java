package com.rental.manager.mappers;

import com.rental.manager.dto.requestdto.GuestPatchRequestDto;
import com.rental.manager.dto.requestdto.GuestRequestDto;
import com.rental.manager.dto.responsedto.GuestResponseDto;
import com.rental.manager.entities.Guest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class GuestMapper {

    public GuestResponseDto toDto(Guest entity) {
        return GuestResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .phoneNumber(entity.getPhoneNumber())
                .notes(entity.getNotes())
                .build();

    }


    public Guest toEntity(GuestRequestDto dto) {
        return Guest.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .notes(dto.getNotes())
                .build();
    }

    public void updateEntity(Guest entity, GuestPatchRequestDto dto) {
        if(dto.getName() != null) {
            entity.setName(dto.getName());
        }
        if(dto.getEmail() != null) {
            entity.setEmail(dto.getEmail());
        }
        if(dto.getPhoneNumber() != null) {
            entity.setPhoneNumber(dto.getPhoneNumber());
        }
        if (dto.getNotes() != null) {
            entity.setNotes(dto.getNotes());
        }
    }
}
