package com.meetandride.view;

import org.springframework.beans.factory.annotation.Autowired;

import com.meetandride.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
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
        setPadding(true);
        setSpacing(true);
        getStyle().set("background-color", "#f8f9fa");

        // 🔹 Titolo e descrizione
        H1 title = new H1("📝 Crea un nuovo account");
        title.getStyle()
                .set("font-weight", "700")
                .set("margin-bottom", "0")
                .set("color", "#2c3e50");

        Paragraph subtitle = new Paragraph("Unisciti a Meet&Ride e partecipa ai raduni automobilistici vicino a te!");
        subtitle.getStyle()
                .set("margin-bottom", "1em")
                .set("font-size", "1.1em")
                .set("color", "#555");

        // 🔹 Campi form
        TextField username = new TextField("Username");
        EmailField email = new EmailField("Email");
        PasswordField password = new PasswordField("Password");
        PasswordField conferma = new PasswordField("Conferma Password");

        username.setRequiredIndicatorVisible(true);
        email.setRequiredIndicatorVisible(true);
        password.setRequiredIndicatorVisible(true);
        conferma.setRequiredIndicatorVisible(true);

        username.setWidthFull();
        email.setWidthFull();
        password.setWidthFull();
        conferma.setWidthFull();

        // 🔹 Pulsanti
        Button registerButton = new Button("🚗 Registrati");
        registerButton.getStyle()
                .set("background-color", "#28a745")
                .set("color", "white")
                .set("border-radius", "8px");

        Button loginButton = new Button("🔑 Vai al login", e ->
            getUI().ifPresent(ui -> ui.navigate("login"))
        );
        loginButton.getStyle()
                .set("background-color", "#007bff")
                .set("color", "white")
                .set("border-radius", "8px");

        Button backButton = new Button("⬅️ Torna indietro", e ->
            getUI().ifPresent(ui -> ui.getPage().getHistory().back())
        );
        backButton.getStyle()
                .set("background-color", "#6c757d")
                .set("color", "white")
                .set("border-radius", "8px");

        // 🔹 Azione Registrazione
        registerButton.addClickListener(e -> {
            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || conferma.isEmpty()) {
                Notification.show("Compila tutti i campi obbligatori!", 3000, Position.TOP_CENTER);
                return;
            }
            if (!password.getValue().equals(conferma.getValue())) {
                Notification.show("Le password non coincidono!", 3000, Position.TOP_CENTER);
                return;
            }
            if (userService.findByUsername(username.getValue()).isPresent()) {
                Notification.show("Username già in uso!", 3000, Position.TOP_CENTER);
                return;
            }
            if (userService.existsByEmail(email.getValue())) {
                Notification.show("Email già registrata!", 3000, Position.TOP_CENTER);
                return;
            }

            userService.registerUser(username.getValue().trim(), email.getValue().trim(), password.getValue());
            Notification.show("✅ Registrazione completata con successo!", 3000, Position.TOP_CENTER);
            getUI().ifPresent(ui -> ui.navigate("login"));
        });

        // 🔹 Layout pulsanti
        HorizontalLayout buttons = new HorizontalLayout(registerButton, loginButton);
        buttons.setSpacing(true);
        buttons.setJustifyContentMode(JustifyContentMode.CENTER);

        // 🔹 Layout finale form
        VerticalLayout formLayout = new VerticalLayout(username, email, password, conferma, buttons, backButton);
        formLayout.setAlignItems(Alignment.CENTER);
        formLayout.setWidth("400px");
        formLayout.getStyle()
                .set("background-color", "white")
                .set("padding", "30px")
                .set("border-radius", "12px")
                .set("box-shadow", "0 4px 12px rgba(0,0,0,0.1)");

        add(title, subtitle, formLayout);
    }
}
