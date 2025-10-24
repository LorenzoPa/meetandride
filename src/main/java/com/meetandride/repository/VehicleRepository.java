package com.meetandride.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.meetandride.model.User;
import com.meetandride.model.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    //ricerca veicolo associato a un utente
    Optional<Vehicle> findByOwner(User owner);

    // ricerca veicolo tramite targa
    Optional<Vehicle> findByTargaIgnoreCase(String targa);

    //Verifica esistenza targa
    boolean existsByTargaIgnoreCase(String targa);
}
