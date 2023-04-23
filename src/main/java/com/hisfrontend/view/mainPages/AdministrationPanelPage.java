package com.hisfrontend.view.mainPages;

import com.hisfrontend.view.mainPages.PagesContent.PatientListGrid;
import com.hisfrontend.view.mainPages.PagesContent.PatientRegisterDialog;
import com.hisfrontend.view.mainPages.PagesContent.UserListGrid;
import com.hisfrontend.view.staticContent.AppLayoutBasic;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("/AdministrationPanel")
public class AdministrationPanelPage extends VerticalLayout {
    private static final String PAGE_NAME = "ADMINISTRATION PANEL";
    private UserListGrid userListGrid;
    public AdministrationPanelPage(){
        initClassFields();
        add(AppLayoutBasic.drawAppLayout(PAGE_NAME));
        add(userListGrid.getTopMenu());
        add(userListGrid.getGrid());

    }

    private void initClassFields(){
        this.userListGrid = new UserListGrid();
    }
}
