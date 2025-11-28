package com.rental.manager.dto.responsedto;


import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ApartmentResponseDTO {
    private UUID id;
    private String title;
    private UserResponseDTO owner;
    private AddressResponseDTO address;
    private String accommodationType;
    private BigDecimal pricePerNight;
    private Double area;
    private Integer numberOfRooms;
    private Integer numberOfBathrooms;
    private Integer MaxGuestsAllowed;
}
