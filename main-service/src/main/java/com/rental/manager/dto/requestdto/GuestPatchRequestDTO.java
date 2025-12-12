package com.rental.manager.dto.requestdto;

import lombok.Data;

@Data
public class GuestPatchRequestDTO {
    private String name;
    private String email;
    private String phoneNumber;
    private String notes;
}
