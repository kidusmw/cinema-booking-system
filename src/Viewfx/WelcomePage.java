package Viewfx;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
public class WelcomePage extends Application {
    @Override
    public void start(Stage stage) {

        // TITLE
        Label title = new Label("WELCOME TO SIMPLE CINEMA BOOKING SYSTEM");
        title.setFont(new Font("Arial", 20));

        // ENTER BUTTON
        Button enterBtn = new Button("ENTER");
        enterBtn.setStyle("-fx-background-color: hotpink; -fx-text-fill: white;");

        // 3 DOT MENU (SIMULATED)
        MenuButton menu = new MenuButton("⋮");

        MenuItem about = new MenuItem("About Us");
        MenuItem complaint = new MenuItem("Complaint");
        MenuItem loginChoice = new MenuItem("Login Choice");

        menu.getItems().addAll(about, complaint, loginChoice);

        // ABOUT ACTION
        about.setOnAction(e -> {
            System.out.println("About: Simple cinema system made by students.");
        });

        // COMPLAINT ACTION (placeholder)
        complaint.setOnAction(e -> {
            System.out.println("Complaint section opened (email form later).");
        });

        // LOGIN CHOICE ACTION
        loginChoice.setOnAction(e -> {
            System.out.println("Go to Admin or Customer selection page.");
        });

        // ENTER ACTION
        enterBtn.setOnAction(e -> {
            System.out.println("Go to User Type Page.");
        });

        VBox root = new VBox(20, title, enterBtn, menu);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 600, 400);

        stage.setTitle("Cinema Booking System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
