package View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class AuthChoice {

    public Button btnLogin;
    public Button btnSignUp;
    public Button btnBack;  // ✅ ADDED

    private static final String BG = "#FAFAFA";
    private static final String WHITE = "#FFFFFF";
    private static final String HOVER = "#EC4899";
    private static final String ACCENT = "#DB2777";
    private static final String TEXT_DARK = "#1E293B";
    private static final String TEXT_MUTED = "#64748B";
    private static final String BORDER = "#E2E8F0";
    private static final String SOFT_PINK = "#FCE7F3";

    public BorderPane getView() {

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BG + ";");
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(20, 30, 20, 30));

        btnBack = new Button("← Back");  // ✅ Back button
        btnBack.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        btnBack.setTextFill(Color.web(TEXT_MUTED));
        btnBack.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 6 12;"
        );
        topBar.getChildren().add(btnBack);
        VBox card = new VBox(25);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(50, 60, 50, 60));
        card.setMaxWidth(450);
        card.setStyle(
                "-fx-background-color: " + WHITE + ";" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 12;" +
                        "-fx-border-width: 1;"
        );
        card.setEffect(new DropShadow(10, Color.rgb(0, 0, 0, 0.06)));
        Label title = new Label("How would you like to continue?");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        title.setTextFill(Color.web(TEXT_DARK));
        title.setWrapText(true);
        title.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        Label subtitle = new Label("Choose an option to get started");
        subtitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        subtitle.setTextFill(Color.web(TEXT_MUTED));
        btnLogin = new Button("Sign in to existing account");
        btnLogin.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        btnLogin.setPrefWidth(Double.MAX_VALUE);
        btnLogin.setPrefHeight(48);
        btnLogin.setTextFill(Color.web(WHITE));
        btnLogin.setStyle(
                "-fx-background-color: " + ACCENT + ";" +
                        "-fx-background-radius: 8;" +
                        "-fx-cursor: hand;"
        );
        btnLogin.setOnMouseEntered(e -> {
            btnLogin.setStyle(
                    "-fx-background-color: " + HOVER + ";" +
                            "-fx-background-radius: 8;" +
                            "-fx-cursor: hand;"
            );
        });
        btnLogin.setOnMouseExited(e -> {
            btnLogin.setStyle(
                    "-fx-background-color: " + ACCENT + ";" +
                            "-fx-background-radius: 8;" +
                            "-fx-cursor: hand;"
            );
        });
        HBox dividerBox = new HBox(10);
        dividerBox.setAlignment(Pos.CENTER);
        Region line1 = new Region();
        line1.setPrefHeight(1);
        line1.setStyle("-fx-background-color: " + BORDER + ";");
        HBox.setHgrow(line1, Priority.ALWAYS);
        Label orLabel = new Label("OR");
        orLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 12));
        orLabel.setTextFill(Color.web(TEXT_MUTED));
        Region line2 = new Region();
        line2.setPrefHeight(1);
        line2.setStyle("-fx-background-color: " + BORDER + ";");
        HBox.setHgrow(line2, Priority.ALWAYS);
        dividerBox.getChildren().addAll(line1, orLabel, line2);

        // Signup button (secondary)
        btnSignUp = new Button("Create new account");
        btnSignUp.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        btnSignUp.setPrefWidth(Double.MAX_VALUE);
        btnSignUp.setPrefHeight(48);
        btnSignUp.setTextFill(Color.web(ACCENT));
        btnSignUp.setStyle(
                "-fx-background-color: " + WHITE + ";" +
                        "-fx-border-color: " + ACCENT + ";" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-width: 1.5;" +
                        "-fx-cursor: hand;"
        );
        btnSignUp.setOnMouseEntered(e -> {
            btnSignUp.setStyle(
                    "-fx-background-color: " + SOFT_PINK + ";" +
                            "-fx-border-color: " + HOVER + ";" +
                            "-fx-border-radius: 8;" +
                            "-fx-background-radius: 8;" +
                            "-fx-border-width: 1.5;" +
                            "-fx-cursor: hand;"
            );
        });
        btnSignUp.setOnMouseExited(e -> {
            btnSignUp.setStyle(
                    "-fx-background-color: " + WHITE + ";" +
                            "-fx-border-color: " + ACCENT + ";" +
                            "-fx-border-radius: 8;" +
                            "-fx-background-radius: 8;" +
                            "-fx-border-width: 1.5;" +
                            "-fx-cursor: hand;"
            );
        });

        card.getChildren().addAll(title, subtitle, btnLogin, dividerBox, btnSignUp);

        root.setTop(topBar);
        root.setCenter(card);

        return root;
    }
}
