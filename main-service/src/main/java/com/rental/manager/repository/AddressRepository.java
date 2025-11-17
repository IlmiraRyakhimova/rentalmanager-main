package com.rental.manager.repository;

import com.rental.manager.entities.apartment.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
    List<Address> findByPostalCode(String postalCode);
    List<Address> findByCountryContainingIgnoreCase(String country);
    List<Address> findByCityContainingIgnoreCase(String city);
    List<Address> findByDistrictContainingIgnoreCase(String district);
    List<Address> findByStreetContainingIgnoreCase(String street);

}
