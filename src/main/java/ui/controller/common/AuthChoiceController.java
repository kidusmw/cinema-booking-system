package ui.controller.common;

import application.AppContext;
import javafx.stage.Stage;
import ui.common.WindowManager;
import ui.view.common.AuthChoice;

public class AuthChoiceController {
    private AuthChoice view;

    public AuthChoiceController(Stage stage, AppContext ctx, NavigationManager nav, String role) {
        this.view = new AuthChoice();
        WindowManager.configureStage(stage, "Choose Option", view.getView(), 500, 450);

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
