package com.rental.manager.mappers;

import com.rental.manager.dto.requestdto.GuestPatchRequestDTO;
import com.rental.manager.dto.requestdto.GuestRequestDTO;
import com.rental.manager.dto.responsedto.GuestResponseDTO;
import com.rental.manager.entities.Guest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class GuestMapper {

    public GuestResponseDTO toDto(Guest entity) {
        GuestResponseDTO dto = new GuestResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setName(entity.getNotes());
        return dto;
    }


    public Guest toEntity(GuestRequestDTO dto) {
        Guest entity = new Guest();
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setNotes(dto.getNotes());
        return entity;
    }

    public Guest toEntity(GuestPatchRequestDTO dto) {
        Guest entity = new Guest();
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
        return entity;
    }
}
