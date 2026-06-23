package DAO;

import Database.DatabaseConnection;
import Model.Show;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShowDAOimp implements ShowDAO {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ShowDAOimp.class);

    @Override
    public boolean addShow(Show show) {
        String sql =
            "INSERT INTO Show_Table(Show_date, Show_time, Movie_ID, Movie_hall_ID) VALUES(?, ?, ?, ?)";

        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setDate(1, new java.sql.Date(show.getShowDate().getTime()));

            String timeStr = show.getShowTime();
            if (timeStr != null && timeStr.length() == 5) timeStr += ":00";
            ps.setTime(2, java.sql.Time.valueOf(timeStr));

            ps.setInt(3, Integer.parseInt(show.getMovieID()));
            ps.setInt(4, Integer.parseInt(show.getMovieHallID()));

            return ps.executeUpdate() > 0;
        } catch (SQLException | NumberFormatException e) {
            log.error("Database operation failed", e);
        }
        return false;
    }

    @Override
    public boolean updateShow(Show show) {
        String sql =
            "UPDATE Show_Table SET Show_date=?, Show_time=?, Movie_ID=?, Movie_hall_ID=? WHERE Show_ID=?";

        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setDate(1, new java.sql.Date(show.getShowDate().getTime()));

            String timeStr = show.getShowTime();
            if (timeStr != null && timeStr.length() == 5) timeStr += ":00";
            ps.setTime(2, java.sql.Time.valueOf(timeStr));

            ps.setInt(3, Integer.parseInt(show.getMovieID()));
            ps.setInt(4, Integer.parseInt(show.getMovieHallID()));
            ps.setInt(5, Integer.parseInt(show.getShowID()));

            return ps.executeUpdate() > 0;
        } catch (SQLException | NumberFormatException e) {
            log.error("Database operation failed", e);
        }
        return false;
    }

    @Override
    public boolean deleteShow(String showID) {
        String sql = "DELETE FROM Show_Table WHERE Show_ID=?";

        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, Integer.parseInt(showID));
            return ps.executeUpdate() > 0;
        } catch (SQLException | NumberFormatException e) {
            log.error("Database operation failed", e);
        }
        return false;
    }

    @Override
    public Show searchShowById(String showID) {
        String sql = "SELECT * FROM Show_Table WHERE Show_ID=?";

        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, Integer.parseInt(showID));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToShow(rs);
                }
            }
        } catch (SQLException | NumberFormatException e) {
            log.error("Database operation failed", e);
        }
        return null;
    }

    @Override
    public List<Show> getAllShows() {
        List<Show> list = new ArrayList<>();
        String sql = "SELECT * FROM Show_Table";

        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                list.add(mapResultSetToShow(rs));
            }
        } catch (SQLException e) {
            log.error("Database operation failed", e);
        }
        return list;
    }

    @Override
    public List<Show> getShowsByMovie(String movieID) {
        List<Show> list = new ArrayList<>();
        String sql =
            "SELECT * FROM Show_Table WHERE Movie_ID=? ORDER BY Show_date, Show_time";

        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, Integer.parseInt(movieID));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToShow(rs));
                }
            }
        } catch (SQLException | NumberFormatException e) {
            log.error("Database operation failed", e);
        }
        return list;
    }

    @Override
    public List<Show> getShowsByDate(Date date) {
        List<Show> list = new ArrayList<>();
        String sql =
            "SELECT * FROM Show_Table WHERE Show_date=? ORDER BY Show_time";

        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setDate(1, new java.sql.Date(date.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToShow(rs));
                }
            }
        } catch (SQLException e) {
            log.error("Database operation failed", e);
        }
        return list;
    }

    @Override
    public List<Show> getShowsByDateRange(Date startDate, Date endDate) {
        List<Show> list = new ArrayList<>();
        String sql =
            "SELECT * FROM Show_Table WHERE Show_date BETWEEN ? AND ? ORDER BY Show_date, Show_time";

        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setDate(1, new java.sql.Date(startDate.getTime()));
            ps.setDate(2, new java.sql.Date(endDate.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToShow(rs));
                }
            }
        } catch (SQLException e) {
            log.error("Database operation failed", e);
        }
        return list;
    }

    @Override
    public List<Show> getShowsByMovieAndDate(String movieID, Date date) {
        List<Show> list = new ArrayList<>();
        String sql =
            "SELECT * FROM Show_Table WHERE Movie_ID=? AND Show_date=? ORDER BY Show_time";

        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, Integer.parseInt(movieID));
            ps.setDate(2, new java.sql.Date(date.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToShow(rs));
                }
            }
        } catch (SQLException | NumberFormatException e) {
            log.error("Database operation failed", e);
        }
        return list;
    }

    @Override
    public List<Show> getShowsByHall(String movieHallID) {
        List<Show> list = new ArrayList<>();
        String sql =
            "SELECT * FROM Show_Table WHERE Movie_hall_ID=? ORDER BY Show_date, Show_time";

        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, Integer.parseInt(movieHallID));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToShow(rs));
                }
            }
        } catch (SQLException | NumberFormatException e) {
            log.error("Database operation failed", e);
        }
        return list;
    }

    @Override
    public boolean checkAvailability(String showID) {
        String sql =
            "SELECT COUNT(*) AS bookedCount " +
            "FROM Booking_Seat bs " +
            "JOIN Booking b ON bs.Booking_ID = b.Booking_ID " +
            "WHERE b.Show_ID = ? AND bs.Status = 'BOOKED'";

        try (
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, Integer.parseInt(showID));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int booked = rs.getInt("bookedCount");
                    int hallCapacity = 50; // Dynamic or fallback value
                    return booked < hallCapacity;
                }
            }
        } catch (SQLException | NumberFormatException e) {
            log.error("Database operation failed", e);
        }
        return false;
    }

    private Show mapResultSetToShow(ResultSet rs) throws SQLException {
        Show s = new Show();
        s.setShowID(String.valueOf(rs.getInt("Show_ID")));
        s.setShowDate(rs.getDate("Show_date"));

        Time dbTime = rs.getTime("Show_time");
        if (dbTime != null) {
            String timeStr = dbTime.toString();
            s.setShowTime(timeStr.substring(0, 5)); // Trims "19:30:00" -> "19:30"
        }

        s.setMovieID(String.valueOf(rs.getInt("Movie_ID")));
        s.setMovieHallID(String.valueOf(rs.getInt("Movie_hall_ID")));
        return s;
    }
}
