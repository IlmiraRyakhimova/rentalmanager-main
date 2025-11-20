package com.rental.manager.dto.requestdto;


import com.rental.manager.entities.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class ApartmentRequestDTO {

    @NotBlank
    private String title;

    @NotBlank
    private UserRequestDTO owner;

    @Valid
    private AddressRequestDTO address;
    private String accommodationType;
    private BigDecimal pricePerNight;
    private Double area;
    private Integer numberOfRooms;
    private Integer numberOfBathrooms;
}
