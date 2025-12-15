package com.rental.manager.dto.requestdto;


import com.rental.manager.validation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordResetRequestDto {

    @NotBlank
    private String token;

    @NotBlank
    @ValidPassword
    private String newPassword;
}
