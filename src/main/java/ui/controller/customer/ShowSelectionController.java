package ui.controller.customer;

import application.AppContext;
import domain.model.Movie;
import domain.model.Showtime;
import domain.model.User;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import ui.controller.common.NavigationManager;
import ui.view.customer.ShowSelectionPage;

public class ShowSelectionController {

    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(ShowSelectionController.class);
    private ShowSelectionPage view;
    private Stage stage;
    private User currentUser;
    private Movie selectedMovie;
    private AppContext ctx;
    private NavigationManager nav;
    private List<Showtime> movieShows;

    private static final String ACCENT = "#DB2777";
    private static final String TEXT_DARK = "#1E293B";
    private static final String TEXT_MUTED = "#64748B";
    private static final String BORDER = "#E2E8F0";
    private static final String WHITE = "#FFFFFF";

    public ShowSelectionController(
            Stage stage,
            AppContext ctx,
            NavigationManager nav,
            User currentUser,
            Movie selectedMovie) {
        this.stage = stage;
        this.ctx = ctx;
        this.nav = nav;
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
        view.btnBack.setOnAction(e -> nav.back());
    }

    private void loadShows() {
        view.showCardsContainer.getChildren().clear();
        view.dateButtonsContainer.getChildren().clear();
        movieShows =
                ctx.showtimeRepo.findByMovieId(selectedMovie.getMovieId()).stream()
                        .collect(Collectors.toList());

        if (movieShows.isEmpty()) {
            Label noShows = new Label("🎬 No shows available for this movie yet.");
            noShows.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
            noShows.setTextFill(Color.web(TEXT_MUTED));
            view.showCardsContainer.getChildren().add(noShows);
            return;
        }
        Set<LocalDate> uniqueDates = new TreeSet<>();
        for (Showtime show : movieShows) {
            if (show.getShowDate() != null) {
                uniqueDates.add(show.getShowDate());
            }
        }
        createDateButtons(uniqueDates);
        displayShowsForDate(null);
    }

    private void createDateButtons(Set<LocalDate> uniqueDates) {

        Button allBtn = createDateButton("All Dates", null);
        allBtn.setStyle(getDateButtonStyle(true));
        view.dateButtonsContainer.getChildren().add(allBtn);

        for (LocalDate date : uniqueDates) {
            String dateStr = date.toString();
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
        btn.setOnAction(
                e -> {
                    // Reset all buttons
                    for (javafx.scene.Node node : view.dateButtonsContainer.getChildren()) {
                        if (node instanceof Button) {
                            ((Button) node).setStyle(getDateButtonStyle(false));
                        }
                    }
                    btn.setStyle(getDateButtonStyle(true));
                    displayShowsForDate(dateValue);
                });
        return btn;
    }

    private void displayShowsForDate(String dateFilter) {
        view.showCardsContainer.getChildren().clear();
        List<Showtime> filteredShows = movieShows;
        if (dateFilter != null) {
            filteredShows =
                    movieShows.stream()
                            .filter(
                                    s ->
                                            s.getShowDate() != null
                                                    && s.getShowDate()
                                                            .toString()
                                                            .equals(dateFilter))
                            .collect(Collectors.toList());
        }

        if (filteredShows.isEmpty()) {
            Label noShows = new Label("No shows for selected date");
            noShows.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
            noShows.setTextFill(Color.web(TEXT_MUTED));
            view.showCardsContainer.getChildren().add(noShows);
            return;
        }
        for (Showtime show : filteredShows) {
            view.showCardsContainer.getChildren().add(createShowCard(show));
        }
    }

    private VBox createShowCard(Showtime show) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15, 20, 15, 20));
        card.setStyle(
                "-fx-background-color: "
                        + WHITE
                        + ";"
                        + "-fx-border-color: "
                        + BORDER
                        + ";"
                        + "-fx-border-radius: 10;"
                        + "-fx-background-radius: 10;"
                        + "-fx-cursor: hand;");
        card.setOnMouseEntered(
                e ->
                        card.setStyle(
                                "-fx-background-color: #FCE7F3;"
                                        + "-fx-border-color: "
                                        + ACCENT
                                        + ";"
                                        + "-fx-border-radius: 10;"
                                        + "-fx-background-radius: 10;"
                                        + "-fx-cursor: hand;"));
        card.setOnMouseExited(
                e ->
                        card.setStyle(
                                "-fx-background-color: "
                                        + WHITE
                                        + ";"
                                        + "-fx-border-color: "
                                        + BORDER
                                        + ";"
                                        + "-fx-border-radius: 10;"
                                        + "-fx-background-radius: 10;"
                                        + "-fx-cursor: hand;"));
        HBox topRow = new HBox(15);
        topRow.setAlignment(Pos.CENTER_LEFT);

        Label dateLabel = new Label("📅 " + show.getShowDate().toString());
        dateLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        dateLabel.setTextFill(Color.web(TEXT_DARK));

        Label timeLabel = new Label("🕐 " + show.getShowTime());
        timeLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        timeLabel.setTextFill(Color.web(ACCENT));

        topRow.getChildren().addAll(dateLabel, timeLabel);
        Label hallLabel = new Label("🏛️ Hall: " + show.getHallId());
        hallLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 12));
        hallLabel.setTextFill(Color.web(TEXT_MUTED));
        Button bookBtn = new Button("Book Seats →");
        bookBtn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        bookBtn.setPrefHeight(32);
        bookBtn.setTextFill(Color.WHITE);
        bookBtn.setStyle(
                "-fx-background-color: "
                        + ACCENT
                        + ";"
                        + "-fx-background-radius: 6;"
                        + "-fx-cursor: hand;");
        bookBtn.setOnAction(
                e ->
                        nav.go(
                                () ->
                                        new ShowSelectionController(
                                                stage, ctx, nav, currentUser, selectedMovie),
                                () ->
                                        new MovieHallSelectionController(
                                                stage,
                                                ctx,
                                                nav,
                                                currentUser,
                                                selectedMovie,
                                                show)));

        HBox bottomRow = new HBox();
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        bottomRow.getChildren().addAll(hallLabel, spacer, bookBtn);

        card.getChildren().addAll(topRow, bottomRow);
        return card;
    }

    private String getDateButtonStyle(boolean active) {
        if (active) {
            return "-fx-background-color: "
                    + ACCENT
                    + ";"
                    + "-fx-text-fill: white;"
                    + "-fx-background-radius: 18;"
                    + "-fx-cursor: hand;"
                    + "-fx-padding: 8 16;"
                    + "-fx-font-weight: bold;";
        } else {
            return "-fx-background-color: "
                    + WHITE
                    + ";"
                    + "-fx-text-fill: "
                    + TEXT_DARK
                    + ";"
                    + "-fx-border-color: "
                    + BORDER
                    + ";"
                    + "-fx-border-radius: 18;"
                    + "-fx-background-radius: 18;"
                    + "-fx-cursor: hand;"
                    + "-fx-padding: 8 16;";
        }
    }

    private String formatDateLabel(LocalDate date) {
        LocalDate today = LocalDate.now();
        if (date.equals(today)) {
            return "Today";
        } else if (date.equals(today.plusDays(1))) {
            return "Tomorrow";
        } else {
            return date.format(DateTimeFormatter.ofPattern("EEE MMM dd"));
        }
    }
}
