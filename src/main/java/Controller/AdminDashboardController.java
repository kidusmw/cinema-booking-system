package Controller;

import DAO.MovieHallDAO;
import Model.Moviehall;
import View.AdminDashboardPage;
import Controller.WelcomeController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import java.util.List;
import javafx.scene.Parent;
public class AdminDashboardController {

    private AdminDashboardPage view;
    private Stage stage;
    private String adminName;

    private static final String ACCENT = "#DB2777";
    private static final String TEXT_DARK = "#1E293B";
    private static final String TEXT_MUTED = "#64748B";
    private static final String BORDER = "#E2E8F0";

    public Parent getView() {
        return view.getView();
    }

    public AdminDashboardController(Stage stage, String adminName) {
        this.stage = stage;
        this.adminName = adminName;
        this.view = new AdminDashboardPage();

        Scene scene = new Scene(view.getView(), 1200, 750);
        stage.setTitle("Admin Dashboard - CinemaBook");
        stage.setScene(scene);
        stage.show();

        view.welcomeLabel.setText("Welcome, " + adminName);

        view.btnLogout.setOnAction(e -> {
            NavigationManager.clear();
            new WelcomeController(stage);
        });

        view.btnDashboard.setOnAction(e -> showDashboard());
        view.btnMovies.setOnAction(e -> showMovies());
        view.btnShows.setOnAction(e -> showShows());
        view.btnHalls.setOnAction(e -> showHalls());
        view.btnSeats.setOnAction(e -> showSeats());
        view.btnBookings.setOnAction(e -> showBookings());
        view.btnPayments.setOnAction(e -> showPayments());
        view.btnUsers.setOnAction(e -> showUsers());
        showDashboard();
    }

    public void injectView(javafx.scene.Parent content) {
        view.contentArea.getChildren().clear();
        view.contentArea.getChildren().add(content);
    }

    public String getAdminName() {
        return this.adminName;
    }

    public void showDashboard() {
        setActiveMenu(view.btnDashboard);
        view.contentArea.getChildren().clear();

        VBox dashboardContent = new VBox(20);
        dashboardContent.setPadding(new Insets(30));

        Label title = new Label("Dashboard Overview");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        title.setTextFill(Color.web(TEXT_DARK));

        HBox statsBox = new HBox(20);
        statsBox.getChildren().addAll(
                createStatCard("🎬", "Total Movies", "11", ACCENT),
                createStatCard("🎫", "Total Bookings", "15", "#10B981"),
                createStatCard("💰", "Revenue", "0.00birr", "#F59E0B"),
                createStatCard("👥", "Total Users", "9", "#8B5CF6")
        );

        dashboardContent.getChildren().addAll(title, statsBox);
        view.contentArea.getChildren().add(dashboardContent);
    }

    private void showMovies() {
        setActiveMenu(view.btnMovies);
        new MovieManagmentContrller(stage, this);
    }

    private void showShows() {
        setActiveMenu(view.btnShows);
        ShowManagmentController c = new ShowManagmentController(stage, this);
        injectView(c.getRootView());
    }

    private void showHalls() {
        setActiveMenu(view.btnHalls);
        MoviehallManagmentController c = new MoviehallManagmentController(stage, this);
        injectView(c.getRootView());
    }

    private void showSeats() {
        setActiveMenu(view.btnSeats);
        SeatManagmentController c = new SeatManagmentController(stage, this);
        injectView(c.getRootView());
    }

    private void showBookings() {
        setActiveMenu(view.btnBookings);
        BookingManagmentController c = new BookingManagmentController(stage, this);
        injectView(c.getRootView());
    }

    private void showPayments() {
        setActiveMenu(view.btnPayments);
        PaymentManagmentController c = new PaymentManagmentController(stage, this);
        injectView(c.getRootView());
    }

    private void showUsers() {
        setActiveMenu(view.btnUsers);
        UserManagmentController c = new UserManagmentController(stage, this);
        injectView(c.getRootView());
    }
    private void setActiveMenu(Button activeBtn) {
        Button[] allButtons = {
                view.btnDashboard, view.btnMovies, view.btnShows,
                view.btnHalls, view.btnSeats, view.btnBookings,
                view.btnPayments, view.btnUsers
        };

        for (Button btn : allButtons) {
            if (btn == activeBtn) {
                btn.setStyle("-fx-background-color: " + ACCENT + "; -fx-text-fill: white; -fx-background-radius: 8; -fx-alignment: center-left; -fx-cursor: hand;");
            } else {
                btn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXT_MUTED + "; -fx-background-radius: 8; -fx-alignment: center-left; -fx-cursor: hand;");
            }
        }
    }

    private VBox createStatCard(String icon, String label, String value, String color) {
        VBox card = new VBox(8);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(20));
        card.setPrefWidth(220);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-color: " + BORDER + "; -fx-border-radius: 12; -fx-border-width: 1;");
        card.setEffect(new DropShadow(5, Color.rgb(0, 0, 0, 0.04)));

        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(24));

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        valueLabel.setTextFill(Color.web(color));

        Label nameLabel = new Label(label);
        nameLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 12));
        nameLabel.setTextFill(Color.web(TEXT_MUTED));

        card.getChildren().addAll(iconLabel, valueLabel, nameLabel);
        return card;
    }
    public Parent getRootView() {
        return view.getView();
    }
}
