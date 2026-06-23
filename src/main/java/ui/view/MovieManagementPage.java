package ui.view;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.text.SimpleDateFormat;
import java.util.Date;
import static ui.common.Theme.*;

public class MovieManagementPage {
    public TableView<ui.model.Movie> movieTable;
    public TextField searchField;
    public Button btnAddMovie;
    public Button btnEdit;
    public Button btnDelete;
    public Button btnRefresh;
    public Button btnBack;
    private static final String HOVER = "#EC4899";
    private static final String WHITE = "#FFFFFF";
    private static final String BG = "#FAFAFA";
    private static final String BG_LIGHT = "#F8FAFC";
    public VBox getView() {
        VBox root = new VBox();
        root.setStyle("-fx-background-color: " + BG + ";");
        root.setPadding(new Insets(30));
        root.setSpacing(20);

        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        btnBack = createSecondaryButton("⬅  Back");
        btnBack.setPrefWidth(90);

        VBox titleBox = new VBox(5);
        Label title = new Label("Movie Management");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        title.setTextFill(Color.web(TEXT_DARK));

        Label subtitle = new Label("Add, edit, or delete movies from your cinema");
        subtitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        subtitle.setTextFill(Color.web(TEXT_MUTED));

        titleBox.getChildren().addAll(title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        btnAddMovie = createPrimaryButton("➕  Add Movie");
        btnAddMovie.setOnMouseEntered(e -> btnAddMovie.setStyle(getPrimaryStyle(true)));
        btnAddMovie.setOnMouseExited(e -> btnAddMovie.setStyle(getPrimaryStyle(false)));
        header.getChildren().addAll(btnBack, titleBox, spacer, btnAddMovie);
        HBox toolbar = new HBox(10);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        searchField = new TextField();
        searchField.setPromptText("🔍 Search by title or genre...");
        searchField.setPrefHeight(38);
        searchField.setPrefWidth(350);
        searchField.setFont(Font.font("Segoe UI", 13));
        searchField.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 8 14;" +
                        "-fx-text-fill: " + TEXT_DARK + ";" +
                        "-fx-prompt-text-fill: " + TEXT_MUTED + ";"
        );

        Region toolbarSpacer = new Region();
        HBox.setHgrow(toolbarSpacer, Priority.ALWAYS);

        btnEdit = createSecondaryButton("✏️  Edit");
        btnDelete = createSecondaryButton("🗑️  Delete");
        btnRefresh = createSecondaryButton("🔄  Refresh");

        toolbar.getChildren().addAll(searchField, toolbarSpacer, btnEdit, btnDelete, btnRefresh);
        movieTable = new TableView<>();
        movieTable.setStyle("-fx-background-color: white; -fx-border-color: " + BORDER + "; -fx-border-radius: 8;");
        movieTable.setPlaceholder(new Label("No movies found. Click 'Add Movie' to create one."));

        VBox.setVgrow(movieTable, Priority.ALWAYS);
        TableColumn<ui.model.Movie, String> idCol = new TableColumn<>("Movie ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("movieID"));
        idCol.setPrefWidth(80);
        TableColumn<ui.model.Movie, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setPrefWidth(180);
        TableColumn<ui.model.Movie, String> genreCol = new TableColumn<>("Genre");
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));
        genreCol.setPrefWidth(110);
        TableColumn<ui.model.Movie, Integer> durationCol = new TableColumn<>("Duration");
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        durationCol.setPrefWidth(100);
        TableColumn<ui.model.Movie, Double> ratingCol = new TableColumn<>("Rating");
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
        ratingCol.setPrefWidth(80);
        TableColumn<ui.model.Movie, String> langCol = new TableColumn<>("Language");
        langCol.setCellValueFactory(new PropertyValueFactory<>("language"));
        langCol.setPrefWidth(100);
        TableColumn<ui.model.Movie, Date> dateCol = new TableColumn<>("Release Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("releaseDate"));
        dateCol.setPrefWidth(110);
        TableColumn<ui.model.Movie, String> posterCol = new TableColumn<>("Poster Path");
        posterCol.setCellValueFactory(new PropertyValueFactory<>("posterPath"));
        posterCol.setPrefWidth(150);
        TableColumn<ui.model.Movie, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        descCol.setPrefWidth(200);
        ratingCol.setCellFactory(col -> new TableCell<ui.model.Movie, Double>() {
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
        durationCol.setCellFactory(col -> new TableCell<ui.model.Movie, Integer>() {
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
        dateCol.setCellFactory(col -> new TableCell<ui.model.Movie, Date>() {
            private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
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
        movieTable.getColumns().addAll(idCol, titleCol, genreCol, durationCol, ratingCol, langCol, dateCol, posterCol, descCol);
        movieTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        root.getChildren().addAll(header, toolbar, movieTable);

        return root;
    }
    private Button createPrimaryButton(String text) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        btn.setPrefHeight(38);
        btn.setStyle(getPrimaryStyle(false));
        return btn;
    }

    private Button createSecondaryButton(String text) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        btn.setPrefHeight(38);
        btn.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-cursor: hand;" +
                        "-fx-text-fill: " + TEXT_DARK + ";"
        );
        return btn;
    }

    private String getPrimaryStyle(boolean hovered) {
        String bg = hovered ? HOVER : ACCENT;
        return
                "-fx-background-color: " + bg + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 8;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 8 16;";
    }
}