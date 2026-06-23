package ui.view;

import static ui.common.Theme.*;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class PaymentPage {
    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(PaymentPage.class);

    public Button btnBack;
    public TextField usernameField;
    public TextField emailField;
    public TextField fullNameField;
    public TextField otpField;
    public Button btnVerify;
    public Label timerLabel;
    public Label timerIconLabel;
    public Label otpInfoLabel;
    public Label errorLabel;
    public VBox summaryBox;
    private VBox root;
    private static final String WHITE = "#FFFFFF";
    private static final String BG = "#FAFAFA";
    private static final String BG_LIGHT = "#F8FAFC";

    public PaymentPage() {
        try {
            createUI();
        } catch (Exception e) {
            log.error("Failed to create PaymentPage UI", e);
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
        btnBack = new Button("← Back to Seats");
        btnBack.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        btnBack.setTextFill(Color.web(TEXT_MUTED));
        btnBack.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-padding: 8 16;");
        topBar.getChildren().add(btnBack);

        VBox header = new VBox(5);
        Label title = new Label("💳 Secure Payment");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        title.setTextFill(Color.web(TEXT_DARK));
        Label subtitle = new Label("Complete your booking with OTP verification");
        subtitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        subtitle.setTextFill(Color.web(TEXT_MUTED));
        header.getChildren().addAll(title, subtitle);
        HBox mainContent = new HBox(20);
        VBox.setVgrow(mainContent, Priority.ALWAYS);

        VBox formCard = new VBox(15);
        formCard.setPadding(new Insets(25));
        formCard.setStyle(
                "-fx-background-color: "
                        + WHITE
                        + ";"
                        + "-fx-border-color: "
                        + BORDER
                        + ";"
                        + "-fx-border-radius: 12;"
                        + "-fx-background-radius: 12;");
        HBox.setHgrow(formCard, Priority.ALWAYS);

        Label formTitle = new Label("Account Information");
        formTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        formTitle.setTextFill(Color.web(TEXT_DARK));

        usernameField = createReadOnlyField("Username");
        emailField = createReadOnlyField("Email");
        fullNameField = createReadOnlyField("Full Name");

        formCard.getChildren()
                .addAll(
                        formTitle,
                        new Separator(),
                        addLabel("👤 Username"),
                        usernameField,
                        addLabel("📧 Email"),
                        emailField,
                        addLabel("✍️ Full Name"),
                        fullNameField,
                        new Separator());

        VBox otpSection = new VBox(10);
        HBox otpHeader = new HBox(10);
        otpHeader.setAlignment(Pos.CENTER_LEFT);

        Label otpTitle = new Label("🔐 OTP Verification");
        otpTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        otpTitle.setTextFill(Color.web(TEXT_DARK));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox timerBox = new HBox(5);
        timerBox.setAlignment(Pos.CENTER);
        timerIconLabel = new Label("⏱️");
        timerIconLabel.setFont(Font.font(16));
        timerLabel = new Label("02:00");
        timerLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        timerLabel.setTextFill(Color.web(ACCENT));
        timerBox.getChildren().addAll(timerIconLabel, timerLabel);

        otpHeader.getChildren().addAll(otpTitle, spacer, timerBox);

        otpInfoLabel = new Label();
        otpInfoLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 12));
        otpInfoLabel.setTextFill(Color.web(WARNING_ORANGE));
        otpInfoLabel.setWrapText(true);
        otpInfoLabel.setStyle(
                "-fx-background-color: #FEF3C7;"
                        + "-fx-padding: 10;"
                        + "-fx-background-radius: 8;");
        otpInfoLabel.setVisible(false);

        Label otpInputLabel = new Label("Enter 6-digit OTP sent to your email:");
        otpInputLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 12));
        otpInputLabel.setTextFill(Color.web(TEXT_MUTED));

        otpField = new TextField();
        otpField.setPromptText("000000");
        otpField.setPrefHeight(45);
        otpField.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        otpField.setAlignment(Pos.CENTER);
        otpField.setStyle(
                "-fx-background-color: "
                        + BG_LIGHT
                        + ";"
                        + "-fx-border-color: "
                        + BORDER
                        + ";"
                        + "-fx-border-radius: 8;"
                        + "-fx-background-radius: 8;"
                        + "-fx-padding: 8 14;"
                        + "-fx-text-fill: "
                        + TEXT_DARK
                        + ";");

        errorLabel = new Label();
        errorLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 12));
        errorLabel.setTextFill(Color.web("#DC2626"));
        errorLabel.setVisible(false);

        btnVerify = new Button("✓  Verify & Pay");
        btnVerify.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        btnVerify.setPrefHeight(50);
        btnVerify.setTextFill(Color.WHITE);
        btnVerify.setStyle(
                "-fx-background-color: "
                        + ACCENT
                        + ";"
                        + "-fx-background-radius: 8;"
                        + "-fx-cursor: hand;");

        otpSection
                .getChildren()
                .addAll(otpHeader, otpInfoLabel, otpInputLabel, otpField, errorLabel, btnVerify);

        formCard.getChildren().add(otpSection);
        VBox summaryCard = new VBox(15);
        summaryCard.setPadding(new Insets(25));
        summaryCard.setPrefWidth(320);
        summaryCard.setStyle(
                "-fx-background-color: "
                        + WHITE
                        + ";"
                        + "-fx-border-color: "
                        + BORDER
                        + ";"
                        + "-fx-border-radius: 12;"
                        + "-fx-background-radius: 12;");

        Label summaryTitle = new Label("📋 Booking Summary");
        summaryTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        summaryTitle.setTextFill(Color.web(TEXT_DARK));

        summaryBox = new VBox(8);

        summaryCard.getChildren().addAll(summaryTitle, new Separator(), summaryBox);
        mainContent.getChildren().addAll(formCard, summaryCard);
        root.getChildren().addAll(topBar, header, mainContent);
        FadeTransition fade = new FadeTransition(Duration.millis(500), root);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    private TextField createReadOnlyField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setPrefHeight(40);
        field.setEditable(false);
        field.setFont(Font.font("Segoe UI", 13));
        field.setStyle(
                "-fx-background-color: "
                        + BG_LIGHT
                        + ";"
                        + "-fx-border-color: "
                        + BORDER
                        + ";"
                        + "-fx-border-radius: 8;"
                        + "-fx-background-radius: 8;"
                        + "-fx-padding: 8 14;"
                        + "-fx-text-fill: "
                        + TEXT_DARK
                        + ";");
        return field;
    }

    private Label addLabel(String text) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 12));
        lbl.setTextFill(Color.web(TEXT_MUTED));
        return lbl;
    }

    public VBox getView() {
        return root;
    }

    private static final String WARNING_ORANGE = "#92400E";
}
