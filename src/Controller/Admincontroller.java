package Controller;
import javafx.scene.Scene;
import javafx.stage.Stage;
import View.AdminPage;

public class Admincontroller {
    private AdminPage view;

    public Admincontroller(Stage stage) {

        view = new AdminPage();

        Scene scene = new Scene(view.getView(), 600, 450);

        stage.setScene(scene);
        stage.setTitle("Admin Dashboard");
        stage.show();

        view.btnAddMovie.setOnAction(e -> {
            System.out.println("Go to Add Movie Page");
        });

        view.btnEditMovie.setOnAction(e -> {
            System.out.println("Go to Edit Movie Page");
        });

        view.btnDeleteMovie.setOnAction(e -> {
            System.out.println("Go to Delete Movie Page");
        });

        view.btnManageShows.setOnAction(e -> {
            System.out.println("Go to Manage Shows Page");
        });

        view.btnViewBookings.setOnAction(e -> {
            System.out.println("Go to Booking View Page");
        });
    }

}
