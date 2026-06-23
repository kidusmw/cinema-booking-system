package ui.controller;

import application.AppContext;
import application.ModelConverter;
import ui.model.Moviehall;
import ui.view.MovieHallManagmentPage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MoviehallManagmentController {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MoviehallManagmentController.class);
    private MovieHallManagmentPage view;
    private Stage stage;
    private final AppContext ctx;
    private final NavigationManager nav;
    private final AdminDashboardController dashboard;
    private ObservableList<Moviehall> hallList;

    public MoviehallManagmentController(Stage stage, AppContext ctx, NavigationManager nav, AdminDashboardController dashboard) {
        this.stage = stage;
        this.ctx = ctx;
        this.nav = nav;
        this.dashboard = dashboard;
        this.view = new MovieHallManagmentPage();

        Scene scene = new Scene(view.getView(), 1100, 700);
        stage.setTitle("Hall Management - CinemaBook Admin");
        stage.setScene(scene);
        stage.show();

        view.btnBack.setOnAction(e -> handleBack());
        loadHalls();
        view.btnAddHall.setOnAction(e -> handleAddHall());
        view.btnEdit.setOnAction(e -> handleEditHall());
        view.btnDelete.setOnAction(e -> handleDeleteHall());
        view.btnRefresh.setOnAction(e -> loadHalls());
        view.searchField.textProperty().addListener((obs, oldVal, newVal) -> filterHalls(newVal));
    }

    public VBox getRootView() {
        return view.getView();
    }

    private void loadHalls() {
        try {
            List<Moviehall> halls = ctx.hallRepo.findAll().stream().map(ModelConverter::toOldHall).collect(Collectors.toList());
            hallList = FXCollections.observableArrayList(halls);
            view.hallTable.setItems(hallList);
            System.out.println("✅ Loaded " + halls.size() + " halls into table");
        } catch (Exception e) {
            log.error("Failed to load halls", e);
            showAlert("Error", "Failed to load halls: " + e.getMessage());
        }
    }

    private void filterHalls(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            view.hallTable.setItems(hallList);
            return;
        }
        ObservableList<Moviehall> filtered = FXCollections.observableArrayList();
        String lower = searchText.toLowerCase();
        for (Moviehall hall : hallList) {
            if (hall.getName().toLowerCase().contains(lower) ||
                    hall.getId().toLowerCase().contains(lower)) {
                filtered.add(hall);
            }
        }
        view.hallTable.setItems(filtered);
    }

    private void handleAddHall() {
        Dialog<Moviehall> dialog = createHallDialog(null);
        Optional<Moviehall> result = dialog.showAndWait();

        result.ifPresent(hall -> {
            ctx.hallRepo.save(ModelConverter.toDomainHall(hall));
            showAlert("Success", "Hall added successfully!");
            loadHalls();
        });
    }

    private void handleEditHall() {
        Moviehall selected = view.hallTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a hall to edit.");
            return;
        }

        Dialog<Moviehall> dialog = createHallDialog(selected);
        Optional<Moviehall> result = dialog.showAndWait();

        result.ifPresent(hall -> {
            // FIXED: Changed .getId(...) to .setId(...)
            hall.setId(selected.getId());

            ctx.hallRepo.save(ModelConverter.toDomainHall(hall));
            showAlert("Success", "Hall updated successfully!");
            loadHalls();
        });
    }
    private void handleDeleteHall() {
        Moviehall selected = view.hallTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a hall to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete Hall");
        confirm.setContentText("Are you sure you want to delete \"" + selected.getName() + "\"?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ctx.hallRepo.delete(Long.parseLong(selected.getId()));
            showAlert("Success", "Hall deleted successfully!");
            loadHalls();
        }
    }
    private void handleBack() {
        System.out.println("Going back to admin dashboard...");
        nav.back();
    }

    private Dialog<Moviehall> createHallDialog(Moviehall existing) {
        Dialog<Moviehall> dialog = new Dialog<>();
        dialog.setTitle(existing == null ? "Add New Hall" : "Edit Hall");
        dialog.setHeaderText(existing == null ? "Enter hall details" : "Update hall details");

        ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(12);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("e.g., Screen 1, VIP Lounge");
        if (existing != null) nameField.setText(existing.getName());

        TextField capacityField = new TextField();
        capacityField.setPromptText("e.g., 50");
        if (existing != null) capacityField.setText(String.valueOf(existing.getCapacity()));
        TextField priceField = new TextField();
        priceField.setPromptText("e.g., 150 (Birr)");
        if (existing != null) {
            priceField.setText(String.valueOf(existing.getCapacity() * 3));  // Example: 3 Birr per seat
        }
        grid.add(new Label("Hall Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Capacity:"), 0, 1);
        grid.add(capacityField, 1, 1);
        grid.add(new Label("Price per Seat (Br):"), 0, 2);  // ✅ Changed to Birr
        grid.add(priceField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButton) {
                if (nameField.getText().trim().isEmpty() ||
                        capacityField.getText().trim().isEmpty() ||
                        priceField.getText().trim().isEmpty()) {
                    showAlert("Invalid Input", "Please fill in all fields.");
                    return null;
                }
                try {
                    Moviehall hall = new Moviehall();
                    hall.setName(nameField.getText().trim());
                    hall.setCapacity(Integer.parseInt(capacityField.getText().trim()));
                    return hall;
                } catch (NumberFormatException e) {
                    showAlert("Invalid Input", "Capacity and Price must be numbers.");
                    return null;
                }
            }
            return null;
        });

        return dialog;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}


