package com.rental.manager.dto.responsedto;

import com.rental.manager.entities.enums.UserRole;
import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class UserResponseDto {
    private UUID id;
    private String name;
    private String email;
    private String phoneNumber;
    private UserRole role;
}
