package com.meetandride.view;

import org.springframework.beans.factory.annotation.Autowired;

import com.meetandride.layout.MainLayout;
import com.meetandride.model.User;
import com.meetandride.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Home | Meet&Ride")
public class HomeView extends VerticalLayout {

    @Autowired
    public HomeView(UserService userService) {
        // Layout di base
        setAlignItems(Alignment.CENTER);
        setSpacing(true);
        setPadding(true);

        // Titolo e descrizione
        H1 title = new H1("🏎️ Benvenuto su Meet&Ride!");
        Paragraph description = new Paragraph(
            "Scopri, crea e partecipa agli eventi automobilistici nella tua zona!"
        );

        User user = userService.getAuthenticatedUser();

        if (user != null) {
            // 👤 Utente loggato
            Span greeting = new Span("Ciao, " + user.getUsername() + "! 👋");
            greeting.getStyle().set("font-size", "1.2em").set("font-weight", "500");

            Button eventiButton = new Button("📅 Vai agli eventi");
            eventiButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("eventi")));

            Button logoutButton = new Button("🚪 Logout");
            logoutButton.addClickListener(e -> getUI().ifPresent(ui -> ui.getPage().setLocation("/logout")));

            HorizontalLayout actions = new HorizontalLayout(eventiButton, logoutButton);
            actions.setSpacing(true);

            add(title, greeting, description, actions);

        } else {
            // 👥 Utente non loggato
            Button loginButton = new Button("🔑 Accedi");
            loginButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("login")));

            Button registerButton = new Button("📝 Registrati");
            registerButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("register")));

            HorizontalLayout actions = new HorizontalLayout(loginButton, registerButton);
            actions.setSpacing(true);

            add(title, description, actions);
        }
    }
}
