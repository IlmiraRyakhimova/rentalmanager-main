package com.rental.manager.service.impl;

import com.rental.manager.dto.requestdto.BookingPatchRequestDTO;
import com.rental.manager.dto.requestdto.BookingPaymentStatusPatchRequestDTO;
import com.rental.manager.dto.requestdto.BookingRequestDTO;
import com.rental.manager.dto.requestdto.BookingStatusPatchRequestDTO;
import com.rental.manager.dto.responsedto.BookingResponseDTO;
import com.rental.manager.entities.Booking;
import com.rental.manager.mappers.BookingMapper;
import com.rental.manager.repository.BookingRepository;
import com.rental.manager.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper mapper;

    @Override
    public BookingResponseDTO createBooking(BookingRequestDTO request) {
        Booking booking = mapper.toEntity(request);
        return mapper.toDTO(bookingRepository.save(booking));
    }

    public BookingResponseDTO updateBooking(UUID id, BookingRequestDTO request) {
        Booking booking = bookingRepository.findById(id).orElseThrow();
        booking.setApartment(mapper.toEntity(request).getApartment());
        booking.setMainGuestName(request.getGuestName());
        booking.setGuestEmail(request.getGuestEmail());
        booking.setGuestPhoneNumber(request.getGuestPhoneNumber());
        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());
        booking.setNumberOfAdults(request.getNumberOfAdults());
        booking.setNumberOfChildren(request.getNumberOfChildren());
        return mapper.toDTO(booking);
    }

    @Override
    public BookingResponseDTO patchBooking(UUID id, BookingPatchRequestDTO request) {
        Booking booking = bookingRepository.findById(id).orElseThrow();
        if (request.getApartment() != null) {
            booking.setApartment(mapper.toEntity(request).getApartment());

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
        return mapper.toDTO(booking);
    }

    @Override
    public BookingResponseDTO patchBookingStatus(UUID id, BookingStatusPatchRequestDTO request) {
        Booking booking = bookingRepository.findById(id).orElseThrow();
        booking.setBookingStatus(request.getBookingStatus());
        return mapper.toDTO(booking);
    }

    @Override
    public BookingResponseDTO patchPaymentStatus(UUID id, BookingPaymentStatusPatchRequestDTO request) {
        Booking booking = bookingRepository.findById(id).orElseThrow();
        booking.setPaymentStatus(request.getPaymentStatus());
        return mapper.toDTO(booking);
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
        return mapper.toDTO(bookingRepository.findById(id).orElseThrow());
    }

    @Override
    public BookingResponseDTO getBookingByBookingCode(String bookingCode) {
        return mapper.toDTO(bookingRepository.findByBookingCode(bookingCode));
    }

    @Override
    public List<BookingResponseDTO>  getBookingsByGuestName(String guestName) {
        List<Booking> bookings = bookingRepository.findByGuestNameContainingIgnoreCase(guestName);
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
    public List<BookingResponseDTO> getBookingsByBookingStatus(String bookingStatus) {
        List<Booking> bookings = bookingRepository.findByBookingStatusContainingIgnoreCase(bookingStatus);
        return bookings.stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<BookingResponseDTO> getBookingsByPaymentStatus(String paymentStatus) {
        List<Booking> bookings = bookingRepository.findByPaymentStatusContainingIgnoreCase(paymentStatus);
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
