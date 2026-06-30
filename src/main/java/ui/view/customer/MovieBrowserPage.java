package ui.view.customer;

import javafx.scene.control.*;
import javafx.scene.layout.*;

public class MovieBrowserPage {

    public Button btnBack;
    public TextField searchField;
    public TilePane movieContainer;

    private VBox root;

    public MovieBrowserPage() {
        createUI();
    }

    private void createUI() {
        root = new VBox(20);
        root.getStyleClass().add("page");
        root.getStyleClass().add("p-30");

        HBox header = new HBox(15);
        header.getStyleClass().add("align-center-left");

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
        searchField.getStyleClass().add("h-38");
        searchField.getStyleClass().add("w-300");
        searchField.getStyleClass().add("search-field");

        header.getChildren().addAll(btnBack, titleBox, spacer, searchField);

        // Movies Grid
        movieContainer = new TilePane();
        movieContainer.setHgap(20);
        movieContainer.setVgap(20);
        movieContainer.getStyleClass().add("pt-20");
        movieContainer.getStyleClass().add("align-center");
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
