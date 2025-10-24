package com.meetandride.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("login")
@PageTitle("Login | Meet&Ride")
public class LoginView extends VerticalLayout {

    public LoginView() {
        // 🔹 Titolo e descrizione
        H1 title = new H1("🔑 Accedi a Meet&Ride");
        title.getStyle()
                .set("font-weight", "700")
                .set("margin-bottom", "0")
                .set("color", "#2c3e50");

        Paragraph subtitle = new Paragraph("Bentornato! Inserisci le tue credenziali per continuare.");
        subtitle.getStyle()
                .set("margin-bottom", "1em")
                .set("font-size", "1.1em")
                .set("color", "#555");

        // 🔹 Form di login
        LoginForm loginForm = new LoginForm();
        loginForm.setAction("login"); // delega a Spring Security
        loginForm.getStyle().set("width", "350px");

        // Traduzione (Italiano)
        LoginI18n i18n = LoginI18n.createDefault();
        i18n.getForm().setTitle("Accesso");
        i18n.getForm().setUsername("Username");
        i18n.getForm().setPassword("Password");
        i18n.getForm().setSubmit("Accedi");
        i18n.getForm().setForgotPassword("Password dimenticata?");
        i18n.getErrorMessage().setTitle("Credenziali non valide");
        i18n.getErrorMessage().setMessage("Controlla nome utente e password e riprova.");
        loginForm.setI18n(i18n);

        // 🔹 Pulsanti aggiuntivi
        Button backButton = new Button("⬅️ Torna indietro", e ->
            getUI().ifPresent(ui -> ui.getPage().getHistory().back())
        );
        backButton.getStyle()
                .set("background-color", "#6c757d")
                .set("color", "white")
                .set("border-radius", "8px");

        Button registerButton = new Button("🆕 Registrati", e ->
            getUI().ifPresent(ui -> ui.navigate("register"))
        );
        registerButton.getStyle()
                .set("background-color", "#28a745")
                .set("color", "white")
                .set("border-radius", "8px");

        HorizontalLayout buttonLayout = new HorizontalLayout(backButton, registerButton);
        buttonLayout.setSpacing(true);
        buttonLayout.setPadding(true);

        // 🔹 Layout generale
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();
        setPadding(true);
        setSpacing(true);
        getStyle().set("background-color", "#f8f9fa");

        // 🔹 Aggiunta al layout
        add(title, subtitle, loginForm, buttonLayout);
    }
}
