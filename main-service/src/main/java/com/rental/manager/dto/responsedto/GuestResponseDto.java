package com.rental.manager.dto.responsedto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class GuestResponseDto {
    private UUID id;
    private String name;
    private String email;
    private String phoneNumber;
    private String notes;
}
