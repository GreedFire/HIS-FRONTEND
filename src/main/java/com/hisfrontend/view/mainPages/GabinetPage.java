package com.hisfrontend.view.mainPages;

import com.hisfrontend.view.staticContent.DefiniedView;
import com.hisfrontend.view.staticContent.NavigatePanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.awt.*;

@Route("/Gabinet")
public class GabinetPage extends VerticalLayout {
    private static final String PAGE_NAME = "GABINET";

    private Button registerPatientBtn = new Button("Rejestruj");
    private HorizontalLayout bottomMenu = new HorizontalLayout();
    public GabinetPage(){
        bottomMenu.add(registerPatientBtn);
        add(NavigatePanel.drawNavigatePanel(PAGE_NAME), bottomMenu);
        navigatePanelStyles();
    }

    private void navigatePanelStyles() {
        bottomMenu.getStyle().set("margin", "auto");
        bottomMenu.getStyle().set("width", "100%");
        bottomMenu.getStyle().set("clear", "both");
        bottomMenu.getStyle().set("position", "absolute");
        bottomMenu.getStyle().set("height", "40px");
        bottomMenu.getStyle().set("bottom", "0");
    }

}
