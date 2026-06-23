package characterization;

import DAO.UserDAOimp;
import Database.DatabaseConnection;
import Model.Admin;
import Model.Customer;
import Model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Characterization test for UserDAOimp.
 * Captures current plaintext login behavior BEFORE refactoring.
 */
class UserDAOimpTest extends AbstractDatabaseTest {

    private UserDAOimp userDAO;

    @BeforeEach
    void setUp() {
        userDAO = new UserDAOimp();
    }

    @Test
    void login_returnsAdmin_whenCredentialsMatchAdminRole() throws Exception {
        insertUser("adminuser", "secret123", "ADMIN");

        User result = userDAO.login("adminuser", "secret123");

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(Admin.class);
        assertThat(result.getUsername()).isEqualTo("adminuser");
        assertThat(result.getPassword()).isEqualTo("secret123");
    }

    @Test
    void login_returnsCustomer_whenCredentialsMatchCustomerRole() throws Exception {
        insertUser("custuser", "pass456", "CUSTOMER");

        User result = userDAO.login("custuser", "pass456");

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(Customer.class);
        assertThat(result.getUsername()).isEqualTo("custuser");
    }

    @Test
    void login_returnsNull_whenPasswordWrong() throws Exception {
        insertUser("user1", "correctpw", "CUSTOMER");

        User result = userDAO.login("user1", "wrongpw");

        assertThat(result).isNull();
    }

    @Test
    void login_returnsNull_whenUsernameNotFound() {
        User result = userDAO.login("nobody", "anything");

        assertThat(result).isNull();
    }

    @Test
    void addUser_storesPlaintextPassword() {
        User user = new Customer();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setUsername("testuser");
        user.setPassword("plaintext-pw");
        user.setEmail("test@example.com");
        user.setPhone("1234567890");
        user.setRole("CUSTOMER");
        user.setLoginStatus("active");

        boolean added = userDAO.addUser(user);

        assertThat(added).isTrue();

        // Verify the password is stored as plaintext (current behavior)
        User fetched = userDAO.login("testuser", "plaintext-pw");
        assertThat(fetched).isNotNull();
        assertThat(fetched.getPassword()).isEqualTo("plaintext-pw");
    }

    private void insertUser(String username, String password, String role) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            String escapedUsername = username.replace("'", "''");
            String escapedPassword = password.replace("'", "''");
            stmt.execute("INSERT INTO User_ (Username, user_Password, user_Role, First_N, Last_N) VALUES ('"
                + escapedUsername + "', '" + escapedPassword + "', '" + role + "', 'F', 'L')");
        }
    }
}
