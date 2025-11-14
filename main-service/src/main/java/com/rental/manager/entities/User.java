package com.rental.manager.entities;

import com.rental.manager.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "auth_service_user_id", unique = true)
    private String authServiceUserId;

    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "agent")
    private List<Apartment> managedApartments;

    @OneToMany(mappedBy = "owner")
    private List<Apartment> ownedApartments;

    public User(String name, String phoneNumber, String email) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public User(String name, String phoneNumber, String email, UserRole role) {
        this(name, phoneNumber, email);
        this.role = role;
    }

    public User(String authServiceUserId, String name, String phoneNumber, String email, UserRole role) {
        this(name, phoneNumber, email, role);
        this.authServiceUserId = authServiceUserId;
    }


}
