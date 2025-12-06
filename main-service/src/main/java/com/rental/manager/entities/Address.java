package com.rental.manager.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "apartment_addresses")
@Getter
@Setter
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(name = "postal_code")
    private String postalCode;

    private String country;
    private String city;
    private String district;
    private String street;

    @Column(name = "building_number")
    private String buildingNumber;

    @Column(name = "floor_number")
    private Integer floorNumber;

    @Column(name = "apartment_number")
    private Integer apartmentNumber;

    @Column(name = "distance_from_city_center_km")
    public Double distanceToCityCenterKm;

    @Column(name = "distance_from_airport_km")
    public Double distanceToAirportKm;

    @Column(name = "distance_from_beach_km")
    public Double distanceTOBeachKm;

    @Transient
    public String getAddress() {
        return String.format("%s, %s, %s, %s, %s, %s, %s, %s",
                        postalCode != null ? postalCode : "",
                        country != null ? country : "",
                        city != null ? city : "",
                        district != null ? district : "",
                        street != null ? street : "",
                        buildingNumber != null ? buildingNumber : "",
                        floorNumber != null ? floorNumber : "",
                        apartmentNumber != null ? apartmentNumber :"")

                .replace(", ,", ",")
                .replaceAll("(^, )|(, $)", "");
    }
}

