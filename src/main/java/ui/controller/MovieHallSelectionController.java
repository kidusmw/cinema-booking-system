package ui.controller;
import Model.Customer;
import Model.Movie;
import Model.Show;
import Model.Moviehall;
import ui.view.MovieHallSelectionPage;
import application.ModelConverter;
import java.util.stream.Collectors;
import application.AppContext;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.List;
import static ui.common.Theme.*;
public class MovieHallSelectionController {
    private MovieHallSelectionPage view;
    private Stage stage;
    private Customer currentUser;
    private Movie selectedMovie;
    private Show selectedShow;
    private AppContext ctx;
    private static final String WHITE = "#FFFFFF";

    public MovieHallSelectionController(Stage stage, AppContext ctx, Customer currentUser, Movie selectedMovie, Show selectedShow) {
        this.stage = stage;
        this.ctx = ctx;
        this.currentUser = currentUser;
        this.selectedMovie = selectedMovie;
        this.selectedShow = selectedShow;
        this.view = new MovieHallSelectionPage();
        Scene scene = new Scene(view.getView(), 1100, 700);
        stage.setTitle("Select Hall - " + selectedMovie.getTitle());
        stage.setScene(scene);
        stage.show();
        view.showInfoLabel.setText("🕐 " + selectedShow.getShowTime() + "  |  📅 " + selectedShow.getShowDate());
        loadHalls();
        view.btnBack.setOnAction(e -> {
            new ShowSelectionController(stage, ctx, currentUser, selectedMovie);
        });
    }
    private void loadHalls() {
        view.hallsContainer.getChildren().clear();
        List<Moviehall> halls = ctx.hallRepo.findAll().stream().map(ModelConverter::toOldHall).collect(Collectors.toList());
        if (halls.isEmpty()) {
            Label noHalls = new Label("🏛️ No halls available");
            noHalls.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
            noHalls.setTextFill(Color.web(TEXT_MUTED));
            view.hallsContainer.getChildren().add(noHalls);
            return;
        }

        for (Moviehall hall : halls) {
            view.hallsContainer.getChildren().add(createHallCard(hall));
        }
    }
    private VBox createHallCard(Moviehall hall) {
        VBox card = new VBox(15);
        card.setPadding(new Insets(20, 25, 20, 25));
        card.setStyle(
                "-fx-background-color: " + WHITE + ";" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 12;" +
                        "-fx-background-radius: 12;" +
                        "-fx-cursor: hand;"
        );
        boolean isVIP = isVIPHall(hall);
        String hallType = isVIP ? "VIP" : "Regular";
        String typeColor = isVIP ? "#F59E0B" : "#3B82F6";
        String typeIcon = isVIP ? "👑" : "🎬";
        card.setOnMouseEntered(e -> card.setStyle(
                "-fx-background-color: " + (isVIP ? "#FEF3C7" : "#DBEAFE") + ";" +
                        "-fx-border-color: " + typeColor + ";" +
                        "-fx-border-radius: 12;" +
                        "-fx-background-radius: 12;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 3);"
        ));
        card.setOnMouseExited(e -> card.setStyle(
                "-fx-background-color: " + WHITE + ";" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 12;" +
                        "-fx-background-radius: 12;" +
                        "-fx-cursor: hand;"
        ));
        HBox topRow = new HBox(15);
        topRow.setAlignment(Pos.CENTER_LEFT);

        Label icon = new Label(typeIcon);
        icon.setFont(Font.font(40));

        VBox nameBox = new VBox(3);
        Label name = new Label(hall.getName());
        name.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        name.setTextFill(Color.web(TEXT_DARK));

        Label type = new Label(hallType + " Hall");
        type.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 12));
        type.setTextFill(Color.web(typeColor));
        nameBox.getChildren().addAll(name, type);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label badge = new Label(isVIP ? "VIP" : "STD");
        badge.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));
        badge.setTextFill(Color.WHITE);
        badge.setStyle(
                "-fx-background-color: " + typeColor + ";" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 4 12;"
        );

        topRow.getChildren().addAll(icon, nameBox, spacer, badge);
        HBox detailsRow = new HBox(20);
        detailsRow.setAlignment(Pos.CENTER_LEFT);

        VBox capacityBox = new VBox(2);
        Label capacityValue = new Label(hall.getCapacity() + " seats");
        capacityValue.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        capacityValue.setTextFill(Color.web(TEXT_DARK));
        Label capacityLabel = new Label("Total Capacity");
        capacityLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 10));
        capacityLabel.setTextFill(Color.web(TEXT_MUTED));
        capacityBox.getChildren().addAll(capacityValue, capacityLabel);
        VBox priceBox = new VBox(2);
        double price = (hall.getPricePerSeat() <= 0) ? 50.00 : hall.getPricePerSeat();

        Label priceValue = new Label(String.format("%.2f Birr", price));
        priceValue.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        priceValue.setTextFill(Color.web("#DB2777"));

        Label priceLabel = new Label("Per Seat");
        priceLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 10));
        priceLabel.setTextFill(Color.web("#64748B"));

        priceBox.getChildren().addAll(priceValue, priceLabel);

        VBox featuresBox = new VBox(2);
        Label featuresValue = new Label(isVIP ? "Recliner, Dolby Atmos" : "Standard Seats, Stereo Sound");
        featuresValue.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 12));
        featuresValue.setTextFill(Color.web(TEXT_DARK));
        Label featuresLabel = new Label("Features");
        featuresLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 10));
        featuresLabel.setTextFill(Color.web(TEXT_MUTED));
        featuresBox.getChildren().addAll(featuresValue, featuresLabel);

        detailsRow.getChildren().addAll(capacityBox, priceBox, featuresBox);
        Button selectBtn = new Button("Select Seats →");
        selectBtn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        selectBtn.setPrefHeight(38);
        selectBtn.setTextFill(Color.WHITE);
        selectBtn.setStyle(
                "-fx-background-color: " + (isVIP ? "#F59E0B" : ACCENT) + ";" +
                        "-fx-background-radius: 8;" +
                        "-fx-cursor: hand;"
        );
        selectBtn.setOnAction(e -> {
            new SeatSelectionController(stage, ctx, currentUser, selectedMovie, selectedShow, hall, isVIP);
        });

        card.getChildren().addAll(topRow, detailsRow, selectBtn);

        card.setOnMouseClicked(e -> {
            if (e.getClickCount() == 1) {
                new SeatSelectionController(stage, ctx, currentUser, selectedMovie, selectedShow, hall, isVIP);
            }
        });

        return card;
    }

    private boolean isVIPHall(Moviehall hall) {
        if (hall.getName() == null) return false;
        String name = hall.getName().toUpperCase();
        return name.contains("VIP");
    }
}