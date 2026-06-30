package infrastructure.persistence;

import domain.model.Booking;
import domain.model.BookingSeat;
import domain.model.BookingStatus;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcBookingRepository {
    private final ConnectionProvider connectionProvider;

    public JdbcBookingRepository(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public Booking save(Booking booking) {
        if (booking.getBookingId() == null) {
            return insert(booking);
        } else {
            return update(booking);
        }
    }

    private Booking insert(Booking booking) {
        String sql =
                "INSERT INTO booking (user_id, show_id, movie_name, status, total_amount, booking_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = connectionProvider.getConnection();
                PreparedStatement ps =
                        conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, booking.getUserId().longValue());
            ps.setLong(2, booking.getShowId().longValue());
            ps.setString(3, booking.getMovieName());
            ps.setString(4, booking.getBookingStatus().getDbValue());
            ps.setDouble(5, booking.getAmount());
            ps.setTimestamp(
                    6,
                    booking.getBookingDate() != null
                            ? Timestamp.valueOf(booking.getBookingDate())
                            : Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    booking.setBookingId(Long.valueOf(keys.getLong(1)));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert booking", e);
        }
        return booking;
    }

    private Booking update(Booking booking) {
        String sql =
                "UPDATE booking SET user_id=?, show_id=?, movie_name=?, status=?, total_amount=? WHERE booking_id=?";
        try (Connection conn = connectionProvider.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, booking.getUserId().longValue());
            ps.setLong(2, booking.getShowId().longValue());
            ps.setString(3, booking.getMovieName());
            ps.setString(4, booking.getBookingStatus().getDbValue());
            ps.setDouble(5, booking.getAmount());
            ps.setLong(6, booking.getBookingId().longValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update booking", e);
        }
        return booking;
    }

    public Optional<Booking> findById(Long id) {
        String sql = "SELECT * FROM booking WHERE booking_id = ?";
        try (Connection conn = connectionProvider.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id.longValue());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Booking booking = mapRow(rs);
                    booking.setSeats(findBookingSeats(id));
                    return Optional.of(booking);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find booking by id", e);
        }
        return Optional.empty();
    }

    public List<Booking> findByUserId(Long userId) {
        String sql = "SELECT * FROM booking WHERE user_id = ? ORDER BY booking_date DESC";
        List<Booking> bookings = new ArrayList<>();
        try (Connection conn = connectionProvider.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId.longValue());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Booking booking = mapRow(rs);
                    booking.setSeats(findBookingSeats(booking.getBookingId()));
                    bookings.add(booking);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find bookings by user id", e);
        }
        return bookings;
    }

    public List<Booking> findAll() {
        String sql =
                "SELECT b.*, u.username FROM booking b "
                        + "LEFT JOIN \"user\" u ON b.user_id = u.user_id "
                        + "ORDER BY b.booking_id DESC";
        List<Booking> bookings = new ArrayList<>();
        try (Connection conn = connectionProvider.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Booking booking = mapRow(rs);
                booking.setSeats(findBookingSeats(booking.getBookingId()));
                bookings.add(booking);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all bookings", e);
        }
        return bookings;
    }

    public void cancel(Long bookingId) {
        String sql = "UPDATE booking SET status = 'cancelled' WHERE booking_id = ?";
        try (Connection conn = connectionProvider.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, bookingId.longValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to cancel booking", e);
        }
    }

    private List<BookingSeat> findBookingSeats(Long bookingId) {
        String sql = "SELECT * FROM booking_seat WHERE booking_id = ?";
        List<BookingSeat> seats = new ArrayList<>();
        try (Connection conn = connectionProvider.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, bookingId.longValue());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    seats.add(
                            new BookingSeat(
                                    Long.valueOf(rs.getLong("booking_id")),
                                    Long.valueOf(rs.getLong("seat_id")),
                                    rs.getDouble("price")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find booking seats", e);
        }
        return seats;
    }

    private static Booking mapRow(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setBookingId(Long.valueOf(rs.getLong("booking_id")));
        booking.setUserId(Long.valueOf(rs.getLong("user_id")));
        booking.setShowId(Long.valueOf(rs.getLong("show_id")));
        booking.setMovieName(rs.getString("movie_name"));
        booking.setBookingStatus(BookingStatus.fromDbValue(rs.getString("status")));
        booking.setAmount(rs.getDouble("total_amount"));
        Timestamp ts = rs.getTimestamp("booking_date");
        if (ts != null) {
            booking.setBookingDate(ts.toLocalDateTime());
        }
        return booking;
    }
}
