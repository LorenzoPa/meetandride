package com.meetandride.view;

import java.util.List;

import com.meetandride.layout.MainLayout;
import com.meetandride.model.User;
import com.meetandride.model.Vehicle;
import com.meetandride.service.UserService;
import com.meetandride.service.VehicleService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route(value = "veicoli", layout = MainLayout.class)
public class VehicleView extends VerticalLayout {

    public VehicleView(VehicleService vehicleService, UserService userService) {

        H1 title = new H1("I miei veicoli 🚗");

        User user = userService.getAuthenticatedUser();
        if (user == null) {
            Notification.show("Devi effettuare il login per visualizzare i tuoi veicoli!");
            return;
        }

        Grid<Vehicle> grid = new Grid<>(Vehicle.class, false);
        grid.addColumn(Vehicle::getMarca).setHeader("Marca");
        grid.addColumn(Vehicle::getModello).setHeader("Modello");
        grid.addColumn(Vehicle::getColore).setHeader("Colore");
        grid.addColumn(Vehicle::getTarga).setHeader("Targa");

        List<Vehicle> veicoli = vehicleService.findByOwner(user);
        grid.setItems(veicoli);

        TextField marca = new TextField("Marca");
        TextField modello = new TextField("Modello");
        TextField colore = new TextField("Colore");
        TextField targa = new TextField("Targa");

        Button addBtn = new Button("Aggiungi veicolo", e -> {
            Vehicle v = new Vehicle(marca.getValue(), modello.getValue(), targa.getValue(), colore.getValue(), user);
            vehicleService.save(v);
            Notification.show("Veicolo aggiunto!");
            getUI().ifPresent(ui -> ui.getPage().reload());
        });

        add(title, grid, marca, modello, colore, targa, addBtn);
        setAlignItems(Alignment.CENTER);
    }
}
