package com.hisfrontend.view.PagesContent;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.theme.lumo.Lumo;

public class AppLayoutBasic {
    private static Label pageNameLabel;
    private static Label usernameLabel;

    public static AppLayout drawAppLayout(String pageName){
        AppLayout appLayout = new AppLayout();
        H1 title = new H1("HIS SYSTEM");
        pageNameLabel = new Label(pageName);
        navigatePanelStyles();
        Icon healthIcon =  new Icon("vaadin", "plus-square-o");
        healthIcon.getStyle().set("color", "red");
        healthIcon.getStyle().set("font-size", "40px");
        appLayout.addToNavbar(healthIcon, title, pageNameLabel, usernameLabel, darkModeButton());
        return appLayout;
    }

    private static void navigatePanelStyles(){
        pageNameLabel.getStyle().set("margin", "auto");
        pageNameLabel.getStyle().set("font-size", "30px");
        pageNameLabel.getStyle().set("font-weight", "bold");
        usernameLabel.getStyle().set("font-weight", "bold");
        usernameLabel.getStyle().set("font-size", "25px");
    }

    private static Button darkModeButton(){
        return new Button("Dark mode", click -> {
            ThemeList themeList = UI.getCurrent().getElement().getThemeList();

            if (themeList.contains(Lumo.DARK)) {
                themeList.remove(Lumo.DARK);
            } else {
                themeList.add(Lumo.DARK);
            }
        });
    }

    public static void setUsernameLabel(Label usernameLabel) {
        AppLayoutBasic.usernameLabel = usernameLabel;
    }
}
