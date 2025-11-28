package com.rental.manager.dto.requestdto;


import lombok.Data;

@Data
public class AddressPatchRequestDTO {
    private String postalCode;
    private String country;
    private String city;
    private String district;
    private String street;
    private String buildingNumber;
    private Integer floorNumber;
    private Integer apartmentNumber;
}
