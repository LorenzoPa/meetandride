package com.meetandride.view;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.meetandride.layout.MainLayout;
import com.meetandride.model.Event;
import com.meetandride.model.Event.Visibilita;
import com.meetandride.model.User;
import com.meetandride.service.EventService;
import com.meetandride.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "eventi", layout = MainLayout.class)
@PageTitle("Eventi | Meet&Ride")
public class EventListView extends VerticalLayout {

    private final EventService eventService;
    private final UserService userService;
    private List<Event> eventi;

    @Autowired
    public EventListView(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;

        H1 title = new H1("🏎️ Raduni Automobilistici");
        title.getStyle().set("margin-bottom", "0");

        // 🔍 Barra di ricerca
        TextField searchField = new TextField();
        searchField.setPlaceholder("Cerca per titolo, località, data o host...");
        searchField.setWidth("350px");

        Button searchButton = new Button("🔍 Cerca");
        Button resetButton = new Button("❌ Reset");

        // Griglia eventi
        Grid<Event> grid = new Grid<>(Event.class, false);
        grid.addColumn(Event::getTitolo).setHeader("Titolo").setSortable(true).setAutoWidth(true);
        grid.addColumn(Event::getLocalita).setHeader("Località").setAutoWidth(true);
        grid.addColumn(Event::getData).setHeader("Data").setAutoWidth(true);
        grid.addColumn(Event::getOrario).setHeader("Orario").setAutoWidth(true);
        grid.addColumn(event -> event.getUser() != null ? event.getUser().getUsername() : "—").setHeader("Host").setAutoWidth(true);

        // 🔹 Colonna Visibilità
        grid.addComponentColumn(event -> {
            Span badge = new Span(event.getVisibilita().name());
            badge.getStyle().set("padding", "4px 8px")
                    .set("border-radius", "6px")
                    .set("font-weight", "bold")
                    .set("color", "white");

            switch (event.getVisibilita()) {
                case APERTO -> badge.getStyle().set("background-color", "#28a745");
                case CHIUSO -> badge.getStyle().set("background-color", "#ffc107");
                case PRIVATO -> badge.getStyle().set("background-color", "#6c757d");
            }
            return badge;
        }).setHeader("Visibilità").setAutoWidth(true);

        // 🔹 Colonna Azione
        grid.addComponentColumn(event -> {
            User current = userService.getAuthenticatedUser();
            Button button = new Button();

            button.getStyle()
                    .set("border", "none")
                    .set("border-radius", "8px")
                    .set("padding", "4px 10px")
                    .set("cursor", "pointer");

            if (current == null) {
                button.setText("Accedi per partecipare");
                button.setEnabled(false);
            } else if (isHost(event, current)) {
                button.setText("🎯 Sei l'host");
                button.setEnabled(false);
            } else if (isParticipating(event, current)) {
                button.setText("❌ Lascia evento");
                button.getStyle().set("background-color", "#dc3545").set("color", "white");
                button.addClickListener(e -> {
                    eventService.removeParticipant(event.getId(), current);
                    Notification.show("Hai lasciato l’evento.", 2500, Position.TOP_CENTER);
                    refreshGrid(grid);
                });
            } else {
                button.setText("✅ Partecipa");
                button.getStyle().set("background-color", "#28a745").set("color", "white");
                button.addClickListener(e -> {
                    eventService.addParticipant(event.getId(), current);
                    Notification.show("Ti sei unito all’evento!", 2500, Position.TOP_CENTER);
                    refreshGrid(grid);
                });
            }
            return button;
        }).setHeader("Azione").setAutoWidth(true);

        // 🔹 Righe cliccabili
        grid.addItemClickListener(e -> {
            Event selected = e.getItem();
            if (selected != null && selected.getId() != null) {
                getUI().ifPresent(ui -> ui.navigate("eventi/dettaglio/" + selected.getId()));
            }
        });

        grid.addClassName("clickable-grid");
        grid.getStyle().set("cursor", "pointer");
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.setSizeFull();

        // Carica e mostra eventi visibili
        eventi = filtraEventiVisibili(eventService.findAll());
        grid.setItems(eventi);

        // 🔍 Ricerca
        searchButton.addClickListener(e -> {
            String query = searchField.getValue().trim().toLowerCase();
            List<Event> filtered = eventi.stream()
                    .filter(ev ->
                            (ev.getTitolo() != null && ev.getTitolo().toLowerCase().contains(query)) ||
                            (ev.getLocalita() != null && ev.getLocalita().toLowerCase().contains(query)) ||
                            (ev.getUser() != null && ev.getUser().getUsername().toLowerCase().contains(query)) ||
                            (ev.getData() != null && ev.getData().toString().contains(query))
                    )
                    .collect(Collectors.toList());
            grid.setItems(filtered);
            if (filtered.isEmpty()) {
                Notification.show("Nessun evento trovato per \"" + query + "\"", 2000, Position.TOP_CENTER);
            }
        });

        resetButton.addClickListener(e -> {
            searchField.clear();
            grid.setItems(eventi);
        });

        // 🔹 Pulsanti azione
        Button creaEvento = new Button("➕ Crea nuovo evento", e -> getUI().ifPresent(ui -> ui.navigate("eventi/nuovo")));
        creaEvento.getStyle().set("background-color", "#007bff").set("color", "white");

        Button aggiorna = new Button("🔄 Aggiorna", e -> refreshGrid(grid));
        aggiorna.getStyle().set("background-color", "#6c757d").set("color", "white");

        HorizontalLayout ricerca = new HorizontalLayout(searchField, searchButton, resetButton);
        HorizontalLayout pulsanti = new HorizontalLayout(creaEvento, aggiorna);
        ricerca.setSpacing(true);
        pulsanti.setSpacing(true);

        add(title, ricerca, pulsanti, grid);
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setPadding(true);
        setSpacing(true);
    }

    // 👤 Utility methods
    private boolean isHost(Event event, User user) {
        return event.getUser() != null && event.getUser().getId().equals(user.getId());
    }

    private boolean isParticipating(Event event, User user) {
        return event.getPartecipanti() != null &&
                event.getPartecipanti().stream().anyMatch(u -> u.getId().equals(user.getId()));
    }

    private void refreshGrid(Grid<Event> grid) {
        eventi = filtraEventiVisibili(eventService.findAll());
        grid.setItems(eventi);
    }

    // 🔒 Mostra solo eventi visibili all'utente
    private List<Event> filtraEventiVisibili(List<Event> eventi) {
        User current = userService.getAuthenticatedUser();
        return eventi.stream()
                .filter(ev ->
                        ev.getVisibilita() == Visibilita.APERTO ||
                        ev.getVisibilita() == Visibilita.CHIUSO ||
                        (ev.getVisibilita() == Visibilita.PRIVATO && current != null && isHost(ev, current))
                )
                .collect(Collectors.toList());
    }
}
