package com.rental.manager.dto.requestdto;

import com.rental.manager.entities.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRequestDTO {

    @NotBlank
    private String name;

    @Email
    private String email;

    @NotBlank
    private String phoneNumber;

    @NotNull
    private UserRole role;
}


//curl -X POST http://localhost:8080/api/apartments \
//        -H "Content-Type: application/json" \
//        -d '{
//        "title": "Уютная квартира в центре",
//        "accommodationType": "Apartment",
//        "owner": {
//        "name": "Test",
//        "email": "test@mail.ru",
//        "role": "OWNER",
//        "phoneNumber": "+79991234560"
//        },
//        "address": {
//        "postalCode": "123456",
//        "country": "Россия",
//        "city": "Москва",
//        "district": "Центральный",
//        "street": "Тверская",
//        "houseNumber": "10",
//        "floorNumber": 5,
//        "apartmentNumber": 42
//        },
//        "pricePerNight": 5000.00,
//        "area": 65.5,
//        "numberOfRooms": 2,
//        "numberOfBathrooms": 1
//        }'