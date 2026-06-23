package ui.view.admin;

import domain.model.Booking;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.*;

public class BookingManagmentPage {

    public TableView<Booking> bookingTable;
    public TextField searchField;
    public Button btnCancelBooking;
    public Button btnRefresh;
    public Button btnBack;

    private VBox root;

    public BookingManagmentPage() {
        createUI();
    }

    private void createUI() {
        root = new VBox(20);
        root.setStyle("-fx-background-color: #FAFAFA;");
        root.setPadding(new Insets(30));

        // Header
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("🎟️ Booking Management");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));

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
        TableColumn<Booking, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("bookingID"));

        // 2. User Name
        TableColumn<Booking, String> customerCol = new TableColumn<>("User");
        customerCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));

        // 3. Date (with formatter)
        TableColumn<Booking, Date> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("bookingDate"));
        dateCol.setCellFactory(
                col ->
                        new TableCell<Booking, Date>() {
                            private final SimpleDateFormat format =
                                    new SimpleDateFormat("yyyy-MM-dd");

                            @Override
                            protected void updateItem(Date item, boolean empty) {
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
        TableColumn<Booking, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("bookingStatus"));

        // 6. Show ID - FIXED: was TableColumn<Booking, Integer> but showID is a String in the model
        TableColumn<Booking, String> showCol = new TableColumn<>("Show ID");
        showCol.setCellValueFactory(new PropertyValueFactory<>("showID"));

        bookingTable.getColumns().addAll(idCol, customerCol, dateCol, movieCol, statusCol, showCol);
        bookingTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

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
