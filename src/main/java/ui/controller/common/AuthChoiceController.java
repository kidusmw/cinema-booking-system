package ui.controller.common;

import application.AppContext;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ui.common.WindowManager;
import ui.view.common.AuthChoice;

public class AuthChoiceController {
    private static final Logger log = LoggerFactory.getLogger(AuthChoiceController.class);
    private AuthChoice view;

    public AuthChoiceController(Stage stage, AppContext ctx, NavigationManager nav, String role) {
        this.view = new AuthChoice();
        WindowManager.configure(stage, "Choose Option", view.getView());
        log.info("Opening Choose Option page");

        view.btnBack.setOnAction(e -> nav.back());

        view.btnLogin.setOnAction(
                e ->
                        nav.go(
                                () -> new AuthChoiceController(stage, ctx, nav, role),
                                () -> new LoginController(stage, ctx, nav, role)));

        view.btnSignUp.setOnAction(
                e ->
                        nav.go(
                                () -> new AuthChoiceController(stage, ctx, nav, role),
                                () -> new SignUpController(stage, ctx, nav, role)));
    }
}
