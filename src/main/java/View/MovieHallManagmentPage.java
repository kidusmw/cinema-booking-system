package View;

import Model.Movie;
import Model.Moviehall;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;
import static ui.common.Theme.*;

public class MovieHallManagmentPage {

    public TableView<Moviehall> hallTable;
    public TextField searchField;
    public Button btnAddHall;
    public Button btnEdit;
    public Button btnDelete;
    public Button btnRefresh;
    public Button btnBack;
    public TableColumn<Moviehall, String> idCol;
    public TableColumn<Moviehall, String> nameCol;
    public TableColumn<Moviehall, Integer> capacityCol;

    private VBox root;

    private static final String HOVER = "#EC4899";
    private static final String WHITE = "#FFFFFF";
    private static final String BG = "#FAFAFA";
    private static final String INFO = "#3B82F6";

    public MovieHallManagmentPage() {
        try {
            createUI();
            refreshTable();
        } catch (Exception e) {
            e.printStackTrace();
            root = new VBox();
            root.getChildren().add(new Label("Error: " + e.getMessage()));
        }
    }

    private void createUI() {
        root = new VBox(20);
        root.setStyle("-fx-background-color: " + BG + ";");
        root.setPadding(new Insets(30));
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(5);
        Label title = new Label("🏛️ Hall Management");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        title.setTextFill(Color.web(TEXT_DARK));

        Label subtitle = new Label("Manage cinema halls and their seating capacity");
        subtitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        subtitle.setTextFill(Color.web(TEXT_MUTED));

        titleBox.getChildren().addAll(title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        btnBack = new Button("← Back to Dashboard");
        btnBack.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        btnBack.setTextFill(Color.web(TEXT_MUTED));
        btnBack.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-padding: 8 16;");

        header.getChildren().addAll(titleBox, spacer, btnBack);
        HBox toolbar = new HBox(10);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        searchField = new TextField();
        searchField.setPromptText("🔍  Search by name...");
        searchField.setPrefHeight(40);
        searchField.setPrefWidth(400);
        searchField.setStyle("-fx-background-color: " + WHITE + "; -fx-border-color: " + BORDER + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 8 14;");

        Region toolbarSpacer = new Region();
        HBox.setHgrow(toolbarSpacer, Priority.ALWAYS);

        btnAddHall = createStyledButton("➕  Add Hall", ACCENT, HOVER);
        toolbar.getChildren().addAll(searchField, toolbarSpacer, btnAddHall);
        hallTable = new TableView<>();
        hallTable.setStyle("-fx-background-color: " + WHITE + "; -fx-border-color: " + BORDER + "; -fx-background-radius: 8;");
        VBox.setVgrow(hallTable, Priority.ALWAYS);
        idCol = new TableColumn<>("Hall ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("MovieHallID"));
        idCol.setPrefWidth(100);

        nameCol = new TableColumn<>("Hall Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(300);

        capacityCol = new TableColumn<>("Capacity");
        capacityCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        capacityCol.setPrefWidth(150);

        hallTable.getColumns().addAll(idCol, nameCol, capacityCol);
        hallTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        btnEdit = createStyledButton("✏️  Edit", INFO, "#2563EB");
        btnDelete = createStyledButton("🗑️  Delete", DANGER, "#B91C1C");
        btnRefresh = createStyledButton("🔄  Refresh", WARNING, "#D97706");

        HBox actionRow = new HBox(10);
        actionRow.setAlignment(Pos.CENTER_RIGHT);
        actionRow.getChildren().addAll(btnEdit, btnDelete, btnRefresh);

        root.getChildren().addAll(header, toolbar, hallTable, actionRow);
    }

    public void refreshTable() {
        try {
            DAO.MovieHallDAO dao = new DAO.MovieHallDAO();
            List<Moviehall> data = dao.getAllMovieHalls();
            ObservableList<Moviehall> list = FXCollections.observableArrayList(data);
            hallTable.setItems(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public VBox getView() { return root; }

    private Button createStyledButton(String text, String normalColor, String hoverColor) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        btn.setPrefHeight(38);
        btn.setTextFill(Color.WHITE);
        btn.setStyle("-fx-background-color: " + normalColor + "; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 8 16;");
        return btn;
    }
}