package ui.view.common;

import static ui.common.Theme.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class UserTypePage {
    public Button btnAdmin;
    public Button btnCustomer;
    public Button btnBack;
    private static final String BG = "#FAFAFA";
    private static final String WHITE = "#FFFFFF";
    private static final String PRIMARY = "#F472B6";
    private static final String HOVER = "#EC4899";

    public BorderPane getView() {

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BG + ";");
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(20, 30, 20, 30));

        btnBack = new Button("← Back");
        btnBack.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        btnBack.setTextFill(Color.web(TEXT_MUTED));
        btnBack.setStyle(
                "-fx-background-color: transparent;" + "-fx-cursor: hand;" + "-fx-padding: 6 12;");
        topBar.getChildren().add(btnBack);
        VBox centerBox = new VBox(30);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(40));
        Label title = new Label("Choose your account type");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        title.setTextFill(Color.web(TEXT_DARK));

        Label subtitle = new Label("Select how you want to continue");
        subtitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        subtitle.setTextFill(Color.web(TEXT_MUTED));
        HBox cardsBox = new HBox(25);
        cardsBox.setAlignment(Pos.CENTER);

        VBox adminCard =
                createRoleCard(
                        "👨‍💼", "Administrator", "Manage movies, shows, and bookings", false);

        VBox customerCard = createRoleCard("🎬", "Customer", "Browse and book movie tickets", true);
        btnAdmin = new Button("Continue as Admin");
        btnCustomer = new Button("Continue as Customer");

        stylePrimaryButton(btnAdmin);
        stylePrimaryButton(btnCustomer);

        adminCard.getChildren().add(btnAdmin);
        customerCard.getChildren().add(btnCustomer);

        cardsBox.getChildren().addAll(adminCard, customerCard);

        centerBox.getChildren().addAll(title, subtitle, cardsBox);

        root.setTop(topBar);
        root.setCenter(centerBox);

        return root;
    }

    private VBox createRoleCard(String emoji, String title, String description, boolean isPrimary) {
        VBox card = new VBox(15);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(30, 25, 30, 25));
        card.setPrefWidth(240);
        card.setPrefHeight(280);
        card.setStyle(
                "-fx-background-color: "
                        + WHITE
                        + ";"
                        + "-fx-background-radius: 12;"
                        + "-fx-border-color: "
                        + BORDER
                        + ";"
                        + "-fx-border-radius: 12;"
                        + "-fx-border-width: 1;");
        card.setEffect(new DropShadow(8, Color.rgb(0, 0, 0, 0.05)));
        Label emojiLabel = new Label(emoji);
        emojiLabel.setFont(Font.font(48));
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        titleLabel.setTextFill(Color.web(TEXT_DARK));
        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 12));
        descLabel.setTextFill(Color.web(TEXT_MUTED));
        descLabel.setWrapText(true);
        descLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        card.getChildren().addAll(emojiLabel, titleLabel, descLabel);
        card.setOnMouseEntered(
                e -> {
                    card.setStyle(
                            "-fx-background-color: "
                                    + WHITE
                                    + ";"
                                    + "-fx-background-radius: 12;"
                                    + "-fx-border-color: "
                                    + PRIMARY
                                    + ";"
                                    + "-fx-border-radius: 12;"
                                    + "-fx-border-width: 2;"
                                    + "-fx-cursor: hand;");
                });
        card.setOnMouseExited(
                e -> {
                    card.setStyle(
                            "-fx-background-color: "
                                    + WHITE
                                    + ";"
                                    + "-fx-background-radius: 12;"
                                    + "-fx-border-color: "
                                    + BORDER
                                    + ";"
                                    + "-fx-border-radius: 12;"
                                    + "-fx-border-width: 1;");
                });

        return card;
    }

    private void stylePrimaryButton(Button btn) {
        btn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        btn.setPrefWidth(180);
        btn.setPrefHeight(38);
        btn.setTextFill(Color.web(WHITE));
        btn.setStyle(
                "-fx-background-color: "
                        + ACCENT
                        + ";"
                        + "-fx-background-radius: 8;"
                        + "-fx-cursor: hand;");
    }

    public void setButtonHover(Button btn) {
        btn.setOnMouseEntered(
                e -> {
                    btn.setStyle(
                            "-fx-background-color: "
                                    + HOVER
                                    + ";"
                                    + "-fx-background-radius: 8;"
                                    + "-fx-cursor: hand;");
                });
        btn.setOnMouseExited(
                e -> {
                    btn.setStyle(
                            "-fx-background-color: "
                                    + ACCENT
                                    + ";"
                                    + "-fx-background-radius: 8;"
                                    + "-fx-cursor: hand;");
                });
    }
}
