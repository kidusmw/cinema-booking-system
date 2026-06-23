package Controller;

import DAO.MovieHallDAO;
import DAO.SeatDAO;
import DAO.SeatDAOimp;
import Model.Moviehall;
import Model.Seat;
import View.SeatManagmentPage;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Parent;
import java.util.List;
import java.util.Optional;

public class SeatManagmentController {

    private SeatManagmentPage view;
    private Stage stage;
    private final AdminDashboardController dashboard;
    private final MovieHallDAO hallDAO = new MovieHallDAO();
    private final SeatDAO seatDAO = new SeatDAOimp();
    private Moviehall selectedHall;

    public SeatManagmentController(Stage stage, AdminDashboardController dashboard) {
        this.stage = stage;
        this.dashboard = dashboard;
        this.view = new SeatManagmentPage();
        loadHalls();
        view.btnBack.setOnAction(e -> handleBack());
        view.btnGenerate.setOnAction(e -> handleGenerateSeats());
        view.btnRefresh.setOnAction(e -> loadSeatsForSelectedHall());
        view.btnDelete.setOnAction(e -> handleDeleteAllSeats());
        view.hallDropdown.setOnAction(e -> {
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
            List<Moviehall> halls = hallDAO.getAllMovieHalls();
            if (halls != null) view.hallDropdown.setItems(FXCollections.observableArrayList(halls));
        } catch (Exception e) { e.printStackTrace(); }
    }
    private void updateHallInfo() {
        if (selectedHall != null) {
            view.hallInfoLabel.setText("🏛️ Active: " + selectedHall.getName() + " | Capacity: " + selectedHall.getCapacity());
        }
    }
    private void loadSeatsForSelectedHall() {
        if (selectedHall == null) return;
        try {
            List<Seat> seats = seatDAO.getSeatsByHall((selectedHall.getId()));
            view.seatTable.setItems(FXCollections.observableArrayList(seats));
            view.statsLabel.setText("📊 Total Seats: " + seats.size());
        } catch (Exception e) { e.printStackTrace(); }
    }
    private void handleGenerateSeats() {
        if (selectedHall == null) return;

        String countText = view.seatCountField.getText().trim();
        System.out.println("DEBUG: Attempting to generate " + countText + " seats for Hall ID: " + selectedHall.getId());

        try {
            int seatCount = Integer.parseInt(countText);
            int hallID = Integer.parseInt(selectedHall.getId());

            for (int i = 1; i <= seatCount; i++) {
                Seat seat = new Seat();
                seat.setSeatNumber("R" + i); // Simplified for testing
                seat.setSeatType("REGULAR");
                seat.setStatus("AVAILABLE");
                seat.setMovieHallID(hallID);

                // CRITICAL: Call the DAO and verify success
                boolean success = seatDAO.addSeat(seat);
                if (!success) {
                    System.out.println("ERROR: DAO failed to add seat index " + i);
                }
            }
            System.out.println("DEBUG: Generation loop finished.");
            loadSeatsForSelectedHall();
            showAlert("Success", "Seats generated.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleDeleteAllSeats() {
        if (selectedHall == null) return;
        try {
            List<Seat> seats = seatDAO.getSeatsByHall((selectedHall.getId()));
            for (Seat s : seats) seatDAO.deleteSeat(s.getSeatID());
            loadSeatsForSelectedHall();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void handleBack() {
        new AdminDashboardController(stage, dashboard.getAdminName());
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}