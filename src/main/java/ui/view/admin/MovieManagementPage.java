package ui.view.admin;

import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

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
        root.setPadding(new Insets(30));
        root.setSpacing(20);

        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        btnBack = createSecondaryButton("⬅  Back");
        btnBack.setPrefWidth(90);

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
        toolbar.setAlignment(Pos.CENTER_LEFT);
        searchField = new TextField();
        searchField.setPromptText("🔍 Search by title or genre...");
        searchField.setPrefHeight(38);
        searchField.setPrefWidth(350);
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
        idCol.setCellValueFactory(new PropertyValueFactory<>("movieID"));
        idCol.setPrefWidth(80);
        TableColumn<domain.model.Movie, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setPrefWidth(180);
        TableColumn<domain.model.Movie, String> genreCol = new TableColumn<>("Genre");
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));
        genreCol.setPrefWidth(110);
        TableColumn<domain.model.Movie, Integer> durationCol = new TableColumn<>("Duration");
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        durationCol.setPrefWidth(100);
        TableColumn<domain.model.Movie, Double> ratingCol = new TableColumn<>("Rating");
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
        ratingCol.setPrefWidth(80);
        TableColumn<domain.model.Movie, String> langCol = new TableColumn<>("Language");
        langCol.setCellValueFactory(new PropertyValueFactory<>("language"));
        langCol.setPrefWidth(100);
        TableColumn<domain.model.Movie, Date> dateCol = new TableColumn<>("Release Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("releaseDate"));
        dateCol.setPrefWidth(110);
        TableColumn<domain.model.Movie, String> posterCol = new TableColumn<>("Poster Path");
        posterCol.setCellValueFactory(new PropertyValueFactory<>("posterPath"));
        posterCol.setPrefWidth(150);
        TableColumn<domain.model.Movie, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        descCol.setPrefWidth(200);
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
                                    int hours = item / 60;
                                    int mins = item % 60;
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
        root.getChildren().addAll(header, toolbar, movieTable);

        return root;
    }

    private Button createPrimaryButton(String text) {
        Button btn = new Button(text);
        btn.setPrefHeight(38);
        btn.getStyleClass().add("primary-button-small");
        return btn;
    }

    private Button createSecondaryButton(String text) {
        Button btn = new Button(text);
        btn.setPrefHeight(38);
        btn.getStyleClass().add("secondary-button");
        return btn;
    }
}
