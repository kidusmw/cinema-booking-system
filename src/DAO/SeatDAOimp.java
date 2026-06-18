package DAO;

import Database.Sqlserverdatabaseconnection;
import Model.Seat;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeatDAOimp implements SeatDAO {
    @Override
    public boolean addSeat(Seat seat) {
        // Note: We do NOT insert Seat_ID because it is IDENTITY (auto-increment)
        String sql = "INSERT INTO Seat (Seat_number, Seat_Type, Status, Movie_hall_ID) VALUES (?, ?, ?, ?)";

        try (Connection con = Sqlserverdatabaseconnection.getConnection(); // Adjust this line to your connection method
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, seat.getSeatNumber());
            pstmt.setString(2, seat.getSeatType());
            pstmt.setString(3, seat.getStatus());
            pstmt.setInt(4, seat.getMovieHallID());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            System.err.println("DB ERROR in addSeat: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public boolean updateSeat(Seat seat) {
        String sql = "UPDATE Seat SET Seat_number=?, Seat_Type=?, Status=?, Movie_hall_ID=? WHERE Seat_ID=?";
        try (Connection con = Sqlserverdatabaseconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, seat.getSeatNumber());
            ps.setString(2, seat.getSeatType());
            ps.setString(3, seat.getStatus());
            ps.setInt(4, seat.getMovieHallID());
            ps.setInt(5, Integer.parseInt(seat.getSeatID()));

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public boolean deleteSeat(String seatID) {
        String sql = "DELETE FROM Seat WHERE Seat_ID=?";
        try (Connection con = Sqlserverdatabaseconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(seatID));
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public Seat searchSeatById(String seatID) {
        String sql = "SELECT * FROM Seat WHERE Seat_ID=?";
        try (Connection con = Sqlserverdatabaseconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(seatID));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSeat(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public List<Seat> getSeatsByHall(String hallID) {
        List<Seat> list = new ArrayList<>();
        String sql = "SELECT * FROM Seat WHERE Movie_hall_ID = ?";

        try (Connection conn = Sqlserverdatabaseconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, hallID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Seat s = new Seat();
                s.setSeatID(rs.getString("Seat_ID"));
                s.setSeatNumber(rs.getString("Seat_number"));
                s.setSeatType(rs.getString("Seat_Type"));
                s.setStatus(rs.getString("Status"));
                s.setMovieHallID(rs.getInt("Movie_hall_ID"));
                list.add(s);
            }
        } catch (Exception e) {
            System.err.println("DB ERROR in getSeatsByHall: " + e.getMessage());
        }
        return list;
    }
    @Override
    public boolean updateSeatStatus(String seatID, String status) {
        String sql = "UPDATE Seat SET Status=? WHERE Seat_ID=?";
        try (Connection con = Sqlserverdatabaseconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, Integer.parseInt(seatID));
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public boolean isSeatAvailable(String seatID) {
        Seat seat = searchSeatById(seatID);
        return seat != null && "AVAILABLE".equalsIgnoreCase(seat.getStatus());
    }
    @Override
    public List<Seat> getSeatsByShow(String showID) {
        return new ArrayList<>();
    }
    @Override
    public List<Seat> getAvailableSeatsByHall(String movieHallID) {
        List<Seat> list = new ArrayList<>();
        String sql = "SELECT * FROM Seat WHERE Movie_hall_ID=? AND Status='AVAILABLE' ORDER BY Seat_number";
        try (Connection con = Sqlserverdatabaseconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, movieHallID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToSeat(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    @Override
    public boolean isSeatBookedForShow(int seatID, String showID) {
        String sql = "SELECT COUNT(*) AS cnt FROM Booking WHERE SeatID=? AND ShowID=? AND Status='CONFIRMED'";
        try (Connection con = Sqlserverdatabaseconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, seatID);
            ps.setString(2, showID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cnt") > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public List<Seat> getAllSeats() {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT * FROM Seat";

        try (Connection conn =Sqlserverdatabaseconnection.getConnection() ;
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Seat seat = new Seat();
                seat.setSeatID(rs.getString("Seat_ID"));
                seat.setSeatNumber(rs.getString("Seat_number"));
                seat.setSeatType(rs.getString("Seat_Type"));
                seat.setStatus(rs.getString("Status"));
                seat.setMovieHallID(rs.getInt("Movie_hall_ID"));

                seats.add(seat);
            }
        } catch (Exception e) {
            System.err.println("DB ERROR in getAllSeats: " + e.getMessage());
            e.printStackTrace();
        }
        return seats;
    }
    private Seat mapResultSetToSeat(ResultSet rs) throws SQLException {
        Seat seat = new Seat();
        seat.setSeatID(String.valueOf(rs.getInt("Seat_ID")));
        seat.setSeatNumber(rs.getString("Seat_number"));
        seat.setSeatType(rs.getString("Seat_Type"));
        seat.setStatus(rs.getString("Status"));
        seat.setMovieHallID(rs.getInt("Movie_hall_ID"));
        return seat;
    }
}
