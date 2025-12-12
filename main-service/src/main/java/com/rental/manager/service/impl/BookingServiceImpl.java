package com.rental.manager.service.impl;

import com.rental.manager.dto.requestdto.BookingPatchRequestDTO;
import com.rental.manager.dto.requestdto.BookingPaymentStatusPatchRequestDTO;
import com.rental.manager.dto.requestdto.BookingRequestDTO;
import com.rental.manager.dto.requestdto.BookingStatusPatchRequestDTO;
import com.rental.manager.dto.responsedto.BookingResponseDTO;
import com.rental.manager.entities.Booking;
import com.rental.manager.entities.Apartment;
import com.rental.manager.entities.Guest;
import com.rental.manager.entities.enums.BookingStatus;
import com.rental.manager.entities.enums.PaymentStatus;
import com.rental.manager.mappers.BookingMapper;
import com.rental.manager.mappers.GuestMapper;
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

    private static final String BOOKING_NOT_FOUND_MSG = "Booking not found with id: ";
    private static final String APARTMENT_NOT_FOUND_MSG = "Apartment not found with id: ";

    private final BookingRepository bookingRepository;
    private final ApartmentRepository apartmentRepository;
    private final GuestRepository guestRepository;
    private final BookingMapper mapper;
    private final GuestMapper guestMapper;

    @Override
    @Transactional
    public BookingResponseDTO createBooking(BookingRequestDTO request) {
        Apartment apartment = apartmentRepository.findById(request.getApartmentId())
                .orElseThrow(() -> new EntityNotFoundException(APARTMENT_NOT_FOUND_MSG + request.getApartmentId()));

        Booking booking = mapper.toEntity(request);
        booking.setApartment(apartment);
        resolveGuest(booking);
        return mapper.toDTO(bookingRepository.save(booking));
    }

    public BookingResponseDTO updateBooking(UUID id, BookingRequestDTO request) {
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
        return mapper.toDTO(bookingRepository.save(booking));
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
    public BookingResponseDTO patchBooking(UUID id, BookingPatchRequestDTO request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(BOOKING_NOT_FOUND_MSG + id));

        if (request.getApartmentId() != null) {
            Apartment apartment = apartmentRepository.findById(request.getApartmentId())
                    .orElseThrow(() -> new EntityNotFoundException(APARTMENT_NOT_FOUND_MSG + request.getApartmentId()));
            booking.setApartment(apartment);
        }

        if (request.getMainGuest() != null) {
            Guest currentGuest = booking.getMainGuest();
            Guest guest = guestMapper.toEntity(request.getMainGuest());
            if (guest.getName() != null) {
                currentGuest.setName(guest.getName());
            }
            if (guest.getEmail() != null) {
                currentGuest.setEmail(guest.getEmail());
            }
            if (guest.getPhoneNumber() != null) {
                currentGuest.setPhoneNumber(guest.getPhoneNumber());
            }
            booking.setMainGuest(currentGuest);
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
        if(request.getNotes() != null) {
            booking.setNotes(request.getNotes());
        }
        return mapper.toDTO(bookingRepository.save(booking));
    }

    @Override
    public BookingResponseDTO patchBookingStatus(UUID id, BookingStatusPatchRequestDTO request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(BOOKING_NOT_FOUND_MSG + id));
        booking.setBookingStatus(request.getBookingStatus());
        return mapper.toDTO(bookingRepository.save(booking));
    }

    @Override
    public BookingResponseDTO patchPaymentStatus(UUID id, BookingPaymentStatusPatchRequestDTO request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(BOOKING_NOT_FOUND_MSG + id));
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
                .orElseThrow(() -> new EntityNotFoundException(BOOKING_NOT_FOUND_MSG + id)));
    }

    @Override
    public BookingResponseDTO getBookingByBookingCode(String bookingCode) {
        return mapper.toDTO(bookingRepository.findByBookingCode(bookingCode));

    }

    @Override
    public List<BookingResponseDTO>  getBookingsByGuestName(String guestName) {
        List<Booking> bookings = bookingRepository.findByMainGuest_NameContainingIgnoreCase(guestName);
        return bookings.stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<BookingResponseDTO> getBookingsByGuestEmail(String guestEmail) {
        List<Booking> bookings = bookingRepository.findByMainGuest_EmailContainingIgnoreCase(guestEmail);
        return bookings.stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<BookingResponseDTO> getBookingsByGuestPhoneNumber(String guestPhoneNumber) {
        List<Booking> bookings = bookingRepository.findByMainGuest_PhoneNumberContainingIgnoreCase(guestPhoneNumber);
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
