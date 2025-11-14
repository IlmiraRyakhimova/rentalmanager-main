package com.rental.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "apartments")
@Getter
@Setter
@NoArgsConstructor
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "apartment_name")
    private String apartmentName;

    private String address;

    @Column(precision = 10, scale = 2, name = "price_per_night")
    private BigDecimal pricePerNight;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_active_for_booking")
    private boolean isActiveForBooking = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", nullable = false)
    private User agent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    public Apartment(String apartmentName, String address, BigDecimal pricePerNight, User agent, User owner) {
        this.apartmentName = apartmentName;
        this.address = address;
        this.pricePerNight = pricePerNight;
        this.agent = agent;
        this.owner = owner;
    }
}

