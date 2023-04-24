package com.hisfrontend.view.mainPages.PagesContent;

import com.hisfrontend.UrlGenerator;
import com.hisfrontend.domain.dto.PatientDto;
import com.hisfrontend.view.LoginPage;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.StringLengthValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class PatientEditDialog extends Dialog{
    private final RestTemplate restTemplate = new RestTemplate();
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginPage.class);
    private final Binder<PatientDto> binder = new Binder<>();

        public PatientEditDialog(PatientListGrid patientListGrid, PatientDto patientDto) {
            //Dialog Header
            Button closeButton = new Button(new Icon("lumo", "cross"), e -> this.close());
            this.setHeaderTitle("PATIENT EDIT");
            this.getHeader().add(closeButton);

            //HTML Elements
            TextField firstnameField = new TextField("Firstname: ");
            TextField surnameField = new TextField("Lastname: ");
            TextField peselField = new TextField("PESEL: ");
            DateTimePicker scheduledDateField = new DateTimePicker("Scheduled Date: ");
            ComboBox<String> sexField = new ComboBox<>("Sex: ");
            Button registerDialogBtn = new Button("UPDATE");

            //Setting HTML elements
            scheduledDateField.setValue(LocalDateTime.now(ZoneId.systemDefault()));
            sexField.setItems("Male", "Female");
            firstnameField.setPlaceholder("Enter firstname");
            surnameField.setPlaceholder("Enter lastname");
            peselField.setPlaceholder("Enter PESEL");
            sexField.setPlaceholder("Pick sex");
            firstnameField.setValue(patientDto.getFirstname());
            surnameField.setValue(patientDto.getSurname());
            peselField.setValue(patientDto.getPesel());
            sexField.setValue(patientDto.getSex());
            scheduledDateField.setValue(patientDto.getScheduledDate());

            //FormLayout
            FormLayout formLayout = new FormLayout();
            formLayout.add(firstnameField, surnameField, peselField,
                    sexField, scheduledDateField);
            formLayout.setResponsiveSteps(
                    new FormLayout.ResponsiveStep("0", 1),
                    new FormLayout.ResponsiveStep("500px", 3));

            this.add(formLayout);
            this.getFooter().add(registerDialogBtn);

            //BINDER
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

            //Button Logic
            registerButtonLogic(patientListGrid, patientDto, registerDialogBtn);
        }

        private void registerButtonLogic(PatientListGrid patientListGrid, PatientDto patientDto, Button actionButton){
            //LOGIC
            actionButton.addClickListener(e -> {
                try {
                    binder.writeBean(patientDto);
                } catch (ValidationException error) {
                    System.out.println("Failed to bind patient data in register dialog window: " + error);
                }
                HttpEntity<PatientDto> request = new HttpEntity<>(patientDto, new HttpHeaders());
                LOGGER.info("Trying to update patient: " + patientDto);
                restTemplate.put(UrlGenerator.UPDATE_PATIENT, request);
                if(binder.validate().isOk()) {
                    LOGGER.info("Patient Updated: " + patientDto);
                    Notification.show("\n" + "Patient data update was successful!", 3000, Notification.Position.MIDDLE);
                    this.close();
                    patientListGrid.updateList();
                }
                else {
                    LOGGER.info("Failed to update patient");
                    Notification.show("There was a problem while updating patient. Please try again later.", 3000, Notification.Position.MIDDLE);
                }
            });
        }

}
