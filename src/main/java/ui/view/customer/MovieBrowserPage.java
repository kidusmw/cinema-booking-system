package ui.view.customer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class MovieBrowserPage {
    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(MovieBrowserPage.class);

    public Button btnBack;
    public TextField searchField;
    public TilePane movieContainer;

    private VBox root;

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
        root.getStyleClass().add("page");
        root.setPadding(new Insets(30));

        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);

        btnBack = new Button("← Back");
        btnBack.getStyleClass().add("back-button");

        VBox titleBox = new VBox(3);
        Label title = new Label("🎬 Now Showing");
        title.getStyleClass().add("title");
        Label subtitle = new Label("Pick a movie to book your seats");
        subtitle.getStyleClass().add("caption");
        titleBox.getChildren().addAll(title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        searchField = new TextField();
        searchField.setPromptText("🔍 Search movies...");
        searchField.setPrefHeight(38);
        searchField.setPrefWidth(300);
        searchField.getStyleClass().add("search-field");

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
        scroll.getStyleClass().add("scroll-pane");

        root.getChildren().addAll(header, scroll);
    }

    public VBox getView() {
        return root;
    }
}
