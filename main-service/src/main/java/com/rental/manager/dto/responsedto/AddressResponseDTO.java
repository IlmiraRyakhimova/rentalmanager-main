package com.rental.manager.dto.responsedto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AddressResponseDTO {
    private UUID id;
    private String postalCode;
    private String country;
    private String city;
    private String district;
    private String street;
    private String houseNumber;
    private Integer floorNumber;
    private Integer apartmentNumber;
}
