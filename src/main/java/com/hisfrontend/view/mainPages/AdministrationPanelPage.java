package com.hisfrontend.view.mainPages;

import com.hisfrontend.view.staticContent.AppLayoutBasic;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("/AdministrationPanel")
public class AdministrationPanelPage extends VerticalLayout {
    private static final String PAGE_NAME = "ADMINISTRATION PANEL";
    public AdministrationPanelPage(){
        add(AppLayoutBasic.drawAppLayout(PAGE_NAME));
    }
}
