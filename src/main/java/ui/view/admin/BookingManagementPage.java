package ui.view.admin;

import domain.model.Booking;
import domain.model.BookingStatus;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

public class BookingManagementPage {

    public TableView<Booking> bookingTable;
    public TextField searchField;
    public Button btnCancelBooking;
    public Button btnRefresh;
    public Button btnBack;

    private VBox root;

    public BookingManagementPage() {
        createUI();
    }

    private void createUI() {
        root = new VBox(20);
        root.getStyleClass().add("page");
        root.getStyleClass().add("p-30");

        // Header
        HBox header = new HBox(15);
        header.getStyleClass().add("align-center-left");
        Label title = new Label("🎟️ Booking Management");
        title.getStyleClass().add("title");

        btnBack = new Button("← Back to Dashboard");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(title, spacer, btnBack);

        // Toolbar
        searchField = new TextField();
        searchField.setPromptText("🔍 Search by customer name...");
        searchField.setPrefSize(400, 40);

        // Table Setup
        bookingTable = new TableView<>();
        VBox.setVgrow(bookingTable, Priority.ALWAYS);

        // 1. Booking ID
        TableColumn<Booking, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("bookingId"));

        // 2. User Name
        TableColumn<Booking, String> customerCol = new TableColumn<>("User");
        customerCol.setCellValueFactory(
                cellData ->
                        new javafx.beans.property.SimpleStringProperty(
                                "User #" + cellData.getValue().getUserId()));

        // 3. Date (with formatter)
        TableColumn<Booking, LocalDateTime> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("bookingDate"));
        dateCol.setCellFactory(
                col ->
                        new TableCell<Booking, LocalDateTime>() {
                            private final DateTimeFormatter format =
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd");

                            @Override
                            protected void updateItem(LocalDateTime item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty || item == null) {
                                    setText(null);
                                } else {
                                    setText(format.format(item));
                                }
                            }
                        });

        // 4. Movie Name
        TableColumn<Booking, String> movieCol = new TableColumn<>("Movie");
        movieCol.setCellValueFactory(new PropertyValueFactory<>("movieName"));

        // 5. Booking Status
        TableColumn<Booking, BookingStatus> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("bookingStatus"));

        // 6. Show ID - FIXED: was TableColumn<Booking, Integer> but showID is a String in the model
        TableColumn<Booking, String> showCol = new TableColumn<>("Show ID");
        showCol.setCellValueFactory(new PropertyValueFactory<>("showId"));

        bookingTable
                .getColumns()
                .addAll(List.of(idCol, customerCol, dateCol, movieCol, statusCol, showCol));
        bookingTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_LAST_COLUMN);
        bookingTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Action Buttons
        btnCancelBooking = new Button("❌ Cancel Booking");
        btnRefresh = new Button("🔄 Refresh");

        HBox actionRow = new HBox(10, btnCancelBooking, btnRefresh);
        root.getChildren().addAll(header, searchField, bookingTable, actionRow);
    }

    public VBox getView() {
        return root;
    }
}
