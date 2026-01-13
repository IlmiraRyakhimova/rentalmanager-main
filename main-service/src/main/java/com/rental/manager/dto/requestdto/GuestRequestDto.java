package com.rental.manager.dto.requestdto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GuestRequestDto {
    @NotBlank
    private String name;

    private String email;

    private String phoneNumber;
    private String notes;
}
