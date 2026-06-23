package Controller;

import Model.Admin;
import Model.Customer;
import Model.User;
import View.Loginpage;
import application.AppContext;
import application.ModelConverter;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Optional;

public class LoginController {
    private final AppContext ctx;
    private Loginpage view;
    private String expectedRole;
    private Stage stage;

    public LoginController(Stage stage, AppContext ctx, String role) {
        this.stage = stage;
        this.ctx = ctx;
        this.expectedRole = role;
        view = new Loginpage();
        Scene scene = new Scene(view.getView(), 900, 650);
        stage.setTitle("Sign in - CinemaBook");
        stage.setScene(scene);
        stage.show();

        view.roleLabel.setText("Sign in as " + capitalize(role));
        view.loginBtn.setOnAction(e -> handleLogin());
        view.backBtn.setOnAction(e -> new AuthChoiceController(stage, ctx, role));
        view.signupLink.setOnAction(e -> new SignUpController(stage, ctx, role));
    }

    private void handleLogin() {
        Optional<domain.model.User> userOpt = ctx.authService.login(
            view.usernameField.getText(), view.passwordField.getText()
        );

        if (userOpt.isEmpty()) {
            view.errorLabel.setText("Invalid username or password");
            view.errorLabel.setVisible(true);
            return;
        }

        User oldUser = ModelConverter.toOldUser(userOpt.get());

        if ("admin".equalsIgnoreCase(expectedRole) && oldUser instanceof Admin) {
            new AdminDashboardController(stage, ctx, oldUser.getFirstName());
        } else if ("customer".equalsIgnoreCase(expectedRole) && oldUser instanceof Customer) {
            new CustomerDashboardController(stage, ctx, (Customer) oldUser);
        } else {
            view.errorLabel.setText("Role mismatch or invalid user type");
            view.errorLabel.setVisible(true);
        }
    }

    private String capitalize(String str) { return str.substring(0, 1).toUpperCase() + str.substring(1); }
}
