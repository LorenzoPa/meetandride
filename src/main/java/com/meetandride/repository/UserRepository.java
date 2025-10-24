package com.meetandride.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.meetandride.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 🔹 Ricerca utente per username
    Optional<User> findByUsername(String username);

    // 🔹 Variante case-insensitive (opzionale, utile per login o inviti)
    Optional<User> findByUsernameIgnoreCase(String username);

    // 🔹 Controlli esistenza (registrazione)
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
