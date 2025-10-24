package com.meetandride.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.meetandride.model.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // Esempi di query personalizzate (facoltative):
    // List<Event> findByLocalita(String localita);
    // List<Event> findByTitoloContainingIgnoreCase(String titolo);
}
