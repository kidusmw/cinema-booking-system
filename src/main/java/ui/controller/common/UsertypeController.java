package ui.controller.common;

import application.AppContext;
import javafx.stage.Stage;
import ui.common.WindowManager;
import ui.view.common.UserTypePage;

public class UsertypeController {

    private UserTypePage view;

    public UsertypeController(Stage stage, AppContext ctx, NavigationManager nav) {
        view = new UserTypePage();

        WindowManager.configureStage(stage, "Select Account Type", view.getView(), 800, 600);

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
