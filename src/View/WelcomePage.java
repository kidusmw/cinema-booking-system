package View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class WelcomePage {

    public Button enterBtn;
    public MenuButton menu;
    private static final String BG = "#FAFAFA";
    private static final String WHITE = "#FFFFFF";
    private static final String PRIMARY = "#F472B6";
    private static final String HOVER = "#EC4899";
    private static final String ACCENT = "#DB2777";
    private static final String TEXT_DARK = "#1E293B";
    private static final String TEXT_MUTED = "#64748B";
    private static final String BORDER = "#E2E8F0";

    public BorderPane getView() {

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BG + ";");
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(20, 40, 20, 40));
        topBar.setStyle(
                "-fx-background-color: " + WHITE + ";" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-width: 0 0 1 0;"
        );
        HBox brandBox = new HBox(8);
        brandBox.setAlignment(Pos.CENTER_LEFT);

        StackPane logo = new StackPane();
        logo.setPrefSize(32, 32);
        logo.setStyle(
                "-fx-background-color: " + PRIMARY + ";" +
                        "-fx-background-radius: 8;"
        );
        Label logoIcon = new Label("🎬");
        logoIcon.setFont(Font.font(16));
        logo.getChildren().add(logoIcon);

        Label brand = new Label("CinemaBook");
        brand.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        brand.setTextFill(Color.web(TEXT_DARK));

        brandBox.getChildren().addAll(logo, brand);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        menu = new MenuButton("☰");
        menu.setFont(Font.font(16));
        menu.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: " + TEXT_MUTED + ";" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 6 12;"
        );
        MenuItem about = new MenuItem("About");
        MenuItem complaint = new MenuItem("Send Feedback");
        MenuItem rate = new MenuItem("Rate Us");
        menu.getItems().addAll(about, complaint, rate);

        topBar.getChildren().addAll(brandBox, spacer, menu);
        VBox heroBox = new VBox(20);
        heroBox.setAlignment(Pos.CENTER);
        heroBox.setPadding(new Insets(60, 40, 40, 40));

        Label eyebrow = new Label("CINEMA BOOKING SYSTEM");
        eyebrow.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        eyebrow.setTextFill(Color.web(ACCENT));
        eyebrow.setPadding(new Insets(4, 12, 4, 12));
        eyebrow.setStyle(
                "-fx-background-color: #FCE7F3;" +
                        "-fx-background-radius: 12;"
        );

        Label title = new Label("Book your perfect\nmovie experience");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 42));
        title.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        title.setTextFill(Color.web(TEXT_DARK));

        Label subtitle = new Label("Browse movies, choose your seats, and enjoy the show.\nSimple, fast, and hassle-free booking.");
        subtitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 16));
        subtitle.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        subtitle.setTextFill(Color.web(TEXT_MUTED));
        enterBtn = new Button("Get Started  →");
        enterBtn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        enterBtn.setPrefWidth(200);
        enterBtn.setPrefHeight(48);
        enterBtn.setTextFill(Color.web(WHITE));
        enterBtn.setStyle(
                "-fx-background-color: " + ACCENT + ";" +
                        "-fx-background-radius: 8;" +
                        "-fx-cursor: hand;"
        );
        enterBtn.setEffect(new DropShadow(8, Color.web(ACCENT, 0.3)));

        enterBtn.setOnMouseEntered(e -> {
            enterBtn.setStyle(
                    "-fx-background-color: " + HOVER + ";" +
                            "-fx-background-radius: 8;" +
                            "-fx-cursor: hand;"
            );
        });
        enterBtn.setOnMouseExited(e -> {
            enterBtn.setStyle(
                    "-fx-background-color: " + ACCENT + ";" +
                            "-fx-background-radius: 8;" +
                            "-fx-cursor: hand;"
            );
        });
        HBox trustBox = new HBox(20);
        trustBox.setAlignment(Pos.CENTER);
        Label trust1 = new Label("✓ Secure booking");
        Label trust2 = new Label("✓ Instant confirmation");
        Label trust3 = new Label("✓ Easy refunds");
        for (Label l : new Label[]{trust1, trust2, trust3}) {
            l.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 12));
            l.setTextFill(Color.web(TEXT_MUTED));
        }
        trustBox.getChildren().addAll(trust1, trust2, trust3);

        heroBox.getChildren().addAll(eyebrow, title, subtitle, enterBtn, trustBox);
        HBox featuresBox = new HBox(25);
        featuresBox.setAlignment(Pos.CENTER);
        featuresBox.setPadding(new Insets(0, 40, 60, 40));

        featuresBox.getChildren().addAll(
                createFeatureCard("🎬", "Latest Movies", "Browse current releases"),
                createFeatureCard("💺", "Choose Seats", "Pick your perfect spot"),
                createFeatureCard("🎫", "Instant Booking", "Get tickets in seconds")
        );

        VBox mainContent = new VBox(20, heroBox, featuresBox);
        mainContent.setAlignment(Pos.CENTER);

        root.setTop(topBar);
        root.setCenter(mainContent);
        about.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About");
            alert.setHeaderText("CinemaBook");
            alert.setContentText("A modern cinema booking system built with JavaFX and SQL Server.\nVersion 1.0");
            alert.showAndWait();
        });

        complaint.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Feedback");
            dialog.setHeaderText("We'd love to hear from you");
            dialog.setContentText("Your message:");
            dialog.showAndWait();
        });

        rate.setOnAction(e -> {
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
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(24, 20, 24, 20));
        card.setPrefWidth(180);
        card.setStyle(
                "-fx-background-color: " + WHITE + ";" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 12;" +
                        "-fx-border-width: 1;"
        );
        card.setEffect(new DropShadow(5, Color.rgb(0, 0, 0, 0.04)));

        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(32));

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        titleLabel.setTextFill(Color.web(TEXT_DARK));

        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 12));
        descLabel.setTextFill(Color.web(TEXT_MUTED));
        descLabel.setWrapText(true);
        descLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        card.getChildren().addAll(iconLabel, titleLabel, descLabel);
        card.setOnMouseEntered(e -> {
            card.setStyle(
                    "-fx-background-color: " + WHITE + ";" +
                            "-fx-background-radius: 12;" +
                            "-fx-border-color: " + PRIMARY + ";" +
                            "-fx-border-radius: 12;" +
                            "-fx-border-width: 1;" +
                            "-fx-cursor: hand;"
            );
        });
        card.setOnMouseExited(e -> {
            card.setStyle(
                    "-fx-background-color: " + WHITE + ";" +
                            "-fx-background-radius: 12;" +
                            "-fx-border-color: " + BORDER + ";" +
                            "-fx-border-radius: 12;" +
                            "-fx-border-width: 1;"
            );
        });

        return card;
    }
}
