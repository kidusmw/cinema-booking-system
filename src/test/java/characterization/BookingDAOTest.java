package characterization;

import DAO.BookingDAO;
import Database.DatabaseConnection;
import Model.Booking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Characterization test for BookingDAO.
 * Captures current addBooking / getAllBookings / cancelBooking behavior.
 */
class BookingDAOTest extends AbstractDatabaseTest {

    private BookingDAO bookingDAO;

    @BeforeEach
    void setUp() {
        bookingDAO = new BookingDAO();
    }

    @Test
    void addBooking_insertsAndReturnsGeneratedId() throws Exception {
        insertUser(1, "testuser", "pw", "customer");
        insertShow(99);

        Booking booking = new Booking();
        booking.setUserID(1);
        booking.setShowID("99");
        booking.setMovieName("Test Movie");
        booking.setBookingStatus("pending");

        boolean result = bookingDAO.addBooking(booking);

        assertThat(result).isTrue();
        assertThat(booking.getBookingID()).isPositive();
    }

    @Test
    void getAllBookings_returnsAllBookings() throws Exception {
        insertUser(1, "user1", "pw", "customer");
        insertShow(99);
        insertBooking(1, 99, "Movie A", "confirmed");
        insertBooking(1, 99, "Movie B", "pending");

        List<Booking> bookings = bookingDAO.getAllBookings();

        assertThat(bookings).isNotEmpty();
        assertThat(bookings).anyMatch(b -> "Movie A".equals(b.getMovieName()));
        assertThat(bookings).anyMatch(b -> "Movie B".equals(b.getMovieName()));
    }

    @Test
    void cancelBooking_updatesStatusToCancelled() throws Exception {
        insertUser(1, "user2", "pw", "customer");
        insertShow(99);
        insertBooking(1, 99, "Cancel Test", "confirmed");

        List<Booking> bookings = bookingDAO.getAllBookings();
        Booking latest = bookings.stream()
            .filter(b -> "Cancel Test".equals(b.getMovieName()))
            .findFirst()
            .orElseThrow(() -> new AssertionError("No booking found"));

        boolean cancelled = bookingDAO.cancelBooking(latest.getBookingID());

        assertThat(cancelled).isTrue();

        Booking fetched = bookingDAO.searchBookingById(latest.getBookingID());
        assertThat(fetched.getBookingStatus()).isEqualTo("cancelled");
    }

    @Test
    void getBookingsByUserId_returnsOnlyThatUsersBookings() throws Exception {
        insertUser(10, "user10", "pw", "customer");
        insertUser(11, "user11", "pw", "customer");
        insertShow(99);
        insertBooking(10, 99, "User10 Movie", "confirmed");
        insertBooking(11, 99, "User11 Movie", "confirmed");

        List<Booking> user10bookings = bookingDAO.getBookingsByUserId(10);

        assertThat(user10bookings).hasSize(1);
        assertThat(user10bookings.get(0).getMovieName()).isEqualTo("User10 Movie");
    }

    private void insertUser(int id, String username, String password, String role) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("INSERT INTO User_ (User_ID, Username, user_Password, user_Role, First_N, Last_N) VALUES ("
                + id + ", '" + username + "', '" + password + "', '" + role + "', 'F', 'L')"
                + " ON CONFLICT (User_ID) DO NOTHING");
        }
    }

    private void insertShow(int showId) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("INSERT INTO Show_Table (Show_ID, Show_date, Show_time, Movie_ID, Movie_hall_ID) VALUES ("
                + showId + ", '2026-07-01', '10:00:00', 1, 1)"
                + " ON CONFLICT (Show_ID) DO NOTHING");
        }
    }

    private void insertBooking(int userId, int showId, String movieName, String status) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("INSERT INTO booking (user_id, show_id, movie_name, status) VALUES ("
                + userId + ", " + showId + ", '" + movieName + "', '" + status + "')");
        }
    }
}
