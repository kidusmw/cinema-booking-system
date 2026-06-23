package ui.controller;

import application.AppContext;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.view.AuthChoice;

public class AuthChoiceController {
    private Stage stage;
    private AuthChoice view;
    private String role;
    private NavigationManager nav;

    public AuthChoiceController(Stage stage, AppContext ctx, NavigationManager nav, String role) {
        this.stage = stage;
        this.nav = nav;
        this.role = role;
        this.view = new AuthChoice();
        stage.setScene(new Scene(view.getView(), 500, 450));
        stage.setTitle("Choose Option");
        stage.show();

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
