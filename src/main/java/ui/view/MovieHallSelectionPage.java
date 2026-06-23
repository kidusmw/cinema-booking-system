package ui.view;

import static ui.common.Theme.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MovieHallSelectionPage {
    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(MovieHallSelectionPage.class);

    public Button btnBack;
    public Label showInfoLabel;
    public Label movieTitle;
    public VBox hallsContainer;

    private VBox root;

    private static final String WHITE = "#FFFFFF";
    private static final String BG = "#FAFAFA";
    private static final String BG_LIGHT = "#F8FAFC";

    public MovieHallSelectionPage() {
        try {
            createUI();
        } catch (Exception e) {
            log.error("Failed to create MovieHallSelectionPage UI", e);
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

        btnBack = new Button("← Back to Showtimes");
        btnBack.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        btnBack.setTextFill(Color.web(TEXT_MUTED));
        btnBack.setStyle(
                "-fx-background-color: transparent;" + "-fx-cursor: hand;" + "-fx-padding: 8 16;");

        topBar.getChildren().add(btnBack);
        VBox header = new VBox(8);

        movieTitle = new Label("🏛️ Select Your Hall");
        movieTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        movieTitle.setTextFill(Color.web(TEXT_DARK));

        showInfoLabel = new Label("Showtime info");
        showInfoLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        showInfoLabel.setTextFill(Color.web(TEXT_MUTED));

        header.getChildren().addAll(movieTitle, showInfoLabel);

        // ===== LEGEND =====
        HBox legend = new HBox(20);
        legend.setAlignment(Pos.CENTER_LEFT);
        legend.setPadding(new Insets(10, 15, 10, 15));
        legend.setStyle("-fx-background-color: " + BG_LIGHT + ";" + "-fx-background-radius: 8;");

        Label legendTitle = new Label("💡 Hall Types (Prices in ETB):");
        legendTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        legendTitle.setTextFill(Color.web(TEXT_DARK));

        HBox regularLegend = new HBox(8);
        regularLegend.setAlignment(Pos.CENTER_LEFT);
        Label regularDot = new Label("🎬");
        Label regularText = new Label("Regular - Standard seating");
        regularText.setFont(Font.font("Segoe UI", 11));
        regularText.setTextFill(Color.web(TEXT_MUTED));
        regularLegend.getChildren().addAll(regularDot, regularText);

        HBox vipLegend = new HBox(8);
        vipLegend.setAlignment(Pos.CENTER_LEFT);
        Label vipDot = new Label("👑");
        Label vipText = new Label("VIP - Premium recliner seats");
        vipText.setFont(Font.font("Segoe UI", 11));
        vipText.setTextFill(Color.web(TEXT_MUTED));
        vipLegend.getChildren().addAll(vipDot, vipText);

        legend.getChildren().addAll(legendTitle, regularLegend, vipLegend);
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: " + BG + "; -fx-border-color: transparent;");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        hallsContainer = new VBox(15);
        hallsContainer.setPadding(new Insets(10, 0, 10, 0));

        scroll.setContent(hallsContainer);
        root.getChildren().addAll(topBar, header, legend, scroll);
    }

    public VBox getView() {
        return root;
    }
}
