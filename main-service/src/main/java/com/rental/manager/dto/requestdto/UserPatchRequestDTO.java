package com.rental.manager.dto.requestdto;

import lombok.Data;

@Data
public class UserPatchRequestDTO {
    private String name;
    private String phoneNumber;
    private String email;
}
