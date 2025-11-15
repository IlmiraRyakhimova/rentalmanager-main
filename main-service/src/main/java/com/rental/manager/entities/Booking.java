package com.rental.manager.entities;

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

    @Column(nullable = false, name = "check_in")
    private LocalDate checkInDate;

    @Column(nullable = false, name = "check_out")
    private LocalDate checkOutDate;

    @Column(nullable = false, name = "total_nights")
    private Integer totalNights;

    @Column(nullable = false, precision = 15, scale = 2, name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "guest_name")
    private String guestName;

    @Column(name = "guest_phone_number")
    private String guestPhoneNumber;

    @Column(name = "guest_email")
    private String guestEmail;

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

    public Booking(Apartment apartment, LocalDate checkIn, LocalDate checkOut,
                    String guestName, String phone, String email) {
        this();
        this.apartment = apartment;
        this.checkInDate = checkIn;
        this.checkOutDate = checkOut;
        this.guestName = guestName;
        this.guestPhoneNumber = phone;
        this.guestEmail = email;
    }


    @PrePersist
    public void onPrePersist() {
        generateBookingCode();
        calculateDerivedFields();
    }

    @PreUpdate
    public void onPreUpdate() {
        calculateDerivedFields();
    }

    public void generateBookingCode() {
        if (this.bookingCode == null) {
            String timestamp = String.valueOf(System.currentTimeMillis());
            String random = String.valueOf((int) (Math.random() * 1000));
            this.bookingCode = "RNT-" + timestamp.substring(timestamp.length() - 6) + "-" + random;
        }
    }


    public void calculateDerivedFields() {
        calculateAndSetTotalNights();
        calculateAndSetTotalPrice();
    }

    public void calculateAndSetTotalNights() {
        if (checkInDate != null && checkOutDate != null) {
            this.totalNights = (int) ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        }
    }

    public void calculateAndSetTotalPrice() {
        if (this.apartment != null &&
                this.apartment.getPricePerNight() != null &&
                this.totalNights != null &&
                this.totalNights > 0) {

            this.totalPrice = this.apartment.getPricePerNight()
                    .multiply(BigDecimal.valueOf(this.totalNights));
        }
    }

    public boolean isValid() {
        return (checkInDate != null && checkOutDate != null && checkOutDate.isAfter(checkInDate));
    }
}
