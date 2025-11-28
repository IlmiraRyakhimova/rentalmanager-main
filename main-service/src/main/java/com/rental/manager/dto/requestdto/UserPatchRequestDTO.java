package com.rental.manager.dto.requestdto;

import lombok.Data;

@Data
public class UserPatchRequestDTO {
    private String Name;
    private String phoneNumber;
    private String email;
}
