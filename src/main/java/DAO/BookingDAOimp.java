package DAO;

import Database.Sqlserverdatabaseconnection;
import Model.Booking;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAOimp implements BookingDAO {
    @Override
    public boolean addBooking(Booking booking) {
        int parsedShowID = 0;
        if (booking.getShowID() != null && !booking.getShowID().trim().isEmpty()) {
            try {
                parsedShowID = Integer.parseInt(booking.getShowID().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ Critical Type Mismatch: Show_ID string '" + booking.getShowID() + "' is not a number!");
                return false;
            }
        }

        String sql = "INSERT INTO Booking(Booking_date, User_ID, Show_ID, Movie_name, BookingStatus) " +
                "VALUES(GETDATE(), ?, ?, ?, ?)";

        try (Connection con = Sqlserverdatabaseconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, booking.getUserID());
            ps.setInt(2, parsedShowID);
            ps.setString(3, booking.getMovieName());
            ps.setString(4, booking.getBookingStatus() != null ? booking.getBookingStatus() : "CONFIRMED");

            // 🚨 CRITICAL PRINT STATEMENT: Look at your console for this block!
            System.out.println("\n📢 [SQL SENT] Attempting to insert into Booking table:");
            System.out.println("   -> User_ID:   " + booking.getUserID());
            System.out.println("   -> Show_ID:   " + parsedShowID + " <--- LOOK AT THIS NUMBER!");
            System.out.println("   -> Movie:     " + booking.getMovieName());
            System.out.println("--------------------------------------------------\n");

            int rows = ps.executeUpdate();

            if (rows > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        booking.setBookingID(keys.getInt(1));
                    }
                }
                System.out.println("✅ Booking saved cleanly! ID: " + booking.getBookingID());
            }
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("❌ Database Core Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public List<Booking> getBookingsByUserId(int userId) {
        List<Booking> historyList = new ArrayList<>();
        String sql = "SELECT Booking_ID, Booking_date, Show_ID, Movie_name, BookingStatus " +
                "FROM Booking WHERE User_ID = ? ORDER BY Booking_date DESC";

        try (Connection con = Sqlserverdatabaseconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Booking b = new Booking();
                    b.setBookingID(rs.getInt("Booking_ID"));

                    // 🛠️ OPTION 2: Convert SQL Timestamp to java.util.Date
                    java.sql.Timestamp dbTimestamp = rs.getTimestamp("Booking_date");
                    if (dbTimestamp != null) {
                        // This creates a new Date object based on the milliseconds from the timestamp
                        b.setBookingDate(new java.util.Date(dbTimestamp.getTime()));
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
            System.out.println("❌ Error fetching booking history: " + e.getMessage());
            e.printStackTrace();
        }
        return historyList;
    }
    @Override
    public boolean updateBooking(Booking booking) {
        String sql = "UPDATE Booking SET User_ID=?, Show_ID=?, Movie_name=?, BookingStatus=? WHERE Booking_ID=?";
        try (Connection con = Sqlserverdatabaseconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, booking.getUserID());
            int parsedShowID = 0;
            if (booking.getShowID() != null && !booking.getShowID().trim().isEmpty()) {
                parsedShowID = Integer.parseInt(booking.getShowID().trim());
            }
            ps.setInt(2, parsedShowID);

            ps.setString(3, booking.getMovieName());
            ps.setString(4, booking.getBookingStatus());
            ps.setInt(5, booking.getBookingID());

            return ps.executeUpdate() > 0;

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteBooking(int bookingID) {
        String sql = "DELETE FROM Booking WHERE Booking_ID=?";
        try (Connection con = Sqlserverdatabaseconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, bookingID);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Booking searchBookingById(int bookingID) {
        String sql = "SELECT b.Booking_ID, b.Booking_date, b.User_ID, b.Show_ID, b.Movie_name, b.BookingStatus, u.Username " +
                "FROM Booking b " +
                "LEFT JOIN Users u ON b.User_ID = u.User_ID " +
                "WHERE b.Booking_ID=?";
        try (Connection con = Sqlserverdatabaseconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, bookingID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Booking> getAllBookings() {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT b.Booking_ID, b.Booking_date, b.User_ID, b.Show_ID, b.Movie_name, b.BookingStatus, u.Username " +
                "FROM Booking b " +
                "LEFT JOIN User_ u ON b.User_ID = u.User_ID " +
                "ORDER BY b.Booking_ID DESC";
        try (Connection con = Sqlserverdatabaseconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
            System.out.println("✅ Loaded " + list.size() + " bookings from DB");

        } catch (SQLException e) {
            System.out.println("❌ Get All Bookings Error: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean cancelBooking(int bookingID) {
        String sql = "UPDATE Booking SET BookingStatus='CANCELLED' WHERE Booking_ID=?";
        try (Connection con = Sqlserverdatabaseconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, bookingID);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    private Booking mapRow(ResultSet rs) throws SQLException {
        Booking b = new Booking();

        b.setBookingID(rs.getInt("Booking_ID"));

        // 💡 FIX: Use getTimestamp to be consistent with the database schema
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