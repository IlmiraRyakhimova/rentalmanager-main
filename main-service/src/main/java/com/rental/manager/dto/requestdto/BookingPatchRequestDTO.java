package com.rental.manager.dto.requestdto;

import com.rental.manager.entities.enums.BookingStatus;
import com.rental.manager.entities.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

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
