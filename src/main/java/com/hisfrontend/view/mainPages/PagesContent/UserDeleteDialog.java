package com.hisfrontend.view.mainPages.PagesContent;

import com.hisfrontend.UrlGenerator;
import com.hisfrontend.domain.dto.UserDto;
import com.hisfrontend.view.LoginPage;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

public class UserDeleteDialog extends Dialog{
    private final RestTemplate restTemplate = new RestTemplate();
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginPage.class);

    public UserDeleteDialog(UserListGrid userListGrid, UserDto userDto) {
            //Dialog Header
            Button closeButton = new Button(new Icon("lumo", "cross"), e -> this.close());
            this.setHeaderTitle("USER DELETE");
            this.getHeader().add(closeButton);

            //HTML Elements
            Label deletionQuestionLabel = new Label("You want to delete user: " + userDto.getName()
                    + " " + userDto.getSurname() + ". Are you sure?");
            Button yesButton = new Button("YES");
            Button noButton = new Button("NO");

            this.add(deletionQuestionLabel);
            this.getFooter().add(yesButton, noButton);

            noButtonLogic(noButton);
            yesButtonLogic(userListGrid, userDto, yesButton);
        }

        private void noButtonLogic(Button button){
            button.addClickListener(e -> this.close());
        }
        private void yesButtonLogic(UserListGrid userListGrid, UserDto userDto, Button button){
            //LOGIC
            button.addClickListener(e -> {
                LOGGER.info("Trying to delete patient: " + userDto);
                restTemplate.delete(UrlGenerator.userDeleteURL(userDto.getId()));
                LOGGER.info("Patient deleted: " + userDto);
                Notification.show("Patient " + userDto.getName() + " " + userDto.getSurname()
                        + " deleted!", 3000, Notification.Position.MIDDLE);
                this.close();

                userListGrid.updateList();
            });
        }
}