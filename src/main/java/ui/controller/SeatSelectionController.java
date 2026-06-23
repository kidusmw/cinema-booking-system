package ui.controller;

import application.ModelConverter;
import java.util.stream.Collectors;
import ui.model.*;
import ui.view.SeatSelectionPage;
import application.AppContext;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.util.*;
import static ui.common.Theme.*;

public class SeatSelectionController {

    private SeatSelectionPage view;
    private Stage stage;
    private Customer currentUser;
    private Movie selectedMovie;
    private Show selectedShow;
    private Moviehall selectedHall;
    private boolean isVIP;
    private AppContext ctx;
    private NavigationManager nav;
    private final Set<String> selectedSeats = new LinkedHashSet<>();
    private List<Seat> allSeats;
    private double seatPrice;


    private static final String AVAILABLE_COLOR = "#10B981";
    private static final String BOOKED_COLOR = "#EF4444";
    private static final String SELECTED_COLOR = "#DB2777";

    public SeatSelectionController(Stage stage, AppContext ctx, NavigationManager nav, Customer currentUser, Movie selectedMovie,
                                   Show selectedShow, Moviehall selectedHall, boolean isVIP) {
        this.stage = stage;
        this.ctx = ctx;
        this.nav = nav;
        this.currentUser = currentUser;
        this.selectedMovie = selectedMovie;
        this.selectedShow = selectedShow;
        this.selectedHall = selectedHall;
        this.isVIP = isVIP;
        this.seatPrice = isVIP ? 120.0 : 50.0;
        this.view = new SeatSelectionPage();

        Scene scene = new Scene(view.getView(), 1200, 750);
        stage.setTitle("Select Seats - " + selectedMovie.getTitle());
        stage.setScene(scene);
        stage.show();
        view.hallNameLabel.setText("🏛️ " + selectedHall.getName());
        view.showInfoLabel.setText("🕐 " + selectedShow.getShowTime() + "  |  📅 " + selectedShow.getShowDate());
        view.priceLabel.setText(String.format("%.0f Birr per seat", seatPrice));
        loadSeats();
        view.btnBack.setOnAction(e -> nav.back());
        view.btnProceed.setOnAction(e -> proceedToPayment());
    }

    private void loadSeats() {
        view.seatGrid.getChildren().clear();
        allSeats = ctx.seatRepo.findByHallId(Long.parseLong(selectedHall.getId())).stream().map(ModelConverter::toOldSeat).collect(Collectors.toList());

        if (allSeats.isEmpty()) {
            Label noSeats = new Label("No seats available. Please ensure seats are generated for this hall.");
            noSeats.setFont(Font.font("Segoe UI", 14));
            view.seatGrid.getChildren().add(noSeats);
            return;
        }

        Map<String, List<Seat>> seatsByRow = new TreeMap<>();
        for (Seat seat : allSeats) {
            String rowLetter = seat.getSeatNumber().replaceAll("\\d", "");
            seatsByRow.computeIfAbsent(rowLetter, k -> new ArrayList<>()).add(seat);
        }

        for (Map.Entry<String, List<Seat>> entry : seatsByRow.entrySet()) {
            HBox rowBox = new HBox(8);
            rowBox.setAlignment(Pos.CENTER);
            Label rowLabel = new Label(entry.getKey());
            rowLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
            rowLabel.setTextFill(Color.web(TEXT_MUTED));
            rowLabel.setPrefWidth(30);
            rowBox.getChildren().add(rowLabel);

            for (Seat seat : entry.getValue()) {
                rowBox.getChildren().add(createSeatButton(seat));
            }
            view.seatGrid.getChildren().add(rowBox);
        }
    }

    private Button createSeatButton(Seat seat) {
        Button btn = new Button(seat.getSeatNumber());
        btn.setPrefSize(40, 40);
        btn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));

        String color;
        boolean clickable = true;

        if ("BOOKED".equalsIgnoreCase(seat.getStatus())) {
            color = BOOKED_COLOR;
            clickable = false;
        } else if (selectedSeats.contains(seat.getSeatID())) {
            color = SELECTED_COLOR;
        } else {
            color = AVAILABLE_COLOR;
        }

        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-background-radius: 6; -fx-cursor: " + (clickable ? "hand" : "not-allowed") + ";");

        if (clickable) {
            final String seatId = seat.getSeatID();
            btn.setOnAction(e -> toggleSeatSelection(seatId, btn));
        } else {
            btn.setDisable(true);
        }
        return btn;
    }

    private void toggleSeatSelection(String seatId, Button btn) {
        if (selectedSeats.contains(seatId)) {
            selectedSeats.remove(seatId);
            btn.setStyle("-fx-background-color: " + AVAILABLE_COLOR + "; -fx-text-fill: white; -fx-background-radius: 6; -fx-cursor: hand;");
        } else {
            if (selectedSeats.size() >= 8) {
                showAlert("Limit Reached", "You can select maximum 8 seats at a time.");
                return;
            }
            selectedSeats.add(seatId);
            btn.setStyle("-fx-background-color: " + SELECTED_COLOR + "; -fx-text-fill: white; -fx-background-radius: 6; -fx-cursor: hand;");
        }
        updateSummary();
    }
    private void updateSummary() {
        int count = selectedSeats.size();
        double total = count * seatPrice;
        view.selectedCountLabel.setText(count + " seat" + (count != 1 ? "s" : "") + " selected");
        view.totalAmountLabel.setText(String.format("%.2f Birr", total));
        view.btnProceed.setDisable(count == 0);
        view.btnProceed.setOpacity(count == 0 ? 0.5 : 1.0);
    }

    private void proceedToPayment() {
        if (selectedSeats.isEmpty()) return;
        nav.go(
            () -> new SeatSelectionController(stage, ctx, nav, currentUser, selectedMovie, selectedShow, selectedHall, isVIP),
            () -> new PaymentController(stage, ctx, nav, currentUser, selectedMovie, selectedShow, selectedHall, new ArrayList<>(selectedSeats), seatPrice)
        );
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}