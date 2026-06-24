package ui.view.admin;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import ui.common.Sidebar;

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
    public Button btnSeats;
    public Label welcomeLabel;

    public BorderPane getView() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("page");

        Sidebar sidebar =
                new Sidebar(
                        "ADMIN PANEL",
                        "\uD83D\uDCCA  Dashboard",
                        "\uD83C\uDFAC  Movies",
                        "\uD83D\uDD50  Shows",
                        "\uD83C\uDFDB\uFE0F  Halls",
                        "\uD83D\uDCBA  Seats",
                        "\uD83C\uDFAB  Bookings",
                        "\uD83D\uDCB3  Payments",
                        "\uD83D\uDC65  Users");

        btnDashboard = sidebar.getMenuButton(0);
        btnMovies = sidebar.getMenuButton(1);
        btnShows = sidebar.getMenuButton(2);
        btnHalls = sidebar.getMenuButton(3);
        btnSeats = sidebar.getMenuButton(4);
        btnBookings = sidebar.getMenuButton(5);
        btnPayments = sidebar.getMenuButton(6);
        btnUsers = sidebar.getMenuButton(7);

        btnLogout = sidebar.getLogoutButton();

        HBox topBar = new HBox();
        topBar.getStyleClass().add("align-center-left");
        topBar.getStyleClass().add("p-20-30");
        topBar.getStyleClass().add("top-bar");

        welcomeLabel = new Label("Welcome, User");
        welcomeLabel.getStyleClass().add("subtitle");

        Region topSpacer = new Region();
        HBox.setHgrow(topSpacer, Priority.ALWAYS);
        HBox adminBadge = new HBox(8);
        adminBadge.getStyleClass().add("align-center");
        adminBadge.getStyleClass().add("p-6-12");
        adminBadge.getStyleClass().add("badge");
        Label adminIcon = new Label("\uD83D\uDC68\u200D\uD83D\uDCBC");
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
}
