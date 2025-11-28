package com.rental.manager.entities;

import com.rental.manager.entities.apartment.Apartment;
import com.rental.manager.entities.enums.BookingStatus;
import com.rental.manager.entities.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Entity
@Table(name = "bookings")
@Getter
@Setter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(name = "booking_code")
    private String bookingCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;

    @Column(nullable = false, name = "check_in_date")
    private LocalDate checkInDate;

    @Column(nullable = false, name = "check_out_date")
    private LocalDate checkOutDate;

    @Transient
    private Integer totalNights;

    @Transient
    private BigDecimal totalPrice;

    @Column(name = "guest_name")
    private String mainGuestName;

    @Column(name = "guest_phone_number")
    private String guestPhoneNumber;

    @Column(name = "guest_email")
    private String guestEmail;

    @Column(name = "number_of_adults")
    private int numberOfAdults;

    @Column (name = "number_of_children")
    private int numberOfChildren;

    @Transient
    private int totalGuests;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status")
    private BookingStatus bookingStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(length = 1000)
    private String notes;

    public Booking() {
        this.bookingStatus = BookingStatus.PENDING;
        this.paymentStatus = PaymentStatus.UNPAID;
        this.createdAt = LocalDateTime.now();
        generateBookingCode();
    }

    public void generateBookingCode() {
        if (this.bookingCode == null) {
            String timestamp = String.valueOf(System.currentTimeMillis());
            String random = String.valueOf((int) (Math.random() * 1000));
            this.bookingCode = "RNT-" + timestamp.substring(timestamp.length() - 6) + "-" + random;
        }
    }

    @PostUpdate
    @PostPersist
    public void calculatePost() {

        totalGuests = numberOfAdults + numberOfChildren;
        if (this.checkInDate != null && this.checkOutDate != null) {
            this.totalNights = (int) ChronoUnit.DAYS.between(this.checkInDate, this.checkOutDate);
        } else {
            this.totalNights = 0;
        }

        if (this.apartment != null && this.apartment.getPricePerNight() != null && this.totalNights > 0) {
            this.totalPrice = this.apartment.getPricePerNight().multiply(BigDecimal.valueOf(this.totalNights));
        } else {
            this.totalPrice = BigDecimal.ZERO;
        }
    }


    public boolean isValid() {
        return (checkInDate != null && checkOutDate != null && checkOutDate.isAfter(checkInDate));
    }
}
