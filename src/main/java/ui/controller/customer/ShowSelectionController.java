package ui.controller.customer;

import application.AppContext;
import domain.model.Movie;
import domain.model.Showtime;
import domain.model.User;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ui.common.WindowManager;
import ui.controller.common.NavigationManager;
import ui.view.customer.ShowSelectionPage;

public class ShowSelectionController {
    private static final Logger log = LoggerFactory.getLogger(ShowSelectionController.class);

    private ShowSelectionPage view;
    private Stage stage;
    private User currentUser;
    private Movie selectedMovie;
    private AppContext ctx;
    private NavigationManager nav;
    private List<Showtime> movieShows;

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

        WindowManager.configure(stage, "Select Showtime", view.getView());
        log.info("Opening Select Showtime page");

        view.movieTitle.setText(selectedMovie.getTitle());
        view.movieGenre.setText(selectedMovie.getGenre());
        view.movieRating.setText(
                "⭐ " + String.format("%.1f", Double.valueOf(selectedMovie.getRating())));
        int hours = selectedMovie.getDuration() / 60;
        int mins = selectedMovie.getDuration() % 60;
        view.movieDuration.setText("⏱ " + hours + "h " + mins + "m");

        loadShows();
        view.btnBack.setOnAction(e -> nav.back());
    }

    private void loadShows() {
        view.showCardsContainer.getChildren().clear();
        view.dateButtonsContainer.getChildren().clear();
        movieShows = ctx.showtimeRepo.findByMovieId(selectedMovie.getMovieId());

        if (movieShows.isEmpty()) {
            Label noShows = new Label("🎬 No shows available for this movie yet.");
            noShows.getStyleClass().add("empty-text");
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
        allBtn.getStyleClass().add("date-button-active");
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
        btn.getStyleClass().add("date-button");
        btn.getStyleClass().add("h-36");
        btn.setOnAction(
                e -> {
                    for (javafx.scene.Node node : view.dateButtonsContainer.getChildren()) {
                        if (node instanceof Button) {
                            ((Button) node).getStyleClass().removeAll("date-button-active");
                            ((Button) node).getStyleClass().add("date-button");
                        }
                    }
                    btn.getStyleClass().removeAll("date-button");
                    btn.getStyleClass().add("date-button-active");
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
            noShows.getStyleClass().add("empty-text");
            view.showCardsContainer.getChildren().add(noShows);
            return;
        }
        for (Showtime show : filteredShows) {
            view.showCardsContainer.getChildren().add(createShowCard(show));
        }
    }

    private VBox createShowCard(Showtime show) {
        VBox card = new VBox(10);
        card.getStyleClass().add("p-15-20");
        card.getStyleClass().add("card-sm");
        HBox topRow = new HBox(15);
        topRow.getStyleClass().add("align-center-left");

        Label dateLabel = new Label("📅 " + show.getShowDate().toString());
        dateLabel.getStyleClass().add("show-date-label");

        Label timeLabel = new Label("🕐 " + show.getShowTime());
        timeLabel.getStyleClass().add("show-time-label");

        topRow.getChildren().addAll(dateLabel, timeLabel);
        Label hallLabel = new Label("🏛️ Hall: " + show.getHallId());
        hallLabel.getStyleClass().add("show-hall-label");
        Button bookBtn = new Button("Book Seats →");
        bookBtn.getStyleClass().add("h-32");
        bookBtn.getStyleClass().add("primary-button-small");
        bookBtn.setOnAction(
                e -> {
                    ctx.hallRepo
                            .findById(show.getHallId())
                            .ifPresent(
                                    hall ->
                                            nav.go(
                                                    () ->
                                                            new ShowSelectionController(
                                                                    stage,
                                                                    ctx,
                                                                    nav,
                                                                    currentUser,
                                                                    selectedMovie),
                                                    () ->
                                                            new SeatSelectionController(
                                                                    stage,
                                                                    ctx,
                                                                    nav,
                                                                    currentUser,
                                                                    selectedMovie,
                                                                    show,
                                                                    hall,
                                                                    hall.isVip())));
                });

        HBox bottomRow = new HBox();
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        bottomRow.getChildren().addAll(hallLabel, spacer, bookBtn);

        card.getChildren().addAll(topRow, bottomRow);
        return card;
    }

    private static String formatDateLabel(LocalDate date) {
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
