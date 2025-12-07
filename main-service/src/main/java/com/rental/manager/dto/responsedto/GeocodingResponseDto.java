package com.rental.manager.dto.responsedto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeocodingResponseDto {
    
    @JsonProperty("place_id")
    private Long placeId;
    
    private String licence;
    
    @JsonProperty("osm_type")
    private String osmType;
    
    @JsonProperty("osm_id")
    private Long osmId;
    
    private String lat;
    
    private String lon;
    
    @JsonProperty("display_name")
    private String displayName;
    
    private AddressDetails address;
    
    private List<String> boundingbox;
    
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AddressDetails {
        
        @JsonProperty("house_number")
        private String houseNumber;
        
        private String road;
        
        private String suburb;
        
        private String city;
        
        private String town;
        
        private String village;
        
        private String state;
        
        @JsonProperty("postcode")
        private String postcode;
        
        private String country;
        
        @JsonProperty("country_code")
        private String countryCode;
        
        private String neighbourhood;
        
        private String quarter;
        
        private String district;
    }
}
