package com.rental.manager.dto.requestdto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ApartmentPatchRequestDTO {
    private String title;
    String accommodationType;
    BigDecimal pricePerNight;
    Double area;
    Integer numberOfRooms;
    Integer numberOfBathrooms;
    AddressPatchRequestDTO address;
    UserPatchRequestDTO owner;
}
