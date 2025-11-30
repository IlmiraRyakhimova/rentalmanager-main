package com.rental.manager.dto.responsedto;


import com.rental.manager.security.jwt.dto.JwtDTO;
import lombok.Data;

@Data
public class AuthResponseDTO {
    private String accessToken;
    private String refreshToken;
    private String type = "Bearer";
    private String email;
    private String name;
}
