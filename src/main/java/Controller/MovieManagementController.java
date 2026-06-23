package Controller;
import View.MovieManagemnetPage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import Model.Movie;
import DAO.MovieDAO;
import java.util.List;
import java.util.Optional;
public class MovieManagementController {
    private MovieManagemnetPage view;
    private Stage stage;
    private AdminDashboardController dashboard; // Kept to navigate back
    private final MovieDAO movieDAO = new MovieDAO();
    private ObservableList<Movie> movieList;

    public MovieManagementController(Stage stage, AdminDashboardController dashboard) {
        this.stage = stage;
        this.dashboard = dashboard;
        view = new MovieManagemnetPage();

        Scene scene = new Scene(view.getView(), 1200, 750);
        stage.setTitle("Manage Movies - CinemaBook Admin");
        stage.setScene(scene);
        stage.show();

        loadMovies();

        view.btnAddMovie.setOnAction(e -> handleAddMovie());
        view.btnEdit.setOnAction(e -> handleEditMovie());
        view.btnDelete.setOnAction(e -> handleDeleteMovie());
        view.btnRefresh.setOnAction(e -> loadMovies());
        if (view.btnBack != null) {
            view.btnBack.setOnAction(e -> handleBackToDashboard());
        }

        view.searchField.textProperty().addListener((obs, oldVal, newVal) -> filterMovies(newVal));

        view.movieTable.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                handleEditMovie();
            }
        });
    }
    private void showAlert(String title, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void handleBackToDashboard() {
            new AdminDashboardController(stage, dashboard.getAdminName());
        }

        private void loadMovies() {
        List<Movie> movies = movieDAO.getAllMovies();
        movieList = FXCollections.observableArrayList(movies);
        view.movieTable.setItems(movieList);
    }

    private void filterMovies(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            view.movieTable.setItems(movieList);
            return;
        }
        ObservableList<Movie> filtered = FXCollections.observableArrayList();
        String lower = searchText.toLowerCase();
        for (Movie movie : movieList) {
            if (movie.getTitle().toLowerCase().contains(lower) ||
                    movie.getGenre().toLowerCase().contains(lower)) {
                filtered.add(movie);
            }
        }
        view.movieTable.setItems(filtered);
    }

    private void handleAddMovie() {
        Dialog<Movie> dialog = createMovieDialog(null);
        Optional<Movie> result = dialog.showAndWait();

        result.ifPresent(movie -> {
            boolean success = movieDAO.addMovie(movie);
            if (success) {
                showAlert("Success", "Movie added successfully!");
                loadMovies();
            } else {
                showAlert("Error", "Failed to add movie.");
            }
        });
    }

    private void handleEditMovie() {
        Movie selected = view.movieTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a movie to edit.");
            return;
        }

        Dialog<Movie> dialog = createMovieDialog(selected);
        Optional<Movie> result = dialog.showAndWait();

        result.ifPresent(movie -> {
            movie.setMovieID(selected.getMovieID());
            boolean success = movieDAO.updateMovie(movie);
            if (success) {
                showAlert("Success", "Movie updated successfully!");
                loadMovies();
            } else {
                showAlert("Error", "Failed to update movie.");
            }
        });
    }

    private void handleDeleteMovie() {
        Movie selected = view.movieTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a movie to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete Movie");
        confirm.setContentText("Are you sure you want to delete \"" + selected.getTitle() + "\"?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = movieDAO.deleteMovieByName(selected.getTitle());
            if (success) {
                showAlert("Success", "Movie deleted successfully!");
                loadMovies();
            } else {
                showAlert("Error", "Failed to delete movie.");
            }
        }
    }

    private Dialog<Movie> createMovieDialog(Movie existing) {
        Dialog<Movie> dialog = new Dialog<>();
        dialog.setTitle(existing == null ? "Add New Movie" : "Edit Movie");
        dialog.setHeaderText(existing == null ? "Enter complete movie details" : "Update movie details");
        ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 50, 10, 10));

        TextField titleField = new TextField();
        titleField.setPromptText("Title");
        if (existing != null) titleField.setText(existing.getTitle());

        TextField genreField = new TextField();
        genreField.setPromptText("Genre");
        if (existing != null) genreField.setText(existing.getGenre());

        TextField durationField = new TextField();
        durationField.setPromptText("Duration (minutes)");
        if (existing != null) durationField.setText(String.valueOf(existing.getDuration()));

        TextField ratingField = new TextField();
        ratingField.setPromptText("Rating");
        if (existing != null) ratingField.setText(String.valueOf(existing.getRating()));

        TextField langField = new TextField();
        langField.setPromptText("e.g. English, Spanish");
        if (existing != null) langField.setText(existing.getLanguage());

        TextArea descArea = new TextArea();
        descArea.setPromptText("Enter synopsis summary...");
        descArea.setPrefRowCount(3);
        if (existing != null) descArea.setText(existing.getDescription());

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("dd-MM-yyyy or yyyy/MM/dd");

        java.time.format.DateTimeFormatter displayFormatter = java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy");

        datePicker.setConverter(new javafx.util.StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return displayFormatter.format(date);
                }
                return "";
            }

            @Override
            public LocalDate fromString(String string) {
                if (string == null || string.trim().isEmpty()) {
                    return null;
                }

                String text = string.trim();
                String[] formats = { "dd-MM-yyyy", "yyyy/MM/dd", "yyyy-MM-dd", "MM/dd/yyyy" };

                for (String format : formats) {
                    try {
                        return LocalDate.parse(text, java.time.format.DateTimeFormatter.ofPattern(format));
                    } catch (java.time.format.DateTimeParseException ignored) {
                        // Try next format
                    }
                }
                return null;
            }
        });

        if (existing != null && existing.getReleaseDate() != null) {
            LocalDate localDate = new java.sql.Date(existing.getReleaseDate().getTime()).toLocalDate();
            datePicker.setValue(localDate);
        }

        TextField posterField = new TextField();
        posterField.setPromptText("No file selected");
        posterField.setEditable(false);
        Button btnBrowse = new Button("Browse...");
        if (existing != null) posterField.setText(existing.getPosterPath());

        btnBrowse.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Movie Poster Image File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.webp")
            );
            java.io.File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                posterField.setText(selectedFile.getAbsolutePath());
            }
        });

        HBox posterBox = new HBox(5, posterField, btnBrowse);
        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Genre:"), 0, 1);
        grid.add(genreField, 1, 1);
        grid.add(new Label("Duration (min):"), 0, 2);
        grid.add(durationField, 1, 2);
        grid.add(new Label("Rating:"), 0, 3);
        grid.add(ratingField, 1, 3);
        grid.add(new Label("Language:"), 0, 4);
        grid.add(langField, 1, 4);
        grid.add(new Label("Release Date:"), 0, 5);
        grid.add(datePicker, 1, 5); // grid now successfully references the declared variable
        grid.add(new Label("Poster File:"), 0, 6);
        grid.add(posterBox, 1, 6);
        grid.add(new Label("Description:"), 0, 7);
        grid.add(descArea, 1, 7);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButton) {
                try {
                    String text = datePicker.getEditor().getText();
                    if (text != null && !text.trim().isEmpty()) {
                        String[] formats = { "dd-MM-yyyy", "yyyy/MM/dd", "yyyy-MM-dd", "MM/dd/yyyy" };
                        LocalDate parsedDate = null;
                        for (String format : formats) {
                            try {
                                parsedDate = LocalDate.parse(text.trim(), java.time.format.DateTimeFormatter.ofPattern(format));
                                break;
                            } catch (Exception ignored) {}
                        }
                        if (parsedDate != null) {
                            datePicker.setValue(parsedDate);
                        }
                    }

                    Movie movie = new Movie();
                    movie.setTitle(titleField.getText().trim());
                    movie.setGenre(genreField.getText().trim());
                    movie.setDuration(Integer.parseInt(durationField.getText().trim()));

                    String rText = ratingField.getText().trim();
                    try {
                        movie.setRating(Double.parseDouble(rText));
                    } catch (NumberFormatException nfe) {
                        movie.setRating(0.0);
                    }

                    movie.setLanguage(langField.getText().trim());
                    movie.setDescription(descArea.getText().trim());
                    movie.setPosterPath(posterField.getText().trim());

                    if (datePicker.getValue() != null) {
                        java.util.Date date = java.util.Date.from(datePicker.getValue().atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
                        movie.setReleaseDate(date);
                    }
                    if (existing != null) {
                        movie.setMovieID(existing.getMovieID());
                    }

                    return movie;
                } catch (NumberFormatException e) {
                    showAlert("Invalid Input", "Duration must be a valid number.");
                    return null;
                }
            }
            return null;
        });

        return dialog;
    }}