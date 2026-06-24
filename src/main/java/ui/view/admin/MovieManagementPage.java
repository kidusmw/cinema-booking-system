package ui.view.admin;

import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

@SuppressWarnings("unchecked")
public class MovieManagementPage {
    public TableView<domain.model.Movie> movieTable;
    public TextField searchField;
    public Button btnAddMovie;
    public Button btnEdit;
    public Button btnDelete;
    public Button btnRefresh;
    public Button btnBack;

    public VBox getView() {
        VBox root = new VBox();
        root.getStyleClass().add("page");
        root.getStyleClass().add("p-30");
        root.getStyleClass().add("gap-20");

        HBox header = new HBox(15);
        header.getStyleClass().add("align-center-left");
        btnBack = createSecondaryButton("⬅  Back");
        btnBack.getStyleClass().add("w-90");

        VBox titleBox = new VBox(5);
        Label title = new Label("Movie Management");
        title.getStyleClass().add("title");

        Label subtitle = new Label("Add, edit, or delete movies from your cinema");
        subtitle.getStyleClass().add("muted-text");

        titleBox.getChildren().addAll(title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        btnAddMovie = createPrimaryButton("➕  Add Movie");
        header.getChildren().addAll(btnBack, titleBox, spacer, btnAddMovie);
        HBox toolbar = new HBox(10);
        toolbar.getStyleClass().add("align-center-left");
        searchField = new TextField();
        searchField.setPromptText("🔍 Search by title or genre...");
        searchField.getStyleClass().add("h-38");
        searchField.getStyleClass().add("w-350");
        searchField.getStyleClass().add("search-field");

        Region toolbarSpacer = new Region();
        HBox.setHgrow(toolbarSpacer, Priority.ALWAYS);

        btnEdit = createSecondaryButton("✏️  Edit");
        btnDelete = createSecondaryButton("🗑️  Delete");
        btnRefresh = createSecondaryButton("🔄  Refresh");

        toolbar.getChildren().addAll(searchField, toolbarSpacer, btnEdit, btnDelete, btnRefresh);
        movieTable = new TableView<>();
        movieTable.getStyleClass().add("table-view");
        movieTable.setPlaceholder(new Label("No movies found. Click 'Add Movie' to create one."));

        VBox.setVgrow(movieTable, Priority.ALWAYS);
        TableColumn<domain.model.Movie, String> idCol = new TableColumn<>("Movie ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("movieId"));
        idCol.getStyleClass().add("w-80");
        TableColumn<domain.model.Movie, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.getStyleClass().add("w-180");
        TableColumn<domain.model.Movie, String> genreCol = new TableColumn<>("Genre");
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));
        genreCol.getStyleClass().add("w-110");
        TableColumn<domain.model.Movie, Integer> durationCol = new TableColumn<>("Duration");
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        durationCol.getStyleClass().add("w-100");
        TableColumn<domain.model.Movie, Double> ratingCol = new TableColumn<>("Rating");
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
        ratingCol.getStyleClass().add("w-80");
        TableColumn<domain.model.Movie, String> langCol = new TableColumn<>("Language");
        langCol.setCellValueFactory(new PropertyValueFactory<>("language"));
        langCol.getStyleClass().add("w-100");
        TableColumn<domain.model.Movie, Date> dateCol = new TableColumn<>("Release Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("releaseDate"));
        dateCol.getStyleClass().add("w-110");
        TableColumn<domain.model.Movie, String> posterCol = new TableColumn<>("Poster Path");
        posterCol.setCellValueFactory(new PropertyValueFactory<>("posterPath"));
        posterCol.getStyleClass().add("w-150");
        TableColumn<domain.model.Movie, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        descCol.getStyleClass().add("w-200");
        ratingCol.setCellFactory(
                col ->
                        new TableCell<domain.model.Movie, Double>() {
                            @Override
                            protected void updateItem(Double item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty || item == null) {
                                    setText(null);
                                } else {
                                    setText(String.format("%.1f", item));
                                }
                            }
                        });
        durationCol.setCellFactory(
                col ->
                        new TableCell<domain.model.Movie, Integer>() {
                            @Override
                            protected void updateItem(Integer item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty || item == null) {
                                    setText(null);
                                } else {
                                    int rawMinutes = item.intValue();
                                    int hours = rawMinutes / 60;
                                    int mins = rawMinutes % 60;

                                    setText(hours + "h " + mins + "m");
                                }
                            }
                        });
        dateCol.setCellFactory(
                col ->
                        new TableCell<domain.model.Movie, Date>() {
                            private final SimpleDateFormat format =
                                    new SimpleDateFormat("yyyy-MM-dd");

                            @Override
                            protected void updateItem(Date item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty || item == null) {
                                    setText(null);
                                } else {
                                    setText(format.format(item));
                                }
                            }
                        });
        movieTable
                .getColumns()
                .addAll(
                        idCol,
                        titleCol,
                        genreCol,
                        durationCol,
                        ratingCol,
                        langCol,
                        dateCol,
                        posterCol,
                        descCol);
        movieTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        movieTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        root.getChildren().addAll(header, toolbar, movieTable);

        return root;
    }

    private static Button createPrimaryButton(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("h-38");
        btn.getStyleClass().add("primary-button-small");
        return btn;
    }

    private static Button createSecondaryButton(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("h-38");
        btn.getStyleClass().add("secondary-button");
        return btn;
    }
}
