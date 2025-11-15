package com.rental.manager.dto.requestdto;

import com.rental.manager.entities.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDTO {
    private String name;
    private String email;
    private String phoneNumber;
    private UserRole role;
}
