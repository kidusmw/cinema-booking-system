package Controller;

import View.UserTypePage;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UsertypeController {

    private UserTypePage view;

    public UsertypeController(Stage stage) {
        view = new UserTypePage();

        Scene scene = new Scene(view.getView(), 800, 600);
        stage.setTitle("Select Account Type");
        stage.setScene(scene);
        stage.show();

        view.setButtonHover(view.btnAdmin);
        view.setButtonHover(view.btnCustomer);

        view.btnBack.setOnAction(e -> {
            NavigationManager.pop();
            new WelcomeController(stage);
        });

        view.btnAdmin.setOnAction(e -> {
            NavigationManager.push("usertype");
            new AuthChoiceController(stage, "admin");
        });

        view.btnCustomer.setOnAction(e -> {
            NavigationManager.push("usertype");
            new AuthChoiceController(stage, "customer");
        });
    }
}
