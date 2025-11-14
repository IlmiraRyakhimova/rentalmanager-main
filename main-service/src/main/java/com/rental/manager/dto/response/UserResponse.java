package com.rental.manager.dto.response;

import com.rental.manager.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private UserRole role;
}
