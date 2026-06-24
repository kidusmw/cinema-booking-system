package ui.controller.common;

import application.AppContext;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ui.common.WindowManager;
import ui.view.common.UserTypePage;

public class UsertypeController {
    private static final Logger log = LoggerFactory.getLogger(UsertypeController.class);

    private UserTypePage view;

    public UsertypeController(Stage stage, AppContext ctx, NavigationManager nav) {
        view = new UserTypePage();

        WindowManager.configure(stage, "Select Account Type", view.getView());
        log.info("Opening Select Account Type page");

        view.btnBack.setOnAction(e -> nav.back());

        view.btnAdmin.setOnAction(
                e ->
                        nav.go(
                                () -> new UsertypeController(stage, ctx, nav),
                                () -> new AuthChoiceController(stage, ctx, nav, "admin")));

        view.btnCustomer.setOnAction(
                e ->
                        nav.go(
                                () -> new UsertypeController(stage, ctx, nav),
                                () -> new AuthChoiceController(stage, ctx, nav, "customer")));
    }
}
