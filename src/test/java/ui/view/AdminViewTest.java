package ui.view;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import ui.view.admin.AdminDashboardPage;
import ui.view.admin.BookingManagementPage;
import ui.view.admin.MovieHallManagementPage;
import ui.view.admin.MovieManagementPage;
import ui.view.admin.PaymentManagementPage;
import ui.view.admin.SeatManagementPage;
import ui.view.admin.ShowManagementPage;
import ui.view.admin.UserManagementPage;

@DisplayName("Admin views render key UI elements")
class AdminViewTest extends ApplicationTest {

    private Stage stage;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setScene(new Scene(new StackPane()));
    }

    @Test
    void adminDashboardHasMenuButtons() {
        AdminDashboardPage page = new AdminDashboardPage();
        stage.getScene().setRoot(page.getView());
        assertNotNull(page.welcomeLabel);
        assertNotNull(page.btnLogout);
    }

    @Test
    void movieManagementPageHasTable() {
        MovieManagementPage page = new MovieManagementPage();
        stage.getScene().setRoot(page.getView());
        assertNotNull(page.movieTable);
        assertNotNull(page.btnAddMovie);
        assertNotNull(page.btnRefresh);
    }

    @Test
    void movieHallManagementPageHasTable() {
        MovieHallManagementPage page = new MovieHallManagementPage();
        stage.getScene().setRoot(page.getView());
        assertNotNull(page.hallTable);
        assertNotNull(page.btnAddHall);
        assertNotNull(page.btnRefresh);
    }

    @Test
    void bookingManagementPageHasTable() {
        BookingManagementPage page = new BookingManagementPage();
        stage.getScene().setRoot(page.getView());
        assertNotNull(page.bookingTable);
        assertNotNull(page.btnRefresh);
    }

    @Test
    void paymentManagementHasTable() {
        PaymentManagementPage page = new PaymentManagementPage();
        stage.getScene().setRoot(page.getView());
        assertNotNull(page.paymentTable);
        assertNotNull(page.btnRefresh);
    }

    @Test
    void seatManagementPageHasDropdown() {
        SeatManagementPage page = new SeatManagementPage();
        stage.getScene().setRoot(page.getView());
        assertNotNull(page.hallDropdown);
        assertNotNull(page.btnGenerate);
    }

    @Test
    void showManagementPageHasTable() {
        ShowManagementPage page = new ShowManagementPage();
        stage.getScene().setRoot(page.getView());
        assertNotNull(page.showTable);
        assertNotNull(page.btnAddShow);
        assertNotNull(page.btnRefresh);
    }

    @Test
    void userManagementPageHasTable() {
        UserManagementPage page = new UserManagementPage();
        stage.getScene().setRoot(page.getView());
        assertNotNull(page.userTable);
        assertNotNull(page.roleDropdown);
        assertNotNull(page.btnRefresh);
    }
}
