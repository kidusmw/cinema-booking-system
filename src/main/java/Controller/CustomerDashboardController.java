package Controller;

import Model.Booking;
import Model.Customer;
import View.CustomerDashboardPage;
import application.AppContext;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.geometry.*;

import java.util.List;
import static ui.common.Theme.*;

public class CustomerDashboardController {
    private CustomerDashboardPage view;
    private Stage stage;
    private Customer currentUser;
    private AppContext ctx;

    public CustomerDashboardController(Stage stage, AppContext ctx, Customer currentUser) {
        this.stage = stage;
        this.ctx = ctx;
        this.currentUser = currentUser;
        this.view = new CustomerDashboardPage();

        Scene scene = new Scene(view.getView(), 1200, 750);
        stage.setTitle("CinemaBook - Customer");
        stage.setScene(scene);
        stage.show();
        view.welcomeLabel.setText("Welcome, " + currentUser.getFirstName() + "!");
        view.btnBrowseMovies.setOnAction(e -> {
            new MovieBrowserController(stage, ctx, currentUser);
        });
        view.btnMyBookings.setOnAction(e -> {
            showMyBookings();
        });
        view.btnLogout.setOnAction(e -> {
            NavigationManager.clear();
            new WelcomeController(stage, ctx);
        });
    }

    private void showMyBookings() {
        List<domain.model.Booking> history = ctx.bookingService.getHistory((long) currentUser.getUserID());
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
                sb.append("Movie: ").append(b.getMovieName() != null ? b.getMovieName() : "N/A").append("\n");
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
