package com.hisfrontend.view.mainPages;

import com.hisfrontend.view.staticContent.NavigatePanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("/Gabinet")
public class GabinetPage extends VerticalLayout {
    private static final String PAGE_NAME = "GABINET";

    private Button takeOnPatientBtn = new Button("Przyjmij");
    private HorizontalLayout bottomMenu = new HorizontalLayout();
    public GabinetPage(){
        bottomMenu.add(takeOnPatientBtn);
        add(NavigatePanel.drawNavigatePanel(PAGE_NAME), bottomMenu);
        createStyles();
    }

    private void createStyles() {
        bottomMenu.getStyle().set("margin", "auto");
        bottomMenu.getStyle().set("width", "100%");
        bottomMenu.getStyle().set("clear", "both");
        bottomMenu.getStyle().set("position", "absolute");
        bottomMenu.getStyle().set("height", "100px");
        bottomMenu.getStyle().set("bottom", "0");
        takeOnPatientBtn.getStyle().set("padding", "20px");
        takeOnPatientBtn.getStyle().set("font-size", "25px");
    }

}
