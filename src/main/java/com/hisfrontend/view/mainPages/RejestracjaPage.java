package com.hisfrontend.view.mainPages;

import com.hisfrontend.UrlGenerator;
import com.hisfrontend.domain.dto.PatientDto;
import com.hisfrontend.view.LoginPage;
import com.hisfrontend.view.staticContent.NavigatePanel;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.StringLengthValidator;
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

    private HorizontalLayout bottomMenu = new HorizontalLayout();
    private Dialog registerDialog = new Dialog();
    Grid<PatientDto> grid = new Grid<>(PatientDto.class, false);


    public RejestracjaPage(){
        bottomMenu.add(registerPatientBtn);
        add(NavigatePanel.drawNavigatePanel(PAGE_NAME), bottomMenu);
        createStyles();
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
        grid.addColumn(PatientDto::getFirstname).setHeader("First name");
        grid.addColumn(PatientDto::getSurname).setHeader("Last name");
        grid.addColumn(PatientDto::getPesel).setHeader("PESEL");
        grid.setItems(list);
        add(grid);
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
        firstnameField.setPlaceholder("Wpisz swoje imię");
        surnameField.setPlaceholder("Wpisz swoje nazwisko");
        peselField.setPlaceholder("Wpisz swój PESEL");
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

    private void createStyles() {
        bottomMenu.getStyle().set("margin", "auto");
        bottomMenu.getStyle().set("width", "100%");
        bottomMenu.getStyle().set("clear", "both");
        bottomMenu.getStyle().set("position", "absolute");
        bottomMenu.getStyle().set("height", "40px");
        bottomMenu.getStyle().set("bottom", "0");
        registerPatientBtn.getStyle().set("padding", "40px");
        registerPatientBtn.getStyle().set("font-size", "25px");
    }
}
