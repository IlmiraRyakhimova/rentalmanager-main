package com.rental.manager.dto.responsedto;

import lombok.Data;


import java.util.UUID;

@Data
public class AddressResponseDTO {
    private UUID id;
    private String postalCode;
    private String country;
    private String city;
    private String district;
    private String street;
    private String buildingNumber;
    private Integer floorNumber;
    private Integer apartmentNumber;
}
