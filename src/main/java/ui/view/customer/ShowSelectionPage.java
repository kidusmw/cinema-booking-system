package ui.view.customer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ShowSelectionPage {
    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(ShowSelectionPage.class);

    public Button btnBack;
    public Label movieTitle;
    public Label movieGenre;
    public Label movieRating;
    public Label movieDuration;
    public HBox dateButtonsContainer;
    public VBox showCardsContainer;

    private VBox root;

    public ShowSelectionPage() {
        try {
            createUI();
        } catch (Exception e) {
            log.error("Failed to create ShowSelectionPage UI", e);
            root = new VBox();
            root.getChildren().add(new Label("Error: " + e.getMessage()));
        }
    }

    private void createUI() {
        root = new VBox(20);
        root.getStyleClass().add("page");
        root.setPadding(new Insets(30));
        HBox topBar = new HBox(15);
        topBar.setAlignment(Pos.CENTER_LEFT);

        btnBack = new Button("← Back to Movies");
        btnBack.getStyleClass().add("back-button");

        topBar.getChildren().add(btnBack);
        VBox movieInfoCard = new VBox(10);
        movieInfoCard.setPadding(new Insets(20, 25, 20, 25));
        movieInfoCard.getStyleClass().add("card");

        movieTitle = new Label("Movie Title");
        movieTitle.getStyleClass().add("title");

        HBox metaRow = new HBox(15);
        metaRow.setAlignment(Pos.CENTER_LEFT);

        movieGenre = new Label("Genre");
        movieGenre.getStyleClass().add("badge");

        movieDuration = new Label("⏱ 2h 30m");
        movieDuration.getStyleClass().add("muted-text");

        movieRating = new Label("⭐ 8.5");
        movieRating.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        movieRating.getStyleClass().add("star-rating");

        metaRow.getChildren().addAll(movieGenre, movieDuration, movieRating);
        movieInfoCard.getChildren().addAll(movieTitle, metaRow);
        VBox dateSection = new VBox(10);
        Label dateLabel = new Label("📅 Select Date");
        dateLabel.getStyleClass().add("body-text");

        ScrollPane dateScroll = new ScrollPane();
        dateScroll.setFitToHeight(true);
        dateScroll.getStyleClass().add("scroll-pane-transparent");
        dateScroll.setPrefHeight(60);

        dateButtonsContainer = new HBox(10);
        dateButtonsContainer.setAlignment(Pos.CENTER_LEFT);
        dateButtonsContainer.setPadding(new Insets(0, 0, 0, 0));

        dateScroll.setContent(dateButtonsContainer);
        dateSection.getChildren().addAll(dateLabel, dateScroll);
        VBox showsSection = new VBox(10);
        Label showsLabel = new Label("🕐 Available Showtimes");
        showsLabel.getStyleClass().add("body-text");

        ScrollPane showsScroll = new ScrollPane();
        showsScroll.setFitToWidth(true);
        showsScroll.getStyleClass().add("scroll-pane");
        VBox.setVgrow(showsScroll, Priority.ALWAYS);

        showCardsContainer = new VBox(10);
        showCardsContainer.setPadding(new Insets(5, 0, 5, 0));

        showsScroll.setContent(showCardsContainer);
        showsSection.getChildren().addAll(showsLabel, showsScroll);
        root.getChildren().addAll(topBar, movieInfoCard, dateSection, showsSection);
    }

    public VBox getView() {
        return root;
    }
}
