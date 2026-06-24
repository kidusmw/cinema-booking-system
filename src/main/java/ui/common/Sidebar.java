package ui.common;

import java.util.function.Consumer;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class Sidebar extends VBox {

    private final Button[] menuButtons;
    private Runnable logoutHandler;

    public Sidebar(String sectionLabel, String... menuItems) {
        super();
        getStyleClass().add("sidebar");

        HBox brandBox = new HBox(8);
        brandBox.getStyleClass().add("align-center-left");
        brandBox.getStyleClass().add("p-0-0-20-8");

        StackPane logo = new StackPane();
        logo.setPrefSize(32, 32);
        logo.getStyleClass().add("logo-badge");
        Label logoIcon = new Label("\uD83C\uDFAC");
        logo.getChildren().add(logoIcon);

        Label brand = new Label("CinemaBook");
        brand.getStyleClass().add("section-title");

        brandBox.getChildren().addAll(logo, brand);

        Label section = new Label(sectionLabel);
        section.getStyleClass().add("muted-text");
        section.getStyleClass().add("p-10-8");

        menuButtons = new Button[menuItems.length];
        for (int i = 0; i < menuItems.length; i++) {
            menuButtons[i] = createMenuButton(menuItems[i]);
        }

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button logout = new Button("\uD83D\uDEAA  Logout");
        logout.setPrefWidth(Double.MAX_VALUE);
        logout.getStyleClass().add("h-40");
        logout.getStyleClass().add("logout-button");
        logout.setOnAction(
                e -> {
                    if (logoutHandler != null) {
                        logoutHandler.run();
                    }
                });

        getChildren().add(brandBox);
        getChildren().add(section);
        getChildren().addAll(menuButtons);
        getChildren().addAll(spacer, logout);
    }

    public void setOnMenuClick(Consumer<String> handler) {
        for (Button btn : menuButtons) {
            btn.setOnAction(
                    e -> {
                        if (handler != null) {
                            handler.accept(btn.getText());
                        }
                    });
        }
    }

    public void setActive(String menuLabel) {
        for (Button btn : menuButtons) {
            btn.getStyleClass().removeAll("sidebar-btn", "active");
            btn.getStyleClass().add("sidebar-btn");
            if (btn.getText().equals(menuLabel)) {
                btn.getStyleClass().add("active");
            }
        }
    }

    public void setLogoutHandler(Runnable handler) {
        this.logoutHandler = handler;
    }

    private static Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(Double.MAX_VALUE);
        btn.getStyleClass().add("h-40");
        btn.getStyleClass().addAll("sidebar-btn");
        return btn;
    }
}
