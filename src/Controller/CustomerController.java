package Controller;
import javafx.scene.Scene;
import javafx.stage.Stage;
import View.CustomerPage;

public class CustomerController {
    private CustomerPage view;

    public CustomerController(Stage stage) {

        view = new CustomerPage();

        Scene scene = new Scene(view.getView(), 600, 450);

        stage.setScene(scene);
        stage.setTitle("Customer Dashboard");
        stage.show();

        view.btnViewMovies.setOnAction(e -> {
            System.out.println("Go to Movie List Page");
        });

        view.btnMyBookings.setOnAction(e -> {
            System.out.println("Go to My Bookings Page");
        });

        view.btnLogout.setOnAction(e -> {
            System.out.println("Back to Welcome Page");
        });
    }

}

