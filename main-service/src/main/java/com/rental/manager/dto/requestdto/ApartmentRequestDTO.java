package com.rental.manager.dto.requestdto;


import com.rental.manager.entities.User;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class ApartmentRequestDTO {
    private String title;
    private UserRequestDTO owner;
    private AddressRequestDTO address;
    private String accommodationType;
    private BigDecimal pricePerNight;
    private Double area;
    private Integer numberOfRooms;
    private Integer numberOfBathrooms;
}
