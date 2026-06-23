package ui.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import static ui.common.Theme.*;

public class LoginPage {
    public Label roleLabel;
    private static final String BG = "#FAFAFA";
    private static final String WHITE = "#FFFFFF";
    private static final String PRIMARY = "#F472B6";
    private static final String HOVER = "#EC4899";

    private static final String BG_LIGHT = "#F8FAFC";
    public TextField usernameField;
    public PasswordField passwordField;
    public Button loginBtn;
    public Button backBtn;
    public Hyperlink signupLink;
    public Label errorLabel;

    public BorderPane getView() {

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BG + ";");

        // Top bar
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(20, 30, 20, 30));

        backBtn = new Button("← Back");
        backBtn.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        backBtn.setTextFill(Color.web(TEXT_MUTED));
        backBtn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 6 12;"
        );

        topBar.getChildren().add(backBtn);
        HBox splitLayout = new HBox(60);
        splitLayout.setAlignment(Pos.CENTER);
        splitLayout.setPadding(new Insets(40));
        VBox leftSide = new VBox(15);
        leftSide.setAlignment(Pos.CENTER_LEFT);
        leftSide.setPrefWidth(320);

        HBox brandBox = new HBox(8);
        brandBox.setAlignment(Pos.CENTER_LEFT);
        StackPane logo = new StackPane();
        logo.setPrefSize(36, 36);
        logo.setStyle(
                "-fx-background-color: " + ACCENT + ";" +
                        "-fx-background-radius: 8;"
        );
        roleLabel = new Label();
        roleLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        roleLabel.setTextFill(Color.web(TEXT_MUTED));
        roleLabel.setText("");
        Label logoIcon = new Label("🎬");
        logoIcon.setFont(Font.font(18));
        logo.getChildren().add(logoIcon);

        Label brand = new Label("CinemaBook");
        brand.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        brand.setTextFill(Color.web(TEXT_DARK));
        brandBox.getChildren().addAll(logo, brand);

        Label welcomeText = new Label("Welcome back");
        welcomeText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        welcomeText.setTextFill(Color.web(TEXT_DARK));

        Label tagline = new Label("Sign in to book your next movie experience");
        tagline.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        tagline.setTextFill(Color.web(TEXT_MUTED));
        tagline.setWrapText(true);

        leftSide.getChildren().addAll(brandBox, welcomeText, tagline);
        VBox card = new VBox(18);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(40, 36, 40, 36));
        card.setPrefWidth(400);
        card.setStyle(
                "-fx-background-color: " + WHITE + ";" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 12;" +
                        "-fx-border-width: 1;"
        );
        // ✅ FIXED: Color.rgb instead of Color.web
        card.setEffect(new DropShadow(10, Color.rgb(0, 0, 0, 0.06)));

        Label cardTitle = new Label("Sign in to your account");
        cardTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        cardTitle.setTextFill(Color.web(TEXT_DARK));
        VBox usernameBox = new VBox(6);
        Label usernameLabel = new Label("Username");
        usernameLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        usernameLabel.setTextFill(Color.web(TEXT_DARK));

        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setPrefHeight(42);
        usernameField.setFont(Font.font("Segoe UI", 13));
        usernameField.setStyle(getTextFieldStyle());
        addFocusEffect(usernameField);

        usernameBox.getChildren().addAll(usernameLabel, usernameField);
        VBox passwordBox = new VBox(6);
        Label passwordLabel = new Label("Password");
        passwordLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        passwordLabel.setTextFill(Color.web(TEXT_DARK));

        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setPrefHeight(42);
        passwordField.setFont(Font.font("Segoe UI", 13));
        passwordField.setStyle(getTextFieldStyle());
        addFocusEffect(passwordField);

        passwordBox.getChildren().addAll(passwordLabel, passwordField);
        errorLabel = new Label("");
        errorLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 12));
        errorLabel.setTextFill(Color.web("#DC2626"));
        errorLabel.setVisible(false);
        loginBtn = new Button("Sign in");
        loginBtn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        loginBtn.setPrefWidth(Double.MAX_VALUE);
        loginBtn.setPrefHeight(44);
        loginBtn.setTextFill(Color.web(WHITE));
        loginBtn.setStyle(
                "-fx-background-color: " + ACCENT + ";" +
                        "-fx-background-radius: 8;" +
                        "-fx-cursor: hand;"
        );

        loginBtn.setOnMouseEntered(e -> {
            loginBtn.setStyle(
                    "-fx-background-color: " + HOVER + ";" +
                            "-fx-background-radius: 8;" +
                            "-fx-cursor: hand;"
            );
        });
        loginBtn.setOnMouseExited(e -> {
            loginBtn.setStyle(
                    "-fx-background-color: " + ACCENT + ";" +
                            "-fx-background-radius: 8;" +
                            "-fx-cursor: hand;"
            );
        });
        HBox signupBox = new HBox(5);
        signupBox.setAlignment(Pos.CENTER);
        Label noAccount = new Label("Don't have an account?");
        noAccount.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 12));
        noAccount.setTextFill(Color.web(TEXT_MUTED));

        signupLink = new Hyperlink("Sign up");
        signupLink.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        signupLink.setTextFill(Color.web(ACCENT));

        signupBox.getChildren().addAll(noAccount, signupLink);

        card.getChildren().addAll(
                cardTitle,
                roleLabel,
                usernameBox,
                passwordBox,
                errorLabel,
                loginBtn,
                signupBox
        );
        splitLayout.getChildren().addAll(leftSide, card);

        root.setTop(topBar);
        root.setCenter(splitLayout);

        return root;
    }

    private String getTextFieldStyle() {
        return
                "-fx-background-color: " + BG_LIGHT + ";" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 8 14;" +
                        "-fx-text-fill: " + TEXT_DARK + ";" +
                        "-fx-prompt-text-fill: " + TEXT_MUTED + ";";
    }

    private void addFocusEffect(TextField field) {
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle(getTextFieldStyle().replace(BORDER, ACCENT));
            } else {
                field.setStyle(getTextFieldStyle());
            }
        });
    }
}
