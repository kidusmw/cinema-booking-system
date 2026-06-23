package View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import static ui.common.Theme.*;

public class SingUpPage {

    public TextField firstNameField;
    public TextField lastNameField;
    public TextField usernameField;
    public PasswordField passwordField;
    public TextField emailField;
    public TextField phoneField;
    public Button signUpBtn;
    public Button backBtn;
    public Hyperlink loginLink;
    public Label errorLabel;
    public Label roleLabel;

    private static final String BG = "#FAFAFA";
    private static final String WHITE = "#FFFFFF";
    private static final String HOVER = "#EC4899";

    private static final String BG_LIGHT = "#F8FAFC";

    public BorderPane getView() {

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BG + ";");
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
        VBox card = new VBox(14);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(35, 50, 35, 50));
        card.setMaxWidth(500);
        card.setStyle(
                "-fx-background-color: " + WHITE + ";" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 12;" +
                        "-fx-border-width: 1;"
        );
        card.setEffect(new DropShadow(10, Color.rgb(0, 0, 0, 0.06)));
        roleLabel = new Label("Create your account");
        roleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        roleLabel.setTextFill(Color.web(TEXT_DARK));

        Label subtitle = new Label("Fill in the details to get started");
        subtitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        subtitle.setTextFill(Color.web(TEXT_MUTED));
        HBox nameBox = new HBox(12);
        firstNameField = createTextField("First name");
        lastNameField = createTextField("Last name");
        nameBox.getChildren().addAll(firstNameField, lastNameField);
        HBox.setHgrow(firstNameField, Priority.ALWAYS);
        HBox.setHgrow(lastNameField, Priority.ALWAYS);
        usernameField = createTextField("Username");
        emailField = createTextField("Email");
        phoneField = createTextField("Phone");
        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setPrefHeight(40);
        passwordField.setFont(Font.font("Segoe UI", 13));
        passwordField.setStyle(getTextFieldStyle());

        errorLabel = new Label("");
        errorLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 12));
        errorLabel.setTextFill(Color.web("#DC2626"));
        errorLabel.setVisible(false);

        signUpBtn = new Button("Create account");
        signUpBtn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        signUpBtn.setPrefWidth(Double.MAX_VALUE);
        signUpBtn.setPrefHeight(44);
        signUpBtn.setTextFill(Color.web(WHITE));
        signUpBtn.setStyle(
                "-fx-background-color: " + ACCENT + ";" +
                        "-fx-background-radius: 8;" +
                        "-fx-cursor: hand;"
        );
        signUpBtn.setOnMouseEntered(e -> {
            signUpBtn.setStyle(
                    "-fx-background-color: " + HOVER + ";" +
                            "-fx-background-radius: 8;" +
                            "-fx-cursor: hand;"
            );
        });
        signUpBtn.setOnMouseExited(e -> {
            signUpBtn.setStyle(
                    "-fx-background-color: " + ACCENT + ";" +
                            "-fx-background-radius: 8;" +
                            "-fx-cursor: hand;"
            );
        });
        HBox loginBox = new HBox(5);
        loginBox.setAlignment(Pos.CENTER);
        Label haveAccount = new Label("Already have an account?");
        haveAccount.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 12));
        haveAccount.setTextFill(Color.web(TEXT_MUTED));

        loginLink = new Hyperlink("Sign in");
        loginLink.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        loginLink.setTextFill(Color.web(ACCENT));

        loginBox.getChildren().addAll(haveAccount, loginLink);

        card.getChildren().addAll(
                roleLabel, subtitle,
                nameBox,
                usernameField,
                emailField,
                phoneField,
                passwordField,
                errorLabel,
                signUpBtn,
                loginBox
        );

        root.setTop(topBar);
        root.setCenter(card);

        return root;
    }

    private TextField createTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setPrefHeight(40);
        field.setFont(Font.font("Segoe UI", 13));
        field.setStyle(getTextFieldStyle());
        return field;
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
}
