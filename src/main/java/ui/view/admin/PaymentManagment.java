package ui.view.admin;

import domain.model.Payment;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

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
        root.setPadding(new Insets(30));
        btnBack = new Button("⬅  Back");
        btnBack.setPrefHeight(38);
        btnBack.getStyleClass().add("secondary-button");
        lblTotalRevenue = new Label("0.00 Birr");
        lblTotalRevenue.getStyleClass().add("text-accent");

        btnRefresh = new Button("🔄  Refresh");
        btnRefresh.setPrefHeight(38);
        btnRefresh.getStyleClass().add("secondary-button");

        paymentTable = new TableView<>();
        paymentTable.getStyleClass().add("table-view");
        paymentTable.setPlaceholder(new Label("No system payment logs detected."));
        VBox.setVgrow(paymentTable, Priority.ALWAYS);
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(5);
        Label title = new Label("Financial Ledger");
        title.getStyleClass().add("title");
        titleBox.getChildren().addAll(title);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        VBox revCard = new VBox(2);
        revCard.setPadding(new Insets(10, 20, 10, 20));
        revCard.getStyleClass().add("stat-card");

        Label cardTitle = new Label("TOTAL REVENUE");
        cardTitle.getStyleClass().add("muted-text");
        revCard.getChildren().addAll(cardTitle, lblTotalRevenue);

        header.getChildren().addAll(btnBack, titleBox, spacer, revCard, btnRefresh);
        TableColumn<Payment, Integer> pIdCol = new TableColumn<>("Payment ID");
        pIdCol.setCellValueFactory(new PropertyValueFactory<>("paymentID"));
        pIdCol.setPrefWidth(90);

        TableColumn<Payment, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(90);

        TableColumn<Payment, Double> amtCol = new TableColumn<>("Amount Paid");
        amtCol.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        amtCol.setPrefWidth(110);

        TableColumn<Payment, String> verifyCol = new TableColumn<>("Verification Code");
        verifyCol.setCellValueFactory(new PropertyValueFactory<>("verificationCode"));
        verifyCol.setPrefWidth(130);

        TableColumn<Payment, Integer> bIdCol = new TableColumn<>("Booking ID");
        bIdCol.setCellValueFactory(new PropertyValueFactory<>("bookingID"));
        bIdCol.setPrefWidth(90);

        TableColumn<Payment, String> methodCol = new TableColumn<>("Method");
        methodCol.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        methodCol.setPrefWidth(100);

        TableColumn<Payment, String> timestampCol = new TableColumn<>("Transaction Timestamp");
        timestampCol.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        timestampCol.setPrefWidth(160);

        paymentTable
                .getColumns()
                .addAll(pIdCol, statusCol, amtCol, verifyCol, bIdCol, methodCol, timestampCol);
        paymentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        root.getChildren().addAll(header, paymentTable);
    }

    // Direct structural pass-through reference without wiping old variable states
    public VBox getView() {
        return root;
    }
}
