package com.rental.manager.dto.requestdto;


import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class BookingPatchRequestDTO {
    private UUID apartmentId;
    GuestPatchRequestDTO mainGuest;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer numberOfAdults;
    private Integer numberOfChildren;
    private String notes;
}
