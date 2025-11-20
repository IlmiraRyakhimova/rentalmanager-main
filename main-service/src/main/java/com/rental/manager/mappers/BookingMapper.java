package com.rental.manager.mappers;

import com.rental.manager.dto.requestdto.BookingRequestDTO;
import com.rental.manager.dto.responsedto.BookingResponseDTO;
import com.rental.manager.entities.Booking;
import com.rental.manager.entities.apartment.Apartment;
import com.rental.manager.repository.ApartmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingMapper {

    private final ApartmentRepository apartmentRepository;

    public BookingResponseDTO toDTO(Booking entity) {
        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setId(entity.getId());
        dto.setBookingCode(entity.getBookingCode());
        dto.setApartmentId(entity.getApartment().getId());
        dto.setApartmentTitle(entity.getApartment().getTitle());
        dto.setGuestName(entity.getMainGuestName());
        dto.setGuestEmail(entity.getGuestEmail());
        dto.setGuestPhoneNumber(entity.getGuestPhoneNumber());
        dto.setCheckInDate(entity.getCheckInDate());
        dto.setCheckOutDate(entity.getCheckOutDate());
        dto.setTotalGuests(entity.getTotalNumberOdGuests());
        dto.setTotalNights(entity.getTotalNights());
        dto.setTotalPrice(entity.getTotalPrice());
        dto.setBookingStatus(entity.getBookingStatus());
        dto.setPaymentStatus(entity.getPaymentStatus());
        return dto;
    }

    public Booking toEntity(BookingRequestDTO dto) {

        Booking entity = new Booking();

        Apartment apartment = apartmentRepository.findById(dto.getApartmentId())
                .orElseThrow(() -> new EntityNotFoundException("Apartment not found with id: " + dto.getApartmentId()));

        entity.setApartment(apartment);
        entity.setMainGuestName(dto.getGuestName());
        entity.setGuestEmail(dto.getGuestEmail());
        entity.setGuestPhoneNumber(dto.getGuestPhoneNumber());
        entity.setCheckInDate(dto.getCheckInDate());
        entity.setCheckOutDate(dto.getCheckOutDate());
        entity.setNumberOfAdults(dto.getNumberOfAdults());
        entity.setNumberOfChildren(dto.getNumberOfChildren());

        return entity;
    }
}
