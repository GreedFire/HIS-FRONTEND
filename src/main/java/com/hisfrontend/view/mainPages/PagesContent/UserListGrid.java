package com.hisfrontend.view.mainPages.PagesContent;

import com.hisfrontend.UrlGenerator;
import com.hisfrontend.domain.dto.PatientDto;
import com.hisfrontend.domain.dto.UserDto;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import lombok.Getter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Getter
public class UserListGrid extends VerticalLayout{
    private final Grid<UserDto> grid = new Grid<>(UserDto.class, false);
    private final HorizontalLayout topMenu = new HorizontalLayout();

    public UserListGrid(){
        drawUserListView();
    }
    private void drawUserListView(){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<UserDto>> responseList = restTemplate.exchange(
                UrlGenerator.GET_USERS, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });
        List<UserDto> userDtoList = responseList.getBody();
        gridFunctionality(userDtoList);
        gridColumnsSetup(userDtoList);
    }

    public void updateList(){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<UserDto>> responseList = restTemplate.exchange(
                UrlGenerator.GET_USERS, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });
        List<UserDto> list = responseList.getBody();
        this.getGrid().setItems(list);
        grid.getColumnByKey("Status").setFooter(String.format("%s items", list.size()));
    }

    private void gridColumnsSetup(List<UserDto> list){
        Grid.Column<UserDto> idColumn = grid.addColumn(UserDto::getId)
                .setHeader("User ID")
                .setSortable(true)
                .setResizable(true)
                .setFooter(String.format("%s items", list.size()));
        Grid.Column<UserDto> usernameColumn = grid.addColumn(UserDto::getUsername)
                .setHeader("Username")
                .setSortable(true)
                .setResizable(true);
        Grid.Column<UserDto> firstnameColumn = grid.addColumn(UserDto::getName)
                .setHeader("Firstname")
                .setSortable(true)
                .setResizable(true);
        Grid.Column<UserDto> lastnameColumn = grid.addColumn(UserDto::getSurname)
                .setHeader("Lastname")
                .setSortable(true)
                .setResizable(true);
//        Grid.Column<UserDto> loggedColumn = grid
//                .addComponentColumn(user -> createStatusBadge(toString()user.isSignedIn()))
//                .setHeader("Logged in")
//                .setSortable(true)
//                .setResizable(true);


        Button showColumnsButton = new Button("Show/Hide columns");
        showColumnsButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        UserListGrid.ColumnToggleContextMenu columnToggleContextMenu = new UserListGrid.ColumnToggleContextMenu(
                showColumnsButton);
        columnToggleContextMenu.addColumnToggleItem("User ID",
                idColumn);
        columnToggleContextMenu.addColumnToggleItem("Username",
                usernameColumn);
        columnToggleContextMenu.addColumnToggleItem("Firstname",
                firstnameColumn);
        columnToggleContextMenu.addColumnToggleItem("Lastname",
                lastnameColumn);
//        columnToggleContextMenu.addColumnToggleItem("Logged in",
//                loggedColumn);

        topMenu.add(showColumnsButton);
    }
    private void gridFunctionality(List<UserDto> list){
        GridListDataView<UserDto> dataView = grid.setItems(list);
        grid.setColumnReorderingAllowed(true);

        TextField searchField = new TextField();
        searchField.setPlaceholder("Username, Name");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(e -> dataView.refreshAll());

        dataView.addFilter(user -> {
            String searchTerm = searchField.getValue().trim();
            if (searchTerm.isEmpty())
                return true;

            boolean matchesFirstName = matchesTerm(user.getName(), searchTerm);
            boolean matchesSurname = matchesTerm(user.getSurname(), searchTerm);
            boolean matchesUserName = matchesTerm(user.getUsername(), searchTerm);
            return matchesFirstName || matchesSurname || matchesUserName;
        });

        topMenu.add(searchField);
    }

    private boolean matchesTerm(String value, String searchTerm) {
        return value.toLowerCase().contains(searchTerm.toLowerCase());
    }

    private Span createStatusBadge(String status) {
        String theme = switch (status) {
            case "true" -> "badge success";
            default -> "badge error";
        };
        Span badge = new Span(status);
        badge.getElement().getThemeList().add(theme);
        return badge;
    }

    private static class ColumnToggleContextMenu extends ContextMenu {
        public ColumnToggleContextMenu(Component target) {
            super(target);
            setOpenOnClick(true);
        }

        void addColumnToggleItem(String label, Grid.Column<UserDto> column) {
            MenuItem menuItem = this.addItem(label, e -> column.setVisible(e.getSource().isChecked()));
            menuItem.setCheckable(true);
            menuItem.setChecked(column.isVisible());
        }
    }
}
