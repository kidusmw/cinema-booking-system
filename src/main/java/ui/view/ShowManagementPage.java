package ui.view;

import Model.Show;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import static ui.common.Theme.*;

public class ShowManagementPage {
    public TableView<Show> showTable;
    public TextField searchField;
    public Button btnAddShow;
    public Button btnEdit;
    public Button btnDelete;
    public Button btnRefresh;
    public Button btnBack;
    private VBox root;
    private static final String HOVER = "#EC4899";
    private static final String BG = "#FAFAFA";
    private static final String BG_LIGHT = "#F8FAFC";
    private static final String WHITE = "#FFFFFF";
    private static final String INFO = "#3B82F6";

    public ShowManagementPage() {
        createUI();
    }
    private void createUI() {
        root = new VBox(20);
        root.setStyle("-fx-background-color: " + BG + ";");
        root.setPadding(new Insets(30));
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(5);
        Label title = new Label("Show Management");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        title.setTextFill(Color.web(TEXT_DARK));

        Label subtitle = new Label("Schedule movies in halls at specific dates and times");
        subtitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        subtitle.setTextFill(Color.web(TEXT_MUTED));

        titleBox.getChildren().addAll(title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        btnBack = new Button("← Back");
        btnBack.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        btnBack.setTextFill(Color.web(TEXT_MUTED));
        btnBack.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 8 16;"
        );
        btnBack.setOnMouseEntered(e -> btnBack.setStyle(
                "-fx-background-color: " + BG_LIGHT + ";" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 8 16;" +
                        "-fx-background-radius: 8;"
        ));
        btnBack.setOnMouseExited(e -> btnBack.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 8 16;"
        ));

        header.getChildren().addAll(titleBox, spacer, btnBack);
        HBox toolbar = new HBox(10);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        searchField = new TextField();
        searchField.setPromptText("🔍  Search by ID, movie name, hall name, or time...");
        searchField.setPrefHeight(40);
        searchField.setPrefWidth(400);
        searchField.setFont(Font.font("Segoe UI", 13));
        searchField.setStyle(
                "-fx-background-color: " + WHITE + ";" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 8 14;" +
                        "-fx-text-fill: " + TEXT_DARK + ";" +
                        "-fx-prompt-text-fill: " + TEXT_MUTED + ";"
        );

        Region toolbarSpacer = new Region();
        HBox.setHgrow(toolbarSpacer, Priority.ALWAYS);
        btnAddShow = createActionButton("➕  Add Show", ACCENT, HOVER, WHITE);
        btnEdit = createActionButton("✏️  Edit", INFO, "#2563EB", WHITE);
        btnDelete = createActionButton("🗑️  Delete", DANGER, "#B91C1C", WHITE);
        btnRefresh = createActionButton("🔄  Refresh", SUCCESS, "#059669", WHITE);

        toolbar.getChildren().addAll(searchField, toolbarSpacer, btnAddShow);
        showTable = new TableView<>();
        showTable.setStyle(
                "-fx-background-color: " + WHITE + ";" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;"
        );
        showTable.setPlaceholder(new Label("No shows scheduled. Click 'Add Show' to create one."));
        VBox.setVgrow(showTable, Priority.ALWAYS);

        TableColumn<Show, String> showIdCol = new TableColumn<>("Show ID");
        showIdCol.setCellValueFactory(new PropertyValueFactory<>("showID"));
        showIdCol.setPrefWidth(120);
        TableColumn<Show, String> movieCol = new TableColumn<>("Movie Name");
        movieCol.setCellValueFactory(new PropertyValueFactory<>("movieName"));
        movieCol.setPrefWidth(180);
        TableColumn<Show, String> hallCol = new TableColumn<>("Hall Name");
        hallCol.setCellValueFactory(new PropertyValueFactory<>("hallName"));
        hallCol.setPrefWidth(140);

        TableColumn<Show, java.util.Date> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("showDate"));
        dateCol.setPrefWidth(130);
        dateCol.setCellFactory(col -> new TableCell<Show, java.util.Date>() {
            @Override
            protected void updateItem(java.util.Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(new java.text.SimpleDateFormat("yyyy-MM-dd").format(item));
                }
            }
        });

        TableColumn<Show, String> timeCol = new TableColumn<>("Time");
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

    public TableView<Show> getShowTable() {
        return showTable;
    }

    public VBox getView() {
        return root;
    }
    private Button createActionButton(String text, String normalColor, String hoverColor, String textColor) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        btn.setPrefHeight(38);
        btn.setTextFill(Color.web(textColor));
        btn.setStyle(
                "-fx-background-color: " + normalColor + ";" +
                        "-fx-background-radius: 8;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 8 16;"
        );
        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: " + hoverColor + ";" +
                        "-fx-background-radius: 8;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 8 16;" +
                        "-fx-text-fill: " + textColor + ";"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: " + normalColor + ";" +
                        "-fx-background-radius: 8;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 8 16;" +
                        "-fx-text-fill: " + textColor + ";"
        ));
        return btn;
    }
}
