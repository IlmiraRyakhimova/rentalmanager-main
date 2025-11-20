package com.rental.manager.dto.requestdto;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class UserPatchRequestDTO {
    private String Name;
    private String phoneNumber;
    private String email;
}
