package com.hisfrontend.view.mainPages;

import com.hisfrontend.view.staticContent.AppLayoutBasic;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("/Office")
public class OfficePage extends VerticalLayout {
    private static final String PAGE_NAME = "OFFICE";

    private Button takeOnPatientBtn = new Button("Przyjmij");
    private HorizontalLayout bottomMenu = new HorizontalLayout();
    public OfficePage(){
        bottomMenu.add(takeOnPatientBtn);
        add(AppLayoutBasic.drawAppLayout(PAGE_NAME), bottomMenu);
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
