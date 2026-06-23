package ui.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import static ui.common.Theme.*;

public class CustomerDashboardPage {

    public Label welcomeLabel;
    public Button btnBrowseMovies;
    public Button btnMyBookings;
    public Button btnLogout;
    public VBox contentArea;

    private VBox root;

    private static final String HOVER = "#EC4899";
    private static final String WHITE = "#FFFFFF";
    private static final String BG = "#FAFAFA";
    private static final String BG_LIGHT = "#F8FAFC";

    public CustomerDashboardPage() {
        try {
            createUI();
        } catch (Exception e) {
            e.printStackTrace();
            root = new VBox();
            root.getChildren().add(new Label("Error: " + e.getMessage()));
        }
    }

    private void createUI() {
        root = new VBox();
        root.setStyle("-fx-background-color: " + BG + ";");

        // ===== TOP BAR =====
        HBox topBar = new HBox(15);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(20, 30, 20, 30));
        topBar.setStyle(
                "-fx-background-color: " + WHITE + ";" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-width: 0 0 1 0;"
        );

        HBox brandBox = new HBox(8);
        brandBox.setAlignment(Pos.CENTER_LEFT);
        StackPane logo = new StackPane();
        logo.setPrefSize(32, 32);
        logo.setStyle("-fx-background-color: " + ACCENT + "; -fx-background-radius: 8;");
        Label logoIcon = new Label("🎬");
        logoIcon.setFont(Font.font(16));
        logo.getChildren().add(logoIcon);
        Label brand = new Label("CinemaBook");
        brand.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        brand.setTextFill(Color.web(TEXT_DARK));
        brandBox.getChildren().addAll(logo, brand);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        welcomeLabel = new Label("Welcome, Customer!");
        welcomeLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        welcomeLabel.setTextFill(Color.web(TEXT_MUTED));

        btnLogout = new Button("🚪  Logout");
        btnLogout.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        btnLogout.setTextFill(Color.web(TEXT_MUTED));
        btnLogout.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 8 16;"
        );

        topBar.getChildren().addAll(brandBox, spacer, welcomeLabel, btnLogout);
        contentArea = new VBox(20);
        contentArea.setPadding(new Insets(40));
        contentArea.setAlignment(Pos.CENTER);
        VBox heroBox = new VBox(15);
        heroBox.setAlignment(Pos.CENTER);
        heroBox.setPadding(new Insets(40, 20, 40, 20));

        Label welcomeText = new Label("🎬 Ready to watch a movie?");
        welcomeText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        welcomeText.setTextFill(Color.web(TEXT_DARK));

        Label subtitle = new Label("Browse the latest movies and book your seats");
        subtitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 16));
        subtitle.setTextFill(Color.web(TEXT_MUTED));

        btnBrowseMovies = new Button("🎬  Browse Movies");
        btnBrowseMovies.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        btnBrowseMovies.setPrefHeight(55);
        btnBrowseMovies.setPrefWidth(280);
        btnBrowseMovies.setTextFill(Color.WHITE);
        btnBrowseMovies.setStyle(
                "-fx-background-color: " + ACCENT + ";" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;"
        );
        btnBrowseMovies.setOnMouseEntered(e -> btnBrowseMovies.setStyle(
                "-fx-background-color: " + HOVER + ";" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;" +
                        "-fx-text-fill: white;"
        ));
        btnBrowseMovies.setOnMouseExited(e -> btnBrowseMovies.setStyle(
                "-fx-background-color: " + ACCENT + ";" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;" +
                        "-fx-text-fill: white;"
        ));

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
        btn.setStyle(
                "-fx-background-color: " + WHITE + ";" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 12;" +
                        "-fx-background-radius: 12;" +
                        "-fx-cursor: hand;"
        );

        VBox content = new VBox(8);
        content.setAlignment(Pos.CENTER);
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(36));
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        titleLabel.setTextFill(Color.web(TEXT_DARK));
        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 11));
        descLabel.setTextFill(Color.web(TEXT_MUTED));
        content.getChildren().addAll(iconLabel, titleLabel, descLabel);
        btn.setGraphic(content);
        return btn;
    }
}
