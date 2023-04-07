package com.hisfrontend.view;

import com.hisfrontend.view.mainPages.GabinetPage;
import com.hisfrontend.view.mainPages.OddzialPage;
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
    private Button oddzialBtn = new Button("Oddział");
    private Button panelAdministracyjnyBtn = new Button("Panel Administracyjny");
    private HorizontalLayout menuLayout = new HorizontalLayout();

    public MainMenu(){
        buttonsLogic();
        menuStyles();
        menuLayout.add(gabinetBtn, oddzialBtn, panelAdministracyjnyBtn);
        add(NavigatePanel.drawNavigatePanel(PAGE_NAME), menuLayout);

    }

    private void menuStyles(){
        menuLayout.getStyle().set("margin", "auto");
        gabinetBtn.getStyle().set("padding", "40px");
        oddzialBtn.getStyle().set("padding", "40px");
        panelAdministracyjnyBtn.getStyle().set("padding", "40px");
    }

    private void buttonsLogic(){
        gabinetBtn.addClickListener(e -> UI.getCurrent().navigate(GabinetPage.class));
        oddzialBtn.addClickListener(e -> UI.getCurrent().navigate(OddzialPage.class));
        panelAdministracyjnyBtn.addClickListener(e -> UI.getCurrent().navigate(PanelAdministracyjnyPage.class));
    }
}
