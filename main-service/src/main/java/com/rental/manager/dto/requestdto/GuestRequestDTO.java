package com.rental.manager.dto.requestdto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GuestRequestDTO {
    @NotBlank
    private String name;

    @Email
    private String email;

    @NotBlank
    private String phoneNumber;
    private String notes;
}
