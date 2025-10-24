package com.meetandride.view;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("login")
@PageTitle("Login | Meet&Ride")
public class LoginView extends VerticalLayout {

    public LoginView() {
        // Titolo e descrizione
        H1 title = new H1("🔑 Accedi a Meet&Ride");
        Paragraph subtitle = new Paragraph("Bentornato! Inserisci le tue credenziali per continuare.");
        subtitle.getStyle().set("margin-bottom", "1em");

        // Login form
        LoginForm loginForm = new LoginForm();
        loginForm.setAction("login"); // delega a Spring Security

        // Traduzione minima (italiano)
        LoginI18n i18n = LoginI18n.createDefault();
        i18n.getForm().setTitle("Accesso");
        i18n.getForm().setUsername("Username");
        i18n.getForm().setPassword("Password");
        i18n.getForm().setSubmit("Accedi");
        i18n.getForm().setForgotPassword("Password dimenticata?");
        i18n.getErrorMessage().setTitle("Credenziali non valide");
        i18n.getErrorMessage().setMessage("Controlla nome utente e password e riprova.");
        loginForm.setI18n(i18n);

        // Stile generale
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        // Aggiunge gli elementi al layout
        add(title, subtitle, loginForm);
    }
}
