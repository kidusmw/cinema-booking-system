package ui.view.admin;

import domain.model.Hall;
import domain.model.Seat;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

public class SeatManagmentPage {
    public TableView<Seat> seatTable;
    public ComboBox<Hall> hallDropdown;
    public TextField seatCountField;
    public Button btnGenerate;
    public Button btnDelete;
    public Button btnRefresh;
    public Button btnBack;
    public Label hallInfoLabel;
    public Label statsLabel;

    private VBox root;

    public SeatManagmentPage() {
        createUI();
    }

    private void createUI() {
        root = new VBox(20);
        root.setStyle("-fx-background-color: #FAFAFA;");
        root.setPadding(new Insets(30));
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        VBox titleBox = new VBox(5);
        Label title = new Label("💺 Seat Management");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        titleBox.getChildren().addAll(title);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        btnBack = new Button("← Back");
        header.getChildren().addAll(titleBox, spacer, btnBack);

        VBox selectorCard = new VBox(15);
        selectorCard.setPadding(new Insets(20));
        selectorCard.setStyle(
                "-fx-background-color: white; -fx-border-color: #E2E8F0; -fx-border-radius: 8;");

        hallDropdown = new ComboBox<>();
        hallDropdown.setPromptText("-- Choose a hall --");
        hallDropdown.setPrefWidth(400);
        hallDropdown.setPrefHeight(40);
        hallDropdown.setFocusTraversable(true); // FIX: Ensure focus
        hallDropdown.setMouseTransparent(false); // FIX: Ensure clickability

        hallInfoLabel = new Label("Select a hall from the dropdown.");
        statsLabel = new Label("");
        selectorCard
                .getChildren()
                .addAll(new Label("Select Hall:"), hallDropdown, hallInfoLabel, statsLabel);
        VBox generateCard = new VBox(15);
        generateCard.setPadding(new Insets(20));
        generateCard.setStyle(
                "-fx-background-color: #F0FDF4; -fx-border-color: #10B981; -fx-border-radius: 8;");

        seatCountField = new TextField();
        seatCountField.setPromptText("e.g., 50");
        seatCountField.setPrefWidth(150);
        seatCountField.setFocusTraversable(true); // FIX: Ensure focus
        seatCountField.setMouseTransparent(false); // FIX: Ensure clickability

        btnGenerate = new Button("⚙️ Generate Seats");
        HBox generateRow = new HBox(10, seatCountField, btnGenerate);
        generateCard.getChildren().addAll(new Label("Number of seats:"), generateRow);

        btnRefresh = new Button("🔄 Refresh");
        btnDelete = new Button("🗑️ Delete All Seats");
        HBox toolbar = new HBox(10, btnRefresh, btnDelete);
        seatTable = new TableView<>();
        VBox.setVgrow(seatTable, Priority.ALWAYS);
        seatTable
                .getColumns()
                .addAll(
                        new TableColumn<>("Seat ID"),
                        new TableColumn<>("Number"),
                        new TableColumn<>("Type"));

        root.getChildren().addAll(header, selectorCard, generateCard, toolbar, seatTable);
    }

    public VBox getView() {
        return root;
    }
}
