package ui.controller.customer;

import static ui.common.Theme.*;

import application.AppContext;
import domain.model.User;
import java.util.List;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import ui.controller.common.NavigationManager;
import ui.controller.common.WelcomeController;
import ui.view.customer.CustomerDashboardPage;

public class CustomerDashboardController {
    private CustomerDashboardPage view;
    private Stage stage;
    private User currentUser;
    private AppContext ctx;
    private NavigationManager nav;

    public CustomerDashboardController(
            Stage stage, AppContext ctx, NavigationManager nav, User currentUser) {
        this.stage = stage;
        this.ctx = ctx;
        this.nav = nav;
        this.currentUser = currentUser;
        this.view = new CustomerDashboardPage();

        Scene scene = new Scene(view.getView(), 1200, 750);
        stage.setTitle("CinemaBook - User");
        stage.setScene(scene);
        stage.show();
        view.welcomeLabel.setText("Welcome, " + currentUser.getFirstName() + "!");
        view.btnBrowseMovies.setOnAction(
                e ->
                        nav.go(
                                () -> new CustomerDashboardController(stage, ctx, nav, currentUser),
                                () -> new MovieBrowserController(stage, ctx, nav, currentUser)));
        view.btnMyBookings.setOnAction(
                e -> {
                    showMyBookings();
                });
        view.btnLogout.setOnAction(e -> nav.goFresh(() -> new WelcomeController(stage, ctx, nav)));
    }

    private void showMyBookings() {
        List<domain.model.Booking> history = ctx.bookingService.getHistory(currentUser.getUserId());
        if (history.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("My Bookings");
            alert.setHeaderText(null);
            alert.setContentText("You haven't booked any tickets yet!");
            alert.showAndWait();
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("YOUR BOOKING HISTORY:\n");
            sb.append("===============================\n\n");

            for (domain.model.Booking b : history) {
                sb.append("Movie: ")
                        .append(b.getMovieName() != null ? b.getMovieName() : "N/A")
                        .append("\n");
                sb.append("Date: ").append(b.getBookingDate()).append("\n");
                sb.append("Booking ID: ").append(b.getBookingId()).append("\n");
                sb.append("Status: ").append(b.getBookingStatus()).append("\n");
                sb.append("-------------------------------------------\n");
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("My Bookings");
            alert.setHeaderText("Active Tickets Found (" + history.size() + ")");
            alert.setContentText(sb.toString());
            alert.showAndWait();
        }
    }
}
