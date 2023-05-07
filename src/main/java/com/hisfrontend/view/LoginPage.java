package com.hisfrontend.view;

import com.hisfrontend.UrlGenerator;
import com.hisfrontend.domain.dto.UserDto;
import com.hisfrontend.view.PagesContent.AppLayoutBasic;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.awt.*;

@Route("/")
public class LoginPage extends VerticalLayout {
//    private final LoginOverlay loginOverlay;
//    private final LoginForm loginForm;
    private Button loginBtn = new Button("Login");
    private RestTemplate restTemplate = new RestTemplate();
    private TextField usernameField = new TextField("Username");
    private PasswordField passwordField = new PasswordField("Password");
    private H1 titleLabel = new H1("HIS SYSTEM");

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginPage.class);
    public LoginPage() {
        Icon healthIcon =  new Icon("vaadin", "plus-square-o");
        healthIcon.getStyle().set("color", "red");
        healthIcon.getStyle().set("font-size", "40px");
        HorizontalLayout titleLayout = new HorizontalLayout();
        titleLayout.add(healthIcon,titleLabel);
        this.getStyle().set("display", "grid");
        this.getStyle().set("place-items", "center");

        add(titleLayout, usernameField, passwordField, loginBtn);

        loginBtnLogic();

        // NEW LOGIN WITH SPRING SECURITY

//            loginOverlay = new LoginOverlay();
//            loginOverlay.setTitle("HIS SYSTEM");
//            loginOverlay.setDescription("Authors: Dawid Majchrzak, Anna Nowacka");
//            loginOverlay.setOpened(true);
//            loginForm = new LoginForm();
//            loginForm.getElement().getThemeList().add("dark");
//            loginForm.getElement().setAttribute("no-autofocus", "");
//            add(loginOverlay, loginForm);

        //===================================================================================================
        //TYMCZASOWE LOGOWANIE - DO USUNIĘCIA POTEM
        //===================================================================================================

//        addAttachListener(e -> {
//            LOGGER.info("TESTOWE LOGOWANIE");
//            UI.getCurrent().setId(Long.toString(1));
//            AppLayoutBasic.setUsernameLabel(new Label("Logged: " + "TEST"+ " " + "USER"));
//            UI.getCurrent().navigate(MainMenu.class);
//            loginOverlay.setOpened(false);
//        });

            //loginBtnLogic();
        //===================================================================================================
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
                    AppLayoutBasic.setUsernameLabel(new Label("Zalogowany: " + userDto.getName() + " " + userDto.getSurname()));
                }

                UI.getCurrent().navigate(MainMenu.class);
                LOGGER.info("Logged in user with id: " + id);
            } else {
                LOGGER.info("Login failed - username:  " + usernameField.getValue());
                Notification.show("Nie udało się zalogować");
            }
        });


    }


}
