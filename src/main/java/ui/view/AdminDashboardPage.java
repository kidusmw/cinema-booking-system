package ui.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import static ui.common.Theme.*;

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
    public Button btnSeats;  // ✅ NEW
    public Label welcomeLabel;

    private static final String BG = "#FAFAFA";
    private static final String SIDEBAR_BG = "#FFFFFF";
    private static final String HOVER = "#EC4899";


    public BorderPane getView() {

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BG + ";");
        VBox sidebar = new VBox(5);
        sidebar.setPrefWidth(240);
        sidebar.setStyle(
                "-fx-background-color: " + SIDEBAR_BG + ";" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-width: 0 1 0 0;"
        );
        sidebar.setPadding(new Insets(20, 12, 20, 12));
        HBox brandBox = new HBox(8);
        brandBox.setAlignment(Pos.CENTER_LEFT);
        brandBox.setPadding(new Insets(0, 0, 20, 8));

        StackPane logo = new StackPane();
        logo.setPrefSize(32, 32);
        logo.setStyle(
                "-fx-background-color: " + ACCENT + ";" +
                        "-fx-background-radius: 8;"
        );
        Label logoIcon = new Label("🎬");
        logoIcon.setFont(Font.font(16));
        logo.getChildren().add(logoIcon);

        Label brand = new Label("CinemaBook");
        brand.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        brand.setTextFill(Color.web(TEXT_DARK));

        brandBox.getChildren().addAll(logo, brand);
        Label menuLabel = new Label("ADMIN PANEL");
        menuLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));
        menuLabel.setTextFill(Color.web(TEXT_MUTED));
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
        btnLogout.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        btnLogout.setPrefWidth(Double.MAX_VALUE);
        btnLogout.setPrefHeight(40);
        btnLogout.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: " + TEXT_MUTED + ";" +
                        "-fx-background-radius: 8;" +
                        "-fx-alignment: center-left;" +
                        "-fx-cursor: hand;"
        );
        btnLogout.setOnMouseEntered(e -> {
            btnLogout.setStyle(
                    "-fx-background-color: #FEE2E2;" +
                            "-fx-text-fill: #DC2626;" +
                            "-fx-background-radius: 8;" +
                            "-fx-alignment: center-left;" +
                            "-fx-cursor: hand;"
            );
        });
        btnLogout.setOnMouseExited(e -> {
            btnLogout.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-text-fill: " + TEXT_MUTED + ";" +
                            "-fx-background-radius: 8;" +
                            "-fx-alignment: center-left;"
            );
        });

        sidebar.getChildren().addAll(
                brandBox,
                menuLabel,
                btnDashboard, btnMovies, btnShows, btnHalls, btnSeats,
                btnBookings, btnPayments, btnUsers,
                spacer,
                btnLogout
        );
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(20, 30, 20, 30));
        topBar.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-width: 0 0 1 0;"
        );

        welcomeLabel = new Label("Welcome, Admin");
        welcomeLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        welcomeLabel.setTextFill(Color.web(TEXT_DARK));

        Region topSpacer = new Region();
        HBox.setHgrow(topSpacer, Priority.ALWAYS);
        HBox adminBadge = new HBox(8);
        adminBadge.setAlignment(Pos.CENTER);
        adminBadge.setPadding(new Insets(6, 12, 6, 12));
        adminBadge.setStyle(
                "-fx-background-color: #FCE7F3;" +
                        "-fx-background-radius: 20;"
        );
        Label adminIcon = new Label("👨‍💼");
        Label adminText = new Label("Administrator");
        adminText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        adminText.setTextFill(Color.web(ACCENT));
        adminBadge.getChildren().addAll(adminIcon, adminText);

        topBar.getChildren().addAll(welcomeLabel, topSpacer, adminBadge);
        contentArea = new VBox();
        contentArea.setStyle("-fx-background-color: " + BG + ";");
        VBox mainArea = new VBox();
        mainArea.getChildren().addAll(topBar, contentArea);
        VBox.setVgrow(contentArea, Priority.ALWAYS);
        root.setLeft(sidebar);
        root.setCenter(mainArea);

        return root;
    }

    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        btn.setPrefWidth(Double.MAX_VALUE);
        btn.setPrefHeight(40);
        btn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: " + TEXT_MUTED + ";" +
                        "-fx-background-radius: 8;" +
                        "-fx-alignment: center-left;" +
                        "-fx-cursor: hand;"
        );

        String originalStyle = btn.getStyle();
        btn.setOnMouseEntered(e -> {
            if (!btn.getStyle().contains(ACCENT)) {
                btn.setStyle(
                        "-fx-background-color: #FCE7F3;" +
                                "-fx-text-fill: " + ACCENT + ";" +
                                "-fx-background-radius: 8;" +
                                "-fx-alignment: center-left;" +
                                "-fx-cursor: hand;"
                );
            }
        });
        btn.setOnMouseExited(e -> {
            if (!btn.getStyle().contains(ACCENT)) {
                btn.setStyle(originalStyle);
            }
        });

        return btn;
    }
}
