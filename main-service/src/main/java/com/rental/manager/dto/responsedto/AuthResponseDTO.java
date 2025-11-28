package com.rental.manager.dto.responsedto;


import lombok.Data;

@Data
public class AuthResponseDTO {
    private String token;
    private String type = "Bearer";
    private String email;
    private String name;
}
