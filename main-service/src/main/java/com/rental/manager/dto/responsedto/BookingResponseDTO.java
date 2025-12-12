package com.rental.manager.dto.responsedto;


import com.rental.manager.entities.enums.BookingStatus;
import com.rental.manager.entities.enums.PaymentStatus;
import lombok.Data;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class BookingResponseDTO {
    private UUID id;
    private String bookingCode;
    private ApartmentResponseDTO apartment;
    private String apartmentTitle;
    private GuestResponseDTO mainGuest;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer totalGuests;
    private Integer totalNights;
    private BigDecimal totalPrice;
    private BookingStatus bookingStatus;
    private PaymentStatus paymentStatus;
    private String notes;
}


