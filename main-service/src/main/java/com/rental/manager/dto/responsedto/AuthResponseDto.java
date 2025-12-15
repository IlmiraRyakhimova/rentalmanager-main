package com.rental.manager.dto.responsedto;

import com.rental.manager.entities.enums.UserRole;
import lombok.Data;

@Data
public class AuthResponseDto {
    private String accessToken;
    private String refreshToken;
    private String type = "Bearer";
    private String email;
    private String name;
    private String phoneNumber;
    private UserRole role;
}
