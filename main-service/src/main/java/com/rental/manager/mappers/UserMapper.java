package com.rental.manager.mappers;

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
}
