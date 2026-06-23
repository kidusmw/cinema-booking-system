package ui.controller;

import application.AppContext;
import java.text.SimpleDateFormat;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import ui.model.*;
import ui.view.TicketPage;

public class TicketController {
    private TicketPage view;
    private Stage stage;
    private Customer currentUser;
    private Movie selectedMovie;
    private Show selectedShow;
    private Moviehall selectedHall;
    private List<String> selectedSeatIds;
    private double totalAmount;
    private List<String> bookingIds;
    private AppContext ctx;
    private NavigationManager nav;
    private static final String TEXT_DARK = "#1E293B";
    private static final String TEXT_MUTED = "#64748B";
    private static final String BORDER = "#E2E8F0";
    private static final String WHITE = "#FFFFFF";
    private static final String BG = "#FAFAFA";

    public TicketController(
            Stage stage,
            AppContext ctx,
            NavigationManager nav,
            Customer currentUser,
            Movie selectedMovie,
            Show selectedShow,
            Moviehall selectedHall,
            List<String> selectedSeatIds,
            double totalAmount,
            List<String> bookingIds) {
        this.stage = stage;
        this.ctx = ctx;
        this.nav = nav;
        this.currentUser = currentUser;
        this.selectedMovie = selectedMovie;
        this.selectedShow = selectedShow;
        this.selectedHall = selectedHall;
        this.selectedSeatIds = selectedSeatIds;
        this.totalAmount = totalAmount;
        this.bookingIds = bookingIds;
        this.view = new TicketPage();

        Scene scene = new Scene(view.getView(), 900, 800);
        stage.setTitle("Your Ticket - CinemaBook");
        stage.setScene(scene);
        stage.show();
        populateTicket();
        view.btnDownload.setOnAction(
                e -> {
                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                    info.setTitle("Download");
                    info.setContentText("Ticket download feature (screenshot for now!)");
                    info.showAndWait();
                });

        view.btnHome.setOnAction(
                e -> {
                    nav.goFresh(
                            () -> new CustomerDashboardController(stage, ctx, nav, currentUser));
                });

        view.btnMyBookings.setOnAction(
                e -> {
                    nav.goFresh(
                            () -> new CustomerDashboardController(stage, ctx, nav, currentUser));
                });
    }

    private void populateTicket() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        view.successLabel.setText("🎉 Booking Confirmed!");
        view.movieTitle.setText(selectedMovie.getTitle());
        view.movieGenre.setText(selectedMovie.getGenre());
        view.dateTimeLabel.setText(
                sdf.format(selectedShow.getShowDate()) + " • " + selectedShow.getShowTime());
        view.hallLabel.setText(selectedHall.getName());
        StringBuilder seatList = new StringBuilder();
        if (selectedSeatIds != null && !selectedSeatIds.isEmpty()) {
            for (int i = 0; i < selectedSeatIds.size(); i++) {
                String seatId = selectedSeatIds.get(i);
                String seatNumber = seatId.substring(seatId.lastIndexOf("-") + 1);
                seatList.append(seatNumber);
                if (i < selectedSeatIds.size() - 1) seatList.append(", ");
            }
        } else {
            seatList.append("No seats selected");
        }
        view.seatsLabel.setText(seatList.toString());
        view.customerLabel.setText(currentUser.getFirstName() + " " + currentUser.getLastName());

        if (bookingIds != null && !bookingIds.isEmpty()) {
            if (bookingIds.size() > 1) {
                view.bookingIdLabel.setText(
                        bookingIds.get(0) + " (+" + (bookingIds.size() - 1) + " more)");
            } else {
                view.bookingIdLabel.setText(bookingIds.get(0));
            }
        } else {
            view.bookingIdLabel.setText("N/A");
        }
        view.totalLabel.setText(String.format("%.2f ETB", totalAmount));
        SimpleDateFormat timeFormat = new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a");
        view.bookedOnLabel.setText(timeFormat.format(new java.util.Date()));
    }
}
