package ui.view.admin;

import domain.model.Showtime;
import java.time.LocalDate;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

public class ShowManagementPage {
    public TableView<Showtime> showTable;
    public TextField searchField;
    public Button btnAddShow;
    public Button btnEdit;
    public Button btnDelete;
    public Button btnRefresh;
    public Button btnBack;
    private VBox root;

    public ShowManagementPage() {
        createUI();
    }

    private void createUI() {
        root = new VBox(20);
        root.getStyleClass().add("page");
        root.setPadding(new Insets(30));
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(5);
        Label title = new Label("Show Management");
        title.getStyleClass().add("title");

        Label subtitle = new Label("Schedule movies in halls at specific dates and times");
        subtitle.getStyleClass().add("muted-text");

        titleBox.getChildren().addAll(title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        btnBack = new Button("← Back");
        btnBack.getStyleClass().add("back-button");

        header.getChildren().addAll(titleBox, spacer, btnBack);
        HBox toolbar = new HBox(10);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        searchField = new TextField();
        searchField.setPromptText("🔍  Search by ID, movie name, hall name, or time...");
        searchField.setPrefHeight(40);
        searchField.setPrefWidth(400);
        searchField.getStyleClass().add("search-field");

        Region toolbarSpacer = new Region();
        HBox.setHgrow(toolbarSpacer, Priority.ALWAYS);
        btnAddShow = createActionButton("➕  Add Show", "primary-button-small");
        btnEdit = createActionButton("✏️  Edit", "secondary-button");
        btnDelete = createActionButton("🗑️  Delete", "danger-button");
        btnRefresh = createActionButton("🔄  Refresh", "secondary-button");

        toolbar.getChildren().addAll(searchField, toolbarSpacer, btnAddShow);
        showTable = new TableView<>();
        showTable.getStyleClass().add("table-view");
        showTable.setPlaceholder(new Label("No shows scheduled. Click 'Add Show' to create one."));
        VBox.setVgrow(showTable, Priority.ALWAYS);

        TableColumn<Showtime, String> showIdCol = new TableColumn<>("Show ID");
        showIdCol.setCellValueFactory(new PropertyValueFactory<>("showId"));
        showIdCol.setPrefWidth(120);
        TableColumn<Showtime, String> movieCol = new TableColumn<>("Movie Name");
        movieCol.setCellValueFactory(new PropertyValueFactory<>("movieName"));
        movieCol.setPrefWidth(180);
        TableColumn<Showtime, String> hallCol = new TableColumn<>("Hall Name");
        hallCol.setCellValueFactory(new PropertyValueFactory<>("hallName"));
        hallCol.setPrefWidth(140);

        TableColumn<Showtime, LocalDate> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("showDate"));
        dateCol.setPrefWidth(130);
        dateCol.setCellFactory(
                col ->
                        new TableCell<Showtime, LocalDate>() {
                            @Override
                            protected void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty || item == null) {
                                    setText(null);
                                } else {
                                    setText(item.toString());
                                }
                            }
                        });

        TableColumn<Showtime, String> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("showTime"));
        timeCol.setPrefWidth(100);

        showTable.getColumns().addAll(showIdCol, movieCol, hallCol, dateCol, timeCol);
        showTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // ===== ASSEMBLE LAYOUT =====
        root.getChildren().addAll(header, toolbar, showTable);

        // Action button row
        HBox actionRow = new HBox(10);
        actionRow.setAlignment(Pos.CENTER_RIGHT);
        actionRow.getChildren().addAll(btnEdit, btnDelete, btnRefresh);
        root.getChildren().add(actionRow);
    }

    public TableView<Showtime> getShowTable() {
        return showTable;
    }

    public VBox getView() {
        return root;
    }

    private Button createActionButton(String text, String cssClass) {
        Button btn = new Button(text);
        btn.setPrefHeight(38);
        btn.getStyleClass().add(cssClass);
        return btn;
    }
}
