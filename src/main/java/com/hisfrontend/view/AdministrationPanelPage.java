package com.hisfrontend.view;

import com.hisfrontend.domain.dto.UserDto;
import com.hisfrontend.view.PagesContent.*;
import com.hisfrontend.view.PagesContent.AppLayoutBasic;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.selection.SingleSelect;
import com.vaadin.flow.router.Route;

@Route("/AdministrationPanel")
public class AdministrationPanelPage extends VerticalLayout {
    private static final String PAGE_NAME = "ADMINISTRATION PANEL";
    private Dialog createDialog;
    private Dialog deleteDialog;
    private Dialog editDialog;
    private Dialog passwordChangeDialog;
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

        ComponentEventListener<ClickEvent<MenuItem>> passwordButtonListener = (e -> {
            SingleSelect<Grid<UserDto>, UserDto> userSelect = userListGrid.getGrid().asSingleSelect();
            UserDto userDto = userSelect.getValue();
            if(!userSelect.isEmpty()) {
                this.passwordChangeDialog = new UserPasswordChangeDialog(userListGrid, userDto);
                this.passwordChangeDialog.open();
            }
        });

        MenuBar menuBar = new MenuBar();
        menuBar.addItem("Create", registerButtonListener);
        menuBar.addItem("Edit", editButtonListener);
        menuBar.addItem("Delete", deleteButtonListener);
        menuBar.addItem("Password Change", passwordButtonListener);
        add(menuBar);
    }
}
