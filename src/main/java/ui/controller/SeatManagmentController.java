package ui.controller;

import application.AppContext;
import application.ModelConverter;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ui.model.Moviehall;
import ui.model.Seat;
import ui.view.SeatManagmentPage;

public class SeatManagmentController {

    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(SeatManagmentController.class);
    private SeatManagmentPage view;
    private Stage stage;
    private final AppContext ctx;
    private final NavigationManager nav;
    private final AdminDashboardController dashboard;
    private Moviehall selectedHall;

    public SeatManagmentController(
            Stage stage,
            AppContext ctx,
            NavigationManager nav,
            AdminDashboardController dashboard) {
        this.stage = stage;
        this.ctx = ctx;
        this.nav = nav;
        this.dashboard = dashboard;
        this.view = new SeatManagmentPage();
        loadHalls();
        view.btnBack.setOnAction(e -> handleBack());
        view.btnGenerate.setOnAction(e -> handleGenerateSeats());
        view.btnRefresh.setOnAction(e -> loadSeatsForSelectedHall());
        view.btnDelete.setOnAction(e -> handleDeleteAllSeats());
        view.hallDropdown.setOnAction(
                e -> {
                    selectedHall = view.hallDropdown.getValue();
                    if (selectedHall != null) {
                        view.seatCountField.setText(String.valueOf(selectedHall.getCapacity()));
                        updateHallInfo();
                        loadSeatsForSelectedHall();
                    }
                });
    }

    public Parent getRootView() {
        return view.getView();
    }

    private void loadHalls() {
        try {
            List<Moviehall> halls =
                    ctx.hallRepo.findAll().stream()
                            .map(ModelConverter::toOldHall)
                            .collect(Collectors.toList());
            if (halls != null) view.hallDropdown.setItems(FXCollections.observableArrayList(halls));
        } catch (Exception e) {
            log.error("Operation failed", e);
        }
    }

    private void updateHallInfo() {
        if (selectedHall != null) {
            view.hallInfoLabel.setText(
                    "🏛️ Active: "
                            + selectedHall.getName()
                            + " | Capacity: "
                            + selectedHall.getCapacity());
        }
    }

    private void loadSeatsForSelectedHall() {
        if (selectedHall == null) return;
        try {
            List<Seat> seats =
                    ctx.seatRepo.findByHallId(Long.parseLong(selectedHall.getId())).stream()
                            .map(ModelConverter::toOldSeat)
                            .collect(Collectors.toList());
            view.seatTable.setItems(FXCollections.observableArrayList(seats));
            view.statsLabel.setText("📊 Total Seats: " + seats.size());
        } catch (Exception e) {
            log.error("Operation failed", e);
        }
    }

    private void handleGenerateSeats() {
        if (selectedHall == null) return;

        String countText = view.seatCountField.getText().trim();
        System.out.println(
                "DEBUG: Attempting to generate "
                        + countText
                        + " seats for Hall ID: "
                        + selectedHall.getId());

        try {
            int seatCount = Integer.parseInt(countText);
            int hallID = Integer.parseInt(selectedHall.getId());

            for (int i = 1; i <= seatCount; i++) {
                Seat seat = new Seat();
                seat.setSeatNumber("R" + i); // Simplified for testing
                seat.setSeatType("REGULAR");
                seat.setStatus("AVAILABLE");
                seat.setMovieHallID(hallID);

                ctx.seatRepo.save(ModelConverter.toDomainSeat(seat));
            }
            System.out.println("DEBUG: Generation loop finished.");
            loadSeatsForSelectedHall();
            showAlert("Success", "Seats generated.");
        } catch (Exception e) {
            log.error("Operation failed", e);
        }
    }

    private void handleDeleteAllSeats() {
        if (selectedHall == null) return;
        try {
            List<domain.model.Seat> seats =
                    ctx.seatRepo.findByHallId(Long.parseLong(selectedHall.getId()));
            for (domain.model.Seat s : seats) {
                s.setStatus("deleted");
                ctx.seatRepo.save(s);
            }
            loadSeatsForSelectedHall();
        } catch (Exception e) {
            log.error("Operation failed", e);
        }
    }

    private void handleBack() {
        nav.back();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
