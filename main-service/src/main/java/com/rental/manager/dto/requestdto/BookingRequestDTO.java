package com.rental.manager.dto.requestdto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class BookingRequestDTO {

    @NotNull
    private UUID apartmentId;

    private GuestRequestDTO mainGuest;
    private Integer numberOfAdults;
    private Integer numberOfChildren;

    @NotNull
    private LocalDate checkInDate;

    @NotNull
    private LocalDate checkOutDate;
    private String notes;
}
