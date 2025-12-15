package com.rental.manager.dto.requestdto;

import com.rental.manager.validation.ValidEmail;
import com.rental.manager.validation.ValidPassword;
import com.rental.manager.validation.ValidPhone;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class GuestPatchRequestDto {
    private String name;

    @ValidEmail
    private String email;

    @ValidPhone
    private String phoneNumber;
    private String notes;
}
