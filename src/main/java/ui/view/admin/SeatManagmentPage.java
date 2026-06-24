package ui.view.admin;

import domain.model.Hall;
import domain.model.Seat;
import javafx.scene.control.*;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

@SuppressWarnings("unchecked")
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
        root.getStyleClass().add("page");
        root.getStyleClass().add("p-30");
        HBox header = new HBox(15);
        header.getStyleClass().add("align-center-left");
        VBox titleBox = new VBox(5);
        Label title = new Label("💺 Seat Management");
        title.getStyleClass().add("title");
        titleBox.getChildren().addAll(title);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        btnBack = new Button("← Back");
        header.getChildren().addAll(titleBox, spacer, btnBack);

        VBox selectorCard = new VBox(15);
        selectorCard.getStyleClass().add("p-20");
        selectorCard.getStyleClass().add("card");

        hallDropdown = new ComboBox<>();
        hallDropdown.setPromptText("-- Choose a hall --");
        hallDropdown.getStyleClass().add("w-400");
        hallDropdown.getStyleClass().add("h-40");
        hallDropdown.setFocusTraversable(true); // FIX: Ensure focus
        hallDropdown.setMouseTransparent(false); // FIX: Ensure clickability

        hallInfoLabel = new Label("Select a hall from the dropdown.");
        statsLabel = new Label("");
        selectorCard
                .getChildren()
                .addAll(new Label("Select Hall:"), hallDropdown, hallInfoLabel, statsLabel);
        VBox generateCard = new VBox(15);
        generateCard.getStyleClass().add("p-20");
        generateCard.getStyleClass().add("card");

        seatCountField = new TextField();
        seatCountField.setPromptText("e.g., 50");
        seatCountField.getStyleClass().add("w-150");
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
        TableColumn<domain.model.Seat, Long> seatIdCol = new TableColumn<>("Seat ID");
        seatIdCol.setCellValueFactory(new PropertyValueFactory<>("seatId"));
        seatIdCol.getStyleClass().add("w-100");
        TableColumn<domain.model.Seat, String> seatNumCol = new TableColumn<>("Number");
        seatNumCol.setCellValueFactory(new PropertyValueFactory<>("seatNumber"));
        seatNumCol.getStyleClass().add("w-150");
        TableColumn<domain.model.Seat, String> seatTypeCol = new TableColumn<>("Type");
        seatTypeCol.setCellValueFactory(new PropertyValueFactory<>("seatType"));
        seatTypeCol.getStyleClass().add("w-150");
        seatTable.getColumns().addAll(seatIdCol, seatNumCol, seatTypeCol);
        seatTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        root.getChildren().addAll(header, selectorCard, generateCard, toolbar, seatTable);
    }

    public VBox getView() {
        return root;
    }
}
