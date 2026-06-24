package ui.view.common;

import javafx.scene.control.*;
import javafx.scene.layout.*;

public class LoginPage {
    public Label roleLabel;

    public TextField usernameField;
    public PasswordField passwordField;
    public Button loginBtn;
    public Button backBtn;
    public Hyperlink signupLink;
    public Label errorLabel;

    public BorderPane getView() {

        BorderPane root = new BorderPane();
        root.getStyleClass().add("page");

        // Top bar
        HBox topBar = new HBox();
        topBar.getStyleClass().add("p-20-30");

        backBtn = new Button("← Back");
        backBtn.getStyleClass().addAll("back-button", "muted-text");

        topBar.getChildren().add(backBtn);
        HBox splitLayout = new HBox(60);
        splitLayout.getStyleClass().add("align-center");
        splitLayout.getStyleClass().add("p-40");
        VBox leftSide = new VBox(15);
        leftSide.getStyleClass().add("align-center-left");
        leftSide.getStyleClass().add("w-320");

        HBox brandBox = new HBox(8);
        brandBox.getStyleClass().add("align-center-left");
        StackPane logo = new StackPane();
        logo.setPrefSize(36, 36);
        logo.getStyleClass().add("logo-badge");
        roleLabel = new Label();
        roleLabel.getStyleClass().add("muted-text");
        roleLabel.setText("");
        Label logoIcon = new Label("🎬");
        logoIcon.getStyleClass().add("subtitle");
        logo.getChildren().add(logoIcon);

        Label brand = new Label("CinemaBook");
        brand.getStyleClass().add("subtitle");
        brandBox.getChildren().addAll(logo, brand);

        Label welcomeText = new Label("Welcome back");
        welcomeText.getStyleClass().add("welcome-title");

        Label tagline = new Label("Sign in to book your next movie experience");
        tagline.getStyleClass().add("muted-text");
        tagline.setWrapText(true);

        leftSide.getChildren().addAll(brandBox, welcomeText, tagline);
        VBox card = new VBox(18);
        card.getStyleClass().add("align-center");
        card.getStyleClass().add("p-40-36");
        card.getStyleClass().add("w-400");
        card.getStyleClass().add("form-card");

        Label cardTitle = new Label("Sign in to your account");
        cardTitle.getStyleClass().add("subtitle");
        VBox usernameBox = new VBox(6);
        Label usernameLabel = new Label("Username");
        usernameLabel.getStyleClass().add("body-text");

        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.getStyleClass().add("h-42");

        usernameBox.getChildren().addAll(usernameLabel, usernameField);
        VBox passwordBox = new VBox(6);
        Label passwordLabel = new Label("Password");
        passwordLabel.getStyleClass().add("body-text");

        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.getStyleClass().add("h-42");

        passwordBox.getChildren().addAll(passwordLabel, passwordField);
        errorLabel = new Label("");
        errorLabel.getStyleClass().add("error-label");
        errorLabel.setVisible(false);
        loginBtn = new Button("Sign in");
        loginBtn.setPrefWidth(Double.MAX_VALUE);
        loginBtn.getStyleClass().add("h-44");
        loginBtn.getStyleClass().add("primary-button");
        HBox signupBox = new HBox(5);
        signupBox.getStyleClass().add("align-center");
        Label noAccount = new Label("Don't have an account?");
        noAccount.getStyleClass().add("caption");

        signupLink = new Hyperlink("Sign up");
        signupLink.getStyleClass().add("caption");
        signupLink.getStyleClass().add("text-accent");

        signupBox.getChildren().addAll(noAccount, signupLink);

        card.getChildren()
                .addAll(
                        cardTitle,
                        roleLabel,
                        usernameBox,
                        passwordBox,
                        errorLabel,
                        loginBtn,
                        signupBox);
        splitLayout.getChildren().addAll(leftSide, card);

        root.setTop(topBar);
        root.setCenter(splitLayout);

        return root;
    }
}
