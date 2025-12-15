package com.rental.manager.dto.responsedto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class AddressResponseDto {
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
