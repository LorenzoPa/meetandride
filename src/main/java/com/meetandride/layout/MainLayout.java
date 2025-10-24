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

@UIScope // 👈 aggiungi questo per dire che vive solo nel contesto UI
@Component // 👈 mantiene Spring injection MA gestito correttamente
public class MainLayout extends AppLayout {

    @Autowired
    public MainLayout(UserService userService) {

        H1 logo = new H1("🏎️ Meet&Ride");

        HorizontalLayout navbar = new HorizontalLayout();
        navbar.setWidthFull();
        navbar.setAlignItems(Alignment.CENTER);
        navbar.addClassNames("navbar");

        RouterLink home = new RouterLink("🏠 Home", com.meetandride.view.HomeView.class);
        RouterLink eventi = new RouterLink("📅 Eventi", com.meetandride.view.EventListView.class);

        navbar.add(logo, home, eventi);
        navbar.expand(logo);

        User user = userService.getAuthenticatedUser();
        if (user != null) {
            Button logout = new Button("🚪 Logout", e -> UI.getCurrent().getPage().setLocation("/logout"));
            navbar.add(logout);
        } else {
            Button login = new Button("🔑 Login", e -> UI.getCurrent().navigate("login"));
            Button register = new Button("📝 Register", e -> UI.getCurrent().navigate("register"));
            navbar.add(login, register);
        }

        addToNavbar(navbar);
    }
}
