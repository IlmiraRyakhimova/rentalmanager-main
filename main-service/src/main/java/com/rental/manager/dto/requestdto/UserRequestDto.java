package com.rental.manager.dto.requestdto;

import com.rental.manager.entities.enums.UserRole;
import com.rental.manager.validation.ValidEmail;
import com.rental.manager.validation.ValidPhone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserRequestDto {

    @NotBlank
    private String name;

    @ValidEmail
    private String email;

    @ValidPhone
    private String phoneNumber;

    @NotNull
    private UserRole role;
}

