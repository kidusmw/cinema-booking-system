package ui.view.customer;

import javafx.scene.control.*;
import javafx.scene.layout.*;

public class MovieHallSelectionPage {

    public Button btnBack;
    public Label showInfoLabel;
    public Label movieTitle;
    public VBox hallsContainer;

    private VBox root;

    public MovieHallSelectionPage() {
        createUI();
    }

    private void createUI() {
        root = new VBox(20);
        root.getStyleClass().add("page");
        root.getStyleClass().add("p-30");
        HBox topBar = new HBox(15);
        topBar.getStyleClass().add("align-center-left");

        btnBack = new Button("← Back to Showtimes");
        btnBack.getStyleClass().add("back-button");

        topBar.getChildren().add(btnBack);
        VBox header = new VBox(8);

        movieTitle = new Label("🏛️ Select Your Hall");
        movieTitle.getStyleClass().add("title");

        showInfoLabel = new Label("Showtime info");
        showInfoLabel.getStyleClass().add("muted-text");

        header.getChildren().addAll(movieTitle, showInfoLabel);

        // ===== LEGEND =====
        HBox legend = new HBox(20);
        legend.getStyleClass().add("align-center-left");
        legend.getStyleClass().add("p-10-15");
        legend.getStyleClass().add("info-card");

        Label legendTitle = new Label("💡 Hall Types (Prices in ETB):");
        legendTitle.getStyleClass().add("caption");

        HBox regularLegend = new HBox(8);
        regularLegend.getStyleClass().add("align-center-left");
        Label regularDot = new Label("🎬");
        Label regularText = new Label("Regular - Standard seating");
        regularText.getStyleClass().add("muted-text");
        regularLegend.getChildren().addAll(regularDot, regularText);

        HBox vipLegend = new HBox(8);
        vipLegend.getStyleClass().add("align-center-left");
        Label vipDot = new Label("👑");
        Label vipText = new Label("VIP - Premium recliner seats");
        vipText.getStyleClass().add("muted-text");
        vipLegend.getChildren().addAll(vipDot, vipText);

        legend.getChildren().addAll(legendTitle, regularLegend, vipLegend);
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.getStyleClass().add("scroll-pane");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        hallsContainer = new VBox(15);
        hallsContainer.getStyleClass().add("p-10-0");

        scroll.setContent(hallsContainer);
        root.getChildren().addAll(topBar, header, legend, scroll);
    }

    public VBox getView() {
        return root;
    }
}
