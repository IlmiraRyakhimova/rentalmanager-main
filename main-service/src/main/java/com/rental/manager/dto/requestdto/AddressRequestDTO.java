package com.rental.manager.dto.requestdto;

import lombok.Getter;
import org.springframework.stereotype.Service;

@Getter
@Service
public class AddressRequestDTO {
    private String postalCode;
    private String country;
    private String city;
    private String district;
    private String street;
    private String houseNumber;
    private Integer floorNumber;
    private Integer apartmentNumber;
}
