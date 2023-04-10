package com.hisfrontend.view.staticContent;

import com.hisfrontend.domain.dto.UserDto;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class NavigatePanel {
    private static HorizontalLayout menuLayout;
    private static Label pageNameLabel;
    private static Label usernameLabel;

    public static HorizontalLayout drawNavigatePanel(String pageName){
        menuLayout = new HorizontalLayout();
        pageNameLabel = new Label(pageName);
        navigatePanelStyles();
        menuLayout.add(pageNameLabel, usernameLabel);
        return menuLayout;
    }

    private static void navigatePanelStyles(){
        menuLayout.getStyle().set("background-color", DefiniedView.HIS_COLOR);
        menuLayout.getStyle().set("margin", "auto");
        menuLayout.getStyle().set("width", "100%");
        menuLayout.getStyle().set("clear", "both");
        menuLayout.getStyle().set("height", "80px");
        menuLayout.getStyle().set("top", "0");
        pageNameLabel.getStyle().set("margin", "auto");
        pageNameLabel.getStyle().set("font-size", "30px");
        pageNameLabel.getStyle().set("font-weight", "bold");
        usernameLabel.getStyle().set("font-weight", "bold");
        usernameLabel.getStyle().set("font-size", "25px");
    }

    public static void setUsernameLabel(Label usernameLabel) {
        NavigatePanel.usernameLabel = usernameLabel;
    }
}
