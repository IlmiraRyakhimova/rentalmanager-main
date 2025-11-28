package com.rental.manager.dto.requestdto;


import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class BookingPatchRequestDTO {
    private UUID apartmentId;
    private String guestName;
    private String guestEmail;
    private String guestPhoneNumber;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer numberOfAdults;
    private Integer numberOfChildren;
}
