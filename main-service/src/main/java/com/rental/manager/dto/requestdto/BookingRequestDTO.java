package com.rental.manager.dto.requestdto;

import com.rental.manager.entities.enums.BookingStatus;
import com.rental.manager.entities.enums.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class BookingRequestDTO {
    private String bookingCode;
    private String apartmentTitle;
    private String guestName;
    private String guestEmail;
    private String guestPhoneNumber;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer totalGuests;
    private Integer totalNights;
    private BigDecimal totalPrice;
    private BookingStatus bookingStatus;
    private PaymentStatus paymentStatus;
}
