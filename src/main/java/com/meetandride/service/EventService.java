package com.meetandride.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meetandride.model.Event;
import com.meetandride.model.User;
import com.meetandride.repository.EventRepository;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UserService userService;

    @Autowired
    public EventService(EventRepository eventRepository, UserService userService) {
        this.eventRepository = eventRepository;
        this.userService = userService;
    }

    // 🔹 Recupera tutti gli eventi
    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    // 🔹 Salva un evento e assegna l’utente loggato come host
    public void save(Event event) {
        User loggedUser = userService.getAuthenticatedUser();
        if (loggedUser != null) {
            event.setUser(loggedUser);
        }
        eventRepository.save(event);
    }

    // 🔹 Elimina un evento
    public void delete(Event event) {
        eventRepository.delete(event);
    }

    // 🔹 Cerca evento per ID
    public Event findById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    public List<Event> findByUser(User user) {
        return eventRepository.findAll()
                .stream()
                .filter(e -> e.getUser().equals(user))
                .toList();
    }

    public void addParticipant(Long eventId, User user) {
        Event event = findById(eventId);
        if (event != null && user != null && !event.getPartecipanti().contains(user)) {
            event.getPartecipanti().add(user);
            eventRepository.save(event);
        }
    }

}
