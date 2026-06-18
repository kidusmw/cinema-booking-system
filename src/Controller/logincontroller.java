package Controller;

import DAO.UserDAOimp;
import Model.Admin;
import Model.Customer;
import Model.User;
import View.Loginpage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
public class logincontroller {
    private final UserDAOimp userDAO = new UserDAOimp();
    private Loginpage view;
    private String expectedRole;
    private Stage stage;

    public logincontroller(Stage stage, String role) {
        this.stage = stage;
        this.expectedRole = role;
        view = new Loginpage();
        Scene scene = new Scene(view.getView(), 900, 650);
        stage.setTitle("Sign in - CinemaBook");
        stage.setScene(scene);
        stage.show();

        view.roleLabel.setText("Sign in as " + capitalize(role));
        view.loginBtn.setOnAction(e -> handleLogin());
        view.backBtn.setOnAction(e -> new AuthChoiceController(stage, role));
        view.signupLink.setOnAction(e -> new SignUpController(stage, role));
    }
    private void handleLogin() {
        User user = userDAO.login(view.usernameField.getText(), view.passwordField.getText());

        if (user == null) {
            view.errorLabel.setText("Invalid username or password");
            view.errorLabel.setVisible(true);
            return;
        }

        if ("admin".equalsIgnoreCase(expectedRole) && user instanceof Admin) {
            new AdminDashboardController(stage, user.getFirstName());
        } else if ("customer".equalsIgnoreCase(expectedRole) && user instanceof Customer) {
            new CustomerDashboardController(stage, (Customer) user);
        } else {
            view.errorLabel.setText("Role mismatch or invalid user type");
            view.errorLabel.setVisible(true);
        }
    }

    private String capitalize(String str) { return str.substring(0, 1).toUpperCase() + str.substring(1); }
}
