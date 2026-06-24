package ui.controller.admin;

import application.AppContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ui.common.WindowManager;
import ui.controller.common.NavigationManager;
import ui.controller.common.WelcomeController;
import ui.view.admin.AdminDashboardPage;

public class AdminDashboardController {

    private AdminDashboardPage view;
    private Stage stage;
    private String adminName;
    private AppContext ctx;
    private NavigationManager nav;

    public AdminDashboardController(
            Stage stage, AppContext ctx, NavigationManager nav, String adminName) {
        this.stage = stage;
        this.ctx = ctx;
        this.nav = nav;
        this.adminName = adminName;
        this.view = new AdminDashboardPage();
        WindowManager.configureStage(
                stage, "Admin Dashboard - CinemaBook", view.getView(), 1200, 750);

        view.welcomeLabel.setText("Welcome, " + adminName);

        view.btnLogout.setOnAction(e -> nav.goFresh(() -> new WelcomeController(stage, ctx, nav)));

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
        dashboardContent.getStyleClass().add("p-30");

        Label title = new Label("Dashboard Overview");
        title.getStyleClass().add("title");

        HBox statsBox = new HBox(20);
        statsBox.getChildren()
                .addAll(
                        createStatCard("Movies", "Total Movies", "11", "accent"),
                        createStatCard("Bookings", "Total Bookings", "15", "success"),
                        createStatCard("Revenue", "Revenue", "0.00birr", "warning"),
                        createStatCard("Users", "Total Users", "9", "purple"));

        dashboardContent.getChildren().addAll(title, statsBox);
        view.contentArea.getChildren().add(dashboardContent);
    }

    private void showMovies() {
        setActiveMenu(view.btnMovies);
        nav.go(
                () -> new AdminDashboardController(stage, ctx, nav, adminName),
                () -> new MovieManagementController(stage, ctx, nav));
    }

    private void showShows() {
        setActiveMenu(view.btnShows);
        ShowManagmentController c = new ShowManagmentController(ctx, this);
        injectView(c.getRootView());
    }

    private void showHalls() {
        setActiveMenu(view.btnHalls);
        MoviehallManagmentController c = new MoviehallManagmentController(stage, ctx, nav);
        injectView(c.getRootView());
    }

    private void showSeats() {
        setActiveMenu(view.btnSeats);
        SeatManagmentController c = new SeatManagmentController(ctx, nav);
        injectView(c.getRootView());
    }

    private void showBookings() {
        setActiveMenu(view.btnBookings);
        BookingManagmentController c = new BookingManagmentController(ctx, this);
        injectView(c.getRootView());
    }

    private void showPayments() {
        setActiveMenu(view.btnPayments);
        PaymentManagmentController c = new PaymentManagmentController(ctx, this);
        injectView(c.getRootView());
    }

    private void showUsers() {
        setActiveMenu(view.btnUsers);
        UserManagmentController c = new UserManagmentController(ctx, this);
        injectView(c.getRootView());
    }

    private void setActiveMenu(Button activeBtn) {
        Button[] allButtons = {
            view.btnDashboard, view.btnMovies, view.btnShows,
            view.btnHalls, view.btnSeats, view.btnBookings,
            view.btnPayments, view.btnUsers
        };

        for (Button btn : allButtons) {
            btn.getStyleClass().remove("sidebar-btn-active");
        }
        activeBtn.getStyleClass().add("sidebar-btn-active");
    }

    private VBox createStatCard(String icon, String label, String value, String colorClass) {
        VBox card = new VBox(8);
        card.getStyleClass().add("align-center-left");
        card.getStyleClass().add("p-20");
        card.getStyleClass().add("w-220");
        card.getStyleClass().add("stat-card");

        Label iconLabel = new Label(icon);
        iconLabel.getStyleClass().add("stat-icon");

        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().addAll("stat-value", "text-" + colorClass);

        Label nameLabel = new Label(label);
        nameLabel.getStyleClass().add("stat-label");

        card.getChildren().addAll(iconLabel, valueLabel, nameLabel);
        return card;
    }

    public javafx.scene.Parent getRootView() {
        return view.getView();
    }
}
