package com.rental.manager.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "guests")
@Getter
@Setter
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column
    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column
    private String email;

    @Column(length = 1000)
    private String notes;
}
