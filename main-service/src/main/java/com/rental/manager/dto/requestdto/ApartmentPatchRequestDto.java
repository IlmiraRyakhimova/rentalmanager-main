package com.rental.manager.dto.requestdto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ApartmentPatchRequestDto {
    private String title;
    private String accommodationType;
    private BigDecimal pricePerNight;
    private Double area;
    private Integer numberOfRooms;
    private Integer numberOfBathrooms;
    private AddressPatchRequestDto address;
    private UserPatchRequestDto owner;
}
