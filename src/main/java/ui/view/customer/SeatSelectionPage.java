package ui.view.customer;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;

public class SeatSelectionPage {

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
        createUI();
    }

    private void createUI() {
        root = new VBox(15);
        root.getStyleClass().add("page");
        root.getStyleClass().add("p-25");

        HBox topBar = new HBox(15);
        topBar.getStyleClass().add("align-center-left");
        btnBack = new Button("← Back to Halls");
        btnBack.getStyleClass().add("back-button");
        topBar.getChildren().add(btnBack);

        HBox infoBar = new HBox(20);
        infoBar.getStyleClass().add("align-center-left");
        infoBar.getStyleClass().add("p-15-20");
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
        legend.getStyleClass().add("align-center");
        legend.getChildren()
                .addAll(
                        createLegendItem("legend-dot-available", "Available"),
                        createLegendItem("legend-dot-selected", "Selected"),
                        createLegendItem("legend-dot-booked", "Booked"));

        VBox screenBox = new VBox(10);
        screenBox.getStyleClass().add("align-center");
        Rectangle screen = new Rectangle(400, 8);
        screen.getStyleClass().add("screen-fill");
        screenLabel = new Label("🎬 SCREEN");
        screenBox.getChildren().addAll(screen, screenLabel);

        VBox seatsVBox = new VBox(20);
        seatsVBox.getStyleClass().add("card");
        seatGrid = new VBox(8);
        seatGrid.getStyleClass().add("align-center");
        seatsVBox.getChildren().add(seatGrid);

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
        root.getChildren().addAll(topBar, infoBar, legend, screenBox, seatsVBox, bottomBar);
    }

    private static HBox createLegendItem(String styleClass, String label) {
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
