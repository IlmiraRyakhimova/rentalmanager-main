package com.rental.manager.security.jwt.dto;

import lombok.Data;

@Data
public class JwtDTO {
    private String token;
    private String refreshToken;

}
