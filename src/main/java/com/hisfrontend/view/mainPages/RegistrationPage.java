package com.hisfrontend.view.mainPages;

import com.hisfrontend.domain.dto.PatientDto;
import com.hisfrontend.view.mainPages.PagesContent.PatientDeleteDialog;
import com.hisfrontend.view.mainPages.PagesContent.PatientRegisterDialog;
import com.hisfrontend.view.mainPages.PagesContent.PatientListGrid;
import com.hisfrontend.view.mainPages.PagesContent.PatientUpdateDialog;
import com.hisfrontend.view.staticContent.AppLayoutBasic;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.selection.SingleSelect;
import com.vaadin.flow.router.Route;

@Route("/Registration")
public class RegistrationPage extends VerticalLayout{
    private static final String PAGE_NAME = "REGISTRATION";
    private Dialog registerDialog;
    private Dialog deleteDialog;
    private Dialog editDialog;
    private PatientListGrid patientListGrid;

    public RegistrationPage(){
        initClassFields();
        add(AppLayoutBasic.drawAppLayout(PAGE_NAME));
        add(patientListGrid.getTopMenu());
        add(patientListGrid.getGrid());
        drawMenuBar();
    }

    private void drawMenuBar(){
        ComponentEventListener<ClickEvent<MenuItem>> registerButtonListener = e -> registerDialog.open();
        ComponentEventListener<ClickEvent<MenuItem>> deleteButtonListener = (e -> {
                    SingleSelect<Grid<PatientDto>, PatientDto> patientSelect = patientListGrid.getGrid().asSingleSelect();
                        PatientDto patientDto = patientSelect.getValue();
            if(!patientSelect.isEmpty()) {
                this.deleteDialog = new PatientDeleteDialog(patientListGrid, patientDto);
                this.deleteDialog.open();
            }
                });

        ComponentEventListener<ClickEvent<MenuItem>> editButtonListener = (e -> {
            SingleSelect<Grid<PatientDto>, PatientDto> patientSelect = patientListGrid.getGrid().asSingleSelect();
            PatientDto patientDto = patientSelect.getValue();
            if(!patientSelect.isEmpty()) {
                this.editDialog = new PatientUpdateDialog(patientListGrid, patientDto);
                this.editDialog.open();
            }
        });

        MenuBar menuBar = new MenuBar();
        menuBar.addItem("Register", registerButtonListener);
        menuBar.addItem("Edit", editButtonListener);
        menuBar.addItem("Delete", deleteButtonListener);
        add(menuBar);
    }

    private void initClassFields(){
        this.patientListGrid = new PatientListGrid();
        this.registerDialog = new PatientRegisterDialog(patientListGrid);


    }
}


