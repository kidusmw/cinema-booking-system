package ui.controller;

import application.AppContext;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import ui.view.SignUpPage;

public class SignUpController {
    private final AppContext ctx;
    private SignUpPage view;
    private String role;
    private Stage stage;
    private NavigationManager nav;

    public SignUpController(Stage stage, AppContext ctx, NavigationManager nav, String role) {
        this.stage = stage;
        this.ctx = ctx;
        this.nav = nav;
        this.role = role;
        view = new SignUpPage();
        Scene scene = new Scene(view.getView(), 800, 700);
        stage.setTitle("Create Account - CinemaBook");
        stage.setScene(scene);
        stage.show();
        view.roleLabel.setText("Sign up as " + capitalize(role));

        view.signUpBtn.setOnAction(e -> handleSignUp());

        view.backBtn.setOnAction(e -> nav.back());

        view.loginLink.setOnAction(
                e ->
                        nav.go(
                                () -> new SignUpController(stage, ctx, nav, role),
                                () -> new LoginController(stage, ctx, nav, role)));
    }

    private void handleSignUp() {
        String firstName = view.firstNameField.getText().trim();
        String lastName = view.lastNameField.getText().trim();
        String username = view.usernameField.getText().trim();
        String password = view.passwordField.getText().trim();
        String email = view.emailField.getText().trim();
        String phone = view.phoneField.getText().trim();

        if (firstName.isEmpty()
                || lastName.isEmpty()
                || username.isEmpty()
                || password.isEmpty()
                || email.isEmpty()
                || phone.isEmpty()) {
            showError("Please fill in all fields");
            return;
        }

        if (ctx.userRepo.findByUsername(username).isPresent()) {
            showError("Username already taken.");
            return;
        }

        try {
            ctx.authService.register(username, password, firstName, lastName, role, phone, email);
            showInfo("Account Created", "Welcome, " + firstName + "!");
            nav.goFresh(() -> new LoginController(stage, ctx, nav, role));
        } catch (Exception e) {
            showError("Failed to create account.");
        }
    }

    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private void showError(String message) {
        view.errorLabel.setText(message);
        view.errorLabel.setVisible(true);
    }

    private void showInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
