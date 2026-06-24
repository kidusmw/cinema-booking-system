package ui.common;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WindowManager {
    public static final int DEFAULT_WIDTH = 1200;
    public static final int DEFAULT_HEIGHT = 800;

    public static Scene configureStage(Stage stage, String title, Parent root, int w, int h) {
        Scene scene = new Scene(root, w, h);
        scene.getStylesheets()
                .add(WindowManager.class.getResource("/css/styles.css").toExternalForm());
        stage.setTitle(title);
        stage.setScene(scene);
        stage.setWidth(w);
        stage.setHeight(h);
        stage.centerOnScreen();
        stage.show();
        return scene;
    }

    public static Scene configureStage(Stage stage, String title, Parent root) {
        return configureStage(stage, title, root, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
}
