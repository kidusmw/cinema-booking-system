package Controller;

import Model.Booking;
import Model.Customer;
import View.CustomerDashboardPage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.geometry.*;
import DAO.BookingDAOimp;

import java.util.List;

public class CustomerDashboardController {
    private CustomerDashboardPage view;
    private Stage stage;
    private Customer currentUser;
    private static final String ACCENT = "#DB2777";
    private static final String HOVER = "#EC4899";
    private static final String TEXT_DARK = "#1E293B";
    private static final String TEXT_MUTED = "#64748B";
    private static final String BORDER = "#E2E8F0";
    private static final String WHITE = "#FFFFFF";
    private static final String BG = "#FAFAFA";

    public CustomerDashboardController(Stage stage, Customer currentUser) {
        this.stage = stage;
        this.currentUser = currentUser;
        this.view = new CustomerDashboardPage();

        Scene scene = new Scene(view.getView(), 1200, 750);
        stage.setTitle("CinemaBook - Customer");
        stage.setScene(scene);
        stage.show();
        view.welcomeLabel.setText("Welcome, " + currentUser.getFirstName() + "!");
        view.btnBrowseMovies.setOnAction(e -> {
            MovieBrowserController browser = new MovieBrowserController(stage, currentUser);
        });
        view.btnMyBookings.setOnAction(e -> {
            showMyBookings();
        });
        view.btnLogout.setOnAction(e -> {
            NavigationManager.clear();
            new WelcomeController(stage);
        });
    }

    private void showMyBookings() {
        int currentUserId = currentUser.getUserID();
        BookingDAOimp bookingDAO = new BookingDAOimp();
        List<Booking> history = bookingDAO.getBookingsByUserId(currentUserId);
        if (history.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("My Bookings");
            alert.setHeaderText(null);
            alert.setContentText("You haven't booked any tickets yet!");
            alert.showAndWait();
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("🎟️ YOUR BOOKING HISTORY:\n");
            sb.append("===============================\n\n");

            for (Booking b : history) {
                sb.append("🎬 Movie: ").append(b.getMovieName()).append("\n");
                sb.append("📅 Date: ").append(b.getBookingDate()).append("\n");
                sb.append("🆔 Booking ID: ").append(b.getBookingID()).append("\n");
                sb.append("🚦 Status: ").append(b.getBookingStatus()).append("\n");
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
