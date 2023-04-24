package com.hisfrontend.view.PagesContent;

import com.hisfrontend.UrlGenerator;
import com.hisfrontend.domain.dto.UserDto;
import com.hisfrontend.view.LoginPage;
import com.vaadin.flow.component.button.Button;
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

public class UserCreateDialog extends Dialog{
    private final RestTemplate restTemplate = new RestTemplate();
    private static final Logger LOGGER = LoggerFactory.getLogger(UserCreateDialog.class);
    private final Binder<UserDto> binder = new Binder<>();

        public UserCreateDialog(UserListGrid userListGrid) {
            //Dialog Header
            Button closeButton = new Button(new Icon("lumo", "cross"), e -> this.close());
            this.setHeaderTitle("USER CREATOR");
            this.getHeader().add(closeButton);

            //HTML Elements
            TextField usernameField = new TextField("Username: ");
            TextField firstnameField = new TextField("Firstname: ");
            TextField lastnameField = new TextField("Lastname: ");
            Button createDialogBtn = new Button("CREATE USER");

            //Setting HTML elements;
            usernameField.setPlaceholder("Enter username");
            firstnameField.setPlaceholder("Enter firstname");
            lastnameField.setPlaceholder("Enter lastname");

            //FormLayout
            FormLayout formLayout = new FormLayout();
            formLayout.add(usernameField, firstnameField, lastnameField);
            formLayout.setResponsiveSteps(
                    new FormLayout.ResponsiveStep("0", 1),
                    new FormLayout.ResponsiveStep("500px", 3));

            this.add(formLayout);
            this.getFooter().add(createDialogBtn);

            //BINDER
            UserDto userDto = new UserDto();
            binder.forField(firstnameField)
                    .withValidator(new StringLengthValidator(
                            "Invalid firstname length", 2, 30))
                    .bind(UserDto::getName, UserDto::setName);
            binder.forField(lastnameField)
                    .withValidator(new StringLengthValidator(
                            "Invalid lastname length", 2, 30))
                    .bind(UserDto::getSurname, UserDto::setSurname);
            binder.forField(usernameField)
                    .withValidator(new StringLengthValidator(
                            "Invalid lastname length", 3, 30))
                    .bind(UserDto::getUsername, UserDto::setUsername);
            userDto.setSignedIn(false);

            //Button Logic
            createButtonLogic(userListGrid, userDto, createDialogBtn);
        }

        private void createButtonLogic(UserListGrid userListGrid, UserDto userDto, Button actionButton){
            //LOGIC
            actionButton.addClickListener(e -> {
                try {
                    binder.writeBean(userDto);
                } catch (ValidationException error) {
                    System.out.println("Failed to bind user data in create dialog window: " + error);
                }
                HttpEntity<UserDto> request = new HttpEntity<>(userDto, new HttpHeaders());
                LOGGER.info("Trying to create user: " + userDto);
                Boolean registerValidator = restTemplate.postForObject(UrlGenerator.CREATE_USER, request, Boolean.class);
                if(registerValidator && binder.validate().isOk()) {
                    LOGGER.info("User Registered: " + userDto);
                    Notification.show("\n" + "User creation was successful!", 3000, Notification.Position.MIDDLE);
                    this.close();
                    userListGrid.updateList();
                }
                else {
                    LOGGER.info("Failed to create user: " + userDto);
                    Notification.show("There was a problem while registering. Please try again later.", 3000, Notification.Position.MIDDLE);
                }
            });
        }

}
