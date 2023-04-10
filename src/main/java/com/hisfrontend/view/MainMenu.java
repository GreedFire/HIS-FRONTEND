package com.hisfrontend.view;

import com.hisfrontend.view.mainPages.GabinetPage;
import com.hisfrontend.view.mainPages.RejestracjaPage;
import com.hisfrontend.view.mainPages.PanelAdministracyjnyPage;
import com.hisfrontend.view.staticContent.NavigatePanel;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("/MainMenu")
public class MainMenu extends VerticalLayout {
    private static final String PAGE_NAME = "MENU";

    private Button gabinetBtn = new Button("Gabinet");
    private Button rejestracjaBtn = new Button("Rejestracja");
    private Button panelAdministracyjnyBtn = new Button("Panel Administracyjny");
    private HorizontalLayout menuLayout = new HorizontalLayout();

    public MainMenu(){
        buttonsLogic();
        menuStyles();
        menuLayout.add(rejestracjaBtn, gabinetBtn, panelAdministracyjnyBtn);
        add(NavigatePanel.drawNavigatePanel(PAGE_NAME), menuLayout);

    }

    private void menuStyles(){
        menuLayout.getStyle().set("margin", "auto");
        gabinetBtn.getStyle().set("padding", "80px");
        rejestracjaBtn.getStyle().set("padding", "80px");
        panelAdministracyjnyBtn.getStyle().set("padding", "80px");
        gabinetBtn.getStyle().set("font-size", "40px");
        rejestracjaBtn.getStyle().set("font-size", "40px");
        panelAdministracyjnyBtn.getStyle().set("font-size", "40px");
    }

    private void buttonsLogic(){
        gabinetBtn.addClickListener(e -> UI.getCurrent().navigate(GabinetPage.class));
        rejestracjaBtn.addClickListener(e -> UI.getCurrent().navigate(RejestracjaPage.class));
        panelAdministracyjnyBtn.addClickListener(e -> UI.getCurrent().navigate(PanelAdministracyjnyPage.class));
    }
}
