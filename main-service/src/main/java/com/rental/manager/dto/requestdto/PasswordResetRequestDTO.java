package com.rental.manager.dto.requestdto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordResetRequestDTO {

    @NotBlank
    private String token;

    @NotBlank
    @Size(min = 8, max = 72)
    private String newPassword;
}
