package com.hisfrontend.view.mainPages;

import com.hisfrontend.view.staticContent.NavigatePanel;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("/PanelAdministracyjny")
public class PanelAdministracyjnyPage extends VerticalLayout {
    private static final String PAGE_NAME = "PANEL ADMINISTRACYJNY";
    public PanelAdministracyjnyPage(){
        add(NavigatePanel.drawNavigatePanel(PAGE_NAME));
    }
}
