package com.hisfrontend.view.mainPages.PagesContent;

import com.hisfrontend.UrlGenerator;
import com.hisfrontend.domain.dto.PatientDto;
import com.hisfrontend.domain.dto.UserDto;
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

public class UserEditDialog extends Dialog{
    private final RestTemplate restTemplate = new RestTemplate();
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginPage.class);
    private final Binder<UserDto> binder = new Binder<>();

        public UserEditDialog(UserListGrid userListGrid, UserDto userDto) {
            //Dialog Header
            Button closeButton = new Button(new Icon("lumo", "cross"), e -> this.close());
            this.setHeaderTitle("USER EDIT");
            this.getHeader().add(closeButton);

            //HTML Elements
            TextField firstnameField = new TextField("Firstname: ");
            TextField surnameField = new TextField("Lastname: ");
            TextField usernameField = new TextField("Username: ");
            Button updateDialogBtn = new Button("UPDATE");

            //Setting HTML elements

            firstnameField.setPlaceholder("Enter firstname");
            surnameField.setPlaceholder("Enter lastname");
            usernameField.setPlaceholder("Enter username");
            usernameField.setValue(userDto.getUsername());
            surnameField.setValue(userDto.getSurname());
            firstnameField.setValue(userDto.getName());


            //FormLayout
            FormLayout formLayout = new FormLayout();
            formLayout.add(usernameField, firstnameField, surnameField);
            formLayout.setResponsiveSteps(
                    new FormLayout.ResponsiveStep("0", 1),
                    new FormLayout.ResponsiveStep("500px", 3));

            this.add(formLayout);
            this.getFooter().add(updateDialogBtn);

            //BINDER
            binder.forField(firstnameField)
                    .withValidator(new StringLengthValidator(
                            "Invalid firstname length", 2, 30))
                    .bind(UserDto::getName, UserDto::setName);
            binder.forField(surnameField)
                    .withValidator(new StringLengthValidator(
                            "Invalid lastname length", 2, 30))
                    .bind(UserDto::getSurname, UserDto::setSurname);
            binder.forField(usernameField)
                    .withValidator(new StringLengthValidator(
                            "Invalid lastname length", 3, 30))
                    .bind(UserDto::getUsername, UserDto::setUsername);

            //Button Logic
            updateButtonLogic(userListGrid, userDto, updateDialogBtn);
        }

        private void updateButtonLogic(UserListGrid userListGrid, UserDto userDto, Button actionButton){
            //LOGIC
            actionButton.addClickListener(e -> {
                try {
                    binder.writeBean(userDto);
                } catch (ValidationException error) {
                    System.out.println("Failed to bind user data in update dialog window: " + error);
                }
                HttpEntity<UserDto> request = new HttpEntity<>(userDto, new HttpHeaders());
                LOGGER.info("Trying to update user: " + userDto);
                restTemplate.put(UrlGenerator.UPDATE_USER, request);
                if(binder.validate().isOk()) {
                    LOGGER.info("User Updated: " + userDto);
                    Notification.show("\n" + "User data update was successful!", 3000, Notification.Position.MIDDLE);
                    this.close();
                    userListGrid.updateList();
                }
                else {
                    LOGGER.info("Failed to update user" + userDto);
                    Notification.show("There was a problem while updating patient. Please try again later.", 3000, Notification.Position.MIDDLE);
                }
            });
        }

}
