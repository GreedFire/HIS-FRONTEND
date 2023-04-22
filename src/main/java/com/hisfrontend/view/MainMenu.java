package com.hisfrontend.view;

import com.hisfrontend.view.mainPages.OfficePage;
import com.hisfrontend.view.mainPages.RegistrationPage.RegistrationPage;
import com.hisfrontend.view.mainPages.AdministrationPanelPage;
import com.hisfrontend.view.staticContent.AppLayoutBasic;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("/MainMenu")
public class MainMenu extends VerticalLayout {
    private static final String PAGE_NAME = "MENU";

    private final Button gabinetBtn = new Button("Office");
    private final Button rejestracjaBtn = new Button("Registration");
    private final Button panelAdministracyjnyBtn = new Button("Administration panel");
    private final HorizontalLayout menuLayout = new HorizontalLayout();

    public MainMenu(){
        buttonsLogic();
        menuStyles();
        menuLayout.add(rejestracjaBtn, gabinetBtn, panelAdministracyjnyBtn);
        add(AppLayoutBasic.drawAppLayout(PAGE_NAME), menuLayout);

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
        gabinetBtn.addClickListener(e -> UI.getCurrent().navigate(OfficePage.class));
        rejestracjaBtn.addClickListener(e -> UI.getCurrent().navigate(RegistrationPage.class));
        panelAdministracyjnyBtn.addClickListener(e -> UI.getCurrent().navigate(AdministrationPanelPage.class));
    }
}
