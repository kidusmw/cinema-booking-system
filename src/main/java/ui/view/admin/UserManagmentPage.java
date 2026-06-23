package ui.view.admin;

import static ui.common.Theme.*;

import domain.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class UserManagmentPage {
    public TableView<User> userTable;
    public ComboBox<String> roleDropdown;
    public Button btnUpdateRole;
    public Button btnRefresh;
    public Button btnBack;
    public Button btnDelete;
    public TextField searchField;
    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(UserManagmentPage.class);
    private VBox root;
    private static final String WHITE = "#FFFFFF";
    private static final String BG = "#FAFAFA";
    private static final String BG_LIGHT = "#F8FAFC";

    public UserManagmentPage() {
        try {
            createUI();
        } catch (Exception e) {
            log.error("Failed to create UserManagmentPage UI", e);
            root = new VBox();
            Label error = new Label("Error loading User Management page: " + e.getMessage());
            error.setWrapText(true);
            root.getChildren().add(error);
        }
    }

    private void createUI() {

        root = new VBox(20);
        root.setStyle("-fx-background-color: " + BG + ";");
        root.setPadding(new Insets(30));
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(5);
        Label title = new Label("👥 User Management");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        title.setTextFill(Color.web(TEXT_DARK));

        Label subtitle = new Label("View, edit, and manage all user accounts");
        subtitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        subtitle.setTextFill(Color.web(TEXT_MUTED));

        titleBox.getChildren().addAll(title, subtitle);

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        btnBack = new Button("← Back");
        btnBack.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        btnBack.setTextFill(Color.web(TEXT_MUTED));
        btnBack.setStyle(
                "-fx-background-color: transparent;" + "-fx-cursor: hand;" + "-fx-padding: 8 16;");

        header.getChildren().addAll(titleBox, headerSpacer, btnBack);
        HBox toolbar = new HBox(10);
        toolbar.setAlignment(Pos.CENTER_LEFT);

        searchField = new TextField();
        searchField.setPromptText("🔍  Search by username, name, or email...");
        searchField.setPrefHeight(40);
        searchField.setPrefWidth(400);
        searchField.setFont(Font.font("Segoe UI", 13));
        searchField.setStyle(
                "-fx-background-color: "
                        + WHITE
                        + ";"
                        + "-fx-border-color: "
                        + BORDER
                        + ";"
                        + "-fx-border-radius: 8;"
                        + "-fx-background-radius: 8;"
                        + "-fx-padding: 8 14;"
                        + "-fx-text-fill: "
                        + TEXT_DARK
                        + ";"
                        + "-fx-prompt-text-fill: "
                        + TEXT_MUTED
                        + ";");

        toolbar.getChildren().add(searchField);
        HBox body = new HBox(20);
        VBox.setVgrow(body, Priority.ALWAYS);
        VBox actionPanel = new VBox(15);
        actionPanel.setPrefWidth(280);
        actionPanel.setPadding(new Insets(20));
        actionPanel.setStyle(
                "-fx-background-color: "
                        + WHITE
                        + ";"
                        + "-fx-border-color: "
                        + BORDER
                        + ";"
                        + "-fx-border-radius: 12;"
                        + "-fx-background-radius: 12;");

        Label actionTitle = new Label("Modify User");
        actionTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        actionTitle.setTextFill(Color.web(TEXT_DARK));
        roleDropdown = new ComboBox<>();
        roleDropdown.getItems().addAll("ADMIN", "CUSTOMER");
        roleDropdown.setPromptText("Select role...");
        roleDropdown.setPrefWidth(240);
        roleDropdown.setPrefHeight(38);
        roleDropdown.setStyle(
                "-fx-background-color: "
                        + WHITE
                        + ";"
                        + "-fx-border-color: "
                        + BORDER
                        + ";"
                        + "-fx-border-radius: 8;"
                        + "-fx-background-radius: 8;"
                        + "-fx-text-fill: "
                        + TEXT_DARK
                        + ";");
        roleDropdown.setCellFactory(
                param ->
                        new ListCell<String>() {
                            @Override
                            protected void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty || item == null) {
                                    setText(null);
                                    setGraphic(null);
                                } else {
                                    setText(item);
                                    setTextFill(Color.web(TEXT_DARK));
                                    setGraphic(null);
                                }
                            }
                        });
        roleDropdown.setButtonCell(
                new ListCell<String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(roleDropdown.getPromptText());
                            setTextFill(Color.web(TEXT_MUTED));
                        } else {
                            setText(item);
                            setTextFill(Color.web(TEXT_DARK));
                        }
                        setGraphic(null);
                    }
                });

        btnUpdateRole = new Button("✏️  Update Role");
        btnUpdateRole.setPrefWidth(240);
        btnUpdateRole.setPrefHeight(38);
        btnUpdateRole.setTextFill(Color.WHITE);
        btnUpdateRole.setStyle(
                "-fx-background-color: "
                        + ACCENT
                        + ";"
                        + "-fx-background-radius: 8;"
                        + "-fx-cursor: hand;"
                        + "-fx-font-weight: bold;");

        btnDelete = new Button("🗑️  Delete User");
        btnDelete.setPrefWidth(240);
        btnDelete.setPrefHeight(38);
        btnDelete.setTextFill(Color.WHITE);
        btnDelete.setStyle(
                "-fx-background-color: "
                        + DANGER
                        + ";"
                        + "-fx-background-radius: 8;"
                        + "-fx-cursor: hand;"
                        + "-fx-font-weight: bold;");

        btnRefresh = new Button("🔄  Refresh");
        btnRefresh.setPrefWidth(240);
        btnRefresh.setPrefHeight(38);
        btnRefresh.setTextFill(Color.web(TEXT_MUTED));
        btnRefresh.setStyle(
                "-fx-background-color: "
                        + BG_LIGHT
                        + ";"
                        + "-fx-background-radius: 8;"
                        + "-fx-cursor: hand;");

        actionPanel
                .getChildren()
                .addAll(
                        actionTitle,
                        new Separator(),
                        new Label("Change Role:"),
                        roleDropdown,
                        btnUpdateRole,
                        new Separator(),
                        btnDelete,
                        btnRefresh);
        userTable = new TableView<>();
        userTable.setStyle(
                "-fx-background-color: "
                        + WHITE
                        + ";"
                        + "-fx-border-color: "
                        + BORDER
                        + ";"
                        + "-fx-border-radius: 8;");
        userTable.setPlaceholder(new Label("No users found."));
        HBox.setHgrow(userTable, Priority.ALWAYS);
        VBox.setVgrow(userTable, Priority.ALWAYS);
        TableColumn<User, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("userID"));
        idCol.setPrefWidth(50);

        TableColumn<User, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        firstNameCol.setPrefWidth(100);

        TableColumn<User, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        lastNameCol.setPrefWidth(100);

        TableColumn<User, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        usernameCol.setPrefWidth(110);

        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailCol.setPrefWidth(160);

        TableColumn<User, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        phoneCol.setPrefWidth(100);

        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        roleCol.setPrefWidth(80);

        TableColumn<User, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("loginStatus"));
        statusCol.setPrefWidth(70);

        userTable
                .getColumns()
                .addAll(
                        idCol,
                        firstNameCol,
                        lastNameCol,
                        usernameCol,
                        emailCol,
                        phoneCol,
                        roleCol,
                        statusCol);
        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        body.getChildren().addAll(actionPanel, userTable);
        root.getChildren().addAll(header, toolbar, body);
    }

    public VBox getView() {
        return root;
    }
}
