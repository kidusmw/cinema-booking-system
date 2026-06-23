package ui.controller;

import ui.view.UserTypePage;
import application.AppContext;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UsertypeController {

    private UserTypePage view;
    private NavigationManager nav;

    public UsertypeController(Stage stage, AppContext ctx, NavigationManager nav) {
        this.nav = nav;
        view = new UserTypePage();

        Scene scene = new Scene(view.getView(), 800, 600);
        stage.setTitle("Select Account Type");
        stage.setScene(scene);
        stage.show();

        view.setButtonHover(view.btnAdmin);
        view.setButtonHover(view.btnCustomer);

        view.btnBack.setOnAction(e -> nav.back());

        view.btnAdmin.setOnAction(e -> nav.go(
            () -> new UsertypeController(stage, ctx, nav),
            () -> new AuthChoiceController(stage, ctx, nav, "admin")
        ));

        view.btnCustomer.setOnAction(e -> nav.go(
            () -> new UsertypeController(stage, ctx, nav),
            () -> new AuthChoiceController(stage, ctx, nav, "customer")
        ));
    }
}
