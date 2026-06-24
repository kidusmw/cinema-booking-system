package ui.view.customer;

import javafx.animation.FadeTransition;
import javafx.scene.control.*;
import javafx.scene.layout.*;
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
        root.getStyleClass().add("page");
        root.getStyleClass().add("p-30");
        HBox topBar = new HBox(15);
        topBar.getStyleClass().add("align-center-left");
        btnBack = new Button("← Back to Seats");
        btnBack.getStyleClass().add("back-button");
        topBar.getChildren().add(btnBack);

        VBox header = new VBox(5);
        Label title = new Label("💳 Secure Payment");
        title.getStyleClass().add("heading");
        Label subtitle = new Label("Complete your booking with OTP verification");
        subtitle.getStyleClass().add("muted-text");
        header.getChildren().addAll(title, subtitle);
        HBox mainContent = new HBox(20);
        VBox.setVgrow(mainContent, Priority.ALWAYS);

        VBox formCard = new VBox(15);
        formCard.getStyleClass().add("p-25");
        formCard.getStyleClass().add("form-card");
        HBox.setHgrow(formCard, Priority.ALWAYS);

        Label formTitle = new Label("Account Information");
        formTitle.getStyleClass().add("section-title");

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
        otpHeader.getStyleClass().add("align-center-left");

        Label otpTitle = new Label("🔐 OTP Verification");
        otpTitle.getStyleClass().add("section-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox timerBox = new HBox(5);
        timerBox.getStyleClass().add("align-center");
        timerIconLabel = new Label("⏱️");
        timerLabel = new Label("02:00");
        timerLabel.getStyleClass().addAll("subtitle", "text-accent");
        timerBox.getChildren().addAll(timerIconLabel, timerLabel);

        otpHeader.getChildren().addAll(otpTitle, spacer, timerBox);

        otpInfoLabel = new Label();
        otpInfoLabel.setWrapText(true);
        otpInfoLabel.getStyleClass().addAll("otp-warning", "text-warning");
        otpInfoLabel.setVisible(false);

        Label otpInputLabel = new Label("Enter 6-digit OTP sent to your email:");
        otpInputLabel.getStyleClass().add("caption");

        otpField = new TextField();
        otpField.setPromptText("000000");
        otpField.getStyleClass().add("h-45");
        otpField.getStyleClass().add("align-center");

        errorLabel = new Label();
        errorLabel.getStyleClass().add("error-label");
        errorLabel.setVisible(false);

        btnVerify = new Button("✓  Verify & Pay");
        btnVerify.getStyleClass().add("h-50");
        btnVerify.getStyleClass().add("primary-button");

        otpSection
                .getChildren()
                .addAll(otpHeader, otpInfoLabel, otpInputLabel, otpField, errorLabel, btnVerify);

        formCard.getChildren().add(otpSection);
        VBox summaryCard = new VBox(15);
        summaryCard.getStyleClass().add("p-25");
        summaryCard.getStyleClass().add("w-320");
        summaryCard.getStyleClass().add("form-card");

        Label summaryTitle = new Label("📋 Booking Summary");
        summaryTitle.getStyleClass().add("section-title");

        summaryBox = new VBox(8);

        summaryCard.getChildren().addAll(summaryTitle, new Separator(), summaryBox);
        mainContent.getChildren().addAll(formCard, summaryCard);
        root.getChildren().addAll(topBar, header, mainContent);
        FadeTransition fade = new FadeTransition(Duration.millis(500), root);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    private static TextField createReadOnlyField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.getStyleClass().add("h-40");
        field.setEditable(false);
        return field;
    }

    private static Label addLabel(String text) {
        Label lbl = new Label(text);
        lbl.getStyleClass().add("caption");
        return lbl;
    }

    public VBox getView() {
        return root;
    }
}
