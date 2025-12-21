package com.rental.manager.dto.requestdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class AddressRequestDto {


    private String postalCode;
    private String country;
    private String city;
    private String district;
    private String street;
    private String buildingNumber;
    private Integer floorNumber;
    private Integer apartmentNumber;
}
