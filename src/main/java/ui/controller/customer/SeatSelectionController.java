package ui.controller.customer;

import application.AppContext;
import domain.model.*;
import java.util.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ui.common.WindowManager;
import ui.controller.common.NavigationManager;
import ui.view.customer.SeatSelectionPage;

public class SeatSelectionController {
    private static final Logger log = LoggerFactory.getLogger(SeatSelectionController.class);

    private SeatSelectionPage view;
    private Stage stage;
    private User currentUser;
    private Movie selectedMovie;
    private Showtime selectedShow;
    private Hall selectedHall;
    private boolean isVIP;
    private AppContext ctx;
    private NavigationManager nav;
    private final Set<String> selectedSeats = new LinkedHashSet<>();
    private List<Seat> allSeats;
    private double seatPrice;

    public SeatSelectionController(
            Stage stage,
            AppContext ctx,
            NavigationManager nav,
            User currentUser,
            Movie selectedMovie,
            Showtime selectedShow,
            Hall selectedHall,
            boolean isVIP) {
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

        WindowManager.configure(stage, "Select Seats", view.getView());
        log.info("Opening Select Seats page");
        view.hallNameLabel.setText("🏛️ " + selectedHall.getName());
        view.showInfoLabel.setText(
                "🕐 " + selectedShow.getShowTime() + "  |  📅 " + selectedShow.getShowDate());
        view.priceLabel.setText(String.format("%.0f Birr per seat", Double.valueOf(seatPrice)));
        loadSeats();
        view.btnBack.setOnAction(e -> nav.back());
        view.btnProceed.setOnAction(e -> proceedToPayment());
    }

    private void loadSeats() {
        view.seatGrid.getChildren().clear();
        allSeats = ctx.seatRepo.findByHallId(selectedHall.getHallId());

        if (allSeats.isEmpty()) {
            Label noSeats =
                    new Label(
                            "No seats available. Please ensure seats are generated for this hall.");
            noSeats.getStyleClass().add("empty-text");
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
            rowBox.getStyleClass().add("align-center");
            Label rowLabel = new Label(entry.getKey());
            rowLabel.getStyleClass().add("muted-text");
            rowLabel.getStyleClass().add("w-30");
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
        btn.getStyleClass().add("seat-button-text");

        boolean clickable = true;

        if (seat.getStatus() == SeatStatus.BOOKED) {
            btn.getStyleClass().add("seat-booked");
            clickable = false;
        } else if (selectedSeats.contains(String.valueOf(seat.getSeatId()))) {
            btn.getStyleClass().add("seat-selected");
        } else {
            btn.getStyleClass().add("seat-available");
        }

        if (clickable) {
            final String seatId = String.valueOf(seat.getSeatId());
            btn.setOnAction(e -> toggleSeatSelection(seatId, btn));
        } else {
            btn.setDisable(true);
        }
        return btn;
    }

    private void toggleSeatSelection(String seatId, Button btn) {
        if (selectedSeats.contains(seatId)) {
            selectedSeats.remove(seatId);
            btn.getStyleClass().removeAll("seat-selected", "seat-booked");
            btn.getStyleClass().add("seat-available");
        } else {
            if (selectedSeats.size() >= 8) {
                showAlert("Limit Reached", "You can select maximum 8 seats at a time.");
                return;
            }
            selectedSeats.add(seatId);
            btn.getStyleClass().removeAll("seat-available", "seat-booked");
            btn.getStyleClass().add("seat-selected");
        }
        updateSummary();
    }

    private void updateSummary() {
        int count = selectedSeats.size();
        double total = count * seatPrice;
        view.selectedCountLabel.setText(count + " seat" + (count != 1 ? "s" : "") + " selected");
        view.totalAmountLabel.setText(String.format("%.2f Birr", Double.valueOf(total)));
        view.btnProceed.setDisable(count == 0);
        view.btnProceed.setOpacity(count == 0 ? 0.5 : 1.0);
    }

    private void proceedToPayment() {
        if (selectedSeats.isEmpty()) return;
        nav.go(
                () ->
                        new SeatSelectionController(
                                stage,
                                ctx,
                                nav,
                                currentUser,
                                selectedMovie,
                                selectedShow,
                                selectedHall,
                                isVIP),
                () ->
                        new PaymentController(
                                stage,
                                ctx,
                                nav,
                                currentUser,
                                selectedMovie,
                                selectedShow,
                                selectedHall,
                                new ArrayList<>(selectedSeats),
                                seatPrice));
    }

    private static void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
