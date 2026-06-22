package DAO;

import Database.DatabaseConnection;
import Model.Booking;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookingDAO {

    private static final Logger logger = LoggerFactory.getLogger(
        BookingDAO.class
    );

    public boolean addBooking(Booking booking) {
        int parsedShowID = 0;
        if (
            booking.getShowID() != null && !booking.getShowID().trim().isEmpty()
        ) {
            try {
                parsedShowID = Integer.parseInt(booking.getShowID().trim());
            } catch (NumberFormatException e) {
                logger.warn(
                    "Invalid show_id format for booking: '{}'",
                    booking.getShowID()
                );
                return false;
            }
        }

        String sql =
            "INSERT INTO booking(booking_date, user_id, show_id, movie_name, status) " +
            "VALUES(CURRENT_DATE, ?, ?, ?, ?)";

        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                sql,
                Statement.RETURN_GENERATED_KEYS
            )
        ) {
            ps.setInt(1, booking.getUserID());
            ps.setInt(2, parsedShowID);
            ps.setString(3, booking.getMovieName());
            ps.setString(
                4,
                booking.getBookingStatus() != null
                    ? booking.getBookingStatus()
                    : "confirmed"
            );

            logger.debug(
                "Inserting booking - userId={}, showId={}, movie={}",
                booking.getUserID(),
                parsedShowID,
                booking.getMovieName()
            );

            int rows = ps.executeUpdate();

            if (rows > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        booking.setBookingID(keys.getInt(1));
                    }
                }
                logger.debug(
                    "Booking saved with ID: {}",
                    booking.getBookingID()
                );
            }
            return rows > 0;
        } catch (SQLException e) {
            logger.error(
                "Failed to add booking for user ID {}",
                booking.getUserID(),
                e
            );
            return false;
        }
    }

    public List<Booking> getBookingsByUserId(int userId) {
        List<Booking> historyList = new ArrayList<>();
        String sql =
            "SELECT booking_id, booking_date, show_id, movie_name, status " +
            "FROM booking WHERE user_id = ? ORDER BY booking_date DESC";

        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Booking b = new Booking();
                    b.setBookingID(rs.getInt("booking_id"));

                    java.sql.Timestamp dbTimestamp = rs.getTimestamp(
                        "booking_date"
                    );
                    if (dbTimestamp != null) {
                        b.setBookingDate(
                            new java.util.Date(dbTimestamp.getTime())
                        );
                    } else {
                        b.setBookingDate(null);
                    }

                    b.setShowID(String.valueOf(rs.getInt("show_id")));
                    b.setMovieName(rs.getString("movie_name"));
                    b.setBookingStatus(rs.getString("status"));

                    historyList.add(b);
                }
            }
        } catch (SQLException e) {
            logger.error(
                "Failed to fetch booking history for user ID {}",
                userId,
                e
            );
        }
        return historyList;
    }

    public boolean updateBooking(Booking booking) {
        String sql =
            "UPDATE booking SET user_id=?, show_id=?, movie_name=?, status=? WHERE booking_id=?";
        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, booking.getUserID());
            int parsedShowID = 0;
            if (
                booking.getShowID() != null &&
                !booking.getShowID().trim().isEmpty()
            ) {
                parsedShowID = Integer.parseInt(booking.getShowID().trim());
            }
            ps.setInt(2, parsedShowID);

            ps.setString(3, booking.getMovieName());
            ps.setString(4, booking.getBookingStatus());
            ps.setInt(5, booking.getBookingID());

            return ps.executeUpdate() > 0;
        } catch (SQLException | NumberFormatException e) {
            logger.error(
                "Failed to update booking ID {}",
                booking.getBookingID(),
                e
            );
            return false;
        }
    }

    public boolean deleteBooking(int bookingID) {
        String sql = "DELETE FROM booking WHERE booking_id=?";
        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, bookingID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Failed to delete booking ID {}", bookingID, e);
            return false;
        }
    }

    public Booking searchBookingById(int bookingID) {
        String sql =
            "SELECT b.booking_id, b.booking_date, b.user_id, b.show_id, b.movie_name, b.status, u.username " +
            "FROM booking b " +
            "LEFT JOIN \"user\" u ON b.user_id = u.user_id " +
            "WHERE b.booking_id=?";
        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, bookingID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to search booking by ID {}", bookingID, e);
        }
        return null;
    }

    public List<Booking> getAllBookings() {
        List<Booking> list = new ArrayList<>();
        String sql =
            "SELECT b.booking_id, b.booking_date, b.user_id, b.show_id, b.movie_name, b.status, u.username " +
            "FROM booking b " +
            "LEFT JOIN \"user\" u ON b.user_id = u.user_id " +
            "ORDER BY b.booking_id DESC";
        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            logger.debug("Loaded {} bookings from DB", list.size());
        } catch (SQLException e) {
            logger.error("Failed to fetch all bookings", e);
        }
        return list;
    }

    public boolean cancelBooking(int bookingID) {
        String sql =
            "UPDATE booking SET status='cancelled' WHERE booking_id=?";
        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, bookingID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Failed to cancel booking ID {}", bookingID, e);
            return false;
        }
    }

    private Booking mapRow(ResultSet rs) throws SQLException {
        Booking b = new Booking();

        b.setBookingID(rs.getInt("booking_id"));

        java.sql.Timestamp ts = rs.getTimestamp("booking_date");
        if (ts != null) {
            b.setBookingDate(new java.util.Date(ts.getTime()));
        }

        b.setUserID(rs.getInt("user_id"));
        b.setShowID(String.valueOf(rs.getInt("show_id")));
        b.setMovieName(rs.getString("movie_name"));
        b.setBookingStatus(rs.getString("status"));
        b.setCustomerName(rs.getString("username"));

        return b;
    }
}
