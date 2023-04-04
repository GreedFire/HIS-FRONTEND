package com.hisfrontend.view.login;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Route
public class LoginPage extends VerticalLayout {
    Label hisLabel = new Label("HIS SYSTEM");
    TextField usernameField = new TextField("Username: ");
    TextField passwordField = new TextField("Password: ");
    Button loginBtn = new Button("ZALOGUJ");
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginPage.class);
    public LoginPage() {
        addStyle();
        add(hisLabel, usernameField, passwordField, loginBtn);

        loginBtn.addClickListener(event -> {
            LOGGER.info("Próba zalogowania");
            Notification.show("JESZCZE NIE DZIAŁA");
        });
    }

    private void addStyle(){
        hisLabel.getStyle().set("color", "blue");
        hisLabel.getStyle().set("font-size", "100px");
        hisLabel.getStyle().set("font-weight", "bold");
        hisLabel.getStyle().set("margin", "auto");
        usernameField.getStyle().set("margin", "auto");
        passwordField.getStyle().set("margin", "auto");
        loginBtn.getStyle().set("margin", "auto");
        passwordField.setRequiredIndicatorVisible(true);
        usernameField.setRequiredIndicatorVisible(true);
    }
}
