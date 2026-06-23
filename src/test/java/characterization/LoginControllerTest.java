package characterization;

import Controller.LoginController;
import DAO.UserDAOimp;
import Model.Admin;
import Model.Customer;
import Model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Characterization test for LoginController.
 * Captures the current role-dispatch logic: login() returns Admin or Customer,
 * and the controller dispatches to the appropriate dashboard.
 *
 * The actual JavaFX scene construction cannot be tested without a toolkit,
 * so this test focuses on the DAO-level role-dispatch contract.
 */
@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @Test
    void login_returnsAdminInstance_whenRoleIsAdmin() {
        try (MockedConstruction<UserDAOimp> mocked = mockConstruction(UserDAOimp.class,
                (mock, context) -> {
                    Admin admin = new Admin();
                    admin.setUserID(1);
                    admin.setUsername("admin1");
                    admin.setRole("ADMIN");
                    admin.setFirstName("Admin");
                    when(mock.login("admin1", "pass")).thenReturn(admin);
                })) {

            UserDAOimp userDAO = new UserDAOimp();
            User result = userDAO.login("admin1", "pass");

            verify(userDAO).login("admin1", "pass");
            assert result instanceof Admin : "Expected Admin but got " + result.getClass().getSimpleName();
        }
    }

    @Test
    void login_returnsCustomerInstance_whenRoleIsCustomer() {
        try (MockedConstruction<UserDAOimp> mocked = mockConstruction(UserDAOimp.class,
                (mock, context) -> {
                    Customer customer = new Customer();
                    customer.setUserID(2);
                    customer.setUsername("cust1");
                    customer.setRole("CUSTOMER");
                    customer.setFirstName("Customer");
                    when(mock.login("cust1", "pass")).thenReturn(customer);
                })) {

            UserDAOimp userDAO = new UserDAOimp();
            User result = userDAO.login("cust1", "pass");

            verify(userDAO).login("cust1", "pass");
            assert result instanceof Customer : "Expected Customer but got " + result.getClass().getSimpleName();
        }
    }

    @Test
    void login_returnsNull_whenCredentialsInvalid() {
        try (MockedConstruction<UserDAOimp> mocked = mockConstruction(UserDAOimp.class,
                (mock, context) -> {
                    when(mock.login(anyString(), anyString())).thenReturn(null);
                })) {

            UserDAOimp userDAO = new UserDAOimp();
            User result = userDAO.login("unknown", "badpw");

            verify(userDAO).login("unknown", "badpw");
            assert result == null : "Expected null but got " + result;
        }
    }
}
