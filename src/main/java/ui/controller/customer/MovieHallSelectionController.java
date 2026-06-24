package ui.controller.customer;

import application.AppContext;
import domain.model.Hall;
import domain.model.Movie;
import domain.model.Showtime;
import domain.model.User;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ui.common.WindowManager;
import ui.controller.common.NavigationManager;
import ui.view.customer.MovieHallSelectionPage;

public class MovieHallSelectionController {
    private MovieHallSelectionPage view;
    private Stage stage;
    private User currentUser;
    private Movie selectedMovie;
    private Showtime selectedShow;
    private AppContext ctx;
    private NavigationManager nav;

    public MovieHallSelectionController(
            Stage stage,
            AppContext ctx,
            NavigationManager nav,
            User currentUser,
            Movie selectedMovie,
            Showtime selectedShow) {
        this.stage = stage;
        this.ctx = ctx;
        this.nav = nav;
        this.currentUser = currentUser;
        this.selectedMovie = selectedMovie;
        this.selectedShow = selectedShow;
        this.view = new MovieHallSelectionPage();
        WindowManager.configureStage(
                stage, "Select Hall - " + selectedMovie.getTitle(), view.getView(), 1100, 700);
        view.showInfoLabel.setText(
                "🕐 " + selectedShow.getShowTime() + "  |  📅 " + selectedShow.getShowDate());
        loadHalls();
        view.btnBack.setOnAction(e -> nav.back());
    }

    private void loadHalls() {
        view.hallsContainer.getChildren().clear();
        List<Hall> halls = ctx.hallRepo.findAll().stream().collect(Collectors.toList());
        if (halls.isEmpty()) {
            Label noHalls = new Label("🏛️ No halls available");
            noHalls.getStyleClass().add("empty-text");
            view.hallsContainer.getChildren().add(noHalls);
            return;
        }

        for (Hall hall : halls) {
            view.hallsContainer.getChildren().add(createHallCard(hall));
        }
    }

    private VBox createHallCard(Hall hall) {
        VBox card = new VBox(15);
        card.getStyleClass().add("p-20-25");
        card.getStyleClass().add("card");
        boolean isVIP = isVIPHall(hall);
        String hallType = isVIP ? "VIP" : "Regular";
        String typeColorClass = isVIP ? "text-warning" : "text-blue";
        String typeBadgeClass = isVIP ? "badge-warning-solid" : "badge-blue-solid";
        String typeIcon = isVIP ? "\uD83D\udc51" : "\uD83C\udfac";

        HBox topRow = new HBox(15);
        topRow.getStyleClass().add("align-center-left");

        Label icon = new Label(typeIcon);
        icon.getStyleClass().add("icon-large");

        VBox nameBox = new VBox(3);
        Label name = new Label(hall.getName());
        name.getStyleClass().add("hall-name");

        Label type = new Label(hallType + " Hall");
        type.getStyleClass().addAll("hall-type", typeColorClass);
        nameBox.getChildren().addAll(name, type);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label badge = new Label(isVIP ? "VIP" : "STD");
        badge.getStyleClass().addAll(typeBadgeClass, "text-white");

        topRow.getChildren().addAll(icon, nameBox, spacer, badge);
        HBox detailsRow = new HBox(20);
        detailsRow.getStyleClass().add("align-center-left");

        VBox capacityBox = new VBox(2);
        Label capacityValue = new Label(hall.getCapacity() + " seats");
        capacityValue.getStyleClass().add("hall-capacity");
        Label capacityLabel = new Label("Total Capacity");
        capacityLabel.getStyleClass().add("hall-meta-label");
        capacityBox.getChildren().addAll(capacityValue, capacityLabel);
        VBox priceBox = new VBox(2);
        double price = hall.isVip() ? 120.00 : 50.00;

        Label priceValue = new Label(String.format("%.2f Birr", price));
        priceValue.getStyleClass().add("hall-price");

        Label priceLabel = new Label("Per Seat");
        priceLabel.getStyleClass().add("hall-meta-label");

        priceBox.getChildren().addAll(priceValue, priceLabel);

        VBox featuresBox = new VBox(2);
        Label featuresValue =
                new Label(isVIP ? "Recliner, Dolby Atmos" : "Standard Seats, Stereo Sound");
        featuresValue.getStyleClass().add("hall-features");
        Label featuresLabel = new Label("Features");
        featuresLabel.getStyleClass().add("hall-meta-label");
        featuresBox.getChildren().addAll(featuresValue, featuresLabel);

        detailsRow.getChildren().addAll(capacityBox, priceBox, featuresBox);
        Button selectBtn = new Button("Select Seats \u2192");
        selectBtn.getStyleClass().add("h-38");
        selectBtn.getStyleClass().add(isVIP ? "warning-button-small" : "primary-button-small");
        selectBtn.setOnAction(e -> navigateToSeatSelection(hall));

        card.getChildren().addAll(topRow, detailsRow, selectBtn);
        card.setOnMouseClicked(e -> navigateToSeatSelection(hall));
        return card;
    }

    private void navigateToSeatSelection(Hall hall) {
        boolean isVIP = isVIPHall(hall);
        nav.go(
                () ->
                        new MovieHallSelectionController(
                                stage, ctx, nav, currentUser, selectedMovie, selectedShow),
                () ->
                        new SeatSelectionController(
                                stage,
                                ctx,
                                nav,
                                currentUser,
                                selectedMovie,
                                selectedShow,
                                hall,
                                isVIP));
    }

    private boolean isVIPHall(Hall hall) {
        if (hall.getName() == null) return false;
        String name = hall.getName().toUpperCase();
        return name.contains("VIP");
    }
}
