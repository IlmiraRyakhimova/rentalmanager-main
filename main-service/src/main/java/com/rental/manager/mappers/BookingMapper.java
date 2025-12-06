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
    private final ApartmentRepository apartmentRepository;


    public BookingResponseDTO toDTO(Booking entity) {
        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setId(entity.getId());
        dto.setBookingCode(entity.getBookingCode());
        dto.setApartment(apartmentMapper.toDTO(entity.getApartment()));
        dto.setApartmentTitle(entity.getApartment().getTitle());
        dto.setGuestName(entity.getMainGuestName());
        dto.setGuestEmail(entity.getGuestEmail());
        dto.setGuestPhoneNumber(entity.getGuestPhoneNumber());
        dto.setCheckInDate(entity.getCheckInDate());
        dto.setCheckOutDate(entity.getCheckOutDate());
        dto.setTotalGuests(entity.getTotalGuests());
        dto.setTotalNights(entity.getTotalNights());
        dto.setTotalPrice(entity.getTotalPrice());
        dto.setBookingStatus(entity.getBookingStatus());
        dto.setPaymentStatus(entity.getPaymentStatus());
        return dto;
    }

    public Booking toEntity(BookingRequestDTO dto) {

        Booking entity = new Booking();
        Apartment apartment = apartmentRepository.findById(dto.getApartmentId()).orElseThrow();



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

    public Booking toEntity(BookingPatchRequestDTO dto) {
        Booking entity = new Booking();
        if (dto.getApartmentId() != null) {
            Apartment apartment = apartmentRepository.findById(dto.getApartmentId()).orElseThrow();


            entity.setApartment(apartment);
        }
        if (dto.getGuestName() != null) {
            entity.setMainGuestName(dto.getGuestName());
        }
        if (dto.getGuestEmail() != null) {
            entity.setGuestEmail(dto.getGuestEmail());
        }
        if (dto.getGuestPhoneNumber() != null) {
            entity.setGuestPhoneNumber(dto.getGuestPhoneNumber());
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
        return entity;
    }
}
