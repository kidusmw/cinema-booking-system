package ui.controller.admin;

import application.AppContext;
import domain.model.Booking;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ui.view.admin.BookingManagmentPage;

public class BookingManagmentController {
    private static final Logger log = LoggerFactory.getLogger(BookingManagmentController.class);

    private BookingManagmentPage view;
    private final AppContext ctx;
    private final AdminDashboardController dashboard;
    private ObservableList<Booking> masterList;

    public BookingManagmentController(AppContext ctx, AdminDashboardController dashboard) {
        this.ctx = ctx;
        this.dashboard = dashboard;
        this.view = new BookingManagmentPage();

        // 1. Initial Load
        loadTableData();

        // 2. Event Bindings
        view.btnBack.setOnAction(e -> handleBack());
        view.btnRefresh.setOnAction(e -> loadTableData());
        view.btnCancelBooking.setOnAction(e -> handleCancelBooking());
        view.searchField.textProperty().addListener((obs, old, newVal) -> filterBookings(newVal));
    }

    private void loadTableData() {
        this.masterList =
                FXCollections.observableArrayList(
                        ctx.bookingRepo.findAll().stream().collect(Collectors.toList()));
        view.bookingTable.setItems(masterList);
        log.info("Data Loaded: {} bookings.", masterList.size());
    }

    private void filterBookings(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            view.bookingTable.setItems(masterList);
            return;
        }
        ObservableList<Booking> filtered = FXCollections.observableArrayList();
        for (Booking b : masterList) {
            if (b.getMovieName() != null
                    && b.getMovieName().toLowerCase().contains(searchText.toLowerCase())) {
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
        ctx.bookingService.cancelBooking(selected.getBookingId());
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
