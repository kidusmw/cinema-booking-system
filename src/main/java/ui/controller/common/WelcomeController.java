package ui.controller.common;

import application.AppContext;
import javafx.stage.Stage;
import ui.common.WindowManager;
import ui.view.common.WelcomePage;

public class WelcomeController {

    private WelcomePage view;

    public WelcomeController(Stage stage, AppContext ctx, NavigationManager nav) {
        view = new WelcomePage();

        WindowManager.configureStage(stage, "CinemaBook - Welcome", view.getView(), 800, 600);

        view.enterBtn.setOnAction(
                e ->
                        nav.go(
                                () -> new WelcomeController(stage, ctx, nav),
                                () -> new UsertypeController(stage, ctx, nav)));
    }
}
