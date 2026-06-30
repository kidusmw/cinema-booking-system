package ui.common;

import javafx.animation.FadeTransition;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class WindowManager {

    private static final Logger log = LoggerFactory.getLogger(WindowManager.class);
    private static final double SIZE_RATIO = 0.75;
    private static final double MIN_RATIO = 0.35;
    private static final double MIN_ABSOLUTE_WIDTH = 800;
    private static final double MIN_ABSOLUTE_HEIGHT = 600;
    private static final double MAX_RATIO = 0.95;

    private WindowManager() {}

    public static void configure(Stage stage, String pageTitle, Parent root) {
        Screen screen = Screen.getPrimary();
        javafx.geometry.Rectangle2D bounds = screen.getVisualBounds();

        double targetWidth = bounds.getWidth() * SIZE_RATIO;
        double targetHeight = bounds.getHeight() * SIZE_RATIO;

        double minWidth = Math.max(bounds.getWidth() * MIN_RATIO, MIN_ABSOLUTE_WIDTH);
        double minHeight = Math.max(bounds.getHeight() * MIN_RATIO, MIN_ABSOLUTE_HEIGHT);
        double maxWidth = bounds.getWidth() * MAX_RATIO;
        double maxHeight = bounds.getHeight() * MAX_RATIO;

        double finalWidth = clamp(targetWidth, minWidth, maxWidth);
        double finalHeight = clamp(targetHeight, minHeight, maxHeight);

        Scene scene = new Scene(root, finalWidth, finalHeight);
        UITheme.loadStylesheet(scene);

        stage.setTitle("CinemaBook \u2014 " + pageTitle);
        stage.setScene(scene);
        stage.setWidth(finalWidth);
        stage.setHeight(finalHeight);
        stage.centerOnScreen();

        FadeTransition fade = new FadeTransition(Duration.millis(200), root);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();

        stage.show();

        log.info(
                "Opened window [{}] — {}x{} (screen: {}x{})",
                pageTitle,
                Integer.valueOf((int) finalWidth),
                Integer.valueOf((int) finalHeight),
                Integer.valueOf((int) bounds.getWidth()),
                Integer.valueOf((int) bounds.getHeight()));
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
