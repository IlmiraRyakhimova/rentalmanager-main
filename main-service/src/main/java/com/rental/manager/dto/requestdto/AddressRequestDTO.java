package com.rental.manager.dto.requestdto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Getter
@Service
public class AddressRequestDTO {


    private String postalCode;
    private String country;
    private String city;
    private String district;

    @NotBlank
    private String street;

    @NotBlank
    private String buildingNumber;
    private Integer floorNumber;

    @NotBlank
    private Integer apartmentNumber;
}
