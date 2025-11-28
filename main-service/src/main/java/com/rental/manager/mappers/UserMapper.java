package com.rental.manager.mappers;

import com.rental.manager.dto.requestdto.UserPatchRequestDTO;
import com.rental.manager.dto.requestdto.UserRequestDTO;
import com.rental.manager.dto.responsedto.UserResponseDTO;
import com.rental.manager.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponseDTO toDTO(User entity) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setEmail(entity.getEmail());
        dto.setRole(entity.getRole());
        return dto;
    }

    public User toEntity(UserRequestDTO dto) {
        User entity = new User();
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setRole(dto.getRole());
        return entity;
    }

    public User toEntity(UserPatchRequestDTO dto) {
        User entity = new User();
        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
        if (dto.getEmail() != null) {
            entity.setEmail(dto.getEmail());
        }
        if (dto.getPhoneNumber() != null) {
            entity.setPhoneNumber(dto.getPhoneNumber());
        }
        return entity;
    }
}
