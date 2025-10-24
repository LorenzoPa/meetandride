package com.meetandride.view;

import org.springframework.beans.factory.annotation.Autowired;

import com.meetandride.layout.MainLayout;
import com.meetandride.model.User;
import com.meetandride.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
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
        setSizeFull();

        // 🔹 Titolo e descrizione
        H1 title = new H1("🏎️ Benvenuto su Meet&Ride!");
        title.getStyle()
                .set("font-weight", "700")
                .set("margin-top", "40px")
                .set("color", "#2c3e50");

        Paragraph description = new Paragraph(
                "Scopri, crea e partecipa agli eventi automobilistici nella tua zona!"
        );
        description.getStyle()
                .set("font-size", "1.1em")
                .set("color", "#555")
                .set("text-align", "center");

        // 🔹 Immagine decorativa (opzionale, se hai una cartella /images)
        Image banner = new Image("images/car_banner.png", "Meet&Ride Banner");
        banner.setWidth("400px");
        banner.getStyle().set("border-radius", "12px").set("box-shadow", "0 4px 12px rgba(0,0,0,0.2)");

        User user = userService.getAuthenticatedUser();

        if (user != null) {
            // 👤 Utente loggato
            Span greeting = new Span("Ciao, " + user.getUsername() + "! 👋");
            greeting.getStyle().set("font-size", "1.3em").set("font-weight", "600");

            Button eventiButton = new Button("📅 Vai agli eventi");
            eventiButton.getStyle()
                    .set("background-color", "#007bff")
                    .set("color", "white")
                    .set("border-radius", "8px");

            eventiButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("eventi")));

            Button logoutButton = new Button("🚪 Logout");
            logoutButton.getStyle()
                    .set("background-color", "#6c757d")
                    .set("color", "white")
                    .set("border-radius", "8px");

            logoutButton.addClickListener(e -> getUI().ifPresent(ui -> ui.getPage().setLocation("/logout")));

            HorizontalLayout actions = new HorizontalLayout(eventiButton, logoutButton);
            actions.setSpacing(true);

            add(title, banner, greeting, description, actions);

        } else {
            // 👥 Utente non loggato
            Button loginButton = new Button("🔑 Accedi");
            loginButton.getStyle()
                    .set("background-color", "#007bff")
                    .set("color", "white")
                    .set("border-radius", "8px");

            Button registerButton = new Button("📝 Registrati");
            registerButton.getStyle()
                    .set("background-color", "#28a745")
                    .set("color", "white")
                    .set("border-radius", "8px");

            loginButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("login")));
            registerButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("register")));

            HorizontalLayout actions = new HorizontalLayout(loginButton, registerButton);
            actions.setSpacing(true);

            add(title, banner, description, actions);
        }
    }
}
