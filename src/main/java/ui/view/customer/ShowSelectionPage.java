package ui.view.customer;

import static ui.common.Theme.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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

    private static final String WHITE = "#FFFFFF";
    private static final String BG = "#FAFAFA";
    private static final String BG_LIGHT = "#F8FAFC";

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
        root.setStyle("-fx-background-color: " + BG + ";");
        root.setPadding(new Insets(30));
        HBox topBar = new HBox(15);
        topBar.setAlignment(Pos.CENTER_LEFT);

        btnBack = new Button("← Back to Movies");
        btnBack.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        btnBack.setTextFill(Color.web(TEXT_MUTED));
        btnBack.setStyle(
                "-fx-background-color: transparent;" + "-fx-cursor: hand;" + "-fx-padding: 8 16;");

        topBar.getChildren().add(btnBack);
        VBox movieInfoCard = new VBox(10);
        movieInfoCard.setPadding(new Insets(20, 25, 20, 25));
        movieInfoCard.setStyle(
                "-fx-background-color: "
                        + WHITE
                        + ";"
                        + "-fx-border-color: "
                        + BORDER
                        + ";"
                        + "-fx-border-radius: 12;"
                        + "-fx-background-radius: 12;");

        movieTitle = new Label("Movie Title");
        movieTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        movieTitle.setTextFill(Color.web(TEXT_DARK));

        HBox metaRow = new HBox(15);
        metaRow.setAlignment(Pos.CENTER_LEFT);

        movieGenre = new Label("Genre");
        movieGenre.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 12));
        movieGenre.setTextFill(Color.WHITE);
        movieGenre.setStyle(
                "-fx-background-color: "
                        + ACCENT
                        + ";"
                        + "-fx-background-radius: 12;"
                        + "-fx-padding: 4 12;");

        movieDuration = new Label("⏱ 2h 30m");
        movieDuration.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        movieDuration.setTextFill(Color.web(TEXT_MUTED));

        movieRating = new Label("⭐ 8.5");
        movieRating.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        movieRating.setTextFill(Color.web("#F59E0B"));

        metaRow.getChildren().addAll(movieGenre, movieDuration, movieRating);
        movieInfoCard.getChildren().addAll(movieTitle, metaRow);
        VBox dateSection = new VBox(10);
        Label dateLabel = new Label("📅 Select Date");
        dateLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        dateLabel.setTextFill(Color.web(TEXT_DARK));

        ScrollPane dateScroll = new ScrollPane();
        dateScroll.setFitToHeight(true);
        dateScroll.setStyle("-fx-background: transparent; -fx-border-color: transparent;");
        dateScroll.setPrefHeight(60);

        dateButtonsContainer = new HBox(10);
        dateButtonsContainer.setAlignment(Pos.CENTER_LEFT);
        dateButtonsContainer.setPadding(new Insets(0, 0, 0, 0));

        dateScroll.setContent(dateButtonsContainer);
        dateSection.getChildren().addAll(dateLabel, dateScroll);
        VBox showsSection = new VBox(10);
        Label showsLabel = new Label("🕐 Available Showtimes");
        showsLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        showsLabel.setTextFill(Color.web(TEXT_DARK));

        ScrollPane showsScroll = new ScrollPane();
        showsScroll.setFitToWidth(true);
        showsScroll.setStyle("-fx-background: " + BG + "; -fx-border-color: transparent;");
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
