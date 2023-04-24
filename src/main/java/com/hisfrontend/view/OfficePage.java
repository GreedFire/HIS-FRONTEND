package com.hisfrontend.view;

import com.hisfrontend.view.PagesContent.AppLayoutBasic;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("/Office")
public class OfficePage extends VerticalLayout {
    private static final String PAGE_NAME = "OFFICE";

    public OfficePage(){
        add(AppLayoutBasic.drawAppLayout(PAGE_NAME));

    }
}
