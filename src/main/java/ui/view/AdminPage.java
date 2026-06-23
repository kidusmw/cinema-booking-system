package ui.view;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class AdminPage  {
    public Button btnAddMovie;
    public Button btnEditMovie;
    public Button btnDeleteMovie;
    public Button btnManageShows;
    public Button btnViewBookings;

    public VBox getView() {

        Label title = new Label("ADMIN DASHBOARD");
        title.setFont(new Font("Arial", 22));
        title.setStyle("-fx-text-fill: #c71585;");

        btnAddMovie = new Button("Add Movie");
        btnEditMovie = new Button("Edit Movie");
        btnDeleteMovie = new Button("Delete Movie");
        btnManageShows = new Button("Manage Shows");
        btnViewBookings = new Button("View Bookings");

        style(btnAddMovie);
        style(btnEditMovie);
        style(btnDeleteMovie);
        style(btnManageShows);
        style(btnViewBookings);

        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);

        root.getChildren().addAll(
                title,
                btnAddMovie,
                btnEditMovie,
                btnDeleteMovie,
                btnManageShows,
                btnViewBookings
        );

        return root;
    }


    private void style(Button b) {
        b.setStyle("-fx-background-color: hotpink; -fx-text-fill: white;");
        b.setPrefWidth(180);
    }
    Button btnBack = new Button("← Back");


}
