package ui.view.customer;

import static ui.common.Theme.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MovieBrowserPage {
    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(MovieBrowserPage.class);

    public Button btnBack;
    public TextField searchField;
    public TilePane movieContainer;

    private VBox root;

    private static final String WHITE = "#FFFFFF";
    private static final String BG = "#FAFAFA";

    public MovieBrowserPage() {
        try {
            createUI();
        } catch (Exception e) {
            log.error("Failed to create MovieBrowserPage UI", e);
            root = new VBox();
            root.getChildren().add(new Label("Error: " + e.getMessage()));
        }
    }

    private void createUI() {
        root = new VBox(20);
        root.setStyle("-fx-background-color: " + BG + ";");
        root.setPadding(new Insets(30));

        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);

        btnBack = new Button("← Back");
        btnBack.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        btnBack.setTextFill(Color.web(TEXT_MUTED));
        btnBack.setStyle(
                "-fx-background-color: transparent;" + "-fx-cursor: hand;" + "-fx-padding: 8 16;");

        VBox titleBox = new VBox(3);
        Label title = new Label("🎬 Now Showing");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        title.setTextFill(Color.web(TEXT_DARK));
        Label subtitle = new Label("Pick a movie to book your seats");
        subtitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 12));
        subtitle.setTextFill(Color.web(TEXT_MUTED));
        titleBox.getChildren().addAll(title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        searchField = new TextField();
        searchField.setPromptText("🔍 Search movies...");
        searchField.setPrefHeight(38);
        searchField.setPrefWidth(300);
        searchField.setFont(Font.font("Segoe UI", 13));
        searchField.setStyle(
                "-fx-background-color: "
                        + WHITE
                        + ";"
                        + "-fx-border-color: "
                        + BORDER
                        + ";"
                        + "-fx-border-radius: 8;"
                        + "-fx-background-radius: 8;"
                        + "-fx-padding: 8 14;");

        header.getChildren().addAll(btnBack, titleBox, spacer, searchField);

        // Movies Grid
        movieContainer = new TilePane();
        movieContainer.setHgap(20);
        movieContainer.setVgap(20);
        movieContainer.setPadding(new Insets(20, 0, 0, 0));
        movieContainer.setAlignment(Pos.CENTER);
        VBox.setVgrow(movieContainer, Priority.ALWAYS);

        ScrollPane scroll = new ScrollPane(movieContainer);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: " + BG + "; -fx-border-color: transparent;");

        root.getChildren().addAll(header, scroll);
    }

    public VBox getView() {
        return root;
    }
}
