package ui.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import static ui.common.Theme.*;

public class TicketPage {

    public Label successLabel;
    public Label movieTitle;
    public Label movieGenre;
    public Label dateTimeLabel;
    public Label hallLabel;
    public Label seatsLabel;
    public Label customerLabel;
    public Label bookingIdLabel;
    public Label totalLabel;
    public Label bookedOnLabel;
    public Button btnDownload;
    public Button btnMyBookings;
    public Button btnHome;

    private VBox root;

    private static final String HOVER = "#EC4899";
    private static final String WHITE = "#FFFFFF";
    private static final String BG = "#FAFAFA";
    private static final String BG_LIGHT = "#F8FAFC";

    public TicketPage() {
        try {
            createUI();
        } catch (Exception e) {
            e.printStackTrace();
            root = new VBox();
            root.getChildren().add(new Label("Error: " + e.getMessage()));
        }
    }

    private void createUI() {
        root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: " + BG + ";");
        root.setPadding(new Insets(30));

        // Success message
        successLabel = new Label("🎉 Booking Confirmed!");
        successLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        successLabel.setTextFill(Color.web(SUCCESS));

        Label subMessage = new Label("Your ticket has been generated. Enjoy the show!");
        subMessage.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        subMessage.setTextFill(Color.web(TEXT_MUTED));

        // ===== TICKET =====
        VBox ticket = new VBox(0);
        ticket.setMaxWidth(600);
        ticket.setEffect(new DropShadow(20, Color.rgb(0, 0, 0, 0.15)));
        ticket.setStyle(
                "-fx-background-color: " + WHITE + ";" +
                        "-fx-background-radius: 15;"
        );

        // Top colored section
        VBox topSection = new VBox(10);
        topSection.setPadding(new Insets(25));
        topSection.setStyle("-fx-background-color: " + ACCENT + ";");

        Label cinemaLabel = new Label("🎬 CINEMABOOK");
        cinemaLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        cinemaLabel.setTextFill(Color.WHITE);

        Label ticketType = new Label("MOVIE TICKET");
        ticketType.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
        ticketType.setTextFill(Color.web("#FCE7F3"));

        topSection.getChildren().addAll(cinemaLabel, ticketType);

        // Perforated line
        HBox perforations = new HBox(0);
        perforations.setAlignment(Pos.CENTER);
        perforations.setPrefHeight(20);
        perforations.setStyle("-fx-background-color: " + WHITE + ";");

        Circle leftCircle = new Circle(10, Color.web(BG));
        leftCircle.setTranslateX(-5);
        Circle rightCircle = new Circle(10, Color.web(BG));
        rightCircle.setTranslateX(5);
        perforations.getChildren().addAll(leftCircle, rightCircle);

        // Middle section with details
        VBox middleSection = new VBox(15);
        middleSection.setPadding(new Insets(25, 30, 25, 30));
        middleSection.setStyle("-fx-background-color: " + WHITE + ";");

        // Movie title
        movieTitle = new Label("Movie Title");
        movieTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        movieTitle.setTextFill(Color.web(TEXT_DARK));
        movieTitle.setWrapText(true);

        movieGenre = new Label("Genre");
        movieGenre.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 12));
        movieGenre.setTextFill(Color.WHITE);
        movieGenre.setStyle(
                "-fx-background-color: " + ACCENT + ";" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 4 12;"
        );

        // Date & Time row
        HBox dateTimeRow = new HBox(30);
        dateTimeRow.setAlignment(Pos.CENTER_LEFT);

        VBox dateBox = createDetailBox("📅 DATE & TIME", "dateTime");
        dateTimeLabel = (Label) dateBox.getChildren().get(1);
        dateTimeRow.getChildren().add(dateBox);

        // Hall row
        VBox hallBox = createDetailBox("🏛️ HALL", "hall");
        hallLabel = (Label) hallBox.getChildren().get(1);
        dateTimeRow.getChildren().add(hallBox);

        // Seats
        VBox seatsBox = createDetailBox("💺 SEATS", "seats");
        seatsLabel = (Label) seatsBox.getChildren().get(1);
        dateTimeRow.getChildren().add(seatsBox);

        // Divider
        Line divider = new Line(0, 0, 540, 0);
        divider.setStroke(Color.web(BORDER));
        divider.setStrokeWidth(1);
        // ✅ FIXED: Using appropriate property collection method implementation
        divider.getStrokeDashArray().addAll(5.0, 5.0);

        // Bottom info
        HBox bottomInfo = new HBox(30);
        bottomInfo.setAlignment(Pos.CENTER_LEFT);

        VBox customerBox = createDetailBox("👤 CUSTOMER", "customer");
        customerLabel = (Label) customerBox.getChildren().get(1);
        bottomInfo.getChildren().add(customerBox);

        VBox bookingBox = createDetailBox("🎫 BOOKING ID", "booking");
        bookingIdLabel = (Label) bookingBox.getChildren().get(1);
        bottomInfo.getChildren().add(bookingBox);

        VBox totalBox = createDetailBox("💰 TOTAL", "total");
        totalLabel = (Label) totalBox.getChildren().get(1);
        totalLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + ACCENT + ";");
        bottomInfo.getChildren().add(totalBox);

        // Booked on
        VBox bookedOnBox = createDetailBox("📅 BOOKED ON", "bookedOn");
        bookedOnLabel = (Label) bookedOnBox.getChildren().get(1);
        bookedOnLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 11));
        bookedOnLabel.setTextFill(Color.web(TEXT_MUTED));

        middleSection.getChildren().addAll(
                movieTitle,
                movieGenre,
                new Region() {{ setPrefHeight(10); }},
                dateTimeRow,
                divider,
                bottomInfo,
                bookedOnBox
        );

        // Bottom barcode section
        VBox barcodeSection = new VBox(10);
        barcodeSection.setPadding(new Insets(20));
        barcodeSection.setAlignment(Pos.CENTER);
        barcodeSection.setStyle("-fx-background-color: " + BG_LIGHT + ";");

        Label barcode = new Label("┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃");
        barcode.setFont(Font.font("Monospaced", 16));
        barcode.setTextFill(Color.web(TEXT_DARK));

        Label scanNote = new Label("Scan at entrance");
        scanNote.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 10));
        scanNote.setTextFill(Color.web(TEXT_MUTED));

        barcodeSection.getChildren().addAll(barcode, scanNote);

        // Assemble ticket
        ticket.getChildren().addAll(topSection, perforations, middleSection, barcodeSection);

        // Action buttons
        HBox actionButtons = new HBox(15);
        actionButtons.setAlignment(Pos.CENTER);
        actionButtons.setPadding(new Insets(20, 0, 0, 0));

        btnDownload = createActionButton("📥  Download Ticket", ACCENT);
        btnMyBookings = createActionButton("🎫  My Bookings", "#3B82F6", "#2563EB");
        btnHome = createActionButton("🏠  Home", "#64748B", "#475569");

        actionButtons.getChildren().addAll(btnDownload, btnMyBookings, btnHome);

        root.getChildren().addAll(successLabel, subMessage, ticket, actionButtons);
    }

    private VBox createDetailBox(String label, String type) {
        VBox box = new VBox(3);
        Label lbl = new Label(label);
        lbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 9));
        lbl.setTextFill(Color.web(TEXT_MUTED));
        Label val = new Label("");
        val.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        val.setTextFill(Color.web(TEXT_DARK));
        box.getChildren().addAll(lbl, val);
        return box;
    }

    private Button createActionButton(String text, String normalColor) {
        return createActionButton(text, normalColor, normalColor);
    }

    private Button createActionButton(String text, String normalColor, String hoverColor) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        btn.setPrefHeight(45);
        btn.setPrefWidth(180);
        btn.setTextFill(Color.WHITE);
        btn.setStyle(
                "-fx-background-color: " + normalColor + ";" +
                        "-fx-background-radius: 8;" +
                        "-fx-cursor: hand;"
        );
        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: " + hoverColor + ";" +
                        "-fx-background-radius: 8;" +
                        "-fx-cursor: hand;" +
                        "-fx-text-fill: white;"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: " + normalColor + ";" +
                        "-fx-background-radius: 8;" +
                        "-fx-cursor: hand;" +
                        "-fx-text-fill: white;"
        ));
        return btn;
    }

    public VBox getView() {
        return root;
    }
}