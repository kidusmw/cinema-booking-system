package Controller;

import Controller.CustomerDashboardController;
import Controller.ShowSelectionController;
import DAO.MovieDAO;
import Model.Customer;
import Model.Movie;
import View.MovieBrowserpage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class MovieBrowserController{
    private MovieBrowserpage view;
    private Stage stage;
    private Customer currentUser;
    private final MovieDAO movieDAO = new MovieDAO();
    private ObservableList<Movie> movies;

    public MovieBrowserController(Stage stage, Customer currentUser) {
        this.stage = stage;
        this.currentUser = currentUser;
        this.view = new MovieBrowserpage();
        Scene scene = new Scene(view.getView(), 1200, 750);
        stage.setTitle("Browse Movies - CinemaBook");
        stage.setScene(scene);
        stage.show();
        loadMovies();
        view.btnBack.setOnAction(e -> {
            new CustomerDashboardController(stage, currentUser);
        });
        view.searchField.textProperty().addListener((obs, old, newVal) -> filterMovies(newVal));
    }

    private void loadMovies() {
        List<Movie> movieList = movieDAO.getAllMovies();
        movies = FXCollections.observableArrayList(movieList);
        view.movieContainer.getChildren().clear();

        for (Movie movie : movies) {
            view.movieContainer.getChildren().add(createMovieCard(movie));
        }
    }

    private void filterMovies(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            loadMovies();
            return;
        }
        view.movieContainer.getChildren().clear();
        String lower = searchText.toLowerCase();
        for (Movie movie : movies) {
            if (movie.getTitle().toLowerCase().contains(lower) ||
                    movie.getGenre().toLowerCase().contains(lower)) {
                view.movieContainer.getChildren().add(createMovieCard(movie));
            }
        }
    }

    private VBox createMovieCard(Movie movie) {
        VBox card = new VBox(0);
        card.setPrefWidth(280);
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: #E2E8F0;" +
                        "-fx-border-radius: 12;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 2);"
        );
        card.setOnMouseEntered(e -> card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: #DB2777;" +
                        "-fx-border-radius: 12;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(219,39,119,0.2), 15, 0, 0, 4);"
        ));
        card.setOnMouseExited(e -> card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: #E2E8F0;" +
                        "-fx-border-radius: 12;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 2);"
        ));
        ImageView poster = new ImageView();
        poster.setFitWidth(280);
        poster.setFitHeight(350);
        poster.setPreserveRatio(false);

        String posterPath = movie.getPosterPath();
        if (posterPath != null && !posterPath.isEmpty() && new File(posterPath).exists()) {
            try {
                poster.setImage(new Image(new File(posterPath).toURI().toString()));
            } catch (Exception e) {
                poster.setImage(createPlaceholderImage(movie.getTitle()));
            }
        } else {
            poster.setImage(createPlaceholderImage(movie.getTitle()));
        }
        VBox info = new VBox(8);
        info.setPadding(new Insets(15));

        Label title = new Label(movie.getTitle());
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        title.setTextFill(Color.web("#1E293B"));
        title.setWrapText(true);

        HBox meta = new HBox(10);
        meta.setAlignment(Pos.CENTER_LEFT);

        Label genre = new Label(movie.getGenre());
        genre.setFont(Font.font("Segoe UI", 11));
        genre.setTextFill(Color.WHITE);
        genre.setStyle("-fx-background-color: #DB2777; -fx-background-radius: 12; -fx-padding: 3 10;");

        int hours = movie.getDuration() / 60;
        int mins = movie.getDuration() % 60;
        Label duration = new Label("⏱ " + hours + "h " + mins + "m");
        duration.setFont(Font.font("Segoe UI", 11));
        duration.setTextFill(Color.web("#64748B"));

        Label rating = new Label("⭐ " + String.format("%.1f", movie.getRating()));
        rating.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));
        rating.setTextFill(Color.web("#F59E0B"));

        meta.getChildren().addAll(genre, duration, rating);
        info.getChildren().addAll(title, meta);

        card.getChildren().addAll(poster, info);
        card.setOnMouseClicked(e -> {
            new ShowSelectionController(stage, currentUser, movie);
        });

        return card;
    }

    private Image createPlaceholderImage(String title) {
        javafx.scene.canvas.Canvas canvas = new javafx.scene.canvas.Canvas(280, 350);
        javafx.scene.canvas.GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(javafx.scene.paint.Color.web("#DB2777"));
        gc.fillRect(0, 0, 280, 350);
        gc.setFill(javafx.scene.paint.Color.WHITE);
        gc.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 20));
        gc.setTextAlign(javafx.scene.text.TextAlignment.CENTER);
        gc.fillText(title != null && title.length() > 20 ? title.substring(0, 20) + "..." : title, 140, 175);
        gc.setFont(javafx.scene.text.Font.font(60));
        gc.fillText("🎬", 140, 100);

        return canvas.snapshot(null, null);
    }
}
