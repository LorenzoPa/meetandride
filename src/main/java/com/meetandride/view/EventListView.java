package com.meetandride.view;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.meetandride.layout.MainLayout;
import com.meetandride.model.Event;
import com.meetandride.model.User;
import com.meetandride.service.EventService;
import com.meetandride.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "eventi", layout = MainLayout.class)
@PageTitle("Eventi | Meet&Ride")
public class EventListView extends VerticalLayout {

    private final EventService eventService;
    private final UserService userService;
    @Autowired
    public EventListView(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;

        // Titolo pagina
        H1 title = new H1("🏎️ Raduni Automobilistici");

        // Griglia eventi
        Grid<Event> grid = new Grid<>(Event.class, false);
        grid.addColumn(Event::getTitolo).setHeader("Titolo").setSortable(true);
        grid.addColumn(Event::getLocalita).setHeader("Località");
        grid.addColumn(Event::getData).setHeader("Data");
        grid.addColumn(Event::getOrario).setHeader("Orario");
        grid.addColumn(event ->
                event.getUser() != null ? event.getUser().getUsername() : "—"
        ).setHeader("Host");
        grid.addComponentColumn(event -> {
    Button join = new Button("Partecipa", e -> {
        User current = userService.getAuthenticatedUser();
        if (current != null) {
            eventService.addParticipant(event.getId(), current);
            Notification.show("Ti sei unito all’evento!");
        } else {
            Notification.show("Effettua il login prima di partecipare!");
        }
    });
    return join;
}).setHeader("Azione");

        // Recupera gli eventi dal database
        List<Event> eventi = eventService.findAll();
        if (eventi.isEmpty()) {
            Notification.show("Nessun evento disponibile al momento.");
        }
        grid.setItems(eventi);
        grid.setSizeFull();

        // Pulsanti azione
        Button creaEvento = new Button("➕ Crea nuovo evento");
        creaEvento.addClickListener(e ->
                getUI().ifPresent(ui -> ui.navigate("eventi/nuovo"))
        );

        Button aggiorna = new Button("🔄 Aggiorna");
        aggiorna.addClickListener(e -> grid.setItems(eventService.findAll()));

        HorizontalLayout pulsanti = new HorizontalLayout(creaEvento, aggiorna);
        pulsanti.setSpacing(true);

        // Layout principale
        add(title, pulsanti, grid);
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setPadding(true);
        setSpacing(true);
    }
}
