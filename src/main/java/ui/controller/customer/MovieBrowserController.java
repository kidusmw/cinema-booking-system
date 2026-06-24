package ui.controller.customer;

import application.AppContext;
import domain.model.Movie;
import domain.model.User;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ui.common.WindowManager;
import ui.controller.common.NavigationManager;
import ui.view.customer.MovieBrowserPage;

public class MovieBrowserController {

    private static final Logger log = LoggerFactory.getLogger(MovieBrowserController.class);
    private MovieBrowserPage view;
    private Stage stage;
    private User currentUser;
    private AppContext ctx;
    private NavigationManager nav;
    private ObservableList<Movie> movies;

    public MovieBrowserController(
            Stage stage, AppContext ctx, NavigationManager nav, User currentUser) {
        this.stage = stage;
        this.ctx = ctx;
        this.nav = nav;
        this.currentUser = currentUser;
        this.view = new MovieBrowserPage();
        WindowManager.configure(stage, "Browse Movies", view.getView());
        loadMovies();
        view.btnBack.setOnAction(e -> nav.back());
        view.searchField.textProperty().addListener((obs, old, newVal) -> filterMovies(newVal));
    }

    private void loadMovies() {
        List<Movie> movieList = ctx.movieRepo.findAll().stream().collect(Collectors.toList());
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
            if (movie.getTitle().toLowerCase().contains(lower)
                    || movie.getGenre().toLowerCase().contains(lower)) {
                view.movieContainer.getChildren().add(createMovieCard(movie));
            }
        }
    }

    private VBox createMovieCard(Movie movie) {
        VBox card = new VBox(0);
        card.getStyleClass().add("w-280");
        card.getStyleClass().add("card");
        ImageView poster = new ImageView();
        poster.setFitWidth(280);
        poster.setFitHeight(350);
        poster.setPreserveRatio(false);

        String posterPath = movie.getPosterPath();
        if (posterPath != null && !posterPath.isEmpty() && new File(posterPath).exists()) {
            try {
                poster.setImage(new Image(new File(posterPath).toURI().toString()));
            } catch (Exception e) {
                log.error("Failed to load poster for movie [{}]", movie.getTitle(), e);
                poster.setImage(createPlaceholderImage(movie.getTitle()));
            }
        } else {
            poster.setImage(createPlaceholderImage(movie.getTitle()));
        }
        VBox info = new VBox(8);
        info.getStyleClass().add("p-15");

        Label title = new Label(movie.getTitle());
        title.getStyleClass().add("movie-title");
        title.setWrapText(true);

        HBox meta = new HBox(10);
        meta.getStyleClass().add("align-center-left");

        Label genre = new Label(movie.getGenre());
        genre.getStyleClass().addAll("movie-genre", "text-white");

        int hours = movie.getDuration() / 60;
        int mins = movie.getDuration() % 60;
        Label duration = new Label("⏱ " + hours + "h " + mins + "m");
        duration.getStyleClass().add("movie-duration");

        Label rating = new Label("⭐ " + String.format("%.1f", Double.valueOf(movie.getRating())));
        rating.getStyleClass().add("movie-rating");

        meta.getChildren().addAll(genre, duration, rating);
        info.getChildren().addAll(title, meta);

        card.getChildren().addAll(poster, info);
        card.setOnMouseClicked(
                e ->
                        nav.go(
                                () -> new MovieBrowserController(stage, ctx, nav, currentUser),
                                () ->
                                        new ShowSelectionController(
                                                stage, ctx, nav, currentUser, movie)));

        return card;
    }

    private static Image createPlaceholderImage(String title) {
        javafx.scene.canvas.Canvas canvas = new javafx.scene.canvas.Canvas(280, 350);
        javafx.scene.canvas.GraphicsContext gc = canvas.getGraphicsContext2D();
        // Canvas exception: cannot use CSS
        gc.setFill(javafx.scene.paint.Color.web("#DB2777"));
        gc.fillRect(0, 0, 280, 350);
        gc.setFill(javafx.scene.paint.Color.WHITE);
        // Canvas exception: cannot use CSS
        gc.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 20));
        gc.setTextAlign(javafx.scene.text.TextAlignment.CENTER);
        gc.fillText(
                title != null && title.length() > 20 ? title.substring(0, 20) + "..." : title,
                140,
                175);
        // Canvas exception: cannot use CSS
        gc.setFont(javafx.scene.text.Font.font(60));
        gc.fillText("🎬", 140, 100);

        return canvas.snapshot(null, null);
    }
}
