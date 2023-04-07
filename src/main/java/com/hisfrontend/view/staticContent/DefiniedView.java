package com.hisfrontend.view.staticContent;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class DefiniedView {

    public static final String HIS_COLOR = "#50c0e6";
    public static final String FOOTER_COLOR = "#c7c4bb";
    public static HorizontalLayout drawFooter(){
        HorizontalLayout footerLayout = new HorizontalLayout();
        footerLayout.getStyle().set("background-color", FOOTER_COLOR);
        footerLayout.getStyle().set("margin", "auto");
        footerLayout.getStyle().set("width", "100%");
        footerLayout.getStyle().set("clear", "both");
        footerLayout.getStyle().set("position", "absolute");
        footerLayout.getStyle().set("height", "40px");
        footerLayout.getStyle().set("bottom", "0");

        Label createdByLabel = new Label("Created by Dawid Majchrzak & Anna Nowacka");
        createdByLabel.getStyle().set("font-size", "24px");
        createdByLabel.getStyle().set("margin-left", "20px");

        footerLayout.add(createdByLabel);

        return footerLayout;
    }
}
