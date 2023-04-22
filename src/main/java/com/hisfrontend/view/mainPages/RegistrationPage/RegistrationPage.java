package com.hisfrontend.view.mainPages.RegistrationPage;

import com.hisfrontend.UrlGenerator;
import com.hisfrontend.domain.dto.PatientDto;
import com.hisfrontend.view.LoginPage;
import com.hisfrontend.view.staticContent.AppLayoutBasic;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.vaadin.flow.data.binder.Binder;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Route("/Registration")
public class RegistrationPage extends VerticalLayout{
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginPage.class);
    private static final String PAGE_NAME = "REGISTRATION";
    private RestTemplate restTemplate;
    private HorizontalLayout topMenu;
    private Dialog registerDialog;
    private Grid<PatientDto> grid;

    public RegistrationPage(){
        initClassFields();
        add(AppLayoutBasic.drawAppLayout(PAGE_NAME));
        registerDialogLogic();
        drawPatientListView();
        drawMenuBar();
    }

    private void drawMenuBar(){
        ComponentEventListener<ClickEvent<MenuItem>> registerListener = e -> registerDialog.open();
        MenuBar menuBar = new MenuBar();
        menuBar.addItem("Register", registerListener);
        menuBar.addItem("Edit");
        menuBar.addItem("Delete");
        add(menuBar);
    }

    private void drawPatientListView(){
        RestTemplate restTemplate1 = new RestTemplate();
        ResponseEntity<List<PatientDto>> responseList = restTemplate1.exchange(
                UrlGenerator.GET_PATIENTS, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });
        List<PatientDto> patientDtoList = responseList.getBody();
        gridFunctionality(patientDtoList);
    }

    private void registerDialogLogic(){
        //HTML
        Button closeButton = new Button(new Icon("lumo", "cross"), e -> registerDialog.close());
        registerDialog.setHeaderTitle("PATIENT REGISTRATION");
        registerDialog.getHeader().add(closeButton);
        VerticalLayout dialogLayout = new VerticalLayout();
        TextField firstnameField = new TextField("Firstname: ");
        TextField surnameField = new TextField("Lastname: ");
        TextField peselField = new TextField("PESEL: ");
        DateTimePicker scheduledDateField = new DateTimePicker("Scheduled Date: ");
        scheduledDateField.setValue(LocalDateTime.now(ZoneId.systemDefault()));
        ComboBox<String> sexField = new ComboBox<>("Sex: ");
        sexField.setItems("Male", "Female");
        firstnameField.setPlaceholder("Enter firstname");
        surnameField.setPlaceholder("Enter lastname");
        peselField.setPlaceholder("Enter PESEL");
        sexField.setPlaceholder("Pick sex");
        Button registerDialogBtn = new Button("Register");
        registerDialog.getFooter().add(registerDialogBtn);
        dialogLayout.add(firstnameField, surnameField, sexField, peselField, scheduledDateField);
        registerDialog.add(dialogLayout);

        //BINDER
        PatientDto patientDto = new PatientDto();
        Binder<PatientDto> binder = new Binder<>();
        binder.forField(firstnameField)
                .withValidator(new StringLengthValidator(
                        "Invalid firstname length", 2, 30))
                .bind(PatientDto::getFirstname, PatientDto::setFirstname);
        binder.forField(surnameField)
                .withValidator(new StringLengthValidator(
                        "Invalid lastname length", 2, 30))
                .bind(PatientDto::getSurname, PatientDto::setSurname);
        binder.forField(peselField)
                .bind(PatientDto::getPesel, PatientDto::setPesel);
        binder.forField(sexField)
                .bind(PatientDto::getSex, PatientDto::setSex);
        binder.forField(scheduledDateField)
                .bind(PatientDto::getScheduledDate, PatientDto::setScheduledDate);
        patientDto.setStatus("registered");

        //LOGIC
        registerDialogBtn.addClickListener(e -> {
            try {
                binder.writeBean(patientDto);
            } catch (ValidationException error) {
                System.out.println("Failed to bind patient data in register dialog window: " + e);
            }
            HttpEntity<PatientDto> request = new HttpEntity<>(patientDto, new HttpHeaders());
            LOGGER.info("Trying to register patient: " + patientDto);
            Boolean registerValidator = restTemplate.postForObject(UrlGenerator.REGISTER_PATIENT, request, Boolean.class);
            if(registerValidator && binder.validate().isOk()) {
                LOGGER.info("Patient Registered");
                Notification.show("\n" + "Registration was successful!", 3000, Notification.Position.MIDDLE);
                registerDialog.close();

                ResponseEntity<List<PatientDto>> responseList = restTemplate.exchange(
                        UrlGenerator.GET_PATIENTS, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
                List<PatientDto> list = responseList.getBody();
                grid.setItems(list);

            }
            else {
                LOGGER.info("Failed to register patient");
                Notification.show("There was a problem while registering. Please try again later.", 3000, Notification.Position.MIDDLE);
            }
        });
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
    private void gridFunctionality(List<PatientDto> list){
        GridListDataView<PatientDto> dataView = grid.setItems(list);
        grid.setColumnReorderingAllowed(true);
        PatientContextMenu contextMenu = new PatientContextMenu(grid);

        Grid.Column<PatientDto> statusColumn = grid
                .addComponentColumn(patient -> createStatusBadge(patient.getStatus()))
                .setHeader("Status")
                .setFooter(String.format("%s items", list.size()))
                .setSortable(true)
                .setResizable(true);
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
        ColumnToggleContextMenu columnToggleContextMenu = new ColumnToggleContextMenu(
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

        topMenu.add(searchField, showColumnsButton);
        add(topMenu, grid);
    }

    private boolean matchesTerm(String value, String searchTerm) {
        return value.toLowerCase().contains(searchTerm.toLowerCase());
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
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private void initClassFields(){
        grid = new Grid<>(PatientDto.class, false);
        topMenu = new HorizontalLayout();
        registerDialog = new Dialog();
    }

    private static class PatientContextMenu extends GridContextMenu<PatientDto> {
        private Dialog editPatientDialog = new Dialog();
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
}


