package com.rental.manager.dto.responsedto;

import lombok.Data;

import java.util.UUID;

@Data
public class GuestResponseDTO {
    private UUID id;
    private String name;
    private String email;
    private String phoneNumber;
    private String notes;
}
