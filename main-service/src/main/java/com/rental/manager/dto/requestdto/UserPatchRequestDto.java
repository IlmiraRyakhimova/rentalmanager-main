package com.rental.manager.dto.requestdto;

import com.rental.manager.validation.ValidEmail;
import com.rental.manager.validation.ValidPhone;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserPatchRequestDto {
    private String name;

    @ValidPhone
    private String phoneNumber;

    @ValidEmail
    private String email;
}
