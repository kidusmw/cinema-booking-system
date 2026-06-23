package ui.controller;

import application.AppContext;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.view.WelcomePage;

public class WelcomeController {

    private WelcomePage view;

    private NavigationManager nav;

    public WelcomeController(Stage stage, AppContext ctx, NavigationManager nav) {
        this.nav = nav;
        view = new WelcomePage();

        Scene scene = new Scene(view.getView(), 800, 600);
        stage.setTitle("CinemaBook - Welcome");
        stage.setScene(scene);
        stage.show();

        view.enterBtn.setOnAction(
                e ->
                        nav.go(
                                () -> new WelcomeController(stage, ctx, nav),
                                () -> new UsertypeController(stage, ctx, nav)));
    }
}
