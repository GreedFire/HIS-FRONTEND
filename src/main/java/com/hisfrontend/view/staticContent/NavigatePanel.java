package com.hisfrontend.view.staticContent;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class NavigatePanel {
    private static HorizontalLayout menuLayout;
    private static Label pageNameLabel;
    public static HorizontalLayout drawNavigatePanel(String pageName){
        menuLayout = new HorizontalLayout();
        pageNameLabel = new Label(pageName);
        navigatePanelStyles();
        menuLayout.add(pageNameLabel);
        return menuLayout;
    }

    private static void navigatePanelStyles(){
        menuLayout.getStyle().set("background-color", DefiniedView.HIS_COLOR);
        menuLayout.getStyle().set("margin", "auto");
        menuLayout.getStyle().set("width", "100%");
        menuLayout.getStyle().set("clear", "both");
        menuLayout.getStyle().set("height", "40px");
        menuLayout.getStyle().set("top", "0");

        pageNameLabel.getStyle().set("margin", "auto");
        pageNameLabel.getStyle().set("font-size", "24px");
        pageNameLabel.getStyle().set("font-weight", "bold");
    }
}
