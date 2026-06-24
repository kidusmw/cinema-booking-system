package ui.view;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import ui.view.common.AuthChoice;
import ui.view.common.LoginPage;
import ui.view.common.SignUpPage;
import ui.view.common.UserTypePage;
import ui.view.common.WelcomePage;

@DisplayName("Common views render key UI elements")
class CommonViewTest extends ApplicationTest {

    private Stage stage;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setScene(new Scene(new StackPane()));
    }

    @Test
    void welcomePageHasEnterButton() {
        WelcomePage page = new WelcomePage();
        stage.getScene().setRoot(page.getView());
        assertNotNull(page.enterBtn);
    }

    @Test
    void welcomePageHasMenu() {
        WelcomePage page = new WelcomePage();
        stage.getScene().setRoot(page.getView());
        assertNotNull(page.menu);
    }

    @Test
    void loginPageHasAllFields() {
        LoginPage page = new LoginPage();
        stage.getScene().setRoot(page.getView());
        assertNotNull(page.usernameField);
        assertNotNull(page.passwordField);
        assertNotNull(page.loginBtn);
        assertNotNull(page.backBtn);
        assertNotNull(page.signupLink);
    }

    @Test
    void signUpPageHasAllFields() {
        SignUpPage page = new SignUpPage();
        stage.getScene().setRoot(page.getView());
        assertNotNull(page.firstNameField);
        assertNotNull(page.lastNameField);
        assertNotNull(page.usernameField);
        assertNotNull(page.passwordField);
        assertNotNull(page.emailField);
        assertNotNull(page.phoneField);
        assertNotNull(page.signUpBtn);
        assertNotNull(page.backBtn);
    }

    @Test
    void userTypePageHasRoleButtons() {
        UserTypePage page = new UserTypePage();
        stage.getScene().setRoot(page.getView());
        assertNotNull(page.btnAdmin);
        assertNotNull(page.btnCustomer);
        assertNotNull(page.btnBack);
    }

    @Test
    void authChoiceHasLoginAndSignUp() {
        AuthChoice page = new AuthChoice();
        stage.getScene().setRoot(page.getView());
        assertNotNull(page.btnLogin);
        assertNotNull(page.btnSignUp);
        assertNotNull(page.btnBack);
    }
}
