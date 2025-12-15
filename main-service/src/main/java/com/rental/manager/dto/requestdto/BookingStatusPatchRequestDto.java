package com.rental.manager.dto.requestdto;


import com.rental.manager.entities.enums.BookingStatus;
import lombok.Data;


@Data
public class BookingStatusPatchRequestDto {
    private BookingStatus bookingStatus;
}
