package com.rental.manager.mappers;

import com.rental.manager.dto.requestdto.BookingPatchRequestDTO;
import com.rental.manager.dto.requestdto.BookingRequestDTO;
import com.rental.manager.dto.responsedto.BookingResponseDTO;
import com.rental.manager.entities.Booking;
import com.rental.manager.entities.Apartment;
import com.rental.manager.repository.ApartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingMapper {

    private final ApartmentMapper apartmentMapper;
    private final GuestMapper guestMapper;
    private final ApartmentRepository apartmentRepository;


    public BookingResponseDTO toDTO(Booking entity) {
        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setId(entity.getId());
        dto.setBookingCode(entity.getBookingCode());
        dto.setApartment(apartmentMapper.toDTO(entity.getApartment()));
        dto.setApartmentTitle(entity.getApartment().getTitle());
        dto.setMainGuest(guestMapper.toDto(entity.getMainGuest()));
        dto.setCheckInDate(entity.getCheckInDate());
        dto.setCheckOutDate(entity.getCheckOutDate());
        dto.setTotalGuests(entity.getTotalGuests());
        dto.setTotalNights(entity.getTotalNights());
        dto.setTotalPrice(entity.getTotalPrice());
        dto.setBookingStatus(entity.getBookingStatus());
        dto.setPaymentStatus(entity.getPaymentStatus());
        dto.setNotes(entity.getNotes());
        return dto;
    }

    public Booking toEntity(BookingRequestDTO dto) {

        Booking entity = new Booking();
        Apartment apartment = apartmentRepository.findById(dto.getApartmentId()).orElseThrow();
        entity.setApartment(apartment);
        entity.setMainGuest(guestMapper.toEntity(dto.getMainGuest()));
        entity.setCheckInDate(dto.getCheckInDate());
        entity.setCheckOutDate(dto.getCheckOutDate());
        entity.setNumberOfAdults(dto.getNumberOfAdults());
        entity.setNumberOfChildren(dto.getNumberOfChildren());
        entity.setNotes(dto.getNotes());

        return entity;
    }

    public Booking toEntity(BookingPatchRequestDTO dto) {
        Booking entity = new Booking();
        if (dto.getApartmentId() != null) {
            Apartment apartment = apartmentRepository.findById(dto.getApartmentId()).orElseThrow();
            entity.setApartment(apartment);
        }
        if (dto.getMainGuest() != null) {
            entity.setMainGuest(guestMapper.toEntity(dto.getMainGuest()));
        }
        if (dto.getCheckInDate() != null) {
            entity.setCheckInDate(dto.getCheckInDate());
        }
        if (dto.getCheckOutDate() != null) {
            entity.setCheckOutDate(dto.getCheckOutDate());
        }
        if (dto.getNumberOfAdults() != null) {
            entity.setNumberOfAdults(dto.getNumberOfAdults());
        }
        if (dto.getNumberOfChildren() != null) {
            entity.setNumberOfChildren(dto.getNumberOfChildren());
        }
        if (dto.getNotes() != null) {
            entity.setNotes(dto.getNotes());
        }
        return entity;
    }
}
