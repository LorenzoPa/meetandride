package com.meetandride.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    //Recupera tutti gli eventi
    @Transactional(readOnly = true)
    public List<Event> findAll() {
        List<Event> events = eventRepository.findAll();

        //Forza il caricamento delle relazioni per evitare LazyInitializationException che dava problemi
        events.forEach(e -> {
            if (e.getUser() != null) {
                e.getUser().getUsername(); // forza il caricamento del proxy User
            }
            if (e.getPartecipanti() != null) {
                e.getPartecipanti().size();
            }
            if (e.getRichieste() != null) {
                e.getRichieste().size();
            }
        });

        return events;
    }

    //Salva un evento e assegna l’utente loggato come host
    public void save(Event event) {
        User loggedUser = userService.getAuthenticatedUser();
        if (loggedUser == null) {
            throw new IllegalStateException("Nessun utente autenticato trovato.");
        }
        event.setUser(loggedUser);
        eventRepository.save(event);
    }

    //Elimina un evento
    public void delete(Event event) {
        eventRepository.delete(event);
    }

    //Cerca evento per ID
    @Transactional(readOnly = true)
    public Event findById(Long id) {
        Event event = eventRepository.findById(id).orElse(null);
        if (event != null) {
            if (event.getUser() != null) event.getUser().getUsername();
            if (event.getPartecipanti() != null) event.getPartecipanti().size();
            if (event.getRichieste() != null) event.getRichieste().size();
        }
        return event;
    }

    //Trova eventi creati da un utente
    @Transactional(readOnly = true)
    public List<Event> findByUser(User user) {
        return eventRepository.findByUser(user);
    }

    //Aggiungi partecipante o richiesta in base alla visibilità
    @Transactional
    public void addParticipant(Long eventId, User user) {
        Event event = getEventOrThrow(eventId);

        switch (event.getVisibilita()) {
            case CHIUSO -> {
                if (!event.hasRequested(user) && !event.isParticipating(user)) {
                    event.addRequest(user);
                }
            }
            default -> { // APERTO o PRIVATO
                if (!event.isParticipating(user)) {
                    event.getPartecipanti().add(user);
                }
            }
        }
        eventRepository.save(event);
    }

    //Rimuovi partecipante
    @Transactional
    public void removeParticipant(Long eventId, User user) {
        Event event = getEventOrThrow(eventId);
        event.getPartecipanti().removeIf(u -> u.getId().equals(user.getId()));
        eventRepository.save(event);
    }

    //Aggiungi richiesta manualmente (per eventi CHIUSI)
    @Transactional
    public void addRequest(Long eventId, User user) {
        Event event = getEventOrThrow(eventId);
        if (!event.hasRequested(user) && !event.isParticipating(user)) {
            event.addRequest(user);
            eventRepository.save(event);
        }
    }

    //Approva richiesta (l’host accetta)
    @Transactional
    public void approveRequest(Long eventId, User user) {
        Event event = getEventOrThrow(eventId);
        if (event.hasRequested(user)) {
            event.approveRequest(user);
            eventRepository.save(event);
        }
    }

    //Rifiuta richiesta (l’host rimuove)
    @Transactional
    public void rejectRequest(Long eventId, User user) {
        Event event = getEventOrThrow(eventId);
        if (event.hasRequested(user)) {
            event.removeRequest(user);
            eventRepository.save(event);
        }
    }

    //Helper privati
    private Event getEventOrThrow(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Evento non trovato con ID: " + eventId));
    }
}
