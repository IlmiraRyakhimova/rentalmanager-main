package com.rental.manager.service.impl;

import com.rental.manager.dto.requestdto.BookingPatchRequestDTO;
import com.rental.manager.dto.requestdto.BookingPaymentStatusPatchRequestDTO;
import com.rental.manager.dto.requestdto.BookingRequestDTO;
import com.rental.manager.dto.requestdto.BookingStatusPatchRequestDTO;
import com.rental.manager.dto.responsedto.BookingResponseDTO;
import com.rental.manager.entities.Booking;
import com.rental.manager.entities.apartment.Apartment;
import com.rental.manager.entities.enums.BookingStatus;
import com.rental.manager.entities.enums.PaymentStatus;
import com.rental.manager.mappers.BookingMapper;
import com.rental.manager.repository.ApartmentRepository;
import com.rental.manager.repository.BookingRepository;
import com.rental.manager.service.BookingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ApartmentRepository apartmentRepository;
    private final BookingMapper mapper;

    @Override
    @Transactional
    public BookingResponseDTO createBooking(BookingRequestDTO request) {
        Apartment apartment = apartmentRepository.findById(request.getApartmentId())
                .orElseThrow(() -> new EntityNotFoundException("Apartment not found with id: " + request.getApartmentId()));

        Booking booking = mapper.toEntity(request);
        booking.setApartment(apartment);
        return mapper.toDTO(bookingRepository.save(booking));
    }

    public BookingResponseDTO updateBooking(UUID id, BookingRequestDTO request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + id));

        Apartment apartment = apartmentRepository.findById(request.getApartmentId())
                .orElseThrow(() -> new EntityNotFoundException("Apartment not found with id: " + request.getApartmentId()));

        booking.setApartment(apartment);
        booking.setMainGuestName(request.getGuestName());
        booking.setGuestEmail(request.getGuestEmail());
        booking.setGuestPhoneNumber(request.getGuestPhoneNumber());
        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());
        booking.setNumberOfAdults(request.getNumberOfAdults());
        booking.setNumberOfChildren(request.getNumberOfChildren());
        return mapper.toDTO(bookingRepository.save(booking));
    }

    @Override
    public BookingResponseDTO patchBooking(UUID id, BookingPatchRequestDTO request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + id));

        if (request.getApartmentId() != null) {
            Apartment apartment = apartmentRepository.findById(request.getApartmentId())
                    .orElseThrow(() -> new EntityNotFoundException("Apartment not found with id: " + request.getApartmentId()));
            booking.setApartment(apartment);
        }
        if (request.getGuestName() != null) {
            booking.setMainGuestName(request.getGuestName());
        }
        if (request.getGuestEmail() != null) {
            booking.setGuestEmail(request.getGuestEmail());
        }
        if (request.getGuestPhoneNumber() != null) {
            booking.setGuestPhoneNumber(request.getGuestPhoneNumber());
        }
        if (request.getCheckInDate() != null) {
            booking.setCheckInDate(request.getCheckInDate());
        }
        if (request.getCheckOutDate() != null) {
            booking.setCheckOutDate(request.getCheckOutDate());
        }
        if (request.getNumberOfAdults() != null) {
            booking.setNumberOfAdults(request.getNumberOfAdults());
        }
        if (request.getNumberOfChildren() != null) {
            booking.setNumberOfChildren(request.getNumberOfChildren());
        }
        return mapper.toDTO(bookingRepository.save(booking));
    }

    @Override
    public BookingResponseDTO patchBookingStatus(UUID id, BookingStatusPatchRequestDTO request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + id));
        booking.setBookingStatus(request.getBookingStatus());
        return mapper.toDTO(bookingRepository.save(booking));
    }

    @Override
    public BookingResponseDTO patchPaymentStatus(UUID id, BookingPaymentStatusPatchRequestDTO request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + id));
        booking.setPaymentStatus(request.getPaymentStatus());
        return mapper.toDTO(bookingRepository.save(booking));
    }

    @Override
    public void deleteBooking(UUID id) {
        bookingRepository.deleteById(id);
    }

    @Override
    public List<BookingResponseDTO> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream().map(mapper::toDTO).toList();
    }

    @Override
    public BookingResponseDTO getBookingById(UUID id) {
        return mapper.toDTO(bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + id)));
    }

    @Override
    public BookingResponseDTO getBookingByBookingCode(String bookingCode) {
        return mapper.toDTO(bookingRepository.findByBookingCode(bookingCode));

    }

    @Override
    public List<BookingResponseDTO>  getBookingsByGuestName(String guestName) {
        List<Booking> bookings = bookingRepository.findByMainGuestNameContainingIgnoreCase(guestName);
        return bookings.stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<BookingResponseDTO> getBookingsByGuestEmail(String guestEmail) {
        List<Booking> bookings = bookingRepository.findByGuestEmailContainingIgnoreCase(guestEmail);
        return bookings.stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<BookingResponseDTO> getBookingsByGuestPhoneNumber(String guestPhoneNumber) {
        List<Booking> bookings = bookingRepository.findByGuestPhoneNumberContainingIgnoreCase(guestPhoneNumber);
        return bookings.stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<BookingResponseDTO> getBookingsByBookingStatus(BookingStatus bookingStatus) {
        List<Booking> bookings = bookingRepository.findByBookingStatus(bookingStatus);
        return bookings.stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<BookingResponseDTO> getBookingsByPaymentStatus(PaymentStatus paymentStatus) {
        List<Booking> bookings = bookingRepository.findByPaymentStatus(paymentStatus);
        return bookings.stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<BookingResponseDTO> getBookingsByApartmentId(UUID apartmentId) {
        List<Booking> bookings = bookingRepository.findByApartmentId(apartmentId);
        return bookings.stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<BookingResponseDTO> getBookingsByApartmentTitle(String apartmentTitle) {
        List<Booking> bookings = bookingRepository.findByApartmentTitleContainingIgnoreCase(apartmentTitle);
        return bookings.stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<BookingResponseDTO> getBookingsByApartmentOwnerId(UUID ownerId) {
        List<Booking> bookings = bookingRepository.findByApartmentOwnerId(ownerId);
        return bookings.stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<BookingResponseDTO> getBookingsByApartmentOwnerName(String ownerName) {
        List<Booking> bookings = bookingRepository.findByApartmentOwnerNameContainingIgnoreCase(ownerName);
        return bookings.stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<BookingResponseDTO> getBookingsByApartmentOwnerEmail(String ownerEmail) {
        List<Booking> bookings = bookingRepository.findByApartmentOwnerEmailContainingIgnoreCase(ownerEmail);
        return bookings.stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<BookingResponseDTO> getBookingsByApartmentOwnerPhoneNumber(String ownerPhoneNumber) {
        List<Booking> bookings = bookingRepository.findByApartmentOwnerPhoneNumberContainingIgnoreCase(ownerPhoneNumber);
        return bookings.stream().map(mapper::toDTO).toList();
    }
}
