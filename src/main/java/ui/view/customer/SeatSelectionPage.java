package ui.view.customer;

import static ui.common.Theme.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SeatSelectionPage {
    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(SeatSelectionPage.class);

    public Button btnBack;
    public Label hallNameLabel;
    public Label showInfoLabel;
    public Label priceLabel;
    public Label screenLabel;
    public VBox seatGrid;
    public Label selectedCountLabel;
    public Label totalAmountLabel;
    public Button btnProceed;
    public FlowPane seatContainer;
    private VBox root;

    public SeatSelectionPage() {
        try {
            createUI();
        } catch (Exception e) {
            log.error("Failed to create SeatSelectionPage UI", e);
            root = new VBox();
            root.getChildren().add(new Label("Error: " + e.getMessage()));
        }
        seatContainer = new FlowPane();
    }

    private void createUI() {
        root = new VBox(15);
        root.getStyleClass().add("page");
        root.setPadding(new Insets(25));

        HBox topBar = new HBox(15);
        topBar.setAlignment(Pos.CENTER_LEFT);
        btnBack = new Button("← Back to Halls");
        btnBack.getStyleClass().add("back-button");
        topBar.getChildren().add(btnBack);

        HBox infoBar = new HBox(20);
        infoBar.setAlignment(Pos.CENTER_LEFT);
        infoBar.setPadding(new Insets(15, 20, 15, 20));
        infoBar.getStyleClass().add("card");

        hallNameLabel = new Label("🏛️ Hall Name");
        hallNameLabel.getStyleClass().add("subtitle");
        showInfoLabel = new Label("Show info");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        priceLabel = new Label("50 Birr per seat");
        priceLabel.getStyleClass().add("text-accent");

        infoBar.getChildren().addAll(hallNameLabel, showInfoLabel, spacer, priceLabel);

        HBox legend = new HBox(25);
        legend.setAlignment(Pos.CENTER);
        legend.getChildren()
                .addAll(
                        createLegendItem("legend-dot-available", "Available"),
                        createLegendItem("legend-dot-selected", "Selected"),
                        createLegendItem("legend-dot-booked", "Booked"));

        VBox screenBox = new VBox(10);
        screenBox.setAlignment(Pos.CENTER);
        Rectangle screen = new Rectangle(400, 8);
        screen.setFill(Color.web(ACCENT));
        screenLabel = new Label("🎬 SCREEN");
        screenBox.getChildren().addAll(screen, screenLabel);

        VBox seatContainer = new VBox(20);
        seatContainer.getStyleClass().add("card");
        seatGrid = new VBox(8);
        seatGrid.setAlignment(Pos.CENTER);
        seatContainer.getChildren().add(seatGrid);

        HBox bottomBar = new HBox(20);
        bottomBar.getStyleClass().add("card");
        VBox summaryBox = new VBox(5);
        selectedCountLabel = new Label("0 seats selected");
        totalAmountLabel = new Label("0.00 Birr");
        totalAmountLabel.getStyleClass().addAll("title", "text-accent");
        summaryBox.getChildren().addAll(selectedCountLabel, totalAmountLabel);

        Region bottomSpacer = new Region();
        HBox.setHgrow(bottomSpacer, Priority.ALWAYS);
        btnProceed = new Button("Proceed to Payment →");
        btnProceed.setDisable(true);
        btnProceed.getStyleClass().add("primary-button");

        bottomBar.getChildren().addAll(summaryBox, bottomSpacer, btnProceed);
        root.getChildren().addAll(topBar, infoBar, legend, screenBox, seatContainer, bottomBar);
    }

    private HBox createLegendItem(String styleClass, String label) {
        HBox item = new HBox(8);
        Rectangle rect = new Rectangle(20, 20);
        rect.getStyleClass().add(styleClass);
        item.getChildren().addAll(rect, new Label(label));
        return item;
    }

    public VBox getView() {
        return root;
    }
}
