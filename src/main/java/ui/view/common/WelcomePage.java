package ui.view.common;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.*;

public class WelcomePage {

    public Button enterBtn;
    public MenuButton menu;

    public BorderPane getView() {

        BorderPane root = new BorderPane();
        root.getStyleClass().add("page");
        HBox topBar = new HBox();
        topBar.getStyleClass().add("align-center-left");
        topBar.getStyleClass().add("p-20-40");
        topBar.getStyleClass().add("top-bar");
        HBox brandBox = new HBox(8);
        brandBox.getStyleClass().add("align-center-left");

        StackPane logo = new StackPane();
        logo.setPrefSize(32, 32);
        logo.getStyleClass().add("logo-badge");
        Label logoIcon = new Label("🎬");
        logoIcon.getStyleClass().add("body-text");
        logo.getChildren().add(logoIcon);

        Label brand = new Label("CinemaBook");
        brand.getStyleClass().add("subtitle");

        brandBox.getChildren().addAll(logo, brand);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        menu = new MenuButton("☰");
        menu.getStyleClass().add("back-button");
        menu.getStyleClass().add("body-text");
        MenuItem about = new MenuItem("About");
        MenuItem complaint = new MenuItem("Send Feedback");
        MenuItem rate = new MenuItem("Rate Us");
        menu.getItems().addAll(about, complaint, rate);

        topBar.getChildren().addAll(brandBox, spacer, menu);
        VBox heroBox = new VBox(20);
        heroBox.getStyleClass().add("align-center");
        heroBox.getStyleClass().add("p-60-40");

        Label eyebrow = new Label("CINEMA BOOKING SYSTEM");
        eyebrow.getStyleClass().add("badge");

        Label title = new Label("Book your perfect\nmovie experience");
        title.getStyleClass().add("hero-title");
        title.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        Label subtitle =
                new Label(
                        "Browse movies, choose your seats, and enjoy the show.\nSimple, fast, and hassle-free booking.");
        subtitle.getStyleClass().add("muted-text");
        subtitle.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        enterBtn = new Button("Get Started  →");
        enterBtn.getStyleClass().add("w-200");
        enterBtn.getStyleClass().add("h-48");
        enterBtn.getStyleClass().add("primary-button");
        HBox trustBox = new HBox(20);
        trustBox.getStyleClass().add("align-center");
        Label trust1 = new Label("✓ Secure booking");
        Label trust2 = new Label("✓ Instant confirmation");
        Label trust3 = new Label("✓ Easy refunds");
        for (Label l : new Label[] {trust1, trust2, trust3}) {
            l.getStyleClass().add("caption");
        }
        trustBox.getChildren().addAll(trust1, trust2, trust3);

        heroBox.getChildren().addAll(eyebrow, title, subtitle, enterBtn, trustBox);
        HBox featuresBox = new HBox(25);
        featuresBox.getStyleClass().add("align-center");
        featuresBox.getStyleClass().add("p-0-40-60-40");

        featuresBox
                .getChildren()
                .addAll(
                        createFeatureCard("🎬", "Latest Movies", "Browse current releases"),
                        createFeatureCard("💺", "Choose Seats", "Pick your perfect spot"),
                        createFeatureCard("🎫", "Instant Booking", "Get tickets in seconds"));

        VBox mainContent = new VBox(20, heroBox, featuresBox);
        mainContent.getStyleClass().add("align-center");

        root.setTop(topBar);
        root.setCenter(mainContent);
        about.setOnAction(
                e -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("About");
                    alert.setHeaderText("CinemaBook");
                    alert.setContentText(
                            "A modern cinema booking system built with JavaFX and SQL Server.\nVersion 1.0");
                    alert.showAndWait();
                });

        complaint.setOnAction(
                e -> {
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Feedback");
                    dialog.setHeaderText("We'd love to hear from you");
                    dialog.setContentText("Your message:");
                    dialog.showAndWait();
                });

        rate.setOnAction(
                e -> {
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Rate Us");
                    dialog.setHeaderText("Rate your experience");
                    dialog.setContentText("Rating (1-5):");
                    dialog.showAndWait();
                });

        return root;
    }

    private VBox createFeatureCard(String icon, String title, String description) {
        VBox card = new VBox(8);
        card.getStyleClass().add("align-center");
        card.getStyleClass().add("p-24-20");
        card.getStyleClass().add("w-180");
        card.getStyleClass().add("card");

        Label iconLabel = new Label(icon);
        iconLabel.getStyleClass().add("icon-emoji");

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("body-text");

        Label descLabel = new Label(description);
        descLabel.getStyleClass().add("caption");
        descLabel.setWrapText(true);
        descLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        card.getChildren().addAll(iconLabel, titleLabel, descLabel);

        return card;
    }
}
