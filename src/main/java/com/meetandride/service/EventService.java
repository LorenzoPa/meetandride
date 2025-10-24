package com.meetandride.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meetandride.model.Event;
import com.meetandride.repository.EventRepository;

@Service
public class EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    //Recupera tutti gli eventi
    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    //Salva o aggiorna un evento
    public void save(Event event) {
        eventRepository.save(event);
    }

    //Elimina un evento
    public void delete(Event event) {
        eventRepository.delete(event);
    }

    //Cerca evento per ID
    public Event findById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }
}
