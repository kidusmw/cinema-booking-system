package ui.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import static ui.common.Theme.*;

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
    private static final String HOVER = "#EC4899";
    private static final String WHITE = "#FFFFFF";
    private static final String BG = "#FAFAFA";

    public SeatSelectionPage() {
        try {
            createUI();
        } catch (Exception e) {
            e.printStackTrace();
            root = new VBox();
            root.getChildren().add(new Label("Error: " + e.getMessage()));
        }
        seatContainer = new FlowPane();
    }

    private void createUI() {
        root = new VBox(15);
        root.setStyle("-fx-background-color: " + BG + ";");
        root.setPadding(new Insets(25));

        HBox topBar = new HBox(15);
        topBar.setAlignment(Pos.CENTER_LEFT);
        btnBack = new Button("← Back to Halls");
        btnBack.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-padding: 8 16;");
        topBar.getChildren().add(btnBack);

        HBox infoBar = new HBox(20);
        infoBar.setAlignment(Pos.CENTER_LEFT);
        infoBar.setPadding(new Insets(15, 20, 15, 20));
        infoBar.setStyle("-fx-background-color: " + WHITE + "; -fx-border-color: " + BORDER + "; -fx-border-radius: 10; -fx-background-radius: 10;");

        hallNameLabel = new Label("🏛️ Hall Name");
        hallNameLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        showInfoLabel = new Label("Show info");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        priceLabel = new Label("50 Birr per seat");
        priceLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        priceLabel.setTextFill(Color.web(ACCENT));

        infoBar.getChildren().addAll(hallNameLabel, showInfoLabel, spacer, priceLabel);

        HBox legend = new HBox(25);
        legend.setAlignment(Pos.CENTER);
        legend.getChildren().addAll(
                createLegendItem("#10B981", "Available"),
                createLegendItem("#DB2777", "Selected"),
                createLegendItem("#EF4444", "Booked")
        );

        VBox screenBox = new VBox(10);
        screenBox.setAlignment(Pos.CENTER);
        Rectangle screen = new Rectangle(400, 8);
        screen.setFill(Color.web(ACCENT));
        screenLabel = new Label("🎬 SCREEN");
        screenBox.getChildren().addAll(screen, screenLabel);

        VBox seatContainer = new VBox(20);
        seatContainer.setStyle("-fx-background-color: " + WHITE + "; -fx-border-color: " + BORDER + "; -fx-border-radius: 12;");
        seatGrid = new VBox(8);
        seatGrid.setAlignment(Pos.CENTER);
        seatContainer.getChildren().add(seatGrid);

        HBox bottomBar = new HBox(20);
        bottomBar.setStyle("-fx-background-color: " + WHITE + "; -fx-border-color: " + BORDER + "; -fx-border-radius: 12;");
        VBox summaryBox = new VBox(5);
        selectedCountLabel = new Label("0 seats selected");
        totalAmountLabel = new Label("0.00 Birr");
        totalAmountLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        totalAmountLabel.setTextFill(Color.web(ACCENT));
        summaryBox.getChildren().addAll(selectedCountLabel, totalAmountLabel);

        Region bottomSpacer = new Region();
        HBox.setHgrow(bottomSpacer, Priority.ALWAYS);
        btnProceed = new Button("Proceed to Payment →");
        btnProceed.setDisable(true);
        btnProceed.setStyle("-fx-background-color: " + ACCENT + "; -fx-background-radius: 8; -fx-cursor: hand; -fx-text-fill: white;");

        bottomBar.getChildren().addAll(summaryBox, bottomSpacer, btnProceed);
        root.getChildren().addAll(topBar, infoBar, legend, screenBox, seatContainer, bottomBar);
    }

    private HBox createLegendItem(String color, String label) {
        HBox item = new HBox(8);
        Rectangle rect = new Rectangle(20, 20);
        rect.setFill(Color.web(color));
        item.getChildren().addAll(rect, new Label(label));
        return item;
    }

    public VBox getView() { return root; }
}