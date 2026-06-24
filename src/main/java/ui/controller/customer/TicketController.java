package ui.controller.customer;

import application.AppContext;
import domain.model.*;
import java.text.SimpleDateFormat;
import java.util.List;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ui.common.WindowManager;
import ui.controller.common.NavigationManager;
import ui.view.customer.TicketPage;

public class TicketController {
    private static final Logger log = LoggerFactory.getLogger(TicketController.class);
    private TicketPage view;
    private User currentUser;
    private Movie selectedMovie;
    private Showtime selectedShow;
    private Hall selectedHall;
    private List<String> selectedSeatIds;
    private double totalAmount;
    private List<String> bookingIds;

    public TicketController(
            Stage stage,
            AppContext ctx,
            NavigationManager nav,
            User currentUser,
            Movie selectedMovie,
            Showtime selectedShow,
            Hall selectedHall,
            List<String> selectedSeatIds,
            double totalAmount,
            List<String> bookingIds) {
        this.currentUser = currentUser;
        this.selectedMovie = selectedMovie;
        this.selectedShow = selectedShow;
        this.selectedHall = selectedHall;
        this.selectedSeatIds = selectedSeatIds;
        this.totalAmount = totalAmount;
        this.bookingIds = bookingIds;
        this.view = new TicketPage();

        WindowManager.configure(stage, "Your Ticket", view.getView());
        log.info("Opening Your Ticket page");
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
        view.successLabel.setText("🎉 Booking Confirmed!");
        view.movieTitle.setText(selectedMovie.getTitle());
        view.movieGenre.setText(selectedMovie.getGenre());
        view.dateTimeLabel.setText(
                selectedShow.getShowDate().toString()
                        + " • "
                        + selectedShow.getShowTime().toString());
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
        view.totalLabel.setText(String.format("%.2f ETB", Double.valueOf(totalAmount)));
        SimpleDateFormat timeFormat = new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a");
        view.bookedOnLabel.setText(timeFormat.format(new java.util.Date()));
    }
}
