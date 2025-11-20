package com.rental.manager.dto.requestdto;

import com.rental.manager.entities.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDTO {
    @NotBlank
    private String name;

    @Email
    private String email;

    @NotBlank
    private String phoneNumber;
}
