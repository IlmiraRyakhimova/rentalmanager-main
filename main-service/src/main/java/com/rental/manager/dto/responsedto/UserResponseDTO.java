package com.rental.manager.dto.responsedto;

import com.rental.manager.entities.User;
import com.rental.manager.entities.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserResponseDTO {
    private UUID id;
    private String name;
    private String email;
    private String phoneNumber;
    private UserRole role;
}
