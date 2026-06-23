package ui.controller.customer;

import static ui.common.Theme.*;

import application.AppContext;
import domain.model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
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
        Scene scene = new Scene(view.getView(), 900, 750);
        stage.setTitle("Payment - CinemaBook");
        stage.setScene(scene);
        stage.show();
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
        totalBox.setAlignment(Pos.CENTER_LEFT);
        Label totalLabel = new Label("Total Amount:");
        totalLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        totalLabel.setTextFill(Color.web(TEXT_DARK));
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label totalValue = new Label(String.format("%.2f Birr", totalAmount));
        totalValue.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        totalValue.setTextFill(Color.web(ACCENT));
        totalBox.getChildren().addAll(totalLabel, spacer, totalValue);
        summary.getChildren().add(totalBox);
    }

    private void addSummaryRow(String label, String value) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(3, 0, 3, 0));

        Label lblLabel = new Label(label);
        lblLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 12));
        lblLabel.setTextFill(Color.web(TEXT_MUTED));
        lblLabel.setPrefWidth(100);

        Label valLabel = new Label(value);
        valLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 12));
        valLabel.setTextFill(Color.web(TEXT_DARK));

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
            view.timerLabel.setTextFill(Color.web(DANGER));
            view.timerIconLabel.setText("⚠️");
        } else if (secondsRemaining <= 60) {
            view.timerLabel.setTextFill(Color.web(WARNING));
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
