package ui.view.customer;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import ui.common.Sidebar;

public class CustomerDashboardPage {

    public Label welcomeLabel;
    public Button btnBrowseMovies;
    public Button btnMyBookings;
    public Button btnLogout;
    public Button btnDashboard;
    public VBox contentArea;

    private BorderPane root;

    public CustomerDashboardPage() {
        createUI();
    }

    private void createUI() {
        root = new BorderPane();
        root.getStyleClass().add("page");

        Sidebar sidebar =
                new Sidebar(
                        "CUSTOMER MENU",
                        "\uD83D\uDCCA  Dashboard",
                        "\uD83C\uDFAC  Browse Movies",
                        "\uD83C\uDFAB  My Bookings");

        btnDashboard = (Button) sidebar.getChildren().get(2);
        btnBrowseMovies = (Button) sidebar.getChildren().get(3);
        btnMyBookings = (Button) sidebar.getChildren().get(4);
        btnLogout = (Button) sidebar.getChildren().get(sidebar.getChildren().size() - 1);

        HBox topBar = new HBox(15);
        topBar.getStyleClass().add("align-center-left");
        topBar.getStyleClass().add("p-20-30");
        topBar.getStyleClass().add("top-bar");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        welcomeLabel = new Label("Welcome, User!");
        welcomeLabel.getStyleClass().addAll("body-text", "muted-text");

        topBar.getChildren().addAll(spacer, welcomeLabel);

        contentArea = new VBox(20);
        contentArea.getStyleClass().add("p-40");
        contentArea.getStyleClass().add("align-center");
        VBox heroBox = new VBox(15);
        heroBox.getStyleClass().add("align-center");
        heroBox.getStyleClass().add("p-40-20");

        Label welcomeText = new Label("\uD83C\uDFAC Ready to watch a movie?");
        welcomeText.getStyleClass().add("welcome-title");

        Label subtitle = new Label("Browse the latest movies and book your seats");
        subtitle.getStyleClass().add("muted-text");

        btnBrowseMovies = new Button("\uD83C\uDFAC  Browse Movies");
        btnBrowseMovies.getStyleClass().add("h-55");
        btnBrowseMovies.getStyleClass().add("w-280");
        btnBrowseMovies.getStyleClass().add("primary-button");

        heroBox.getChildren().addAll(welcomeText, subtitle, btnBrowseMovies);
        HBox quickActions = new HBox(20);
        quickActions.getStyleClass().add("align-center");

        btnMyBookings = createActionCard("\uD83C\uDFAB", "My Bookings", "View your tickets");
        quickActions.getChildren().add(btnMyBookings);

        contentArea.getChildren().addAll(heroBox, quickActions);

        VBox mainArea = new VBox();
        mainArea.getChildren().addAll(topBar, contentArea);
        VBox.setVgrow(contentArea, Priority.ALWAYS);
        root.setLeft(sidebar);
        root.setCenter(mainArea);
    }

    public BorderPane getView() {
        return root;
    }

    private static Button createActionCard(String icon, String title, String description) {
        Button btn = new Button();
        btn.setPrefSize(220, 130);
        btn.getStyleClass().add("card");

        VBox content = new VBox(8);
        content.getStyleClass().add("align-center");
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
