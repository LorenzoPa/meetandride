package com.meetandride.layout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meetandride.model.User;
import com.meetandride.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.annotation.UIScope;

@UIScope
@Component
public class MainLayout extends AppLayout {

    @Autowired
    public MainLayout(UserService userService) {

        H1 logo = new H1("🏎️ Meet&Ride");

        HorizontalLayout navbar = new HorizontalLayout();
        navbar.setWidthFull();
        navbar.setAlignItems(Alignment.CENTER);
        navbar.addClassNames("navbar");

        // 🔹 Link comuni
        RouterLink home = new RouterLink("🏠 Home", com.meetandride.view.HomeView.class);
        RouterLink eventi = new RouterLink("📅 Eventi", com.meetandride.view.EventListView.class);

        navbar.add(logo, home, eventi);
        navbar.expand(logo);

        // 🔹 Se l’utente è loggato
        User user = userService.getAuthenticatedUser();
        if (user != null) {
            RouterLink mieiEventi = new RouterLink("🏁 I miei eventi", com.meetandride.view.MyEventsView.class);
            RouterLink veicolo = new RouterLink("🚗 Veicolo", com.meetandride.view.VehicleView.class);

            Button logout = new Button("🚪 Logout", e -> UI.getCurrent().getPage().setLocation("/logout"));

            navbar.add(mieiEventi, veicolo, logout);
        } else {
            Button login = new Button("🔑 Login", e -> UI.getCurrent().navigate("login"));
            Button register = new Button("📝 Registrati", e -> UI.getCurrent().navigate("register"));
            navbar.add(login, register);
        }

        addToNavbar(navbar);
    }
}
