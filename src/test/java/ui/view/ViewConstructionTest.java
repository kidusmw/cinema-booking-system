package ui.view;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ui.view.admin.AdminDashboardPage;
import ui.view.admin.BookingManagementPage;
import ui.view.admin.MovieHallManagementPage;
import ui.view.admin.MovieManagementPage;
import ui.view.admin.PaymentManagementPage;
import ui.view.admin.SeatManagementPage;
import ui.view.admin.ShowManagementPage;
import ui.view.admin.UserManagementPage;
import ui.view.common.AuthChoice;
import ui.view.common.LoginPage;
import ui.view.common.SignUpPage;
import ui.view.common.UserTypePage;
import ui.view.common.WelcomePage;
import ui.view.customer.CustomerDashboardPage;
import ui.view.customer.MovieBrowserPage;
import ui.view.customer.MovieHallSelectionPage;
import ui.view.customer.PaymentPage;
import ui.view.customer.SeatSelectionPage;
import ui.view.customer.ShowSelectionPage;
import ui.view.customer.TicketPage;

@DisplayName("All views construct without error")
class ViewConstructionTest {

    @BeforeAll
    static void initToolkit() {
        new JFXPanel();
    }

    @Test
    void welcomePage() {
        assertNotNull(new WelcomePage().getView());
    }

    @Test
    void loginPage() {
        assertNotNull(new LoginPage().getView());
    }

    @Test
    void signUpPage() {
        assertNotNull(new SignUpPage().getView());
    }

    @Test
    void userTypePage() {
        assertNotNull(new UserTypePage().getView());
    }

    @Test
    void authChoice() {
        assertNotNull(new AuthChoice().getView());
    }

    @Test
    void customerDashboardPage() {
        assertNotNull(new CustomerDashboardPage().getView());
    }

    @Test
    void movieBrowserPage() {
        assertNotNull(new MovieBrowserPage().getView());
    }

    @Test
    void movieHallSelectionPage() {
        assertNotNull(new MovieHallSelectionPage().getView());
    }

    @Test
    void showSelectionPage() {
        assertNotNull(new ShowSelectionPage().getView());
    }

    @Test
    void seatSelectionPage() {
        assertNotNull(new SeatSelectionPage().getView());
    }

    @Test
    void paymentPage() {
        assertNotNull(new PaymentPage().getView());
    }

    @Test
    void ticketPage() {
        assertNotNull(new TicketPage().getView());
    }

    @Test
    void adminDashboardPage() {
        assertNotNull(new AdminDashboardPage().getView());
    }

    @Test
    void bookingManagementPage() {
        assertNotNull(new BookingManagementPage().getView());
    }

    @Test
    void movieHallManagementPage() {
        assertNotNull(new MovieHallManagementPage().getView());
    }

    @Test
    void movieManagementPage() {
        assertNotNull(new MovieManagementPage().getView());
    }

    @Test
    void paymentManagementPage() {
        assertNotNull(new PaymentManagementPage().getView());
    }

    @Test
    void seatManagementPage() {
        assertNotNull(new SeatManagementPage().getView());
    }

    @Test
    void showManagementPage() {
        assertNotNull(new ShowManagementPage().getView());
    }

    @Test
    void userManagementPage() {
        assertNotNull(new UserManagementPage().getView());
    }
}
