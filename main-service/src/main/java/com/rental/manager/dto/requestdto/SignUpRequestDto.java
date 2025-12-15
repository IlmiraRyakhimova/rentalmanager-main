package com.rental.manager.dto.requestdto;

import com.rental.manager.entities.enums.UserRole;
import com.rental.manager.validation.ValidEmail;
import com.rental.manager.validation.ValidPassword;
import com.rental.manager.validation.ValidPhone;
import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SignUpRequestDto {
    @NotBlank
    private String name;

    @ValidEmail
    private String email;

    @ValidPhone
    private String phoneNumber;

    @NotNull
    private UserRole role;

    @ValidPassword
    private String password;
}

