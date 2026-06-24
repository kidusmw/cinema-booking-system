package ui.view.customer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class CustomerDashboardPage {
    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(CustomerDashboardPage.class);

    public Label welcomeLabel;
    public Button btnBrowseMovies;
    public Button btnMyBookings;
    public Button btnLogout;
    public VBox contentArea;

    private VBox root;

    public CustomerDashboardPage() {
        try {
            createUI();
        } catch (Exception e) {
            log.error("Failed to create CustomerDashboardPage UI", e);
            root = new VBox();
            root.getChildren().add(new Label("Error: " + e.getMessage()));
        }
    }

    private void createUI() {
        root = new VBox();
        root.getStyleClass().add("page");

        // ===== TOP BAR =====
        HBox topBar = new HBox(15);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(20, 30, 20, 30));
        topBar.getStyleClass().add("top-bar");

        HBox brandBox = new HBox(8);
        brandBox.setAlignment(Pos.CENTER_LEFT);
        StackPane logo = new StackPane();
        logo.setPrefSize(32, 32);
        logo.getStyleClass().add("logo-badge");
        Label logoIcon = new Label("🎬");
        logo.getChildren().add(logoIcon);
        Label brand = new Label("CinemaBook");
        brand.getStyleClass().add("subtitle");
        brandBox.getChildren().addAll(logo, brand);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        welcomeLabel = new Label("Welcome, User!");
        welcomeLabel.getStyleClass().addAll("body-text", "muted-text");

        btnLogout = new Button("🚪  Logout");
        btnLogout.getStyleClass().add("back-button");

        topBar.getChildren().addAll(brandBox, spacer, welcomeLabel, btnLogout);
        contentArea = new VBox(20);
        contentArea.setPadding(new Insets(40));
        contentArea.setAlignment(Pos.CENTER);
        VBox heroBox = new VBox(15);
        heroBox.setAlignment(Pos.CENTER);
        heroBox.setPadding(new Insets(40, 20, 40, 20));

        Label welcomeText = new Label("🎬 Ready to watch a movie?");
        welcomeText.getStyleClass().add("welcome-title");

        Label subtitle = new Label("Browse the latest movies and book your seats");
        subtitle.getStyleClass().add("muted-text");

        btnBrowseMovies = new Button("🎬  Browse Movies");
        btnBrowseMovies.setPrefHeight(55);
        btnBrowseMovies.setPrefWidth(280);
        btnBrowseMovies.getStyleClass().add("primary-button");

        heroBox.getChildren().addAll(welcomeText, subtitle, btnBrowseMovies);
        HBox quickActions = new HBox(20);
        quickActions.setAlignment(Pos.CENTER);

        btnMyBookings = createActionCard("🎫", "My Bookings", "View your tickets");
        quickActions.getChildren().add(btnMyBookings);

        contentArea.getChildren().addAll(heroBox, quickActions);

        root.getChildren().addAll(topBar, contentArea);
    }

    public VBox getView() {
        return root;
    }

    private Button createActionCard(String icon, String title, String description) {
        Button btn = new Button();
        btn.setPrefSize(220, 130);
        btn.getStyleClass().add("card");

        VBox content = new VBox(8);
        content.setAlignment(Pos.CENTER);
        Label iconLabel = new Label(icon);
        iconLabel.getStyleClass().add("icon-emoji");
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("body-text");
        Label descLabel = new Label(description);
        descLabel.getStyleClass().add("caption");
        content.getChildren().addAll(iconLabel, titleLabel, descLabel);
        btn.setGraphic(content);
        return btn;
    }
}
