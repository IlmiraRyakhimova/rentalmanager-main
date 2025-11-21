package com.rental.manager.dto.requestdto;

import com.rental.manager.entities.enums.BookingStatus;
import com.rental.manager.entities.enums.PaymentStatus;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class BookingPatchRequestDTO {
    private ApartmentPatchRequestDTO apartment;
    private String guestName;
    private String guestEmail;
    private String guestPhoneNumber;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer numberOfAdults;
    private Integer numberOfChildren;
}
