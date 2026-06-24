package ui.controller.customer;

import application.AppContext;
import domain.model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import ui.common.WindowManager;
import ui.controller.common.NavigationManager;
import ui.view.customer.PaymentPage;

public class PaymentController {
    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(PaymentController.class);
    private PaymentPage view;
    private Stage stage;
    private User currentUser;
    private Movie selectedMovie;
    private Showtime selectedShow;
    private Hall selectedHall;
    private List<String> selectedSeatIds;
    private double totalAmount;
    private boolean isVIP;
    private AppContext ctx;
    private NavigationManager nav;
    private Timeline countdownTimer;
    private int secondsRemaining = 120;
    private String generatedOTP;
    private int failedAttempts = 0;

    public PaymentController(
            Stage stage,
            AppContext ctx,
            NavigationManager nav,
            User currentUser,
            Movie selectedMovie,
            Showtime selectedShow,
            Hall selectedHall,
            List<String> selectedSeatIds,
            double seatPrice) {
        this.stage = stage;
        this.ctx = ctx;
        this.nav = nav;
        this.currentUser = currentUser;
        this.selectedMovie = selectedMovie;
        this.selectedShow = selectedShow;
        this.selectedHall = selectedHall;
        this.selectedSeatIds = selectedSeatIds;
        this.totalAmount = seatPrice * selectedSeatIds.size();
        this.view = new PaymentPage();
        WindowManager.configure(stage, "Payment", view.getView());
        log.info("Opening Payment page");
        generatedOTP = generateOTP();
        view.usernameField.setText(currentUser.getUsername());
        view.emailField.setText(currentUser.getEmail());
        view.fullNameField.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
        displayBookingSummary();
        view.otpInfoLabel.setText("💡 For this project, your OTP is: " + generatedOTP);
        view.otpInfoLabel.setVisible(true);
        startCountdown();
        view.btnVerify.setOnAction(e -> verifyPayment());
        view.btnBack.setOnAction(e -> nav.back());
    }

    private void displayBookingSummary() {
        VBox summary = view.summaryBox;
        summary.getChildren().clear();
        addSummaryRow("🎬 Movie", selectedMovie.getTitle());
        addSummaryRow("🏛️ Hall", selectedHall.getName() + (isVIP ? " (VIP)" : ""));
        addSummaryRow("📅 Date", selectedShow.getShowDate().toString());
        addSummaryRow("🕐 Time", selectedShow.getShowTime().toString());
        addSummaryRow("💺 Seats", String.join(", ", selectedSeatIds));
        addSummaryRow("👤 User", currentUser.getFirstName() + " " + currentUser.getLastName());
        summary.getChildren().add(new Separator());

        HBox totalBox = new HBox();
        totalBox.getStyleClass().add("align-center-left");
        Label totalLabel = new Label("Total Amount:");
        totalLabel.getStyleClass().add("section-title");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label totalValue = new Label(String.format("%.2f Birr", totalAmount));
        totalValue.getStyleClass().addAll("price-large", "text-accent");
        totalBox.getChildren().addAll(totalLabel, spacer, totalValue);
        summary.getChildren().add(totalBox);
    }

    private void addSummaryRow(String label, String value) {
        HBox row = new HBox();
        row.getStyleClass().add("align-center-left");
        row.getStyleClass().add("p-3-0");

        Label lblLabel = new Label(label);
        lblLabel.getStyleClass().add("payment-label");
        lblLabel.getStyleClass().add("w-100");

        Label valLabel = new Label(value);
        valLabel.getStyleClass().add("payment-value");

        row.getChildren().addAll(lblLabel, valLabel);
        view.summaryBox.getChildren().add(row);
    }

    private void startCountdown() {
        countdownTimer = new Timeline();
        countdownTimer.setCycleCount(Timeline.INDEFINITE);

        KeyFrame keyFrame =
                new KeyFrame(
                        Duration.seconds(1),
                        event -> {
                            secondsRemaining--;
                            updateCountdownDisplay();
                            if (secondsRemaining <= 0) {
                                countdownTimer.stop();
                                otpExpired();
                            }
                        });

        countdownTimer.getKeyFrames().add(keyFrame);
        countdownTimer.play();
    }

    private void updateCountdownDisplay() {
        int minutes = secondsRemaining / 60;
        int seconds = secondsRemaining % 60;
        view.timerLabel.setText(String.format("%02d:%02d", minutes, seconds));

        if (secondsRemaining <= 30) {
            view.timerLabel.getStyleClass().add("text-danger");
            view.timerIconLabel.setText("⚠️");
        } else if (secondsRemaining <= 60) {
            view.timerLabel.getStyleClass().add("text-warning");
            view.timerIconLabel.setText("⏰");
        }
    }

    private void otpExpired() {
        view.btnVerify.setDisable(true);
        view.errorLabel.setText("❌ OTP expired!");
        view.errorLabel.setVisible(true);
    }

    private void verifyPayment() {
        if (view.otpField.getText().trim().equals(generatedOTP)) {
            countdownTimer.stop();
            processSuccessfulPayment();
        } else {
            failedAttempts++;
            view.errorLabel.setText("Invalid OTP. Attempts: " + failedAttempts);
            view.errorLabel.setVisible(true);
        }
    }

    private void processSuccessfulPayment() {
        try {
            List<Long> domainSeatIds =
                    selectedSeatIds.stream().map(Long::parseLong).collect(Collectors.toList());
            Long userId = currentUser.getUserId();
            Long showId = selectedShow.getShowId();
            ctx.bookingFacade.bookAndPay(userId, showId, domainSeatIds, totalAmount, "CARD");
            showSuccessAndTicket();
        } catch (Exception e) {
            log.error("Payment processing failed", e);
            view.errorLabel.setText("Payment failed: " + e.getMessage());
            view.errorLabel.setVisible(true);
        }
    }

    private void showSuccessAndTicket() {
        nav.goFresh(
                () ->
                        new TicketController(
                                stage,
                                ctx,
                                nav,
                                currentUser,
                                selectedMovie,
                                selectedShow,
                                selectedHall,
                                selectedSeatIds,
                                totalAmount,
                                new ArrayList<>()));
    }

    private static final Random OTP_RANDOM = new Random();

    private String generateOTP() {
        return String.format("%06d", OTP_RANDOM.nextInt(1000000));
    }
}
