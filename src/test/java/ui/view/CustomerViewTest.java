package ui.view;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import ui.view.customer.CustomerDashboardPage;
import ui.view.customer.MovieBrowserPage;
import ui.view.customer.MovieHallSelectionPage;
import ui.view.customer.PaymentPage;
import ui.view.customer.SeatSelectionPage;
import ui.view.customer.ShowSelectionPage;
import ui.view.customer.TicketPage;

@DisplayName("Customer views render key UI elements")
class CustomerViewTest extends ApplicationTest {

    private Stage stage;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setScene(new Scene(new StackPane()));
    }

    @Test
    void customerDashboardHasWelcomeLabel() {
        CustomerDashboardPage page = new CustomerDashboardPage();
        stage.getScene().setRoot(page.getView());
        assertNotNull(page.welcomeLabel);
        assertNotNull(page.btnBrowseMovies);
        assertNotNull(page.btnLogout);
    }

    @Test
    void movieBrowserPageHasSearchField() {
        MovieBrowserPage page = new MovieBrowserPage();
        stage.getScene().setRoot(page.getView());
        assertNotNull(page.searchField);
        assertNotNull(page.btnBack);
        assertNotNull(page.movieContainer);
    }

    @Test
    void movieHallSelectionPageHasBackButton() {
        MovieHallSelectionPage page = new MovieHallSelectionPage();
        stage.getScene().setRoot(page.getView());
        assertNotNull(page.btnBack);
        assertNotNull(page.hallsContainer);
    }

    @Test
    void showSelectionPageHasMovieTitle() {
        ShowSelectionPage page = new ShowSelectionPage();
        stage.getScene().setRoot(page.getView());
        assertNotNull(page.movieTitle);
        assertNotNull(page.btnBack);
        assertNotNull(page.showCardsContainer);
    }

    @Test
    void seatSelectionPageHasBackButton() {
        SeatSelectionPage page = new SeatSelectionPage();
        stage.getScene().setRoot(page.getView());
        assertNotNull(page.btnBack);
        assertNotNull(page.hallNameLabel);
        assertNotNull(page.seatGrid);
        assertNotNull(page.btnProceed);
    }

    @Test
    void paymentPageHasSummaryBox() {
        PaymentPage page = new PaymentPage();
        stage.getScene().setRoot(page.getView());
        assertNotNull(page.summaryBox);
        assertNotNull(page.btnVerify);
        assertNotNull(page.btnBack);
    }

    @Test
    void ticketPageHasSuccessLabel() {
        TicketPage page = new TicketPage();
        stage.getScene().setRoot(page.getView());
        assertNotNull(page.successLabel);
        assertNotNull(page.movieTitle);
        assertNotNull(page.btnHome);
    }
}
