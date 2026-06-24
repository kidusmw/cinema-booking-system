package ui.view.common;

import javafx.scene.control.*;
import javafx.scene.layout.*;

public class SignUpPage {

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

    public BorderPane getView() {

        BorderPane root = new BorderPane();
        root.getStyleClass().add("page");
        HBox topBar = new HBox();
        topBar.getStyleClass().add("p-20-30");

        backBtn = new Button("← Back");
        backBtn.getStyleClass().addAll("back-button", "muted-text");
        topBar.getChildren().add(backBtn);
        VBox card = new VBox(14);
        card.getStyleClass().add("align-center");
        card.getStyleClass().add("p-35-50");
        card.getStyleClass().add("w-500");
        card.getStyleClass().add("form-card");
        roleLabel = new Label("Create your account");
        roleLabel.getStyleClass().add("heading");

        Label subtitle = new Label("Fill in the details to get started");
        subtitle.getStyleClass().add("muted-text");
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
        passwordField.getStyleClass().add("h-40");

        errorLabel = new Label("");
        errorLabel.getStyleClass().add("error-label");
        errorLabel.setVisible(false);

        signUpBtn = new Button("Create account");
        signUpBtn.setPrefWidth(Double.MAX_VALUE);
        signUpBtn.getStyleClass().add("h-44");
        signUpBtn.getStyleClass().add("primary-button");
        HBox loginBox = new HBox(5);
        loginBox.getStyleClass().add("align-center");
        Label haveAccount = new Label("Already have an account?");
        haveAccount.getStyleClass().add("caption");

        loginLink = new Hyperlink("Sign in");
        loginLink.getStyleClass().add("caption");
        loginLink.getStyleClass().add("text-accent");

        loginBox.getChildren().addAll(haveAccount, loginLink);

        card.getChildren()
                .addAll(
                        roleLabel,
                        subtitle,
                        nameBox,
                        usernameField,
                        emailField,
                        phoneField,
                        passwordField,
                        errorLabel,
                        signUpBtn,
                        loginBox);

        root.setTop(topBar);
        root.setCenter(card);

        return root;
    }

    private TextField createTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.getStyleClass().add("h-40");
        return field;
    }
}
