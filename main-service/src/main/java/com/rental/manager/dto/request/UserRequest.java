package com.rental.manager.dto.request;

import com.rental.manager.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    private String name;
    private String email;
    private String phoneNumber;
    private UserRole role;
}
