package Controller;

import javafx.scene.Scene;
import javafx.stage.Stage;
import View.WelcomePage;
public class WelcomeController {

    private WelcomePage view;

    public WelcomeController(Stage stage) {

        view = new WelcomePage();

        Scene scene = new Scene(view.getView(), 800, 600);
        stage.setTitle("CinemaBook - Welcome");
        stage.setScene(scene);
        stage.show();

        view.enterBtn.setOnAction(e -> {
            NavigationManager.clear();
            NavigationManager.push("welcome");
            new UsertypeController(stage);
        });
    }

}
