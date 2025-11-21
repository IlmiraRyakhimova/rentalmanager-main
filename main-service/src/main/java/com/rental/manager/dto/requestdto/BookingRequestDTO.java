package com.rental.manager.dto.requestdto;

import com.rental.manager.entities.enums.BookingStatus;
import com.rental.manager.entities.enums.PaymentStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class BookingRequestDTO {

    @NotNull
    private ApartmentRequestDTO apartment;

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
