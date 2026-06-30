package ui.view.customer;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

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

    public TicketPage() {
        createUI();
    }

    private void createUI() {
        root = new VBox(20);
        root.getStyleClass().add("align-center");
        root.getStyleClass().add("page");
        root.getStyleClass().add("p-30");

        // Success message
        successLabel = new Label("🎉 Booking Confirmed!");
        successLabel.getStyleClass().add("welcome-title");
        successLabel.getStyleClass().add("text-success");

        Label subMessage = new Label("Your ticket has been generated. Enjoy the show!");
        subMessage.getStyleClass().addAll("body-text", "muted-text");

        // ===== TICKET =====
        VBox ticket = new VBox(0);
        ticket.getStyleClass().add("w-600");
        ticket.getStyleClass().add("card");

        // Top colored section
        VBox topSection = new VBox(10);
        topSection.getStyleClass().add("p-25");
        topSection.getStyleClass().add("ticket-header");

        Label cinemaLabel = new Label("🎬 CINEMABOOK");
        cinemaLabel.getStyleClass().addAll("body-text", "text-white");

        Label ticketType = new Label("MOVIE TICKET");
        ticketType.getStyleClass().add("caption");
        ticketType.getStyleClass().add("ticket-type-text");

        topSection.getChildren().addAll(cinemaLabel, ticketType);

        // Perforated line
        HBox perforations = new HBox(0);
        perforations.getStyleClass().add("align-center");
        perforations.getStyleClass().add("h-20");
        perforations.getStyleClass().add("ticket-perforations");

        Circle leftCircle = new Circle(10);
        leftCircle.getStyleClass().add("ticket-circle-left");
        leftCircle.setTranslateX(-5);
        Circle rightCircle = new Circle(10);
        rightCircle.getStyleClass().add("ticket-circle-right");
        rightCircle.setTranslateX(5);
        perforations.getChildren().addAll(leftCircle, rightCircle);

        // Middle section with details
        VBox middleSection = new VBox(15);
        middleSection.getStyleClass().add("p-25-30");
        middleSection.getStyleClass().add("ticket-perforations");

        // Movie title
        movieTitle = new Label("Movie Title");
        movieTitle.getStyleClass().add("title");
        movieTitle.setWrapText(true);

        movieGenre = new Label("Genre");
        movieGenre.getStyleClass().add("badge");

        // Date & Time row
        HBox dateTimeRow = new HBox(30);
        dateTimeRow.getStyleClass().add("align-center-left");

        VBox dateBox = createDetailBox("📅 DATE & TIME");
        dateTimeLabel = (Label) dateBox.getChildren().get(1);
        dateTimeRow.getChildren().add(dateBox);

        // Hall row
        VBox hallBox = createDetailBox("🏛️ HALL");
        hallLabel = (Label) hallBox.getChildren().get(1);
        dateTimeRow.getChildren().add(hallBox);

        // Seats
        VBox seatsBox = createDetailBox("💺 SEATS");
        seatsLabel = (Label) seatsBox.getChildren().get(1);
        dateTimeRow.getChildren().add(seatsBox);

        // Divider
        Line divider = new Line(0, 0, 540, 0);
        divider.getStyleClass().add("ticket-divider");

        // Bottom info
        HBox bottomInfo = new HBox(30);
        bottomInfo.getStyleClass().add("align-center-left");

        VBox customerBox = createDetailBox("👤 CUSTOMER");
        customerLabel = (Label) customerBox.getChildren().get(1);
        bottomInfo.getChildren().add(customerBox);

        VBox bookingBox = createDetailBox("🎫 BOOKING ID");
        bookingIdLabel = (Label) bookingBox.getChildren().get(1);
        bottomInfo.getChildren().add(bookingBox);

        VBox totalBox = createDetailBox("💰 TOTAL");
        totalLabel = (Label) totalBox.getChildren().get(1);
        totalLabel.getStyleClass().add("price-label");
        bottomInfo.getChildren().add(totalBox);

        // Booked on
        VBox bookedOnBox = createDetailBox("📅 BOOKED ON");
        bookedOnLabel = (Label) bookedOnBox.getChildren().get(1);
        bookedOnLabel.getStyleClass().add("caption");

        middleSection
                .getChildren()
                .addAll(
                        movieTitle,
                        movieGenre,
                        new Region() {
                            {
                                getStyleClass().add("h-10");
                            }
                        },
                        dateTimeRow,
                        divider,
                        bottomInfo,
                        bookedOnBox);

        // Bottom barcode section
        VBox barcodeSection = new VBox(10);
        barcodeSection.getStyleClass().add("p-20");
        barcodeSection.getStyleClass().add("align-center");
        barcodeSection.getStyleClass().add("ticket-barcode-bg");

        Label barcode = new Label("┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃");
        barcode.getStyleClass().add("barcode-text");

        Label scanNote = new Label("Scan at entrance");
        scanNote.getStyleClass().add("caption");

        barcodeSection.getChildren().addAll(barcode, scanNote);

        // Assemble ticket
        ticket.getChildren().addAll(topSection, perforations, middleSection, barcodeSection);

        // Action buttons
        HBox actionButtons = new HBox(15);
        actionButtons.getStyleClass().add("align-center");
        actionButtons.getStyleClass().add("pt-20");

        btnDownload = createActionButton("📥  Download Ticket");
        btnMyBookings = createActionButton("🎫  My Bookings");
        btnHome = createActionButton("🏠  Home");

        actionButtons.getChildren().addAll(btnDownload, btnMyBookings, btnHome);

        root.getChildren().addAll(successLabel, subMessage, ticket, actionButtons);
    }

    private static VBox createDetailBox(String label) {
        VBox box = new VBox(3);
        Label lbl = new Label(label);
        lbl.getStyleClass().add("caption");
        Label val = new Label("");
        val.getStyleClass().add("ticket-detail-value");
        box.getChildren().addAll(lbl, val);
        return box;
    }

    private Button createActionButton(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("h-45");
        btn.getStyleClass().add("w-180");
        btn.getStyleClass().add("primary-button");
        return btn;
    }

    public VBox getView() {
        return root;
    }
}
