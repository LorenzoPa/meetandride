package com.meetandride.view;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.meetandride.model.Event;
import com.meetandride.service.EventService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("eventi")
public class EventListView extends VerticalLayout {
    private final EventService eventService;

    @Autowired
    public EventListView(EventService eventService) {
        this.eventService = eventService;

        H1 title = new H1("Raduni Automobilistici");
        Grid<Event> grid = new Grid<>(Event.class, false);

        grid.addColumn(Event::getTitolo).setHeader("Titolo").setSortable(true);
        grid.addColumn(Event::getLocalita).setHeader("Località");
        grid.addColumn(Event::getData).setHeader("Data");
        grid.addColumn(Event::getOrario).setHeader("Orario");
        grid.addColumn(Event::getUser).setHeader("User");

        /* 
        List<Event> eventi = List.of(
                new Event("Cars & Coffee Pisa", "Pisa", LocalDate.now(), "10:00", "Loren"),
                new Event("Tuning Night", "Livorno", LocalDate.now().plusDays(2), "21:00", "Alex"),
                new Event("Raduno d’Epoca", "Lucca", LocalDate.now().plusWeeks(1), "09:30", "Marta")
        ); 
        */
        List<Event> eventi = eventService.findAll();
        grid.setItems(eventi);
        Button creaEvento = new Button("Crea nuovo evento!");
        creaEvento.addClickListener(e ->
                getUI().ifPresent(ui -> ui.navigate("eventi/nuovo"))
        );

        add(title, grid, creaEvento);
        setSizeFull();
        grid.setSizeFull();
        setAlignItems(Alignment.CENTER);
    }

}
