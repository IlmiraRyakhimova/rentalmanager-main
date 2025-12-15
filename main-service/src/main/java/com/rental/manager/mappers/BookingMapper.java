package com.rental.manager.mappers;

import com.rental.manager.dto.requestdto.BookingPatchRequestDto;
import com.rental.manager.dto.requestdto.BookingRequestDto;
import com.rental.manager.dto.responsedto.BookingResponseDto;
import com.rental.manager.entities.Booking;
import com.rental.manager.entities.Apartment;
import com.rental.manager.repository.ApartmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingMapper {

    private final ApartmentMapper apartmentMapper;
    private final GuestMapper guestMapper;
    private final ApartmentRepository apartmentRepository;


    public BookingResponseDto toDto(Booking entity) {
        return BookingResponseDto.builder()
                .id(entity.getId())
                .bookingCode(entity.getBookingCode())
                .apartment(apartmentMapper.toDto(entity.getApartment()))
                .apartmentTitle(entity.getApartment().getTitle())
                .mainGuest(guestMapper.toDto(entity.getMainGuest()))
                .checkInDate(entity.getCheckInDate())
                .checkOutDate(entity.getCheckOutDate())
                .totalGuests(entity.getTotalGuests())
                .totalNights(entity.getTotalNights())
                .totalPrice(entity.getTotalPrice())
                .bookingStatus(entity.getBookingStatus())
                .paymentStatus(entity.getPaymentStatus())
                .notes(entity.getNotes())
                .build();
    }

    public Booking toEntity(BookingRequestDto dto) {

        return Booking.builder()
                .apartment(apartmentRepository.findById(dto.getApartmentId()).orElseThrow())
                .mainGuest(guestMapper.toEntity(dto.getMainGuest()))
                .checkInDate(dto.getCheckInDate())
                .checkOutDate(dto.getCheckOutDate())
                .numberOfAdults(dto.getNumberOfAdults())
                .numberOfChildren(dto.getNumberOfChildren())
                .notes(dto.getNotes())
                .build();
    }

    public void updateEntity(Booking entity, BookingPatchRequestDto dto) {
        if (dto.getApartmentId() != null) {
            Apartment apartment = apartmentRepository.findById(dto.getApartmentId())
                    .orElseThrow(() -> new EntityNotFoundException("Апартаменты не найдены с id: " + dto.getApartmentId()));
            entity.setApartment(apartment);
        }
        if (dto.getMainGuest() != null) {
            guestMapper.updateEntity(entity.getMainGuest(), dto.getMainGuest());
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
    }
}
