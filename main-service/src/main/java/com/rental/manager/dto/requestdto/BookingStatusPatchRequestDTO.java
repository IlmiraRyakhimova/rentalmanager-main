package com.rental.manager.dto.requestdto;


import com.rental.manager.entities.enums.BookingStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingStatusPatchRequestDTO {
    private BookingStatus bookingStatus;
}
