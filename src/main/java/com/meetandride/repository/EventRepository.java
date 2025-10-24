package com.meetandride.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.meetandride.model.Event;
import com.meetandride.model.User;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // 🔹 Trova eventi creati da un certo utente (host)
    List<Event> findByUserUsername(String username);

    // 🔹 Ricerca eventi per titolo (case-insensitive, utile per barra di ricerca)
    List<Event> findByTitoloContainingIgnoreCase(String titolo);

    // 🔹 Ricerca per località
    List<Event> findByLocalitaContainingIgnoreCase(String localita);

    // 🔹 Ricerca per data
    List<Event> findByData(LocalDate data);

    // 🔹 Ricerca combinata: utile per filtri multipli
    List<Event> findByTitoloContainingIgnoreCaseAndLocalitaContainingIgnoreCase(String titolo, String localita);

    // 🔹 Tutti gli eventi creati da un certo user (entità diretta)
    List<Event> findByUser(User user);
}
