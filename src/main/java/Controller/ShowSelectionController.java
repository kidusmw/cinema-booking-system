package Controller;

import DAO.ShowDAO;
import DAO.ShowDAOimp;
import Model.Show;
import Model.Movie;
import Model.Customer;
import View.ShowSelectionPage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
// ✅ FIXED: Missing geometric alignment package import added to resolve variable Pos crash
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ShowSelectionController {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ShowSelectionController.class);
    private ShowSelectionPage view;
    private Stage stage;
    private Customer currentUser;
    private Movie selectedMovie;
    private final ShowDAO showDAO = new ShowDAOimp();
    private List<Show> movieShows;

    private static final String ACCENT = "#DB2777";
    private static final String HOVER = "#EC4899";
    private static final String TEXT_DARK = "#1E293B";
    private static final String TEXT_MUTED = "#64748B";
    private static final String BORDER = "#E2E8F0";
    private static final String WHITE = "#FFFFFF";
    private static final String BG = "#FAFAFA";
    private static final String BG_LIGHT = "#F8FAFC";
    private static final String SUCCESS = "#10B981";

    public ShowSelectionController(Stage stage, Customer currentUser, Movie selectedMovie) {
        this.stage = stage;
        this.currentUser = currentUser;
        this.selectedMovie = selectedMovie;
        this.view = new ShowSelectionPage();

        Scene scene = new Scene(view.getView(), 1100, 700);
        stage.setTitle("Select Showtime - " + selectedMovie.getTitle());
        stage.setScene(scene);
        stage.show();

        view.movieTitle.setText(selectedMovie.getTitle());
        view.movieGenre.setText(selectedMovie.getGenre());
        view.movieRating.setText("⭐ " + String.format("%.1f", selectedMovie.getRating()));
        int hours = selectedMovie.getDuration() / 60;
        int mins = selectedMovie.getDuration() % 60;
        view.movieDuration.setText("⏱ " + hours + "h " + mins + "m");

        loadShows();
        view.btnBack.setOnAction(e -> {
            new MovieBrowserController(stage, currentUser);
        });
    }

    private void loadShows() {
        view.showCardsContainer.getChildren().clear();
        view.dateButtonsContainer.getChildren().clear();
        movieShows = showDAO.getShowsByMovie(selectedMovie.getMovieID());

        if (movieShows.isEmpty()) {
            Label noShows = new Label("🎬 No shows available for this movie yet.");
            noShows.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
            noShows.setTextFill(Color.web(TEXT_MUTED));
            view.showCardsContainer.getChildren().add(noShows);
            return;
        }
        Set<Date> uniqueDates = new TreeSet<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Show show : movieShows) {
            if (show.getShowDate() != null) {
                String dateStr = sdf.format(show.getShowDate());
                try {
                    uniqueDates.add(sdf.parse(dateStr));
                } catch (Exception e) {
                    log.error("Failed to parse date", e);
                }
            }
        }
        createDateButtons(uniqueDates, sdf);
        displayShowsForDate(null, sdf);
    }

    private void createDateButtons(Set<Date> uniqueDates, SimpleDateFormat sdf) {

        Button allBtn = createDateButton("All Dates", null);
        allBtn.setStyle(getDateButtonStyle(true));
        view.dateButtonsContainer.getChildren().add(allBtn);

        for (Date date : uniqueDates) {
            String dateStr = sdf.format(date);
            Button dateBtn = createDateButton(formatDateLabel(date), dateStr);
            view.dateButtonsContainer.getChildren().add(dateBtn);
        }
    }

    private Button createDateButton(String label, String dateValue) {
        Button btn = new Button(label);
        btn.setUserData(dateValue);
        btn.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 12));
        btn.setPrefHeight(36);
        btn.setStyle(getDateButtonStyle(false));
        btn.setOnAction(e -> {
            // Reset all buttons
            for (javafx.scene.Node node : view.dateButtonsContainer.getChildren()) {
                if (node instanceof Button) {
                    ((Button) node).setStyle(getDateButtonStyle(false));
                }
            }
            btn.setStyle(getDateButtonStyle(true));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            displayShowsForDate(dateValue, sdf);
        });
        return btn;
    }

    private void displayShowsForDate(String dateFilter, SimpleDateFormat sdf) {
        view.showCardsContainer.getChildren().clear();
        List<Show> filteredShows = movieShows;
        if (dateFilter != null) {
            filteredShows = movieShows.stream()
                    .filter(s -> s.getShowDate() != null && sdf.format(s.getShowDate()).equals(dateFilter))
                    .collect(Collectors.toList());
        }

        if (filteredShows.isEmpty()) {
            Label noShows = new Label("No shows for selected date");
            noShows.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
            noShows.setTextFill(Color.web(TEXT_MUTED));
            view.showCardsContainer.getChildren().add(noShows);
            return;
        }
        for (Show show : filteredShows) {
            view.showCardsContainer.getChildren().add(createShowCard(show, sdf));
        }
    }

    private VBox createShowCard(Show show, SimpleDateFormat sdf) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15, 20, 15, 20));
        card.setStyle(
                "-fx-background-color: " + WHITE + ";" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;"
        );
        card.setOnMouseEntered(e -> card.setStyle(
                "-fx-background-color: #FCE7F3;" +
                        "-fx-border-color: " + ACCENT + ";" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;"
        ));
        card.setOnMouseExited(e -> card.setStyle(
                "-fx-background-color: " + WHITE + ";" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;"
        ));
        HBox topRow = new HBox(15);
        topRow.setAlignment(Pos.CENTER_LEFT);

        Label dateLabel = new Label("📅 " + sdf.format(show.getShowDate()));
        dateLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        dateLabel.setTextFill(Color.web(TEXT_DARK));

        Label timeLabel = new Label("🕐 " + show.getShowTime());
        timeLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        timeLabel.setTextFill(Color.web(ACCENT));

        topRow.getChildren().addAll(dateLabel, timeLabel);
        Label hallLabel = new Label("🏛️ Hall: " + show.getMovieHallID());
        hallLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 12));
        hallLabel.setTextFill(Color.web(TEXT_MUTED));
        Button bookBtn = new Button("Book Seats →");
        bookBtn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        bookBtn.setPrefHeight(32);
        bookBtn.setTextFill(Color.WHITE);
        bookBtn.setStyle(
                "-fx-background-color: " + ACCENT + ";" +
                        "-fx-background-radius: 6;" +
                        "-fx-cursor: hand;"
        );
        bookBtn.setOnAction(e -> {
            new MovieHallSelectionController(stage, currentUser, selectedMovie, show);
        });

        HBox bottomRow = new HBox();
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        bottomRow.getChildren().addAll(hallLabel, spacer, bookBtn);

        card.getChildren().addAll(topRow, bottomRow);
        return card;
    }

    private String getDateButtonStyle(boolean active) {
        if (active) {
            return
                    "-fx-background-color: " + ACCENT + ";" +
                            "-fx-text-fill: white;" +
                            "-fx-background-radius: 18;" +
                            "-fx-cursor: hand;" +
                            "-fx-padding: 8 16;" +
                            "-fx-font-weight: bold;";
        } else {
            return
                    "-fx-background-color: " + WHITE + ";" +
                            "-fx-text-fill: " + TEXT_DARK + ";" +
                            "-fx-border-color: " + BORDER + ";" +
                            "-fx-border-radius: 18;" +
                            "-fx-background-radius: 18;" +
                            "-fx-cursor: hand;" +
                            "-fx-padding: 8 16;";
        }
    }

    private String formatDateLabel(Date date) {
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEE");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Calendar today = Calendar.getInstance();

        if (cal.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                cal.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            return "Today";
        } else if (cal.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                cal.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR) + 1) {
            return "Tomorrow";
        } else {
            return dayFormat.format(date) + " " + dateFormat.format(date);
        }
    }
}