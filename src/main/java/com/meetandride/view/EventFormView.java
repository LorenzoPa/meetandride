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

        setAlignItems(Alignment.CENTER);
        setWidth("600px");
        setSpacing(true);
        setPadding(true);

        // 🔹 Titolo pagina
        H1 title = new H1("🛠️ Crea un nuovo evento");

        // 🔹 Campi del form
        TextField titolo = new TextField("Titolo");
        titolo.setRequiredIndicatorVisible(true);
        titolo.setPlaceholder("Es. Raduno a Livorno");

        TextArea descrizione = new TextArea("Descrizione");
        descrizione.setPlaceholder("Descrivi brevemente il tuo evento...");
        descrizione.setWidthFull();

        TextField localita = new TextField("Località");
        localita.setRequiredIndicatorVisible(true);
        localita.setPlaceholder("Es. Pisa, Firenze...");

        DatePicker data = new DatePicker("Data");
        data.setRequiredIndicatorVisible(true);

        TextField orario = new TextField("Orario");
        orario.setPlaceholder("Es. 10:00");

        // 🔹 Selettore visibilità
        Select<Visibilita> visibilita = new Select<>();
        visibilita.setLabel("Visibilità");
        visibilita.setItems(Visibilita.values());
        visibilita.setItemLabelGenerator(v -> switch (v) {
            case APERTO -> "Aperto (chiunque può partecipare)";
            case CHIUSO -> "Chiuso (solo su approvazione)";
            case PRIVATO -> "Privato (solo visibile a te)";
        });
        visibilita.setValue(Visibilita.APERTO);

        // 🔹 Pulsanti
        Button salvaButton = new Button("💾 Salva evento");
        salvaButton.getStyle().set("background-color", "#4CAF50").set("color", "white");

        Button annullaButton = new Button("↩️ Annulla", e -> 
            getUI().ifPresent(ui -> ui.navigate("eventi"))
        );

        // 🔹 Azione Salva
        salvaButton.addClickListener(e -> {
            User current = userService.getAuthenticatedUser();
            if (current == null) {
                Notification.show("Effettua il login per creare un evento.", 3000, Position.TOP_CENTER);
                return;
            }

            if (titolo.isEmpty() || localita.isEmpty() || data.isEmpty()) {
                Notification.show("Compila tutti i campi obbligatori!", 2500, Position.TOP_CENTER);
                return;
            }

            Event evento = new Event(
                titolo.getValue().trim(),
                descrizione.getValue().trim(),
                visibilita.getValue(),
                localita.getValue().trim(),
                data.getValue(),
                orario.getValue().trim(),
                current
            );

            eventService.save(evento);
            Notification.show("✅ Evento \"" + titolo.getValue() + "\" creato con successo!", 3000, Position.TOP_CENTER);
            getUI().ifPresent(ui -> ui.navigate("eventi"));
        });

        // Layout pulsanti
        HorizontalLayout pulsanti = new HorizontalLayout(salvaButton, annullaButton);
        pulsanti.setSpacing(true);

        // Layout principale
        add(title, titolo, descrizione, localita, data, orario, visibilita, pulsanti);
    }
}
