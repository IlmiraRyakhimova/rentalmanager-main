package com.rental.manager.dto.requestdto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailVerificationRequestDTO {
    @NotBlank(message = "Обязательное поле")
    @Email(message = "Некорректный формат email")
    private String email;
}
