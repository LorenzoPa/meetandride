package com.meetandride.view;

import java.util.Optional;

import com.meetandride.layout.MainLayout;
import com.meetandride.model.User;
import com.meetandride.model.Vehicle;
import com.meetandride.service.UserService;
import com.meetandride.service.VehicleService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "veicoli", layout = MainLayout.class)
@PageTitle("Il mio veicolo | Meet&Ride")
public class VehicleView extends VerticalLayout implements BeforeEnterObserver {

    private final VehicleService vehicleService;
    private final UserService userService;

    public VehicleView(VehicleService vehicleService, UserService userService) {
        this.vehicleService = vehicleService;
        this.userService = userService;

        setAlignItems(Alignment.CENTER);
        setSpacing(true);
        setPadding(true);
        setSizeFull();
        getStyle().set("background-color", "#f8f9fa");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            Notification.show("⚠️ Devi effettuare il login per accedere a questa pagina.", 3000, Position.TOP_CENTER);
            event.forwardTo("login");
            return;
        }

        removeAll();
        add(buildViewFor(user));
    }

    private VerticalLayout buildViewFor(User user) {
        VerticalLayout layout = new VerticalLayout();
        layout.setAlignItems(Alignment.CENTER);
        layout.setSpacing(true);
        layout.setWidth("500px");
        layout.getStyle()
                .set("background-color", "white")
                .set("padding", "30px")
                .set("border-radius", "12px")
                .set("box-shadow", "0 4px 12px rgba(0,0,0,0.1)");

        H1 title = new H1("🚗 Il mio veicolo");
        title.getStyle()
                .set("font-weight", "700")
                .set("color", "#2c3e50");

        Optional<Vehicle> existingVehicleOpt = vehicleService.findByOwner(user);

        // 🔹 Se l’utente non ha ancora un veicolo
        if (existingVehicleOpt.isEmpty()) {
            Paragraph info = new Paragraph("Non hai ancora registrato un veicolo. Inserisci i dati qui sotto 👇");
            info.getStyle().set("color", "#555");
            layout.add(title, info);
        } else {
            Paragraph info = new Paragraph("Questo è il veicolo attualmente registrato:");
            info.getStyle().set("color", "#555");

            Grid<Vehicle> grid = new Grid<>(Vehicle.class, false);
            grid.addColumn(Vehicle::getMarca).setHeader("Marca");
            grid.addColumn(Vehicle::getModello).setHeader("Modello");
            grid.addColumn(Vehicle::getColore).setHeader("Colore");
            grid.addColumn(Vehicle::getTarga).setHeader("Targa");
            grid.setItems(existingVehicleOpt.get());
            layout.add(title, info, grid);
        }

        // 🔹 Campi input
        TextField marca = new TextField("Marca");
        TextField modello = new TextField("Modello");
        TextField colore = new TextField("Colore");
        TextField targa = new TextField("Targa");

        marca.setRequiredIndicatorVisible(true);
        modello.setRequiredIndicatorVisible(true);
        targa.setRequiredIndicatorVisible(true);

        existingVehicleOpt.ifPresent(v -> {
            marca.setValue(v.getMarca());
            modello.setValue(v.getModello());
            colore.setValue(v.getColore() != null ? v.getColore() : "");
            targa.setValue(v.getTarga());
        });

        // 🔹 Pulsanti
        Button salvaBtn = new Button(existingVehicleOpt.isPresent() ? "💾 Aggiorna veicolo" : "➕ Aggiungi veicolo");
        salvaBtn.getStyle()
                .set("background-color", "#28a745")
                .set("color", "white")
                .set("border-radius", "8px");

        Button eliminaBtn = new Button("🗑️ Elimina veicolo");
        eliminaBtn.getStyle()
                .set("background-color", "#dc3545")
                .set("color", "white")
                .set("border-radius", "8px");
        eliminaBtn.setVisible(existingVehicleOpt.isPresent());

        // 🔹 Azioni
        salvaBtn.addClickListener(e -> {
            try {
                if (marca.isEmpty() || modello.isEmpty() || targa.isEmpty()) {
                    Notification.show("Compila tutti i campi obbligatori!", 3000, Position.TOP_CENTER);
                    return;
                }

                Vehicle nuovo = new Vehicle(
                        marca.getValue().trim(),
                        modello.getValue().trim(),
                        targa.getValue().trim(),
                        colore.getValue().trim(),
                        user
                );

                vehicleService.saveOrUpdateFor(user, nuovo);
                Notification.show("✅ Veicolo salvato con successo!", 3000, Position.TOP_CENTER);
                getUI().ifPresent(ui -> ui.getPage().reload());
            } catch (IllegalArgumentException ex) {
                Notification.show("❌ " + ex.getMessage(), 3000, Position.TOP_CENTER);
            }
        });

        eliminaBtn.addClickListener(e -> {
            vehicleService.deleteFor(user);
            Notification.show("🚮 Veicolo rimosso con successo!", 3000, Position.TOP_CENTER);
            getUI().ifPresent(ui -> ui.getPage().reload());
        });

        HorizontalLayout buttons = new HorizontalLayout(salvaBtn, eliminaBtn);
        buttons.setSpacing(true);

        layout.add(marca, modello, colore, targa, buttons);
        return layout;
    }
}
