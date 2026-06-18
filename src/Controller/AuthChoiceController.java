package Controller;

import View.AuthChoice;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AuthChoiceController {
    private Stage stage;
    private AuthChoice view;
    private String role;
    public AuthChoiceController(Stage stage, String role) {
        this.stage = stage;
        this.role = role;
        this.view = new AuthChoice();
        stage.setScene(new Scene(view.getView(), 500, 450));
        stage.setTitle("Choose Option");
        stage.show();

        view.btnBack.setOnAction(e -> {
            NavigationManager.pop();
            new UsertypeController(stage);
        });

        view.btnLogin.setOnAction(e -> {
            NavigationManager.push("auth");
            new logincontroller(stage, role);
        });

        view.btnSignUp.setOnAction(e -> {
            NavigationManager.push("auth");
            new SignUpController(stage, role);
        });
    }
}
