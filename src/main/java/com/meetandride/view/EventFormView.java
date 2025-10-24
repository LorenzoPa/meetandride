package com.meetandride.view;

import org.springframework.beans.factory.annotation.Autowired;

import com.meetandride.layout.MainLayout;
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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "eventi/nuovo", layout = MainLayout.class)
@PageTitle("Crea nuovo evento | Meet&Ride")
public class EventFormView extends VerticalLayout {

    private final EventService eventService;
    private final UserService userService;

    @Autowired
    public EventFormView(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;

        // Titolo pagina
        H1 title = new H1("🛠️ Crea un nuovo evento");

        // Campi del form
        TextField titolo = new TextField("Titolo");
        titolo.setRequired(true);

        TextArea descrizione = new TextArea("Descrizione");
        descrizione.setPlaceholder("Descrivi brevemente il tuo evento...");

        TextField localita = new TextField("Località");
        localita.setRequired(true);

        DatePicker data = new DatePicker("Data");
        data.setRequired(true);

        TextField orario = new TextField("Orario (es. 10:00)");
        orario.setPlaceholder("Inserisci un orario");

        Select<String> visibilita = new Select<>();
        visibilita.setLabel("Visibilità");
        visibilita.setItems("Aperto", "Chiuso", "Privato");
        visibilita.setValue("Aperto");

        // Pulsanti
        Button salvaButton = new Button("💾 Salva evento");
        Button annullaButton = new Button("↩️ Annulla");

        // 🔹 Azione Salva
        salvaButton.addClickListener(e -> {
            if (titolo.isEmpty() || localita.isEmpty() || data.isEmpty()) {
                Notification.show("Compila tutti i campi obbligatori!");
                return;
            }

            Event evento = new Event();
            evento.setTitolo(titolo.getValue());
            evento.setDescrizione(descrizione.getValue());
            evento.setVisibilita(visibilita.getValue());
            evento.setLocalita(localita.getValue());
            evento.setData(data.getValue());
            evento.setOrario(orario.getValue());
            evento.setUser(userService.getAuthenticatedUser());

            eventService.save(evento);
            Notification.show("✅ Evento \"" + titolo.getValue() + "\" creato con successo!");
            getUI().ifPresent(ui -> ui.navigate("eventi"));
        });

        // 🔹 Azione Annulla
        annullaButton.addClickListener(e ->
            getUI().ifPresent(ui -> ui.navigate("eventi"))
        );

        // Layout pulsanti
        HorizontalLayout pulsanti = new HorizontalLayout(salvaButton, annullaButton);
        pulsanti.setSpacing(true);

        // Layout principale
        add(title, titolo, descrizione, localita, data, orario, visibilita, pulsanti);
        setAlignItems(Alignment.CENTER);
        setSpacing(true);
        setPadding(true);
        setWidth("600px");
    }
}
