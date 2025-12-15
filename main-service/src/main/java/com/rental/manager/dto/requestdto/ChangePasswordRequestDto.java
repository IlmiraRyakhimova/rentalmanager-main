package com.rental.manager.dto.requestdto;

import com.rental.manager.validation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePasswordRequestDto {

    @NotBlank
    private String oldPassword;

    @NotBlank
    @ValidPassword
    private String newPassword;
}
