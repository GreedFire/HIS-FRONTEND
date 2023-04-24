package com.hisfrontend.view.PagesContent;

import com.hisfrontend.UrlGenerator;
import com.hisfrontend.domain.dto.PatientDto;
import com.hisfrontend.view.LoginPage;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

public class PatientDeleteDialog extends Dialog{
    private final RestTemplate restTemplate = new RestTemplate();
    private static final Logger LOGGER = LoggerFactory.getLogger(PatientDeleteDialog.class);

    public PatientDeleteDialog(PatientListGrid patientListGrid, PatientDto patientDto) {
            //Dialog Header
            Button closeButton = new Button(new Icon("lumo", "cross"), e -> this.close());
            this.setHeaderTitle("PATIENT DELETION");
            this.getHeader().add(closeButton);

            //HTML Elements
            Label deletionQuestionLabel = new Label("You want to delete patient " + patientDto.getFirstname()
                    + " " + patientDto.getSurname() + ". Are you sure?");
            Button yesButton = new Button("YES");
            Button noButton = new Button("NO");

            this.add(deletionQuestionLabel);
            this.getFooter().add(yesButton, noButton);

            noButtonLogic(noButton);
            yesButtonLogic(patientListGrid, patientDto, yesButton);
        }

        private void noButtonLogic(Button button){
            button.addClickListener(e -> this.close());
        }
        private void yesButtonLogic(PatientListGrid patientListGrid, PatientDto patientDto, Button button){
            //LOGIC
            button.addClickListener(e -> {
                LOGGER.info("Trying to delete patient: " + patientDto);
                restTemplate.delete(UrlGenerator.patientDeleteURL(patientDto.getId()));
                LOGGER.info("Patient deleted: " + patientDto);
                Notification.show("Patient " + patientDto.getFirstname() + " " + patientDto.getSurname()
                        + " deleted!", 3000, Notification.Position.MIDDLE);
                this.close();

                patientListGrid.updateList();
            });
        }
}