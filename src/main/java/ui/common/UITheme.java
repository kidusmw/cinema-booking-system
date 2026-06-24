package ui.common;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

public class UITheme {

    public static void loadStylesheet(Scene scene) {
        scene.getStylesheets().add(UITheme.class.getResource("/css/styles.css").toExternalForm());
    }

    public static void styleButton(Button btn, String... classes) {
        btn.getStyleClass().addAll(classes);
    }

    public static void styleLabel(Label label, String... classes) {
        label.getStyleClass().addAll(classes);
    }

    public static void styleTable(TableView<?> table, String... classes) {
        table.getStyleClass().addAll(classes);
    }

    public static void styleTextField(TextField field, String... classes) {
        field.getStyleClass().addAll(classes);
    }

    public static void styleRegion(Region region, String... classes) {
        region.getStyleClass().addAll(classes);
    }
}
