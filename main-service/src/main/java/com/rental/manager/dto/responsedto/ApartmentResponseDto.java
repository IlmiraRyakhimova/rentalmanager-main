package com.rental.manager.dto.responsedto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class ApartmentResponseDto {
    private UUID id;
    private String title;
    private UserResponseDto owner;
    private AddressResponseDto address;
    private String accommodationType;
    private BigDecimal pricePerNight;
    private Double area;
    private Integer numberOfRooms;
    private Integer numberOfBathrooms;
}
