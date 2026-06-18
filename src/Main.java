import javafx.application.Application;
import javafx.stage.Stage;
import Controller.WelcomeController;
public class Main extends Application {
    @Override
    public void start(Stage stage) {
        new WelcomeController(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
