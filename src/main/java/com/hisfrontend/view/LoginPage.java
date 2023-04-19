package com.hisfrontend.view;

import com.hisfrontend.view.staticContent.AppLayoutBasic;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

@Route("/")
public class LoginPage extends VerticalLayout {
    @Autowired
    private RestTemplate restTemplate;
    private LoginOverlay loginOverlay;
    private LoginForm loginForm;

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginPage.class);
    public LoginPage() {
        // NEW LOGIN WITH SPRING SECURITY

            loginOverlay = new LoginOverlay();
            loginOverlay.setTitle("HIS SYSTEM");
            loginOverlay.setDescription("Authors: Dawid Majchrzak, Anna Nowacka");
            loginOverlay.setOpened(true);
            loginForm = new LoginForm();
            loginForm.getElement().getThemeList().add("dark");
            loginForm.getElement().setAttribute("no-autofocus", "");
            add(loginOverlay, loginForm);




        //===================================================================================================
        //TYMCZASOWE LOGOWANIE - DO USUNIĘCIA POTEM
        //===================================================================================================

        addAttachListener(e -> {
            LOGGER.info("TESTOWE LOGOWANIE");
            UI.getCurrent().setId(Long.toString(1));
            AppLayoutBasic.setUsernameLabel(new Label("Logged: " + "TEST"+ " " + "USER"));
            UI.getCurrent().navigate(MainMenu.class);
            loginOverlay.setOpened(false);
        });

            //loginBtnLogic();
        //===================================================================================================
    }


//    private void loginBtnLogic(){
//        loginBtn.addClickListener(event -> {
//            LOGGER.info("Próba zalogowania");
//            Long id = restTemplate.getForObject(UrlGenerator.getUserIdURL(usernameField.getValue(), passwordField.getValue()), Long.class);
//            if (id != null) {
//                UI.getCurrent().setId(Long.toString(id));
//                if (UI.getCurrent().getId().isPresent()) {
//                    restTemplate.put(UrlGenerator.userSignInURL(id), null);
//                    UserDto userDto = restTemplate.getForObject(UrlGenerator.getUser(id), UserDto.class);
//                    NavigatePanel.setUsernameLabel(new Label("Zalogowany: " + userDto.getName() + " " + userDto.getSurname()));
//                }
//
//                UI.getCurrent().navigate(MainMenu.class);
//                LOGGER.info("Logged in user with id: " + id);
//            } else {
//                LOGGER.info("Login failed - username:  " + usernameField.getValue());
//                Notification.show("Nie udało się zalogować");
//            }
//        });
//
//
//    }


}
