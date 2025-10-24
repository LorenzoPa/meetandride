package com.meetandride.view;

import org.springframework.beans.factory.annotation.Autowired;

import com.meetandride.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("register")
@PageTitle("Registrazione | Meet&Ride")
public class RegisterView extends VerticalLayout {

    private final UserService userService;

    @Autowired
    public RegisterView(UserService userService) {
        this.userService = userService;

        // Layout base
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();
        setSpacing(true);

        // Titolo e descrizione
        H1 title = new H1("📝 Crea un nuovo account");
        Paragraph subtitle = new Paragraph("Unisciti a Meet&Ride e partecipa ai raduni automobilistici vicino a te!");
        subtitle.getStyle().set("margin-bottom", "1em");

        // Campi form
        TextField username = new TextField("Username");
        EmailField email = new EmailField("Email");
        PasswordField password = new PasswordField("Password");
        PasswordField conferma = new PasswordField("Conferma Password");

        username.setRequired(true);
        email.setRequired(true);
        password.setRequired(true);
        conferma.setRequired(true);

        // Pulsanti
        Button registerButton = new Button("🚗 Registrati");
        Button loginButton = new Button("↩️ Torna al login");

        // Azione Registrazione
        registerButton.addClickListener(event -> {
            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || conferma.isEmpty()) {
                Notification.show("Compila tutti i campi!");
                return;
            }
            if (!password.getValue().equals(conferma.getValue())) {
                Notification.show("Le password non coincidono!");
                return;
            }
            if (userService.findByUsername(username.getValue()).isPresent()) {
                Notification.show("Username già in uso!");
                return;
            }
            if (userService.existsByEmail(email.getValue())) {
                Notification.show("Email già registrata!");
                return;
            }

            userService.registerUser(username.getValue(), email.getValue(), password.getValue());
            Notification.show("Registrazione completata con successo!");
            getUI().ifPresent(ui -> ui.navigate("login"));
        });

        // Azione Torna al Login
        loginButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("login")));

        HorizontalLayout buttons = new HorizontalLayout(registerButton, loginButton);
        buttons.setSpacing(true);

        // Aggiunta componenti
        add(title, subtitle, username, email, password, conferma, buttons);
        setWidth("400px");
    }
}
