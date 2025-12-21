package com.rental.manager.dto.requestdto;

import com.rental.manager.validation.ValidPhone;
import lombok.Data;

@Data
public class ChangePhoneNumberRequest {
    @ValidPhone
    private String phoneNumber;
}
