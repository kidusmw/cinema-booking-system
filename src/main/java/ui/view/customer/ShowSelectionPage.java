package ui.view.customer;

import javafx.scene.control.*;
import javafx.scene.layout.*;

public class ShowSelectionPage {

    public Button btnBack;
    public Label movieTitle;
    public Label movieGenre;
    public Label movieRating;
    public Label movieDuration;
    public HBox dateButtonsContainer;
    public VBox showCardsContainer;

    private VBox root;

    public ShowSelectionPage() {
        createUI();
    }

    private void createUI() {
        root = new VBox(20);
        root.getStyleClass().add("page");
        root.getStyleClass().add("p-30");
        HBox topBar = new HBox(15);
        topBar.getStyleClass().add("align-center-left");

        btnBack = new Button("← Back to Movies");
        btnBack.getStyleClass().add("back-button");

        topBar.getChildren().add(btnBack);
        VBox movieInfoCard = new VBox(10);
        movieInfoCard.getStyleClass().add("p-20-25");
        movieInfoCard.getStyleClass().add("card");

        movieTitle = new Label("Movie Title");
        movieTitle.getStyleClass().add("title");

        HBox metaRow = new HBox(15);
        metaRow.getStyleClass().add("align-center-left");

        movieGenre = new Label("Genre");
        movieGenre.getStyleClass().add("badge");

        movieDuration = new Label("⏱ 2h 30m");
        movieDuration.getStyleClass().add("muted-text");

        movieRating = new Label("⭐ 8.5");
        movieRating.getStyleClass().add("star-rating");
        movieRating.getStyleClass().add("star-rating");

        metaRow.getChildren().addAll(movieGenre, movieDuration, movieRating);
        movieInfoCard.getChildren().addAll(movieTitle, metaRow);
        VBox dateSection = new VBox(10);
        Label dateLabel = new Label("📅 Select Date");
        dateLabel.getStyleClass().add("body-text");

        ScrollPane dateScroll = new ScrollPane();
        dateScroll.setFitToHeight(true);
        dateScroll.getStyleClass().add("scroll-pane-transparent");
        dateScroll.getStyleClass().add("h-60");

        dateButtonsContainer = new HBox(10);
        dateButtonsContainer.getStyleClass().add("align-center-left");
        dateButtonsContainer.getStyleClass().add("p-0");

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
        showCardsContainer.getStyleClass().add("p-5-0");

        showsScroll.setContent(showCardsContainer);
        showsSection.getChildren().addAll(showsLabel, showsScroll);
        root.getChildren().addAll(topBar, movieInfoCard, dateSection, showsSection);
    }

    public VBox getView() {
        return root;
    }
}
