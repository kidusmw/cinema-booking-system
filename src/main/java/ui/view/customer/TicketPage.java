package ui.view.customer;

import static ui.common.Theme.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TicketPage {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TicketPage.class);

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

    public TicketPage() {
        try {
            createUI();
        } catch (Exception e) {
            log.error("Failed to create TicketPage UI", e);
            root = new VBox();
            root.getChildren().add(new Label("Error: " + e.getMessage()));
        }
    }

    private void createUI() {
        root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("page");
        root.setPadding(new Insets(30));

        // Success message
        successLabel = new Label("🎉 Booking Confirmed!");
        successLabel.getStyleClass().add("welcome-title");
        successLabel.setTextFill(Color.web(SUCCESS));

        Label subMessage = new Label("Your ticket has been generated. Enjoy the show!");
        subMessage.getStyleClass().addAll("body-text", "muted-text");

        // ===== TICKET =====
        VBox ticket = new VBox(0);
        ticket.setMaxWidth(600);
        ticket.getStyleClass().add("card");

        // Top colored section
        VBox topSection = new VBox(10);
        topSection.setPadding(new Insets(25));
        topSection.getStyleClass().add("ticket-header");

        Label cinemaLabel = new Label("🎬 CINEMABOOK");
        cinemaLabel.getStyleClass().addAll("body-text", "text-white");

        Label ticketType = new Label("MOVIE TICKET");
        ticketType.getStyleClass().add("caption");
        ticketType.setTextFill(Color.web("#FCE7F3"));

        topSection.getChildren().addAll(cinemaLabel, ticketType);

        // Perforated line
        HBox perforations = new HBox(0);
        perforations.setAlignment(Pos.CENTER);
        perforations.setPrefHeight(20);
        perforations.getStyleClass().add("ticket-perforations");

        Circle leftCircle = new Circle(10, Color.web("#FAFAFA"));
        leftCircle.setTranslateX(-5);
        Circle rightCircle = new Circle(10, Color.web("#FAFAFA"));
        rightCircle.setTranslateX(5);
        perforations.getChildren().addAll(leftCircle, rightCircle);

        // Middle section with details
        VBox middleSection = new VBox(15);
        middleSection.setPadding(new Insets(25, 30, 25, 30));
        middleSection.getStyleClass().add("ticket-perforations");

        // Movie title
        movieTitle = new Label("Movie Title");
        movieTitle.getStyleClass().add("title");
        movieTitle.setWrapText(true);

        movieGenre = new Label("Genre");
        movieGenre.getStyleClass().add("badge");

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
        totalLabel.getStyleClass().add("price-label");
        bottomInfo.getChildren().add(totalBox);

        // Booked on
        VBox bookedOnBox = createDetailBox("📅 BOOKED ON", "bookedOn");
        bookedOnLabel = (Label) bookedOnBox.getChildren().get(1);
        bookedOnLabel.getStyleClass().add("caption");

        middleSection
                .getChildren()
                .addAll(
                        movieTitle,
                        movieGenre,
                        new Region() {
                            {
                                setPrefHeight(10);
                            }
                        },
                        dateTimeRow,
                        divider,
                        bottomInfo,
                        bookedOnBox);

        // Bottom barcode section
        VBox barcodeSection = new VBox(10);
        barcodeSection.setPadding(new Insets(20));
        barcodeSection.setAlignment(Pos.CENTER);
        barcodeSection.getStyleClass().add("ticket-barcode-bg");

        Label barcode = new Label("┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃");
        barcode.setFont(Font.font("Monospaced", 16));

        Label scanNote = new Label("Scan at entrance");
        scanNote.getStyleClass().add("caption");

        barcodeSection.getChildren().addAll(barcode, scanNote);

        // Assemble ticket
        ticket.getChildren().addAll(topSection, perforations, middleSection, barcodeSection);

        // Action buttons
        HBox actionButtons = new HBox(15);
        actionButtons.setAlignment(Pos.CENTER);
        actionButtons.setPadding(new Insets(20, 0, 0, 0));

        btnDownload = createActionButton("📥  Download Ticket");
        btnMyBookings = createActionButton("🎫  My Bookings");
        btnHome = createActionButton("🏠  Home");

        actionButtons.getChildren().addAll(btnDownload, btnMyBookings, btnHome);

        root.getChildren().addAll(successLabel, subMessage, ticket, actionButtons);
    }

    private VBox createDetailBox(String label, String type) {
        VBox box = new VBox(3);
        Label lbl = new Label(label);
        lbl.getStyleClass().add("caption");
        Label val = new Label("");
        val.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        box.getChildren().addAll(lbl, val);
        return box;
    }

    private Button createActionButton(String text) {
        Button btn = new Button(text);
        btn.setPrefHeight(45);
        btn.setPrefWidth(180);
        btn.getStyleClass().add("primary-button");
        return btn;
    }

    public VBox getView() {
        return root;
    }
}
