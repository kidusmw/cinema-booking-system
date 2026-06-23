package Controller;

import DAO.UserDAO;
import DAO.UserDAOimp;
import Model.User;
import View.UserManagmentPage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.util.List;
import java.util.Optional;

public class UserManagmentController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserManagmentController.class);
    private final UserManagmentPage view;
    private final Stage stage;
    private final AdminDashboardController dashboard;
    private final UserDAO userDAO = new UserDAOimp();
    private ObservableList<User> userList = FXCollections.observableArrayList();

    public UserManagmentController(Stage stage, AdminDashboardController dashboard) {
        this.stage = stage;
        this.dashboard = dashboard;
        this.view = new UserManagmentPage();
        view.roleDropdown.getItems().setAll("ADMIN", "CUSTOMER");
        loadUsers();
        view.btnRefresh.setOnAction(e -> loadUsers());
        view.btnUpdateRole.setOnAction(e -> handleUpdateRole());
        view.btnDelete.setOnAction(e -> handleDeleteUser());
        view.btnBack.setOnAction(e -> handleBack());

        view.searchField.textProperty().addListener((obs, oldVal, newVal) -> filterUsers(newVal));

        view.userTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateFields(newSelection);
            }
        });
    }

    public javafx.scene.layout.VBox getRootView() {
        return view.getView();
    }

    private void populateFields(User user) {

        view.roleDropdown.setValue(user.getRole());
    }
    private void loadUsers() {
        try {
            List<User> users = userDAO.getAllUsers();
            if (users == null || users.isEmpty()) {
                log.warn("Database query executed but returned 0 records.");
            }
            userList = FXCollections.observableArrayList(users);
            view.userTable.setItems(userList);
        } catch (Exception e) {
            log.error("Failed to load users", e);
            showAlert("Database Connection Error", "Could not read users from SQL: " + e.getMessage());
        }
    }
    private void filterUsers(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            view.userTable.setItems(userList);
            return;
        }
        ObservableList<User> filtered = FXCollections.observableArrayList();
        String lower = searchText.toLowerCase();
        for (User user : userList) {
            if (user.getUsername().toLowerCase().contains(lower) ||
                    user.getFirstName().toLowerCase().contains(lower) ||
                    user.getLastName().toLowerCase().contains(lower) ||
                    (user.getEmail() != null && user.getEmail().toLowerCase().contains(lower))) {
                filtered.add(user);
            }
        }
        view.userTable.setItems(filtered);
    }
    private void handleUpdateRole() {
        User selected = view.userTable.getSelectionModel().getSelectedItem();
        String selectedRole = view.roleDropdown.getValue();

        if (selected == null) {
            showAlert("No Selection", "Please click a specific user row in the table first.");
            return;
        }
        if (selectedRole == null) {
            showAlert("No Role Chosen", "Please select a target role from the dropdown.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Role Change");
        confirm.setHeaderText("Update User Role");
        confirm.setContentText("Change user " + selected.getUsername() + "'s system access to " + selectedRole + "?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            selected.setRole(selectedRole);
            boolean success = userDAO.updateUser(selected);
            if (success) {
                showAlert("Success", "User security role altered smoothly!");
                loadUsers(); // Dynamic real-time window table reload
            } else {
                showAlert("SQL Execution Failure", "Database rejected update statement. Validate connection parameters.");
            }
        }
    }
    private void handleDeleteUser() {
        User selected = view.userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a user account row to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete User Account");
        confirm.setContentText("Are you sure you want to delete user \"" + selected.getUsername() + "\"?\nThis structural operation cannot be undone.");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = userDAO.deleteUser(selected.getUserID());
            if (success) {
                showAlert("Success", "User account wiped successfully.");
                loadUsers();
            } else {
                showAlert("SQL Error", "Could not complete account deletion. Active transaction dependencies found.");
            }
        }
    }
    private void handleBack() {
        if (dashboard != null) {
            dashboard.showDashboard();
        } else {
            showAlert("Navigation Error", "Parent dashboard context tracker was lost.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}