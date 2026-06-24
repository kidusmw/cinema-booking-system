package ui.controller.common;

import application.AppContext;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.view.common.AuthChoice;

public class AuthChoiceController {
    private Stage stage;
    private AuthChoice view;
    private NavigationManager nav;

    public AuthChoiceController(Stage stage, AppContext ctx, NavigationManager nav, String role) {
        this.stage = stage;
        this.nav = nav;
        this.view = new AuthChoice();
        Scene scene = new Scene(view.getView(), 500, 450);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        stage.setScene(scene);
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
