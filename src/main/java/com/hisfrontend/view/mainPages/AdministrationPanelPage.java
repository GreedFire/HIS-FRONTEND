package com.hisfrontend.view.mainPages;

import com.hisfrontend.domain.dto.PatientDto;
import com.hisfrontend.domain.dto.UserDto;
import com.hisfrontend.view.mainPages.PagesContent.*;
import com.hisfrontend.view.staticContent.AppLayoutBasic;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.selection.SingleSelect;
import com.vaadin.flow.router.Route;
import org.apache.catalina.User;

@Route("/AdministrationPanel")
public class AdministrationPanelPage extends VerticalLayout {
    private static final String PAGE_NAME = "ADMINISTRATION PANEL";
    private Dialog createDialog;
    private Dialog deleteDialog;
    private Dialog editDialog;
    private UserListGrid userListGrid;
    public AdministrationPanelPage(){
        initClassFields();
        add(AppLayoutBasic.drawAppLayout(PAGE_NAME));
        add(userListGrid.getTopMenu());
        add(userListGrid.getGrid());
        drawMenuBar();
    }

    private void initClassFields(){
        this.userListGrid = new UserListGrid();
        this.createDialog = new UserCreateDialog(userListGrid);
    }

    private void drawMenuBar(){
        ComponentEventListener<ClickEvent<MenuItem>> registerButtonListener = e -> createDialog.open();
        ComponentEventListener<ClickEvent<MenuItem>> deleteButtonListener = (e -> {
            SingleSelect<Grid<UserDto>, UserDto> patientSelect = userListGrid.getGrid().asSingleSelect();
            UserDto userDto = patientSelect.getValue();
            if(!patientSelect.isEmpty()) {
                this.deleteDialog = new UserDeleteDialog(userListGrid, userDto);
                this.deleteDialog.open();
            }
        });

        ComponentEventListener<ClickEvent<MenuItem>> editButtonListener = (e -> {
            SingleSelect<Grid<UserDto>, UserDto> userSelect = userListGrid.getGrid().asSingleSelect();
            UserDto userDto = userSelect.getValue();
            if(!userSelect.isEmpty()) {
                this.editDialog = new UserEditDialog(userListGrid, userDto);
                this.editDialog.open();
            }
        });

        MenuBar menuBar = new MenuBar();
        menuBar.addItem("Create", registerButtonListener);
        menuBar.addItem("Edit", editButtonListener);
        menuBar.addItem("Delete", deleteButtonListener);
        add(menuBar);
    }
}
