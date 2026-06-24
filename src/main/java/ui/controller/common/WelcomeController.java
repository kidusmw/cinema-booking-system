package ui.controller.common;

import application.AppContext;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ui.common.WindowManager;
import ui.view.common.WelcomePage;

public class WelcomeController {
    private static final Logger log = LoggerFactory.getLogger(WelcomeController.class);

    private WelcomePage view;

    public WelcomeController(Stage stage, AppContext ctx, NavigationManager nav) {
        view = new WelcomePage();

        WindowManager.configure(stage, "Welcome", view.getView());
        log.info("Opening Welcome page");

        view.enterBtn.setOnAction(
                e ->
                        nav.go(
                                () -> new WelcomeController(stage, ctx, nav),
                                () -> new UsertypeController(stage, ctx, nav)));
    }
}
