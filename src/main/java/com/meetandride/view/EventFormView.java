package com.meetandride.view;

import org.springframework.beans.factory.annotation.Autowired;

import com.meetandride.model.Event;
import com.meetandride.service.EventService;
import com.meetandride.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("eventi/nuovo")
public class EventFormView extends VerticalLayout {

    private final EventService eventService;
    private final UserService userService;
    @Autowired
    public EventFormView(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;

        H1 title = new H1("Aggiungi evento");

        TextField titolo = new TextField("Titolo");
        TextArea descrizione = new TextArea("Descrizione");
        TextField localita = new TextField("Localita");
        TextField orario = new TextField("Orario");
        DatePicker data = new DatePicker("Data");

        Select<String> visibilita = new Select<>();
        visibilita.setLabel("Visibilita");
        visibilita.setItems("Aperto", "Chiuso", "Privato");
        visibilita.setValue("Aperto");

        Button salvaButton = new Button("💾 Salva");
        Button annullaButton = new Button("↩️ Annulla");

        // Azione bottone Salva
        salvaButton.addClickListener(e -> {
            // salvo dati nel db
            Event evento = new Event();
            evento.setTitolo(titolo.getValue());
            evento.setDescrizione(descrizione.getValue());
            evento.setVisibilita(visibilita.getValue());
            evento.setLocalita(localita.getValue());
            evento.setData(data.getValue());
            evento.setOrario(orario.getValue());
            //evento.setHost("Loren"); //
            evento.setUser(userService.getAuthenticatedUser());

            eventService.save(evento);
            Notification.show("Evento \"" + titolo.getValue() + "\" creato con successo!");
            getUI().ifPresent(ui -> ui.navigate("eventi"));
        });

        // Azione bottone Annulla
        annullaButton.addClickListener(e
                -> getUI().ifPresent(ui -> ui.navigate("eventi"))
        );

        // Layout orizzontale per i pulsanti
        HorizontalLayout pulsanti = new HorizontalLayout(salvaButton, annullaButton);

        // Aggiunta di tutti i componenti alla view
        add(title, titolo, descrizione, localita, data, orario, visibilita, pulsanti);

        // Stile
        setAlignItems(Alignment.CENTER);
        setWidth("600px");
    }
}
