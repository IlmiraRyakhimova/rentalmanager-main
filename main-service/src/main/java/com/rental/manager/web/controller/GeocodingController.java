package com.rental.manager.web.controller;

import com.rental.manager.dto.responsedto.AddressSuggestionDto;
import com.rental.manager.service.GeocodingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/geocoding")
@RequiredArgsConstructor
public class GeocodingController {

    private final GeocodingService geocodingService;

    /**
     * Поиск адресов по запросу
     * GET /api/geocoding/search?q=Москва, Тверская 10
     */
    @GetMapping("/search")
    public ResponseEntity<List<AddressSuggestionDto>> searchAddress(@RequestParam String q) {
        List<AddressSuggestionDto> results = geocodingService.searchAddress(q);
        return ResponseEntity.ok(results);
    }

    /**
     * Обратное геокодирование (координаты -> адрес)
     * GET /api/geocoding/reverse?lat=55.7558&lon=37.6173
     */
    @GetMapping("/reverse")
    public ResponseEntity<AddressSuggestionDto> reverseGeocode(
            @RequestParam Double lat,
            @RequestParam Double lon) {
        AddressSuggestionDto result = geocodingService.reverseGeocode(lat, lon);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    /**
     * Получение координат по адресу
     * GET /api/geocoding/coordinates?address=Москва, Тверская 10
     */
    @GetMapping("/coordinates")
    public ResponseEntity<AddressSuggestionDto> getCoordinates(@RequestParam String address) {
        AddressSuggestionDto result = geocodingService.getCoordinates(address);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    /**
     * Валидация адреса
     * GET /api/geocoding/validate?country=Россия&city=Москва&street=Тверская&houseNumber=10
     */
    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateAddress(
            @RequestParam String country,
            @RequestParam String city,
            @RequestParam String street,
            @RequestParam String houseNumber) {
        boolean isValid = geocodingService.validateAddress(country, city, street, houseNumber);
        return ResponseEntity.ok(isValid);
    }
}
