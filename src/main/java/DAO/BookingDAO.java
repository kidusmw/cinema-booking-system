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
                    "Invalid Show_ID format for booking: '{}'",
                    booking.getShowID()
                );
                return false;
            }
        }

        String sql =
            "INSERT INTO Booking(Booking_date, User_ID, Show_ID, Movie_name, BookingStatus) " +
            "VALUES(GETDATE(), ?, ?, ?, ?)";

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
                    : "CONFIRMED"
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
            "SELECT Booking_ID, Booking_date, Show_ID, Movie_name, BookingStatus " +
            "FROM Booking WHERE User_ID = ? ORDER BY Booking_date DESC";

        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Booking b = new Booking();
                    b.setBookingID(rs.getInt("Booking_ID"));

                    java.sql.Timestamp dbTimestamp = rs.getTimestamp(
                        "Booking_date"
                    );
                    if (dbTimestamp != null) {
                        b.setBookingDate(
                            new java.util.Date(dbTimestamp.getTime())
                        );
                    } else {
                        b.setBookingDate(null);
                    }

                    b.setShowID(String.valueOf(rs.getInt("Show_ID")));
                    b.setMovieName(rs.getString("Movie_name"));
                    b.setBookingStatus(rs.getString("BookingStatus"));

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
            "UPDATE Booking SET User_ID=?, Show_ID=?, Movie_name=?, BookingStatus=? WHERE Booking_ID=?";
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
        String sql = "DELETE FROM Booking WHERE Booking_ID=?";
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
        // NOTE: this query references a "Users" table that does not exist in
        // script.sql (the real table is "User_") - this almost certainly
        // throws every time it's called. Left as-is for now; revisit when
        // script.sql itself gets reviewed.
        String sql =
            "SELECT b.Booking_ID, b.Booking_date, b.User_ID, b.Show_ID, b.Movie_name, b.BookingStatus, u.Username " +
            "FROM Booking b " +
            "LEFT JOIN Users u ON b.User_ID = u.User_ID " +
            "WHERE b.Booking_ID=?";
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
            "SELECT b.Booking_ID, b.Booking_date, b.User_ID, b.Show_ID, b.Movie_name, b.BookingStatus, u.Username " +
            "FROM Booking b " +
            "LEFT JOIN User_ u ON b.User_ID = u.User_ID " +
            "ORDER BY b.Booking_ID DESC";
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
            "UPDATE Booking SET BookingStatus='CANCELLED' WHERE Booking_ID=?";
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

        b.setBookingID(rs.getInt("Booking_ID"));

        java.sql.Timestamp ts = rs.getTimestamp("Booking_date");
        if (ts != null) {
            b.setBookingDate(new java.util.Date(ts.getTime()));
        }

        b.setUserID(rs.getInt("User_ID"));
        b.setShowID(String.valueOf(rs.getInt("Show_ID")));
        b.setMovieName(rs.getString("Movie_name"));
        b.setBookingStatus(rs.getString("BookingStatus"));
        b.setCustomerName(rs.getString("Username"));

        return b;
    }
}
