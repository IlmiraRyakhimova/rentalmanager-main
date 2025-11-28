package com.rental.manager.dto.requestdto;

import lombok.Data;
import java.math.BigDecimal;

@Data
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
