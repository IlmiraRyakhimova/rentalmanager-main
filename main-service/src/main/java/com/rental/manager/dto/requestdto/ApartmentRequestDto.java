package com.rental.manager.dto.requestdto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ApartmentRequestDto {

    @NotBlank
    private String title;

    @Valid
    private UserRequestDto owner;

    @Valid
    private AddressRequestDto address;
    private String accommodationType;
    private BigDecimal pricePerNight;
    private Double area;
    private Integer numberOfRooms;
    private Integer numberOfBathrooms;
}