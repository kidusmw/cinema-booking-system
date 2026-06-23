import Controller.WelcomeController;
import infrastructure.config.AppConfig;
import infrastructure.persistence.FlywayMigrator;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        AppConfig config = AppConfig.load("/db.properties");
        FlywayMigrator.migrate(config);
        new WelcomeController(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
