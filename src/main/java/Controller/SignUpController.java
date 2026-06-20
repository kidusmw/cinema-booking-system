package Controller;

import DAO.UserDAOimp;
import Model.User;
import View.SingUpPage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import Model.Customer;
import Model.Admin;
import java.util.Date;

public class SignUpController {
    private final UserDAOimp userDAO = new UserDAOimp();
    private SingUpPage view;
    private String role;
    private Stage stage;

    public SignUpController(Stage stage, String role) {
        this.stage = stage;
        this.role = role;
        view = new SingUpPage();
        Scene scene = new Scene(view.getView(), 800, 700);
        stage.setTitle("Create Account - CinemaBook");
        stage.setScene(scene);
        stage.show();
        view.roleLabel.setText("Sign up as " + capitalize(role));

        view.signUpBtn.setOnAction(e -> handleSignUp());

        view.backBtn.setOnAction(e -> {
            NavigationManager.pop();
            new AuthChoiceController(stage, role);
        });

        view.loginLink.setOnAction(e -> new LoginController(stage, role));
    }

    private void handleSignUp() {
        String firstName = view.firstNameField.getText().trim();
        String lastName = view.lastNameField.getText().trim();
        String username = view.usernameField.getText().trim();
        String password = view.passwordField.getText().trim();
        String email = view.emailField.getText().trim();
        String phone = view.phoneField.getText().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() ||
                password.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            showError("Please fill in all fields");
            return;
        }

        if (userDAO.isUsernameExists(username)) {
            showError("Username already taken.");
            return;
        }

        User newUser;
        if ("admin".equalsIgnoreCase(role)) {
            newUser = new Admin();
        } else {
            newUser = new Customer();
        }
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setEmail(email);
        newUser.setPhone(phone);
        newUser.setRole(role.toUpperCase());
        newUser.setLoginStatus("active");
        newUser.setRegistrationDate(new Date());

        if (userDAO.addUser(newUser)) {
            showInfo("Account Created", "Welcome, " + firstName + "!");
            new LoginController(stage, role);
        } else {
            showError("Failed to create account.");
        }
    }

    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private void showError(String message) { view.errorLabel.setText(message); view.errorLabel.setVisible(true); }
    private void showInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}