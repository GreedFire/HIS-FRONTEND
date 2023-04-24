package com.hisfrontend.view.mainPages;

import com.hisfrontend.view.staticContent.AppLayoutBasic;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("/Office")
public class OfficePage extends VerticalLayout {
    private static final String PAGE_NAME = "OFFICE";

    public OfficePage(){
        add(AppLayoutBasic.drawAppLayout(PAGE_NAME));

    }
}
