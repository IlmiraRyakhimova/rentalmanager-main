package com.rental.manager.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "auth_service_user_id")
    private String authServiceUserId;

    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String email;

    @OneToMany(mappedBy = "agent")
    private List<Apartment> managedApartments;

    @OneToMany(mappedBy = "owner")
    private List<Apartment> ownedApartments;

    public User(String name, String phoneNumber, String email) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public User(String authServiceUserId, String name, String phoneNumber, String email) {
        this(name, phoneNumber, email);
        this.authServiceUserId = authServiceUserId;
    }


}
