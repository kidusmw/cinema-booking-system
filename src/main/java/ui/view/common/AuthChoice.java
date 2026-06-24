package ui.view.common;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class AuthChoice {

    public Button btnLogin;
    public Button btnSignUp;
    public Button btnBack; // ✅ ADDED

    public BorderPane getView() {

        BorderPane root = new BorderPane();
        root.getStyleClass().add("page");
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(20, 30, 20, 30));

        btnBack = new Button("← Back"); // ✅ Back button
        btnBack.getStyleClass().addAll("back-button", "muted-text");
        topBar.getChildren().add(btnBack);
        VBox card = new VBox(25);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(50, 60, 50, 60));
        card.setMaxWidth(450);
        card.getStyleClass().add("form-card");
        Label title = new Label("How would you like to continue?");
        title.getStyleClass().add("heading");
        title.setWrapText(true);
        title.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        Label subtitle = new Label("Choose an option to get started");
        subtitle.getStyleClass().add("muted-text");
        btnLogin = new Button("Sign in to existing account");
        btnLogin.setPrefWidth(Double.MAX_VALUE);
        btnLogin.setPrefHeight(48);
        btnLogin.getStyleClass().add("primary-button");
        HBox dividerBox = new HBox(10);
        dividerBox.setAlignment(Pos.CENTER);
        Region line1 = new Region();
        line1.setPrefHeight(1);
        line1.getStyleClass().add("divider");
        HBox.setHgrow(line1, Priority.ALWAYS);
        Label orLabel = new Label("OR");
        orLabel.getStyleClass().add("caption");
        Region line2 = new Region();
        line2.setPrefHeight(1);
        line2.getStyleClass().add("divider");
        HBox.setHgrow(line2, Priority.ALWAYS);
        dividerBox.getChildren().addAll(line1, orLabel, line2);

        // Signup button (secondary)
        btnSignUp = new Button("Create new account");
        btnSignUp.setPrefWidth(Double.MAX_VALUE);
        btnSignUp.setPrefHeight(48);
        btnSignUp.getStyleClass().add("secondary-button");

        card.getChildren().addAll(title, subtitle, btnLogin, dividerBox, btnSignUp);

        root.setTop(topBar);
        root.setCenter(card);

        return root;
    }
}
