package ui.view.admin;

import domain.model.Payment;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

@SuppressWarnings({"unchecked", "deprecation"})
public class PaymentManagment {
    public TableView<Payment> paymentTable;
    public Button btnRefresh;
    public Button btnBack;
    public Label lblTotalRevenue;

    private VBox root;

    public PaymentManagment() {
        createUI();
    }

    private void createUI() {
        root = new VBox(25);
        root.getStyleClass().add("page");
        root.getStyleClass().add("p-30");
        btnBack = new Button("⬅  Back");
        btnBack.getStyleClass().add("h-38");
        btnBack.getStyleClass().add("secondary-button");
        lblTotalRevenue = new Label("0.00 Birr");
        lblTotalRevenue.getStyleClass().add("text-accent");

        btnRefresh = new Button("🔄  Refresh");
        btnRefresh.getStyleClass().add("h-38");
        btnRefresh.getStyleClass().add("secondary-button");

        paymentTable = new TableView<>();
        paymentTable.getStyleClass().add("table-view");
        paymentTable.setPlaceholder(new Label("No system payment logs detected."));
        VBox.setVgrow(paymentTable, Priority.ALWAYS);
        HBox header = new HBox(15);
        header.getStyleClass().add("align-center-left");

        VBox titleBox = new VBox(5);
        Label title = new Label("Financial Ledger");
        title.getStyleClass().add("title");
        titleBox.getChildren().addAll(title);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        VBox revCard = new VBox(2);
        revCard.getStyleClass().add("p-10-20");
        revCard.getStyleClass().add("stat-card");

        Label cardTitle = new Label("TOTAL REVENUE");
        cardTitle.getStyleClass().add("muted-text");
        revCard.getChildren().addAll(cardTitle, lblTotalRevenue);

        header.getChildren().addAll(btnBack, titleBox, spacer, revCard, btnRefresh);
        TableColumn<Payment, Integer> pIdCol = new TableColumn<>("Payment ID");
        pIdCol.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        pIdCol.getStyleClass().add("w-90");

        TableColumn<Payment, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.getStyleClass().add("w-90");

        TableColumn<Payment, Double> amtCol = new TableColumn<>("Amount Paid");
        amtCol.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        amtCol.getStyleClass().add("w-110");

        TableColumn<Payment, String> verifyCol = new TableColumn<>("Method");
        verifyCol.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        verifyCol.getStyleClass().add("w-100");

        TableColumn<Payment, Integer> bIdCol = new TableColumn<>("Booking ID");
        bIdCol.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        bIdCol.getStyleClass().add("w-90");

        TableColumn<Payment, String> methodCol = new TableColumn<>("Method");
        methodCol.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        methodCol.getStyleClass().add("w-100");

        TableColumn<Payment, String> timestampCol = new TableColumn<>("Transaction Timestamp");
        timestampCol.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        timestampCol.getStyleClass().add("w-160");

        paymentTable
                .getColumns()
                .addAll(pIdCol, statusCol, amtCol, verifyCol, bIdCol, methodCol, timestampCol);
        paymentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        paymentTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        root.getChildren().addAll(header, paymentTable);
    }

    // Direct structural pass-through reference without wiping old variable states
    public VBox getView() {
        return root;
    }
}
