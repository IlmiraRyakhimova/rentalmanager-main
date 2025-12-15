package com.rental.manager.dto.requestdto;


import com.rental.manager.validation.ValidEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class EmailVerificationRequestDto {
    @NotBlank(message = "Обязательное поле")
    @ValidEmail
    private String email;
}
