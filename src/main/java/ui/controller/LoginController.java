package ui.controller;

import application.AppContext;
import application.ModelConverter;
import java.util.Optional;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.model.Admin;
import ui.model.Customer;
import ui.model.User;
import ui.view.LoginPage;

public class LoginController {
    private final AppContext ctx;
    private LoginPage view;
    private String expectedRole;
    private Stage stage;
    private NavigationManager nav;

    public LoginController(Stage stage, AppContext ctx, NavigationManager nav, String role) {
        this.stage = stage;
        this.ctx = ctx;
        this.nav = nav;
        this.expectedRole = role;
        view = new LoginPage();
        Scene scene = new Scene(view.getView(), 900, 650);
        stage.setTitle("Sign in - CinemaBook");
        stage.setScene(scene);
        stage.show();

        view.roleLabel.setText("Sign in as " + capitalize(role));
        view.loginBtn.setOnAction(e -> handleLogin());
        view.backBtn.setOnAction(e -> nav.back());
        view.signupLink.setOnAction(
                e ->
                        nav.go(
                                () -> new LoginController(stage, ctx, nav, role),
                                () -> new SignUpController(stage, ctx, nav, role)));
    }

    private void handleLogin() {
        Optional<domain.model.User> userOpt =
                ctx.authService.login(view.usernameField.getText(), view.passwordField.getText());

        if (userOpt.isEmpty()) {
            view.errorLabel.setText("Invalid username or password");
            view.errorLabel.setVisible(true);
            return;
        }

        User oldUser = ModelConverter.toOldUser(userOpt.get());

        if ("admin".equalsIgnoreCase(expectedRole) && oldUser instanceof Admin) {
            nav.goFresh(
                    () -> new AdminDashboardController(stage, ctx, nav, oldUser.getFirstName()));
        } else if ("customer".equalsIgnoreCase(expectedRole) && oldUser instanceof Customer) {
            nav.goFresh(() -> new CustomerDashboardController(stage, ctx, nav, (Customer) oldUser));
        } else {
            view.errorLabel.setText("Role mismatch or invalid user type");
            view.errorLabel.setVisible(true);
        }
    }

    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
