package ui.view;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;


public class CustomerPage  {

    public Button btnViewMovies;
    public Button btnMyBookings;
    public Button btnLogout;

    public VBox getView() {

        Label title = new Label("CUSTOMER DASHBOARD");
        title.setFont(new Font("Arial", 22));
        title.setStyle("-fx-text-fill: #c71585;");

        Label welcome = new Label("Welcome to Simple Cinema Booking System");

        btnViewMovies = new Button("View Movies");
        btnMyBookings = new Button("My Bookings");
        btnLogout = new Button("Logout");

        style(btnViewMovies);
        style(btnMyBookings);
        style(btnLogout);

        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);

        root.getChildren().addAll(
                title,
                welcome,
                btnViewMovies,
                btnMyBookings,
                btnLogout
        );

        return root;
    }

    private void style(Button b) {
        b.setStyle("-fx-background-color: hotpink; -fx-text-fill: white;");
        b.setPrefWidth(180);
    }
}


