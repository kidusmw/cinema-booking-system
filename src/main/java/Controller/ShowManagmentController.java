package Controller;

import DAO.ShowDAO;
import DAO.ShowDAOimp;
import Model.Show;
import View.ShowManagementPage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ShowManagmentController {

    private final ShowManagementPage view;
    private final Stage stage;
    private final AdminDashboardController dashboard;
    private final ShowDAO showDAO = new ShowDAOimp();
    private ObservableList<Show> showList;
    private final Map<String, String> movieMap = new HashMap<>();
    private final Map<String, String> hallMap = new HashMap<>();
    private final Map<String, String> movieMapById = new HashMap<>();
    private final Map<String, String> hallMapById = new HashMap<>();

    public ShowManagmentController(
        Stage stage,
        AdminDashboardController dashboard
    ) {
        this.stage = stage;
        this.dashboard = dashboard;
        this.view = new ShowManagementPage();
        loadLiveDatabaseDropdownMaps();
        loadShows();
        view.btnAddShow.setOnAction(e -> handleAddShow());
        view.btnEdit.setOnAction(e -> handleEditShow());
        view.btnDelete.setOnAction(e -> handleDeleteShow());
        view.btnRefresh.setOnAction(e -> {
            loadLiveDatabaseDropdownMaps();
            loadShows();
        });
        view.btnBack.setOnAction(e -> handleBack());

        view.searchField
            .textProperty()
            .addListener((obs, oldVal, newVal) -> filterShows(newVal));

        view.showTable.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) handleEditShow();
        });
    }

    public VBox getRootView() {
        return view.getView();
    }

    private void loadLiveDatabaseDropdownMaps() {
        movieMap.clear();
        hallMap.clear();
        movieMapById.clear();
        hallMapById.clear();

        String movieQuery = "SELECT Movie_ID, Title FROM dbo.Movie_";
        String hallQuery = "SELECT Movie_hall_ID, Name FROM dbo.Movie_Hall_";

        try (Connection conn = Database.DatabaseConnection.getConnection()) {
            try (
                PreparedStatement ps = conn.prepareStatement(movieQuery);
                ResultSet rs = ps.executeQuery()
            ) {
                while (rs.next()) {
                    String id = rs.getString("Movie_ID");
                    String title = rs.getString("Title");
                    movieMap.put(title, id);
                    movieMapById.put(id, title);
                }
            }
            try (
                PreparedStatement ps = conn.prepareStatement(hallQuery);
                ResultSet rs = ps.executeQuery()
            ) {
                while (rs.next()) {
                    String id = rs.getString("Movie_hall_ID");
                    String name = rs.getString("Name");
                    hallMap.put(name, id);
                    hallMapById.put(id, name);
                }
            }
        } catch (Exception ex) {
            System.out.println(
                "Error pulling database references: " + ex.getMessage()
            );
        }
    }

    private void loadShows() {
        List<Show> shows = showDAO.getAllShows();
        for (Show show : shows) {
            if (
                show.getMovieID() != null &&
                movieMapById.containsKey(show.getMovieID())
            ) {
                show.setMovieName(movieMapById.get(show.getMovieID()));
            }
            if (
                show.getMovieHallID() != null &&
                hallMapById.containsKey(show.getMovieHallID())
            ) {
                show.setHallName(hallMapById.get(show.getMovieHallID()));
            }
        }

        showList = FXCollections.observableArrayList(shows);
        view.showTable.setItems(showList);
    }

    private void filterShows(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            view.getShowTable().setItems(showList);
            return;
        }
        ObservableList<Show> filtered = FXCollections.observableArrayList();
        String lower = searchText.toLowerCase();
        for (Show show : showList) {
            if (
                show.getShowID().toLowerCase().contains(lower) ||
                (show.getMovieName() != null &&
                    show.getMovieName().toLowerCase().contains(lower)) ||
                (show.getHallName() != null &&
                    show.getHallName().toLowerCase().contains(lower)) ||
                show.getShowTime().toLowerCase().contains(lower)
            ) {
                filtered.add(show);
            }
        }
        view.showTable.setItems(filtered);
    }

    private void handleAddShow() {
        Dialog<Show> dialog = createShowDialog(null);
        Optional<Show> result = dialog.showAndWait();

        result.ifPresent(show -> {
            boolean success = showDAO.addShow(show);
            if (success) {
                loadShows();
            } else {
                showAlert("Database Error", "Failed to add show.");
            }
        });
    }

    private void handleEditShow() {
        Show selected = view.showTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Dialog<Show> dialog = createShowDialog(selected);
        Optional<Show> result = dialog.showAndWait();

        result.ifPresent(show -> {
            show.setShowID(selected.getShowID());
            if (showDAO.updateShow(show)) {
                loadShows();
            } else {
                showAlert("Database Error", "Failed to write changes.");
            }
        });
    }

    private void handleDeleteShow() {
        Show selected = view.showTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setContentText(
            "Delete show ID: \"" + selected.getShowID() + "\"?"
        );

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (showDAO.deleteShow(selected.getShowID())) {
                loadShows();
            } else {
                showAlert("Conflict", "Cannot clear show. Tickets assigned.");
            }
        }
    }

    private void handleBack() {
        if (dashboard != null) {
            dashboard.showDashboard();
        } else {
            showAlert(
                "Routing Error",
                "Cannot go back: Dashboard connection lost."
            );
        }
    }

    private Dialog<Show> createShowDialog(Show existing) {
        Dialog<Show> dialog = new Dialog<>();
        dialog.setTitle(
            existing == null
                ? "Schedule New Screening"
                : "Modify Screening Properties"
        );

        ButtonType saveButtonType = ButtonType.OK;
        dialog
            .getDialogPane()
            .getButtonTypes()
            .addAll(saveButtonType, ButtonType.CANCEL);
        Button btnSave = (Button) dialog
            .getDialogPane()
            .lookupButton(saveButtonType);
        if (btnSave != null) btnSave.setText("Save Configurations");

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);
        grid.setPadding(new Insets(20, 40, 10, 20));

        TextField showIDField = new TextField();
        if (existing != null) showIDField.setText(existing.getShowID());
        showIDField.setDisable(existing != null);

        ComboBox<String> movieComboBox = new ComboBox<>();
        movieComboBox.getItems().addAll(movieMap.keySet());
        if (existing != null) movieComboBox.setValue(existing.getMovieName());

        ComboBox<String> hallComboBox = new ComboBox<>();
        hallComboBox.getItems().addAll(hallMap.keySet());
        if (existing != null) hallComboBox.setValue(existing.getHallName());

        DatePicker datePicker = new DatePicker();
        if (existing != null && existing.getShowDate() != null) {
            datePicker.setValue(
                new java.sql.Date(
                    existing.getShowDate().getTime()
                ).toLocalDate()
            );
        }

        TextField timeField = new TextField();
        if (existing != null) timeField.setText(existing.getShowTime());

        grid.add(new Label("Show Code Key:"), 0, 0);
        grid.add(showIDField, 1, 0);
        grid.add(new Label("Film Feature:"), 0, 1);
        grid.add(movieComboBox, 1, 1);
        grid.add(new Label("Auditorium:"), 0, 2);
        grid.add(hallComboBox, 1, 2);
        grid.add(new Label("Date:"), 0, 3);
        grid.add(datePicker, 1, 3);
        grid.add(new Label("Time (24h):"), 0, 4);
        grid.add(timeField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        if (btnSave != null) {
            btnSave.addEventFilter(ActionEvent.ACTION, event -> {
                if (
                    showIDField.getText().trim().isEmpty() ||
                    movieComboBox.getValue() == null ||
                    hallComboBox.getValue() == null ||
                    datePicker.getValue() == null ||
                    timeField.getText().trim().isEmpty()
                ) {
                    showAlert("Validation", "All fields are required!");
                    event.consume();
                }
            });
        }

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                Show show = new Show();
                show.setShowID(showIDField.getText().trim());

                String selectedTitle = movieComboBox.getValue();
                String selectedHallName = hallComboBox.getValue();

                show.setMovieName(selectedTitle);
                show.setHallName(selectedHallName);

                show.setMovieID(movieMap.get(selectedTitle));
                show.setMovieHallID(hallMap.get(selectedHallName));

                if (datePicker.getValue() != null) {
                    show.setShowDate(
                        java.sql.Date.valueOf(datePicker.getValue())
                    );
                }
                show.setShowTime(timeField.getText().trim());
                return show;
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
