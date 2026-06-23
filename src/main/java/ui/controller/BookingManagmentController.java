package ui.controller;

import application.AppContext;
import application.ModelConverter;
import Model.Booking;
import ui.view.BookingManagmentPage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.stream.Collectors;

public class BookingManagmentController {

    private BookingManagmentPage view;
    private Stage stage;
    private final AppContext ctx;
    private final AdminDashboardController dashboard;
    private ObservableList<Booking> masterList;

    public BookingManagmentController(Stage stage, AppContext ctx, AdminDashboardController dashboard) {
        this.stage = stage;
        this.ctx = ctx;
        this.dashboard = dashboard;
        this.view = new BookingManagmentPage();

        // ✅ DO NOT set a new Scene here — the dashboard injects this view
        // Scene scene = new Scene(view.getView(), 1100, 700); ← REMOVED
        // stage.setScene(scene);                              ← REMOVED
        // stage.show();                                       ← REMOVED

        // 1. Initial Load
        loadTableData();

        // 2. Event Bindings
        view.btnBack.setOnAction(e -> handleBack());
        view.btnRefresh.setOnAction(e -> loadTableData());
        view.btnCancelBooking.setOnAction(e -> handleCancelBooking());
        view.searchField.textProperty().addListener((obs, old, newVal) -> filterBookings(newVal));
    }

    private void loadTableData() {
        this.masterList = FXCollections.observableArrayList(ctx.bookingRepo.findAll().stream().map(ModelConverter::toOldBooking).collect(Collectors.toList()));
        view.bookingTable.setItems(masterList);
        System.out.println("✅ Data Loaded: " + masterList.size() + " bookings.");
    }

    private void filterBookings(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            view.bookingTable.setItems(masterList);
            return;
        }
        ObservableList<Booking> filtered = FXCollections.observableArrayList();
        for (Booking b : masterList) {
            if (b.getCustomerName() != null && b.getCustomerName().toLowerCase().contains(searchText.toLowerCase())) {
                filtered.add(b);
            }
        }
        view.bookingTable.setItems(filtered);
    }

    private void handleCancelBooking() {
        Booking selected = view.bookingTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a booking to cancel.");
            return;
        }
        ctx.bookingService.cancelBooking((long) selected.getBookingID());
        showAlert("Success", "Booking Cancelled");
        loadTableData();
    }

    private void handleBack() {
        // ✅ Just tell the dashboard to show the dashboard view — no new scene needed
        dashboard.showDashboard();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public VBox getRootView() {
        return view.getView();
    }
}