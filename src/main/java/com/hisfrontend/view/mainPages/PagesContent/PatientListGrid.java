package com.hisfrontend.view.mainPages.PagesContent;

import com.hisfrontend.UrlGenerator;
import com.hisfrontend.domain.dto.PatientDto;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import lombok.Getter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Getter
public class PatientListGrid extends VerticalLayout {
    private final Grid<PatientDto> grid = new Grid<>(PatientDto.class, false);
    private final HorizontalLayout topMenu = new HorizontalLayout();

    public PatientListGrid(){
        drawPatientListView();
    }
    private void drawPatientListView(){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<PatientDto>> responseList = restTemplate.exchange(
                UrlGenerator.GET_PATIENTS, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });
        List<PatientDto> patientDtoList = responseList.getBody();
        gridFunctionality(patientDtoList);
        gridColumnsSetup(patientDtoList);
    }

    private void gridColumnsSetup(List<PatientDto> list){
        Grid.Column<PatientDto> statusColumn = grid
                .addComponentColumn(patient -> createStatusBadge(patient.getStatus()))
                .setHeader("Status")
                .setSortable(true)
                .setResizable(true);

//        if(list.isEmpty())
//            statusColumn = grid.getColumnByKey("Status").setFooter(String.format("%s items", 0));
//        else grid.getColumnByKey("Status").setFooter(String.format("%s items", list.size()));


        Grid.Column<PatientDto> scheduledDateColumn = grid.addColumn(new LocalDateTimeRenderer<>(PatientDto::getScheduledDate,"dd/MM/YYYY HH:mm"))
                .setHeader("Scheduled Date")
                .setSortable(true)
                .setResizable(true);
        Grid.Column<PatientDto> firstnameColumn = grid.addColumn(PatientDto::getFirstname)
                .setHeader("Firstname")
                .setSortable(true)
                .setResizable(true);
        Grid.Column<PatientDto> surnameColumn = grid.addColumn(PatientDto::getSurname)
                .setHeader("Lastname")
                .setSortable(true)
                .setResizable(true);
        Grid.Column<PatientDto> peselColumn = grid.addColumn(PatientDto::getPesel)
                .setHeader("PESEL")
                .setSortable(true)
                .setResizable(true);
        Grid.Column<PatientDto> idColumn = grid.addColumn(PatientDto::getId)
                .setHeader("Patient ID")
                .setSortable(true)
                .setResizable(true);
        Grid.Column<PatientDto> sexColumn = grid.addColumn(PatientDto::getSex)
                .setHeader("Sex")
                .setSortable(true)
                .setResizable(true);
        Grid.Column<PatientDto> registrationDateColumn = grid.addColumn(new LocalDateTimeRenderer<>(PatientDto::getRegistrationDate,"dd/MM/YYYY HH:mm"))
                .setHeader("Registration Date")
                .setSortable(true)
                .setResizable(true);

        Button showColumnsButton = new Button("Show/Hide columns");
        showColumnsButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        PatientListGrid.ColumnToggleContextMenu columnToggleContextMenu = new PatientListGrid.ColumnToggleContextMenu(
                showColumnsButton);
        columnToggleContextMenu.addColumnToggleItem("Patient ID",
                idColumn);
        columnToggleContextMenu.addColumnToggleItem("Firstname",
                firstnameColumn);
        columnToggleContextMenu.addColumnToggleItem("Lastname",
                surnameColumn);
        columnToggleContextMenu.addColumnToggleItem("PESEL",
                peselColumn);
        columnToggleContextMenu.addColumnToggleItem("Sex",
                sexColumn);
        columnToggleContextMenu.addColumnToggleItem("Status",
                statusColumn);
        columnToggleContextMenu.addColumnToggleItem("Registration Date",
                registrationDateColumn);
        columnToggleContextMenu.addColumnToggleItem("Scheduled Date",
                scheduledDateColumn);

        topMenu.add(showColumnsButton);
    }
    private void gridFunctionality(List<PatientDto> list){
        GridListDataView<PatientDto> dataView = grid.setItems(list);
        grid.setColumnReorderingAllowed(true);
        new PatientListGrid.PatientContextMenu(grid);

        TextField searchField = new TextField();
        searchField.setPlaceholder("Name, PESEL");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(e -> dataView.refreshAll());

        dataView.addFilter(patient -> {
            String searchTerm = searchField.getValue().trim();
            if (searchTerm.isEmpty())
                return true;

            boolean matchesFirstName = matchesTerm(patient.getFirstname(), searchTerm);
            boolean matchesSurname = matchesTerm(patient.getSurname(), searchTerm);
            boolean matchesPesel = matchesTerm(patient.getPesel(), searchTerm);
            return matchesFirstName || matchesSurname || matchesPesel;
        });

        topMenu.add(searchField);
    }

    private Span createStatusBadge(String status) {
        String theme = switch (status) {
            case "registered" -> "badge contrast";
            case "admitted" -> "badge success";
            case "canceled" -> "badge error";
            default -> "badge";
        };
        Span badge = new Span(status);
        badge.getElement().getThemeList().add(theme);
        return badge;
    }

    private boolean matchesTerm(String value, String searchTerm) {
        return value.toLowerCase().contains(searchTerm.toLowerCase());
    }

    private static class PatientContextMenu extends GridContextMenu<PatientDto> {
        private final Dialog editPatientDialog = new Dialog();
        public PatientContextMenu(Grid<PatientDto> target) {
            super(target);

            addItem("Edit", e -> e.getItem().ifPresent(PatientDto -> {
                editPatientDialog.setHeaderTitle("Edit Dialog");
                editPatientDialog.open();
            }));
            addItem("Delete", e -> e.getItem().ifPresent(PatientDto -> {
                // System.out.printf("Delete: %s%n", person.getFullName());
            }));
        }
    }

    private static class ColumnToggleContextMenu extends ContextMenu {
        public ColumnToggleContextMenu(Component target) {
            super(target);
            setOpenOnClick(true);
        }

        void addColumnToggleItem(String label, Grid.Column<PatientDto> column) {
            MenuItem menuItem = this.addItem(label, e -> column.setVisible(e.getSource().isChecked()));
            menuItem.setCheckable(true);
            menuItem.setChecked(column.isVisible());
        }
    }
}
