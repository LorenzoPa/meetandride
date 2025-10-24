package com.meetandride.view;

import org.springframework.beans.factory.annotation.Autowired;

import com.meetandride.layout.MainLayout;
import com.meetandride.model.Event;
import com.meetandride.model.User;
import com.meetandride.service.EventService;
import com.meetandride.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "eventi/dettaglio", layout = MainLayout.class)
@PageTitle("Dettagli Evento | Meet&Ride")
public class EventDetailView extends VerticalLayout implements HasUrlParameter<Long> {

    private final EventService eventService;
    private final UserService userService;

    private Event evento;
    private Button actionButton;
    private Paragraph countParagraph;
    private Grid<User> gridPartecipanti;
    private Grid<User> gridRichieste;

    @Autowired
    public EventDetailView(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;

        setAlignItems(Alignment.CENTER);
        setWidth("850px");
        setSpacing(true);
        setPadding(true);
    }

    @Override
    public void setParameter(BeforeEvent event, Long eventId) {
        this.evento = eventService.findById(eventId);
        removeAll();

        if (evento == null) {
            add(new H1("Evento non trovato"));
            return;
        }

        User current = userService.getAuthenticatedUser();

        //Info evento
        H1 titolo = new H1(evento.getTitolo());
        Paragraph descrizione = new Paragraph(evento.getDescrizione() != null ? evento.getDescrizione() : "(nessuna descrizione)");
        Paragraph localita = new Paragraph(" Località: " + evento.getLocalita());
        Paragraph data = new Paragraph(" Data: " + evento.getData());
        Paragraph orario = new Paragraph(" Orario: " + evento.getOrario());
        Paragraph visibilita = new Paragraph(" Visibilità: " + evento.getVisibilita());
        Paragraph host = new Paragraph(" Creatore: " + (evento.getUser() != null ? evento.getUser().getUsername() : "—"));
        countParagraph = new Paragraph(" Partecipanti: " + getPartecipantiCount());

        //Bottone dinamico
        actionButton = new Button();
        refreshButtonState(current);
        actionButton.addClickListener(e -> handleActionButton(current));

        Button indietro = new Button("← Torna alla lista", ev -> getUI().ifPresent(ui -> ui.navigate("eventi")));

        //Se l’utente è host → pulsanti gestionali
        HorizontalLayout hostActions = new HorizontalLayout();
        if (current != null && isHost(current)) {
            Button modifica = new Button("Modifica evento", e -> getUI().ifPresent(ui -> ui.navigate("eventi/modifica/" + evento.getId())));
            Button elimina = new Button("Elimina evento", e -> showDeleteConfirmation());
            hostActions.add(modifica, elimina);
            hostActions.setSpacing(true);
        }

        //Campo invito (solo per host)
        VerticalLayout inviteLayout = new VerticalLayout();
        if (current != null && isHost(current)) {
            H2 titoloInvito = new H2("Invita un utente");
            TextField usernameField = new TextField("Username dell’utente");
            usernameField.setPlaceholder("Inserisci lo username...");

            Button invitaBtn = new Button("Invita utente", e -> {
                String username = usernameField.getValue().trim();
                if (username.isEmpty()) {
                    Notification.show("Inserisci uno username valido.", 2500, Position.TOP_CENTER);
                    return;
                }

                var userOpt = userService.findByUsername(username);
                if (userOpt.isEmpty()) {
                    Notification.show("Utente \"" + username + "\" non trovato.", 2500, Position.TOP_CENTER);
                    return;
                }

                User invited = userOpt.get();

                if (evento.isParticipating(invited)) {
                    Notification.show(username + " partecipa già a questo evento.", 2500, Position.TOP_CENTER);
                    return;
                }

                if (evento.hasRequested(invited)) {
                    Notification.show(username + " ha già inviato una richiesta.", 2500, Position.TOP_CENTER);
                    return;
                }

                eventService.addParticipant(evento.getId(), invited);
                Notification.show("Utente \"" + username + "\" invitato con successo!", 2500, Position.TOP_CENTER);
                usernameField.clear();
                reloadData();
            });

            inviteLayout.add(titoloInvito, usernameField, invitaBtn);
            inviteLayout.setAlignItems(Alignment.CENTER);
            inviteLayout.setSpacing(true);
        }

        // 🔹 Partecipanti
        H2 titoloPartecipanti = new H2("Lista partecipanti");
        gridPartecipanti = new Grid<>(User.class, false);
        gridPartecipanti.addColumn(User::getUsername).setHeader("Username").setAutoWidth(true);
        refreshPartecipantiGrid();

        add(titolo, descrizione, localita, data, orario, visibilita, host, countParagraph, actionButton, indietro);

        if (hostActions.getComponentCount() > 0) {
            add(hostActions);
        }

        if (inviteLayout.getComponentCount() > 0) {
            add(inviteLayout);
        }

        if (current != null && isHost(current)) {
            addHostRequestsSection();
        }

        add(titoloPartecipanti, gridPartecipanti);
    }

    // ===========================================================
    //Dialogo conferma eliminazione
    // ===========================================================
    private void showDeleteConfirmation() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Conferma eliminazione evento");

        Paragraph text = new Paragraph("Sei sicuro di voler eliminare questo evento? L’azione è irreversibile.");

        Button conferma = new Button("Elimina", e -> {
            eventService.delete(evento);
            dialog.close();
            Notification.show("Evento eliminato con successo.", 3000, Position.TOP_CENTER);
            getUI().ifPresent(ui -> ui.navigate("eventi"));
        });

        Button annulla = new Button("Annulla", e -> dialog.close());
        dialog.add(text, new HorizontalLayout(conferma, annulla));
        dialog.open();
    }

    // ===========================================================
    //HOST PANEL - Gestione richieste in sospeso
    // ===========================================================
    private void addHostRequestsSection() {
        if (evento.getRichieste() == null || evento.getRichieste().isEmpty()) {
            add(new Paragraph("Nessuna richiesta in sospeso."));
            return;
        }

        H2 titoloRichieste = new H2("Richieste in sospeso");
        gridRichieste = new Grid<>(User.class, false);
        gridRichieste.addColumn(User::getUsername).setHeader("Username").setAutoWidth(true);

        gridRichieste.addComponentColumn(user -> {
            Button accetta = new Button("Approva", e -> {
                eventService.approveRequest(evento.getId(), user);
                Notification.show(user.getUsername() + " è stato approvato.", 2500, Position.TOP_CENTER);
                reloadData();
            });

            Button rifiuta = new Button("Rifiuta", e -> {
                eventService.rejectRequest(evento.getId(), user);
                Notification.show(user.getUsername() + " è stato rifiutato.", 2500, Position.TOP_CENTER);
                reloadData();
            });

            return new HorizontalLayout(accetta, rifiuta);
        }).setHeader("Azione");

        gridRichieste.setItems(evento.getRichieste());
        add(titoloRichieste, gridRichieste);
    }

    // ===========================================================
    //Azioni utente
    // ===========================================================
    private void handleActionButton(User current) {
        if (current == null) {
            Notification.show("Effettua il login per partecipare!", 2500, Position.TOP_CENTER);
            return;
        }

        if (isHost(current)) {
            Notification.show("Sei l’host di questo evento!", 2500, Position.TOP_CENTER);
            return;
        }

        switch (evento.getVisibilita()) {
            case CHIUSO -> handleClosedEvent(current);
            default -> handleOpenOrPrivateEvent(current);
        }

        reloadData();
    }

    private void handleClosedEvent(User current) {
        if (evento.isParticipating(current)) {
            eventService.removeParticipant(evento.getId(), current);
            Notification.show("Hai lasciato l’evento.", 2500, Position.TOP_CENTER);
        } else if (evento.hasRequested(current)) {
            Notification.show("Hai già inviato una richiesta di partecipazione.", 2500, Position.TOP_CENTER);
        } else {
            eventService.addRequest(evento.getId(), current);
            Notification.show("Richiesta di partecipazione inviata!", 2500, Position.TOP_CENTER);
        }
    }

    private void handleOpenOrPrivateEvent(User current) {
        if (evento.isParticipating(current)) {
            eventService.removeParticipant(evento.getId(), current);
            Notification.show("Hai lasciato l’evento.", 2500, Position.TOP_CENTER);
        } else {
            eventService.addParticipant(evento.getId(), current);
            Notification.show("Ti sei unito all’evento!", 2500, Position.TOP_CENTER);
        }
    }

    // ===========================================================
    //Refresh helpers
    // ===========================================================
    private void reloadData() {
        evento = eventService.findById(evento.getId());
        refreshButtonState(userService.getAuthenticatedUser());
        updatePartecipantiCount();
        refreshPartecipantiGrid();

        if (gridRichieste != null) {
            remove(gridRichieste);
        }
        if (isHost(userService.getAuthenticatedUser())) {
            addHostRequestsSection();
        }
    }

    private void refreshPartecipantiGrid() {
        if (evento.getPartecipanti() != null) {
            gridPartecipanti.setItems(evento.getPartecipanti());
        }
    }

    private void refreshButtonState(User current) {
        if (current == null) {
            actionButton.setText("Effettua il login");
            actionButton.setEnabled(true);
            return;
        }

        if (isHost(current)) {
            actionButton.setText("Sei l'host");
            actionButton.setEnabled(false);
            return;
        }

        switch (evento.getVisibilita()) {
            case CHIUSO -> {
                if (evento.isParticipating(current)) {
                    actionButton.setText("Lascia evento");
                } else if (evento.hasRequested(current)) {
                    actionButton.setText("Richiesta in attesa");
                    actionButton.setEnabled(false);
                    return;
                } else {
                    actionButton.setText("Richiedi partecipazione");
                }
            }
            default -> {
                if (evento.isParticipating(current)) {
                    actionButton.setText("Non partecipo");
                } else {
                    actionButton.setText("Partecipa");
                }
            }
        }
        actionButton.setEnabled(true);
    }

    // ===========================================================
    //Utility methods
    // ===========================================================
    private int getPartecipantiCount() {
        return evento.getPartecipanti() != null ? evento.getPartecipanti().size() : 0;
    }

    private void updatePartecipantiCount() {
        countParagraph.setText("👥 Partecipanti: " + getPartecipantiCount());
    }

    private boolean isHost(User user) {
        return user != null && evento.getUser() != null && evento.getUser().getId().equals(user.getId());
    }
}
