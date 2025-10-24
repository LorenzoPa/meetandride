package com.meetandride.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Meet&Ride - Home")
@Route("")
public class HomeView extends VerticalLayout {
    public HomeView(){
        H1 title = new H1("Benvenuto su Meet&Ride!");
        Paragraph description = new Paragraph("Scopri, crea e partecipa agli eventi automobilistici nella tua zona!");
        Button exploreButton = new Button("Vai alla lista eventi");
        exploreButton.addClickListener(e ->
                getUI().ifPresent(ui -> ui.navigate("eventi"))
        );
        add(title, description, exploreButton);
        setAlignItems(Alignment.CENTER);
    }
}
