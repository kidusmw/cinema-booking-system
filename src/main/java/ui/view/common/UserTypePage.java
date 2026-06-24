package ui.view.common;

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
        topBar.getStyleClass().add("p-20-30");

        btnBack = new Button("← Back");
        btnBack.getStyleClass().add("back-button");
        topBar.getChildren().add(btnBack);
        VBox centerBox = new VBox(30);
        centerBox.getStyleClass().add("align-center");
        centerBox.getStyleClass().add("p-40");
        Label title = new Label("Choose your account type");
        title.getStyleClass().add("title");

        Label subtitle = new Label("Select how you want to continue");
        subtitle.getStyleClass().add("muted-text");
        HBox cardsBox = new HBox(25);
        cardsBox.getStyleClass().add("align-center");

        VBox adminCard =
                createRoleCard("👨‍💼", "Administrator", "Manage movies, shows, and bookings");

        VBox customerCard = createRoleCard("🎬", "User", "Browse and book movie tickets");
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

    private static VBox createRoleCard(String emoji, String title, String description) {
        VBox card = new VBox(15);
        card.getStyleClass().add("align-center");
        card.getStyleClass().add("p-30-25");
        card.getStyleClass().add("w-240");
        card.getStyleClass().add("h-280");
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

    private static void stylePrimaryButton(Button btn) {
        btn.getStyleClass().add("primary-button");
        btn.getStyleClass().add("w-180");
        btn.getStyleClass().add("h-38");
    }
}
