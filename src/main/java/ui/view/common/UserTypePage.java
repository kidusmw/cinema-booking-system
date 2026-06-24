package ui.view.common;

import static ui.common.Theme.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class UserTypePage {
    public Button btnAdmin;
    public Button btnCustomer;
    public Button btnBack;

    public BorderPane getView() {

        BorderPane root = new BorderPane();
        root.getStyleClass().add("page");
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(20, 30, 20, 30));

        btnBack = new Button("← Back");
        btnBack.getStyleClass().add("back-button");
        topBar.getChildren().add(btnBack);
        VBox centerBox = new VBox(30);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(40));
        Label title = new Label("Choose your account type");
        title.getStyleClass().add("title");

        Label subtitle = new Label("Select how you want to continue");
        subtitle.getStyleClass().add("muted-text");
        HBox cardsBox = new HBox(25);
        cardsBox.setAlignment(Pos.CENTER);

        VBox adminCard =
                createRoleCard(
                        "👨‍💼", "Administrator", "Manage movies, shows, and bookings", false);

        VBox customerCard = createRoleCard("🎬", "User", "Browse and book movie tickets", true);
        btnAdmin = new Button("Continue as User");
        btnCustomer = new Button("Continue as User");

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
        card.getStyleClass().add("card");
        Label emojiLabel = new Label(emoji);
        emojiLabel.getStyleClass().add("icon-emoji");
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("subtitle");
        Label descLabel = new Label(description);
        descLabel.getStyleClass().add("muted-text");
        descLabel.setWrapText(true);
        descLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        card.getChildren().addAll(emojiLabel, titleLabel, descLabel);

        return card;
    }

    private void stylePrimaryButton(Button btn) {
        btn.getStyleClass().add("primary-button");
        btn.setPrefWidth(180);
        btn.setPrefHeight(38);
    }
}
