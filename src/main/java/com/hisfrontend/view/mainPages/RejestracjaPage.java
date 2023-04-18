package com.hisfrontend.view.mainPages;

import com.hisfrontend.UrlGenerator;
import com.hisfrontend.domain.dto.PatientDto;
import com.hisfrontend.view.LoginPage;
import com.hisfrontend.view.staticContent.NavigatePanel;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ValidationException;
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

import java.util.List;

@Route("/Rejestracja")
public class RejestracjaPage extends VerticalLayout {
    @Autowired
    private RestTemplate restTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginPage.class);
    private static final String PAGE_NAME = "REJESTRACJA";
    private Button registerPatientBtn = new Button("Zarejestruj");

    private HorizontalLayout topMenu = new HorizontalLayout();

    private Dialog registerDialog = new Dialog();
    Grid<PatientDto> grid = new Grid<>(PatientDto.class, false);


    public RejestracjaPage(){
        add(NavigatePanel.drawNavigatePanel(PAGE_NAME));

        dialogLogic();
        patientListView();

        registerPatientBtn.addClickListener(e -> {
            registerDialog.open();
        });
    }

    private void patientListView(){
        RestTemplate restTemplate1 = new RestTemplate();
        ResponseEntity<List<PatientDto>> responseList = restTemplate1.exchange(
                UrlGenerator.GET_PATIENTS, HttpMethod.GET, null, new ParameterizedTypeReference<List<PatientDto>>() {
                });
        List<PatientDto> list = responseList.getBody();
        Grid.Column<PatientDto> firstnameColumn = grid.addColumn(PatientDto::getFirstname)
                .setHeader("Imię")
                .setFooter(String.format("%s pozycji", list.size()))
                .setSortable(true);
        Grid.Column<PatientDto> surnameColumn = grid.addColumn(PatientDto::getSurname)
                .setHeader("Nazwisko")
                .setSortable(true);
        Grid.Column<PatientDto> peselColumn = grid.addColumn(PatientDto::getPesel)
                .setHeader("PESEL")
                .setSortable(true);

        GridListDataView<PatientDto> dataView = grid.setItems(list);

        Button showColumnsButton = new Button("Pokaż/Ukryj kolumny");
        showColumnsButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        ColumnToggleContextMenu columnToggleContextMenu = new ColumnToggleContextMenu(
                showColumnsButton);
        columnToggleContextMenu.addColumnToggleItem("Imię",
                firstnameColumn);
        columnToggleContextMenu.addColumnToggleItem("Nazwisko",
                surnameColumn);
        columnToggleContextMenu.addColumnToggleItem("PESEL",
                peselColumn);

        TextField searchField = new TextField();
        searchField.setWidth("50%");
        searchField.setPlaceholder("Szukaj");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(e -> dataView.refreshAll());

        dataView.addFilter(patient -> {
            String searchTerm = searchField.getValue().trim();
            if (searchTerm.isEmpty())
                return true;

            boolean matchesFirstName = matchesTerm(patient.getFirstname(),
                    searchTerm);
            boolean matchesSurname = matchesTerm(patient.getSurname(), searchTerm);
            boolean matchesPesel = matchesTerm(patient.getPesel(),
                    searchTerm);

            return matchesFirstName || matchesSurname || matchesPesel;
        });

        topMenu.add(searchField, showColumnsButton);
        add(topMenu, grid, registerPatientBtn);
    }

    private boolean matchesTerm(String value, String searchTerm) {
        return value.toLowerCase().contains(searchTerm.toLowerCase());
    }

    private void dialogLogic(){
        //HTML
        Button closeButton = new Button(new Icon("lumo", "cross"), e -> registerDialog.close());
        registerDialog.setHeaderTitle("REJESTRACJA");
        registerDialog.getHeader().add(closeButton);
        VerticalLayout dialogLayout = new VerticalLayout();
        TextField firstnameField = new TextField("Imię: ");
        TextField surnameField = new TextField("Nazwisko: ");
        TextField peselField = new TextField("PESEL: ");
        firstnameField.setPlaceholder("Podaj imię");
        surnameField.setPlaceholder("Podaj nazwisko");
        peselField.setPlaceholder("Podaj PESEL");
        Button registerDialogBtn = new Button("Zarejestruj");
        registerDialog.getFooter().add(registerDialogBtn);
        dialogLayout.add(firstnameField, surnameField, peselField);
        registerDialog.add(dialogLayout);

        //BINDER
        PatientDto patientDto = new PatientDto();
        Binder<PatientDto> binder = new Binder<>();
        binder.forField(firstnameField)
                .withValidator(new StringLengthValidator(
                        "Niepoprawna długość imienia", 2, 30))
                .bind(PatientDto::getFirstname, PatientDto::setFirstname);
        binder.forField(surnameField)
                .withValidator(new StringLengthValidator(
                        "Niepoprawna długość nazwiska", 2, 30))
                .bind(PatientDto::getSurname, PatientDto::setSurname);
        binder.forField(peselField)
                .bind(PatientDto::getPesel, PatientDto::setPesel);

        //LOGIC
        registerDialogBtn.addClickListener(e -> {
            try {
                binder.writeBean(patientDto);
            } catch (ValidationException error) {
                System.out.println("Failed to bind patient data in register dialog window: " + e);
            }
            HttpEntity<PatientDto> request = new HttpEntity<>(patientDto, new HttpHeaders());
            Boolean registerValidator = false;
            LOGGER.info("Trying to register patient: " + patientDto);
            registerValidator = restTemplate.postForObject(UrlGenerator.REGISTER_PATIENT, request, Boolean.class);
            if(registerValidator && binder.validate().isOk()) {
                LOGGER.info("Patient Registered");
                Notification.show("Rejestracja przebiegła pomyślnie!", 3000, Notification.Position.MIDDLE);
                registerDialog.close();

                ResponseEntity<List<PatientDto>> responseList = restTemplate.exchange(
                        UrlGenerator.GET_PATIENTS, HttpMethod.GET, null, new ParameterizedTypeReference<List<PatientDto>>() {
                        });
                List<PatientDto> list = responseList.getBody();
                grid.setItems(list);
            }
            else {
                LOGGER.info("Failed to register patient");
                Notification.show("Wystąpił problem podczas rejestracji. Spróbuj ponownie później.", 3000, Notification.Position.MIDDLE);
            }
        });
    }


    private static class ColumnToggleContextMenu extends ContextMenu {
        public ColumnToggleContextMenu(Component target) {
            super(target);
            setOpenOnClick(true);
        }

        void addColumnToggleItem(String label, Grid.Column<PatientDto> column) {
            MenuItem menuItem = this.addItem(label, e -> {
                column.setVisible(e.getSource().isChecked());
            });
            menuItem.setCheckable(true);
            menuItem.setChecked(column.isVisible());
        }
    }

}


