package com.hisfrontend.view.mainPages;

import com.hisfrontend.UrlGenerator;
import com.hisfrontend.domain.dto.PatientDto;
import com.hisfrontend.view.LoginPage;
import com.hisfrontend.view.mainPages.PagesContent.PatientListGrid;
import com.hisfrontend.view.staticContent.AppLayoutBasic;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private Dialog registerDialog;
    private PatientListGrid patientListGrid;

    public RegistrationPage(){
        initClassFields();
        add(AppLayoutBasic.drawAppLayout(PAGE_NAME));
        registerDialogLogic();
        add(patientListGrid.getTopMenu());
        add(patientListGrid.getGrid());
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

    private void registerDialogLogic(){
        //HTML
        Button closeButton = new Button(new Icon("lumo", "cross"), e -> registerDialog.close());
        registerDialog.setHeaderTitle("PATIENT REGISTRATION");
        registerDialog.getHeader().add(closeButton);
        //FORM VADIN COMPONENT???
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
                patientListGrid.getGrid().setItems(list);

            }
            else {
                LOGGER.info("Failed to register patient");
                Notification.show("There was a problem while registering. Please try again later.", 3000, Notification.Position.MIDDLE);
            }
        });
    }


    private void initClassFields(){
        registerDialog = new Dialog();
        restTemplate = new RestTemplate();
        patientListGrid = new PatientListGrid();
    }
}


