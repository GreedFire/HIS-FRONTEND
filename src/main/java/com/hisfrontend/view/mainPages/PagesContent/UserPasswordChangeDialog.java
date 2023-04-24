package com.hisfrontend.view.mainPages.PagesContent;

import com.hisfrontend.UrlGenerator;
import com.hisfrontend.domain.dto.UserDto;
import com.hisfrontend.view.LoginPage;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.StringLengthValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

public class UserPasswordChangeDialog extends Dialog {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginPage.class);
    private final Binder<UserDto> binder = new Binder<>();
    PasswordField passwordNewField = new PasswordField("New Password");
    PasswordField passwordConfirmField = new PasswordField("Confirm Password");

    public UserPasswordChangeDialog(UserListGrid userListGrid, UserDto userDto) {
        //Dialog Header
        Button closeButton = new Button(new Icon("lumo", "cross"), e -> this.close());
        this.setHeaderTitle("PASSWORD CHANGE");
        this.getHeader().add(closeButton);

        //HTML Elements
        Label label = new Label("User: " + userDto.getName() + " " + userDto.getSurname());

        VerticalLayout layout = new VerticalLayout(label, passwordNewField, passwordConfirmField);
        Button changePasswordButton = new Button("CHANGE PASSWORD");

        this.add(layout);
        this.getFooter().add(changePasswordButton);

            //BINDER
            binder.forField(passwordConfirmField)
                    .withValidator(new StringLengthValidator(
                            "Invalid password length", 8, 30))
                    .asRequired("")
                    .bind(UserDto::getPassword, UserDto::setPassword);
            binder.forField(passwordNewField)
                    .asRequired("")
                    .withValidator(new StringLengthValidator(
                            "Invalid password length", 8, 30));

            changePasswordButtonLogic(userListGrid, userDto, changePasswordButton);
    }

    private void changePasswordButtonLogic(UserListGrid userListGrid, UserDto userDto, Button button){
        //LOGIC
        button.addClickListener(e -> {
            if(passwordNewField.getValue().equals(passwordConfirmField.getValue()) && binder.validate().isOk()) {
                try {
                    binder.writeBean(userDto);
                } catch (ValidationException error) {
                    System.out.println("Failed to bind user data in password change dialog window: " + error);
                }
                HttpEntity<UserDto> request = new HttpEntity<>(userDto, new HttpHeaders());
                LOGGER.info("Trying to change user password: " + userDto);
                restTemplate.put(UrlGenerator.UPDATE_USER_PASSWORD, request);
                LOGGER.info("Password changed for: " + userDto);
                Notification.show("Password changed for " + userDto.getName() + " " + userDto.getSurname()
                        , 3000, Notification.Position.MIDDLE);
                this.close();

                userListGrid.updateList();
            }
            else if (!binder.validate().isOk())
                Notification.show("Something is wrong! ", 10000, Notification.Position.TOP_CENTER);

        });

    }

}
