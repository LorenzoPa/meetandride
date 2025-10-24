package com.meetandride.view;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.meetandride.layout.MainLayout;
import com.meetandride.model.Event;
import com.meetandride.model.User;
import com.meetandride.service.EventService;
import com.meetandride.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "eventi/miei", layout = MainLayout.class)
@PageTitle("I miei eventi | Meet&Ride")
public class MyEventsView extends VerticalLayout implements BeforeEnterObserver {

    private final EventService eventService;
    private final UserService userService;

    @Autowired
    public MyEventsView(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;

        setAlignItems(Alignment.CENTER);
        setSpacing(true);
        setPadding(true);
        setWidth("900px");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        User current = userService.getAuthenticatedUser();

        if (current == null) {
            Notification.show("⚠️ Devi accedere per vedere i tuoi eventi.", 3000, Position.TOP_CENTER);
            event.forwardTo("login");
            return;
        }

        removeAll(); // pulizia

        H1 titolo = new H1("📅 I miei eventi");
        titolo.getStyle().set("margin-bottom", "1em");
        add(titolo);

        // === EVENTI CREATI ===
        List<Event> creati = eventService.findByUser(current);
        H2 creatiTitle = new H2("🧑‍🔧 Eventi creati da me");
        Grid<Event> gridCreati = new Grid<>(Event.class, false);

        if (creati.isEmpty()) {
            add(creatiTitle, new Paragraph("Non hai ancora creato nessun evento."));
        } else {
            gridCreati.addColumn(Event::getTitolo).setHeader("Titolo").setSortable(true);
            gridCreati.addColumn(Event::getLocalita).setHeader("Località");
            gridCreati.addColumn(Event::getData).setHeader("Data");
            gridCreati.addColumn(e -> e.getVisibilita().name()).setHeader("Visibilità");
            gridCreati.addComponentColumn(e -> {
                Button dettagli = new Button("🔍 Dettagli", ev -> UI.getCurrent().navigate("eventi/dettaglio/" + e.getId()));
                Button modifica = new Button("✏️ Modifica", ev -> UI.getCurrent().navigate("eventi/modifica/" + e.getId()));
                return new HorizontalLayout(dettagli, modifica);
            }).setHeader("Azioni");

            gridCreati.setItems(creati);
            gridCreati.setWidthFull();
            add(creatiTitle, gridCreati);
        }

        // === EVENTI PARTECIPATI ===
        H2 partecipatiTitle = new H2("🚗 Eventi a cui partecipo");
        List<Event> partecipati = eventService.findAll().stream()
                .filter(e -> e.getPartecipanti() != null &&
                             e.getPartecipanti().stream().anyMatch(u -> u.getId().equals(current.getId())))
                .toList();

        Grid<Event> gridPartecipati = new Grid<>(Event.class, false);
        if (partecipati.isEmpty()) {
            add(partecipatiTitle, new Paragraph("Non stai partecipando a nessun evento al momento."));
        } else {
            gridPartecipati.addColumn(Event::getTitolo).setHeader("Titolo").setSortable(true);
            gridPartecipati.addColumn(e -> e.getUser() != null ? e.getUser().getUsername() : "—").setHeader("Host");
            gridPartecipati.addColumn(Event::getLocalita).setHeader("Località");
            gridPartecipati.addColumn(Event::getData).setHeader("Data");
            gridPartecipati.addComponentColumn(e -> {
                Button dettagli = new Button("🔍 Vai ai dettagli", ev -> UI.getCurrent().navigate("eventi/dettaglio/" + e.getId()));
                return dettagli;
            }).setHeader("Azione");

            gridPartecipati.setItems(partecipati);
            gridPartecipati.setWidthFull();
            add(partecipatiTitle, gridPartecipati);
        }

        Button indietro = new Button("⬅️ Torna agli eventi", e -> getUI().ifPresent(ui -> ui.navigate("eventi")));
        add(indietro);
    }
}
