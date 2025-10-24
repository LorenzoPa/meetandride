package com.meetandride.view;

import org.springframework.beans.factory.annotation.Autowired;

import com.meetandride.layout.MainLayout;
import com.meetandride.model.Event;
import com.meetandride.model.Event.Visibilita;
import com.meetandride.model.User;
import com.meetandride.service.EventService;
import com.meetandride.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "eventi/modifica", layout = MainLayout.class)
@PageTitle("Modifica Evento | Meet&Ride")
public class EventEditView extends VerticalLayout implements HasUrlParameter<Long> {

    private final EventService eventService;
    private final UserService userService;

    private Event evento;

    private TextField titolo;
    private TextArea descrizione;
    private TextField localita;
    private DatePicker data;
    private TextField orario;
    private Select<Visibilita> visibilita;

    @Autowired
    public EventEditView(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;

        setAlignItems(Alignment.CENTER);
        setWidth("600px");
        setSpacing(true);
        setPadding(true);
    }

    @Override
    public void setParameter(BeforeEvent event, Long eventId) {
        this.evento = eventService.findById(eventId);
        removeAll();

        if (evento == null) {
            add(new H1("Evento non trovato"));
            return;
        }

        User current = userService.getAuthenticatedUser();
        if (current == null || !isHost(current)) {
            add(new H1("Accesso negato"));
            Notification.show("Non hai i permessi per modificare questo evento.", 3000, Position.TOP_CENTER);
            return;
        }

        H1 title = new H1("✏️ Modifica evento");

        // Campi form
        titolo = new TextField("Titolo");
        titolo.setValue(evento.getTitolo() != null ? evento.getTitolo() : "");
        titolo.setRequiredIndicatorVisible(true);

        descrizione = new TextArea("Descrizione");
        descrizione.setPlaceholder("Aggiungi dettagli utili per i partecipanti...");
        descrizione.setValue(evento.getDescrizione() != null ? evento.getDescrizione() : "");
        descrizione.setWidthFull();

        localita = new TextField("Località");
        localita.setValue(evento.getLocalita() != null ? evento.getLocalita() : "");
        localita.setRequiredIndicatorVisible(true);

        data = new DatePicker("Data");
        data.setValue(evento.getData());
        data.setRequiredIndicatorVisible(true);

        orario = new TextField("Orario");
        orario.setPlaceholder("Es. 10:30");
        orario.setValue(evento.getOrario() != null ? evento.getOrario() : "");

        visibilita = new Select<>();
        visibilita.setLabel("Visibilità");
        visibilita.setItems(Visibilita.values());
        visibilita.setValue(evento.getVisibilita());

        // Pulsanti
        Button salvaButton = new Button("💾 Salva modifiche", e -> salvaModifiche());
        salvaButton.getStyle().set("background-color", "#4CAF50").set("color", "white");

        Button annullaButton = new Button("↩️ Annulla", e -> 
            getUI().ifPresent(ui -> ui.navigate("eventi/dettaglio/" + evento.getId()))
        );

        HorizontalLayout pulsanti = new HorizontalLayout(salvaButton, annullaButton);
        pulsanti.setSpacing(true);

        add(title, titolo, descrizione, localita, data, orario, visibilita, pulsanti);
    }

    private void salvaModifiche() {
        if (titolo.isEmpty() || localita.isEmpty() || data.isEmpty()) {
            Notification.show("Compila tutti i campi obbligatori!", 2500, Position.TOP_CENTER);
            return;
        }

        evento.setTitolo(titolo.getValue().trim());
        evento.setDescrizione(descrizione.getValue().trim());
        evento.setLocalita(localita.getValue().trim());
        evento.setData(data.getValue());
        evento.setOrario(orario.getValue().trim());
        evento.setVisibilita(visibilita.getValue());

        eventService.save(evento);

        Notification.show("✅ Modifiche salvate con successo!", 3000, Position.TOP_CENTER);
        getUI().ifPresent(ui -> ui.navigate("eventi/dettaglio/" + evento.getId()));
    }

    private boolean isHost(User user) {
        return evento.getUser() != null && evento.getUser().getId().equals(user.getId());
    }
}
