package com.rental.manager.dto.responsedto;

import com.rental.manager.entities.User;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
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
