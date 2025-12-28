package com.rental.manager.service.impl;

import com.rental.manager.dto.requestdto.BookingPatchRequestDto;
import com.rental.manager.dto.requestdto.BookingPaymentStatusPatchRequestDto;
import com.rental.manager.dto.requestdto.BookingRequestDto;
import com.rental.manager.dto.requestdto.BookingStatusPatchRequestDto;
import com.rental.manager.dto.responsedto.BookingResponseDto;
import com.rental.manager.entities.Booking;
import com.rental.manager.entities.Apartment;
import com.rental.manager.entities.Guest;
import com.rental.manager.entities.User;
import com.rental.manager.entities.enums.BookingStatus;
import com.rental.manager.entities.enums.PaymentStatus;
import com.rental.manager.mappers.BookingMapper;
import com.rental.manager.service.EmailService;
import com.rental.manager.repository.ApartmentRepository;
import com.rental.manager.repository.BookingRepository;
import com.rental.manager.repository.GuestRepository;
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

    private static final String BOOKING_NOT_FOUND_MSG = "Бронирование не найдено с id: ";
    private static final String APARTMENT_NOT_FOUND_MSG = "Апартаменты не найдены с id: ";

    private final BookingRepository bookingRepository;
    private final ApartmentRepository apartmentRepository;
    private final GuestRepository guestRepository;
    private final EmailService emailService;
    private final BookingMapper mapper;

    @Override
    @Transactional
    public BookingResponseDto createBooking(BookingRequestDto request) {
        Apartment apartment = apartmentRepository.findById(request.getApartmentId())
                .orElseThrow(() -> new EntityNotFoundException(APARTMENT_NOT_FOUND_MSG + request.getApartmentId()));

        Booking booking = mapper.toEntity(request);
        booking.setApartment(apartment);
        resolveGuest(booking);
        Booking savedBooking = bookingRepository.save(booking);
        return mapper.toDto(savedBooking);
    }

    public BookingResponseDto updateBooking(UUID id, BookingRequestDto request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(BOOKING_NOT_FOUND_MSG + id));

        Apartment apartment = apartmentRepository.findById(request.getApartmentId())
                .orElseThrow(() -> new EntityNotFoundException(APARTMENT_NOT_FOUND_MSG + request.getApartmentId()));

        booking.setApartment(apartment);
        resolveGuest(booking);
        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());
        booking.setNumberOfAdults(request.getNumberOfAdults());
        booking.setNumberOfChildren(request.getNumberOfChildren());
        booking.setNotes(request.getNotes());
        return mapper.toDto(bookingRepository.save(booking));
    }

    private void resolveGuest(Booking booking) {
        Guest guest = booking.getMainGuest();
        if (guest == null) {
            return;
        }

        Guest existingGuest = findExistingGuest(guest);
        if (existingGuest != null) {
            booking.setMainGuest(existingGuest);
        } else {
            booking.setMainGuest(guestRepository.save(guest));
        }
    }

    private Guest findExistingGuest(Guest guest) {
        if (guest.getPhoneNumber() != null && !guest.getPhoneNumber().isBlank()) {
            return guestRepository.findByPhoneNumber(guest.getPhoneNumber());
        }

        if (guest.getEmail() != null && !guest.getEmail().isBlank()) {
            return guestRepository.findByEmail(guest.getEmail());
        }
            return null;
    }

    @Override
    public BookingResponseDto patchBooking(UUID id, BookingPatchRequestDto request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(BOOKING_NOT_FOUND_MSG + id));

       mapper.updateEntity(booking, request);
        return mapper.toDto(bookingRepository.save(booking));
    }

    @Override
    public BookingResponseDto patchBookingStatus(UUID id, BookingStatusPatchRequestDto request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(BOOKING_NOT_FOUND_MSG + id));
        booking.setBookingStatus(request.getBookingStatus());
        Booking savedBooking = bookingRepository.save(booking);
//        if (request.getBookingStatus() == BookingStatus.CONFIRMED) {
//            String guestEmail = savedBooking.getMainGuest().getEmail();
//            String guestName = savedBooking.getMainGuest().getName();
//            String bookingCode = savedBooking.getBookingCode();
//            emailService.sendBookingInfoToGuest(guestEmail, guestName, bookingCode,
//                    savedBooking.getCheckInDate(), savedBooking.getCheckOutDate());
//            String ownerEmail = savedBooking.getApartment().getOwner().getEmail();
//            String ownerName = savedBooking.getApartment().getOwner().getName();
//            emailService.sendBookingInfoToOwner(ownerEmail, ownerName, guestName, bookingCode,
//                    savedBooking.getCheckInDate(), savedBooking.getCheckOutDate());
//        }
        return mapper.toDto(savedBooking);
    }

    @Override
    public BookingResponseDto patchPaymentStatus(UUID id, BookingPaymentStatusPatchRequestDto request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(BOOKING_NOT_FOUND_MSG + id));
        booking.setPaymentStatus(request.getPaymentStatus());
        return mapper.toDto(bookingRepository.save(booking));
    }

    @Override
    public void deleteBooking(UUID id) {
        bookingRepository.deleteById(id);
    }

    @Override
    public List<BookingResponseDto> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream().map(mapper::toDto).toList();
    }

    @Override
    public BookingResponseDto getBookingById(UUID id) {
        return mapper.toDto(bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(BOOKING_NOT_FOUND_MSG + id)));
    }

    @Override
    public BookingResponseDto getBookingByBookingCode(String bookingCode) {
        return mapper.toDto(bookingRepository.findByBookingCode(bookingCode));

    }

    @Override
    public List<BookingResponseDto>  getBookingsByGuestName(String guestName) {
        List<Booking> bookings = bookingRepository.findByMainGuest_NameContainingIgnoreCase(guestName);
        return bookings.stream().map(mapper::toDto).toList();
    }

    @Override
    public List<BookingResponseDto> getBookingsByGuestEmail(String guestEmail) {
        List<Booking> bookings = bookingRepository.findByMainGuest_EmailContainingIgnoreCase(guestEmail);
        return bookings.stream().map(mapper::toDto).toList();
    }

    @Override
    public List<BookingResponseDto> getBookingsByGuestPhoneNumber(String guestPhoneNumber) {
        List<Booking> bookings = bookingRepository.findByMainGuest_PhoneNumberContainingIgnoreCase(guestPhoneNumber);
        return bookings.stream().map(mapper::toDto).toList();
    }

    @Override
    public List<BookingResponseDto> getBookingsByBookingStatus(BookingStatus bookingStatus) {
        List<Booking> bookings = bookingRepository.findByBookingStatus(bookingStatus);
        return bookings.stream().map(mapper::toDto).toList();
    }

    @Override
    public List<BookingResponseDto> getBookingsByPaymentStatus(PaymentStatus paymentStatus) {
        List<Booking> bookings = bookingRepository.findByPaymentStatus(paymentStatus);
        return bookings.stream().map(mapper::toDto).toList();
    }

    @Override
    public List<BookingResponseDto> getBookingsByApartmentId(UUID apartmentId) {
        List<Booking> bookings = bookingRepository.findByApartmentId(apartmentId);
        return bookings.stream().map(mapper::toDto).toList();
    }

    @Override
    public List<BookingResponseDto> getBookingsByApartmentTitle(String apartmentTitle) {
        List<Booking> bookings = bookingRepository.findByApartmentTitleContainingIgnoreCase(apartmentTitle);
        return bookings.stream().map(mapper::toDto).toList();
    }

    @Override
    public List<BookingResponseDto> getBookingsByApartmentOwnerId(UUID ownerId) {
        List<Booking> bookings = bookingRepository.findByApartmentOwnerId(ownerId);
        return bookings.stream().map(mapper::toDto).toList();
    }

    @Override
    public List<BookingResponseDto> getBookingsByApartmentOwnerName(String ownerName) {
        List<Booking> bookings = bookingRepository.findByApartmentOwnerNameContainingIgnoreCase(ownerName);
        return bookings.stream().map(mapper::toDto).toList();
    }

    @Override
    public List<BookingResponseDto> getBookingsByApartmentOwnerEmail(String ownerEmail) {
        List<Booking> bookings = bookingRepository.findByApartmentOwnerEmailContainingIgnoreCase(ownerEmail);
        return bookings.stream().map(mapper::toDto).toList();
    }

    @Override
    public List<BookingResponseDto> getBookingsByApartmentOwnerPhoneNumber(String ownerPhoneNumber) {
        List<Booking> bookings = bookingRepository.findByApartmentOwnerPhoneNumberContainingIgnoreCase(ownerPhoneNumber);
        return bookings.stream().map(mapper::toDto).toList();
    }
}
