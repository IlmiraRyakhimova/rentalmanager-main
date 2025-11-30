package com.rental.manager.dto.requestdto;

import com.rental.manager.entities.enums.UserRole;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SignUpRequestDTO {
    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Pattern(regexp = "^\\+?\\d{10,15}$")
    private String phoneNumber;

    @NotNull
    private UserRole role;

    @NotBlank
    @Size(min = 8, max = 72)
    private String password;
}

