package com.rental.manager.dto.requestdto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ApartmentRequestDTO {

    @NotBlank
    private String title;

    @Valid
    private UserRequestDTO owner;

    @Valid
    private AddressRequestDTO address;
    private String accommodationType;
    private BigDecimal pricePerNight;
    private Double area;
    private Integer numberOfRooms;
    private Integer numberOfBathrooms;
}