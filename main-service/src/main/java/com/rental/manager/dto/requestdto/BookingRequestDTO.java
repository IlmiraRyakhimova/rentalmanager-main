package com.rental.manager.dto.requestdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class BookingRequestDTO {

    @NotNull
    private UUID apartmentId;

    @NotBlank
    private String guestName;

    @Email
    private String guestEmail;

    @NotBlank
    private String guestPhoneNumber;
    private Integer numberOfAdults;
    private Integer numberOfChildren;

    @NotNull
    private LocalDate checkInDate;

    @NotNull
    private LocalDate checkOutDate;
}
