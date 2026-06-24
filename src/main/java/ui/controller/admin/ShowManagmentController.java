package ui.controller.admin;

import application.AppContext;
import domain.model.Showtime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ui.view.admin.ShowManagementPage;

public class ShowManagmentController {
    private static final Logger log = LoggerFactory.getLogger(ShowManagmentController.class);

    private final ShowManagementPage view;
    private final AppContext ctx;
    private final AdminDashboardController dashboard;
    private ObservableList<Showtime> showList;
    private final Map<String, String> movieMap = new HashMap<>();
    private final Map<String, String> hallMap = new HashMap<>();
    private final Map<String, String> movieMapById = new HashMap<>();
    private final Map<String, String> hallMapById = new HashMap<>();

    public ShowManagmentController(AppContext ctx, AdminDashboardController dashboard) {
        this.ctx = ctx;
        this.dashboard = dashboard;
        this.view = new ShowManagementPage();
        loadLiveDatabaseDropdownMaps();
        loadShows();
        view.btnAddShow.setOnAction(e -> handleAddShow());
        view.btnEdit.setOnAction(e -> handleEditShow());
        view.btnDelete.setOnAction(e -> handleDeleteShow());
        view.btnRefresh.setOnAction(
                e -> {
                    loadLiveDatabaseDropdownMaps();
                    loadShows();
                });
        view.btnBack.setOnAction(e -> handleBack());

        view.searchField.textProperty().addListener((obs, oldVal, newVal) -> filterShows(newVal));

        view.showTable.setOnMouseClicked(
                e -> {
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

        List<domain.model.Movie> movies = ctx.movieRepo.findAll();
        for (domain.model.Movie m : movies) {
            String id = String.valueOf(m.getMovieId());
            String title = m.getTitle();
            movieMap.put(title, id);
            movieMapById.put(id, title);
        }

        List<domain.model.Hall> halls = ctx.hallRepo.findAll();
        for (domain.model.Hall h : halls) {
            String id = String.valueOf(h.getHallId());
            String name = h.getName();
            hallMap.put(name, id);
            hallMapById.put(id, name);
        }
    }

    private void loadShows() {
        List<Showtime> shows = ctx.showtimeRepo.findAll().stream().collect(Collectors.toList());
        for (Showtime show : shows) {
            if (show.getMovieId() != null
                    && movieMapById.containsKey(String.valueOf(show.getMovieId()))) {
                show.setMovieName(movieMapById.get(String.valueOf(show.getMovieId())));
            }
            if (show.getHallId() != null
                    && hallMapById.containsKey(String.valueOf(show.getHallId()))) {
                show.setHallName(hallMapById.get(String.valueOf(show.getHallId())));
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
        ObservableList<Showtime> filtered = FXCollections.observableArrayList();
        String lower = searchText.toLowerCase();
        for (Showtime show : showList) {
            if (String.valueOf(show.getShowId()).contains(lower)
                    || (show.getMovieName() != null
                            && show.getMovieName().toLowerCase().contains(lower))
                    || (show.getHallName() != null
                            && show.getHallName().toLowerCase().contains(lower))
                    || show.getShowTime().toString().toLowerCase().contains(lower)) {
                filtered.add(show);
            }
        }
        view.showTable.setItems(filtered);
    }

    private void handleAddShow() {
        Dialog<Showtime> dialog = createShowDialog(null);
        Optional<Showtime> result = dialog.showAndWait();

        result.ifPresent(
                show -> {
                    ctx.showtimeRepo.save(show);
                    loadShows();
                });
    }

    private void handleEditShow() {
        Showtime selected = view.showTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Dialog<Showtime> dialog = createShowDialog(selected);
        Optional<Showtime> result = dialog.showAndWait();

        result.ifPresent(
                show -> {
                    show.setShowId(selected.getShowId());
                    ctx.showtimeRepo.save(show);
                    loadShows();
                });
    }

    private void handleDeleteShow() {
        Showtime selected = view.showTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setContentText("Delete show ID: \"" + selected.getShowId() + "\"?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ctx.showtimeRepo.delete(selected.getShowId());
            loadShows();
        }
    }

    private void handleBack() {
        if (dashboard != null) {
            dashboard.showDashboard();
        } else {
            showAlert("Routing Error", "Cannot go back: Dashboard connection lost.");
        }
    }

    private Dialog<Showtime> createShowDialog(Showtime existing) {
        Dialog<Showtime> dialog = new Dialog<>();
        dialog.setTitle(
                existing == null ? "Schedule New Screening" : "Modify Screening Properties");

        ButtonType saveButtonType = ButtonType.OK;
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        Button btnSave = (Button) dialog.getDialogPane().lookupButton(saveButtonType);
        if (btnSave != null) btnSave.setText("Save Configurations");

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);
        grid.getStyleClass().add("p-20-40-10-20");

        TextField showIDField = new TextField();
        if (existing != null) showIDField.setText(String.valueOf(existing.getShowId()));
        showIDField.setDisable(existing != null);

        ComboBox<String> movieComboBox = new ComboBox<>();
        movieComboBox.getItems().addAll(movieMap.keySet());
        if (existing != null) movieComboBox.setValue(existing.getMovieName());

        ComboBox<String> hallComboBox = new ComboBox<>();
        hallComboBox.getItems().addAll(hallMap.keySet());
        if (existing != null) hallComboBox.setValue(existing.getHallName());

        DatePicker datePicker = new DatePicker();
        if (existing != null && existing.getShowDate() != null) {
            datePicker.setValue(existing.getShowDate());
        }

        TextField timeField = new TextField();
        if (existing != null) timeField.setText(existing.getShowTime().toString());

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
            btnSave.addEventFilter(
                    ActionEvent.ACTION,
                    event -> {
                        if (showIDField.getText().trim().isEmpty()
                                || movieComboBox.getValue() == null
                                || hallComboBox.getValue() == null
                                || datePicker.getValue() == null
                                || timeField.getText().trim().isEmpty()) {
                            showAlert("Validation", "All fields are required!");
                            event.consume();
                        }
                    });
        }

        dialog.setResultConverter(
                dialogButton -> {
                    if (dialogButton == saveButtonType) {
                        Showtime show = new Showtime();
                        show.setShowId(Long.parseLong(showIDField.getText().trim()));

                        String selectedTitle = movieComboBox.getValue();
                        String selectedHallName = hallComboBox.getValue();

                        show.setMovieName(selectedTitle);
                        show.setHallName(selectedHallName);

                        show.setMovieId(Long.parseLong(movieMap.get(selectedTitle)));
                        show.setHallId(Long.parseLong(hallMap.get(selectedHallName)));

                        if (datePicker.getValue() != null) {
                            show.setShowDate(datePicker.getValue());
                        }
                        show.setShowTime(java.time.LocalTime.parse(timeField.getText().trim()));
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
