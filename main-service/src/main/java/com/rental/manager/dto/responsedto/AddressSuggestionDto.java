package com.rental.manager.dto.responsedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressSuggestionDto {
    
    private String displayName;
    private String country;
    private String city;
    private String street;
    private String houseNumber;
    private String postalCode;
    private Double latitude;
    private Double longitude;
    private String placeId;
}
