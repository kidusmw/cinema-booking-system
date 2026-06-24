package ui.view.admin;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class AdminDashboardPage {
    public VBox contentArea;
    public Button btnDashboard;
    public Button btnMovies;
    public Button btnShows;
    public Button btnHalls;
    public Button btnBookings;
    public Button btnPayments;
    public Button btnUsers;
    public Button btnLogout;
    public Button btnSeats; // ✅ NEW
    public Label welcomeLabel;

    public BorderPane getView() {

        BorderPane root = new BorderPane();
        root.getStyleClass().add("page");
        VBox sidebar = new VBox(5);
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPadding(new Insets(20, 12, 20, 12));
        HBox brandBox = new HBox(8);
        brandBox.setAlignment(Pos.CENTER_LEFT);
        brandBox.setPadding(new Insets(0, 0, 20, 8));

        StackPane logo = new StackPane();
        logo.setPrefSize(32, 32);
        logo.getStyleClass().add("logo-badge");
        Label logoIcon = new Label("🎬");
        logo.getChildren().add(logoIcon);

        Label brand = new Label("CinemaBook");
        brand.getStyleClass().add("section-title");

        brandBox.getChildren().addAll(logo, brand);
        Label menuLabel = new Label("ADMIN PANEL");
        menuLabel.getStyleClass().add("muted-text");
        menuLabel.setPadding(new Insets(10, 8, 10, 8));
        btnDashboard = createMenuButton("📊  Dashboard");
        btnMovies = createMenuButton("🎬  Movies");
        btnShows = createMenuButton("🕐  Shows");
        btnHalls = createMenuButton("🏛️  Halls");
        btnBookings = createMenuButton("🎫  Bookings");
        btnPayments = createMenuButton("💳  Payments");
        btnUsers = createMenuButton("👥  Users");

        btnSeats = createMenuButton("💺  Seats");
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        btnLogout = new Button("🚪  Logout");
        btnLogout.setPrefWidth(Double.MAX_VALUE);
        btnLogout.setPrefHeight(40);
        btnLogout.getStyleClass().add("logout-button");

        sidebar.getChildren()
                .addAll(
                        brandBox,
                        menuLabel,
                        btnDashboard,
                        btnMovies,
                        btnShows,
                        btnHalls,
                        btnSeats,
                        btnBookings,
                        btnPayments,
                        btnUsers,
                        spacer,
                        btnLogout);
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(20, 30, 20, 30));
        topBar.getStyleClass().add("top-bar");

        welcomeLabel = new Label("Welcome, User");
        welcomeLabel.getStyleClass().add("subtitle");

        Region topSpacer = new Region();
        HBox.setHgrow(topSpacer, Priority.ALWAYS);
        HBox adminBadge = new HBox(8);
        adminBadge.setAlignment(Pos.CENTER);
        adminBadge.setPadding(new Insets(6, 12, 6, 12));
        adminBadge.getStyleClass().add("badge");
        Label adminIcon = new Label("👨‍💼");
        Label adminText = new Label("Administrator");
        adminText.getStyleClass().addAll("caption", "text-accent");
        adminBadge.getChildren().addAll(adminIcon, adminText);

        topBar.getChildren().addAll(welcomeLabel, topSpacer, adminBadge);
        contentArea = new VBox();
        contentArea.getStyleClass().add("content-area");
        VBox mainArea = new VBox();
        mainArea.getChildren().addAll(topBar, contentArea);
        VBox.setVgrow(contentArea, Priority.ALWAYS);
        root.setLeft(sidebar);
        root.setCenter(mainArea);

        return root;
    }

    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(Double.MAX_VALUE);
        btn.setPrefHeight(40);
        btn.getStyleClass().add("menu-button");

        return btn;
    }
}
