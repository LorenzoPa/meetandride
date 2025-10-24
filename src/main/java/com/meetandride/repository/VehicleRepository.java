package com.meetandride.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.meetandride.model.User;
import com.meetandride.model.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    // 🔹 Recupera veicolo associato a un utente specifico
    Optional<Vehicle> findByOwner(User owner);

    // 🔹 Recupera veicolo tramite targa (case-insensitive)
    Optional<Vehicle> findByTargaIgnoreCase(String targa);

    // 🔹 Verifica esistenza targa (utile per registrazione)
    boolean existsByTargaIgnoreCase(String targa);
}
