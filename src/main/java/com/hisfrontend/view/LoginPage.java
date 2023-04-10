package com.hisfrontend.view;

import com.hisfrontend.UrlGenerator;
import com.hisfrontend.domain.dto.UserDto;
import com.hisfrontend.view.staticContent.DefiniedView;
import com.hisfrontend.view.staticContent.NavigatePanel;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

@Route("/")
public class LoginPage extends VerticalLayout {
    @Autowired
    private RestTemplate restTemplate;
    Label hisLabel = new Label("HIS SYSTEM");
    TextField usernameField = new TextField("Username: ");
    TextField passwordField = new TextField("Password: ");
    Button loginBtn = new Button("ZALOGUJ");
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginPage.class);
    public LoginPage() {
        addStyle();
        add(hisLabel, usernameField, passwordField, loginBtn, DefiniedView.drawFooter());

        loginBtnLogic();
    }

    private void loginBtnLogic(){
        loginBtn.addClickListener(event -> {
            LOGGER.info("Próba zalogowania");

            Long id = restTemplate.getForObject(UrlGenerator.getUserIdURL(usernameField.getValue(), passwordField.getValue()), Long.class);
            if (id != null) {
                UI.getCurrent().setId(Long.toString(id));
                if (UI.getCurrent().getId().isPresent()) {
                    restTemplate.put(UrlGenerator.userSignInURL(id), null);
                    UserDto userDto = restTemplate.getForObject(UrlGenerator.getUser(id), UserDto.class);
                    NavigatePanel.setUsernameLabel(new Label("Zalogowany: " + userDto.getName() + " " + userDto.getSurname()));

                }

                UI.getCurrent().navigate(MainMenu.class);
                LOGGER.info("Logged in user with id: " + id);
            } else {
                LOGGER.info("Login failed - username:  " + usernameField.getValue());
                Notification.show("Nie udało się zalogować");
            }
        });
    }

    private void addStyle(){
        hisLabel.getStyle().set("color", DefiniedView.HIS_COLOR);
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
