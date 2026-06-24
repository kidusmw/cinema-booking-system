package ui.view.admin;

import domain.model.Hall;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

@SuppressWarnings({"unchecked", "deprecation"})
public class MovieHallManagmentPage {

    public TableView<Hall> hallTable;
    public TextField searchField;
    public Button btnAddHall;
    public Button btnEdit;
    public Button btnDelete;
    public Button btnRefresh;
    public Button btnBack;
    public TableColumn<Hall, String> idCol;
    public TableColumn<Hall, String> nameCol;
    public TableColumn<Hall, Integer> capacityCol;

    private VBox root;

    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(MovieHallManagmentPage.class);

    public MovieHallManagmentPage() {
        try {
            createUI();
        } catch (Exception e) {
            log.error("Failed to create MovieHallManagmentPage UI", e);
            root = new VBox();
            root.getChildren().add(new Label("Error: " + e.getMessage()));
        }
    }

    private void createUI() {
        root = new VBox(20);
        root.getStyleClass().add("page");
        root.getStyleClass().add("p-30");
        HBox header = new HBox(15);
        header.getStyleClass().add("align-center-left");

        VBox titleBox = new VBox(5);
        Label title = new Label("🏛️ Hall Management");
        title.getStyleClass().add("title");

        Label subtitle = new Label("Manage cinema halls and their seating capacity");
        subtitle.getStyleClass().add("muted-text");

        titleBox.getChildren().addAll(title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        btnBack = new Button("← Back to Dashboard");
        btnBack.getStyleClass().add("back-button");

        header.getChildren().addAll(titleBox, spacer, btnBack);
        HBox toolbar = new HBox(10);
        toolbar.getStyleClass().add("align-center-left");
        searchField = new TextField();
        searchField.setPromptText("🔍  Search by name...");
        searchField.getStyleClass().add("h-40");
        searchField.getStyleClass().add("w-400");
        searchField.getStyleClass().add("search-field");

        Region toolbarSpacer = new Region();
        HBox.setHgrow(toolbarSpacer, Priority.ALWAYS);

        btnAddHall = createStyledButton("➕  Add Hall", "primary-button-small");
        toolbar.getChildren().addAll(searchField, toolbarSpacer, btnAddHall);
        hallTable = new TableView<>();
        hallTable.getStyleClass().add("table-view");
        VBox.setVgrow(hallTable, Priority.ALWAYS);
        idCol = new TableColumn<>("Hall ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("hallId"));
        idCol.getStyleClass().add("w-100");

        nameCol = new TableColumn<>("Hall Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.getStyleClass().add("w-300");

        capacityCol = new TableColumn<>("Capacity");
        capacityCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        capacityCol.getStyleClass().add("w-150");

        hallTable.getColumns().addAll(idCol, nameCol, capacityCol);
        hallTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        hallTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        btnEdit = createStyledButton("✏️  Edit", "secondary-button");
        btnDelete = createStyledButton("🗑️  Delete", "danger-button");
        btnRefresh = createStyledButton("🔄  Refresh", "secondary-button");

        HBox actionRow = new HBox(10);
        actionRow.getStyleClass().add("align-center-right");
        actionRow.getChildren().addAll(btnEdit, btnDelete, btnRefresh);

        root.getChildren().addAll(header, toolbar, hallTable, actionRow);
    }

    public VBox getView() {
        return root;
    }

    private static Button createStyledButton(String text, String cssClass) {
        Button btn = new Button(text);
        btn.getStyleClass().add("h-38");
        btn.getStyleClass().add(cssClass);
        return btn;
    }
}
