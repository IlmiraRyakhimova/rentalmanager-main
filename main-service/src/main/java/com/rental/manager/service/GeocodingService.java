package com.rental.manager.service;

import com.rental.manager.dto.responsedto.AddressSuggestionDto;
import com.rental.manager.dto.responsedto.GeocodingResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с геокодированием адресов через Nominatim (OpenStreetMap)
 */
@Slf4j
@Service
public class GeocodingService {

    private final WebClient webClient;

    @Value("${geocoding.nominatim.url:https://nominatim.openstreetmap.org}")
    private String nominatimUrl;

    @Value("${geocoding.nominatim.user-agent:RentalManagerApp/1.0}")
    private String userAgent;

    public GeocodingService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    /**
     * Поиск адресов по запросу
     *
     * @param query поисковый запрос (например, "Москва, Тверская 10")
     * @return список найденных адресов
     */
    public List<AddressSuggestionDto> searchAddress(String query) {
        if (query == null || query.trim().isEmpty()) {
            return List.of();
        }

        try {
            List<GeocodingResponseDto> results = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("https")
                            .host("nominatim.openstreetmap.org")
                            .path("/search")
                            .queryParam("q", query)
                            .queryParam("format", "json")
                            .queryParam("addressdetails", "1")
                            .queryParam("limit", "10")
                            .build())
                    .header("User-Agent", userAgent)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<GeocodingResponseDto>>() {})
                    .timeout(Duration.ofSeconds(5))
                    .onErrorResume(e -> {
                        log.error("Error searching address: {}", e.getMessage());
                        return Mono.just(List.of());
                    })
                    .block();

            if (results == null) {
                return List.of();
            }

            return results.stream()
                    .map(this::mapToSuggestion)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Error during geocoding search", e);
            return List.of();
        }
    }

    /**
     * Поиск адреса по координатам (обратное геокодирование)
     *
     * @param latitude широта
     * @param longitude долгота
     * @return найденный адрес или null
     */
    public AddressSuggestionDto reverseGeocode(Double latitude, Double longitude) {
        if (latitude == null || longitude == null) {
            return null;
        }

        try {
            GeocodingResponseDto result = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("https")
                            .host("nominatim.openstreetmap.org")
                            .path("/reverse")
                            .queryParam("lat", latitude)
                            .queryParam("lon", longitude)
                            .queryParam("format", "json")
                            .queryParam("addressdetails", "1")
                            .build())
                    .header("User-Agent", userAgent)
                    .retrieve()
                    .bodyToMono(GeocodingResponseDto.class)
                    .timeout(Duration.ofSeconds(5))
                    .onErrorResume(e -> {
                        log.error("Error in reverse geocoding: {}", e.getMessage());
                        return Mono.empty();
                    })
                    .block();

            return result != null ? mapToSuggestion(result) : null;

        } catch (Exception e) {
            log.error("Error during reverse geocoding", e);
            return null;
        }
    }

    /**
     * Получение координат по адресу
     *
     * @param address полный адрес
     * @return координаты или null
     */
    public AddressSuggestionDto getCoordinates(String address) {
        List<AddressSuggestionDto> results = searchAddress(address);
        return results.isEmpty() ? null : results.get(0);
    }

    /**
     * Валидация адреса
     *
     * @param country страна
     * @param city город
     * @param street улица
     * @param houseNumber номер дома
     * @return true если адрес существует
     */
    public boolean validateAddress(String country, String city, String street, String houseNumber) {
        String query = String.format("%s, %s, %s %s", country, city, street, houseNumber);
        List<AddressSuggestionDto> results = searchAddress(query);
        return !results.isEmpty();
    }

    /**
     * Маппинг ответа Nominatim в DTO
     */
    private AddressSuggestionDto mapToSuggestion(GeocodingResponseDto response) {
        AddressSuggestionDto suggestion = new AddressSuggestionDto();
        suggestion.setDisplayName(response.getDisplayName());
        suggestion.setPlaceId(response.getPlaceId() != null ? response.getPlaceId().toString() : null);

        if (response.getLat() != null && response.getLon() != null) {
            try {
                suggestion.setLatitude(Double.parseDouble(response.getLat()));
                suggestion.setLongitude(Double.parseDouble(response.getLon()));
            } catch (NumberFormatException e) {
                log.warn("Invalid coordinates: lat={}, lon={}", response.getLat(), response.getLon());
            }
        }

        if (response.getAddress() != null) {
            GeocodingResponseDto.AddressDetails addr = response.getAddress();
            suggestion.setCountry(addr.getCountry());
            
            // Город может быть в разных полях
            String city = addr.getCity();
            if (city == null) city = addr.getTown();
            if (city == null) city = addr.getVillage();
            suggestion.setCity(city);
            
            suggestion.setStreet(addr.getRoad());
            suggestion.setHouseNumber(addr.getHouseNumber());
            suggestion.setPostalCode(addr.getPostcode());
        }

        return suggestion;
    }
}
