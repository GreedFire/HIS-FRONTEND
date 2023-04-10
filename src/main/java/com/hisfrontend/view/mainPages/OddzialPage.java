package com.hisfrontend.view.mainPages;

import com.hisfrontend.view.staticContent.NavigatePanel;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("/Oddzial")
public class OddzialPage extends VerticalLayout {
    private static final String PAGE_NAME = "ODDZIAŁ";
    public OddzialPage(){
        add(NavigatePanel.drawNavigatePanel(PAGE_NAME));
    }
}
