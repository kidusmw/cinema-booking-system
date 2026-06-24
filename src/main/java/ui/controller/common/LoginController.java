package ui.controller.common;

import application.AppContext;
import domain.model.User;
import java.util.Optional;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ui.common.WindowManager;
import ui.controller.admin.AdminDashboardController;
import ui.controller.customer.CustomerDashboardController;
import ui.view.common.LoginPage;

public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
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
        WindowManager.configure(stage, "Sign In", view.getView());
        log.info("Opening Sign In page");

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

        User user = userOpt.get();

        if ("admin".equalsIgnoreCase(expectedRole)) {
            nav.goFresh(() -> new AdminDashboardController(stage, ctx, nav, user.getFirstName()));
        } else if ("customer".equalsIgnoreCase(expectedRole)) {
            nav.goFresh(() -> new CustomerDashboardController(stage, ctx, nav, user));
        } else {
            view.errorLabel.setText("Role mismatch or invalid user type");
            view.errorLabel.setVisible(true);
        }
    }

    private static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
