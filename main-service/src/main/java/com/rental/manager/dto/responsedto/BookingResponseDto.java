package com.rental.manager.dto.responsedto;


import com.rental.manager.entities.enums.BookingStatus;
import com.rental.manager.entities.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class BookingResponseDto {
    private UUID id;
    private String bookingCode;
    private ApartmentResponseDto apartment;
    private String apartmentTitle;
    private GuestResponseDto mainGuest;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer totalGuests;
    private Integer totalNights;
    private BigDecimal totalPrice;
    private BookingStatus bookingStatus;
    private PaymentStatus paymentStatus;
    private String notes;
}


