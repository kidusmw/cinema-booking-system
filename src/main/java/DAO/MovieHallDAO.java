package DAO;

import Database.DatabaseConnection;
import Model.Moviehall;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MovieHallDAO {

    private static final Logger logger = LoggerFactory.getLogger(
        MovieHallDAO.class
    );

    public boolean addMovieHall(Moviehall hall) {
        String sql = "INSERT INTO Movie_Hall_ (Name, Capacity) VALUES (?, ?)";
        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, hall.getName());
            ps.setInt(2, hall.getCapacity());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            logger.error("Failed to add movie hall: {}", hall.getName(), e);
            return false;
        }
    }

    public boolean updateMovieHall(Moviehall hall) {
        String sql =
            "UPDATE Movie_Hall_ SET Name=?, Capacity=? WHERE Movie_hall_ID=?";
        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, hall.getName());
            ps.setInt(2, hall.getCapacity());
            ps.setString(3, hall.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            logger.error("Failed to update movie hall ID: {}", hall.getId(), e);
            return false;
        }
    }

    public boolean deleteMovieHall(String id) {
        String sql = "DELETE FROM Movie_Hall_ WHERE Movie_hall_ID=?";
        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            logger.error("Failed to delete movie hall ID: {}", id, e);
            return false;
        }
    }

    public Moviehall searchMovieHallById(String id) {
        String sql = "SELECT * FROM Movie_Hall_ WHERE Movie_hall_ID=?";
        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Moviehall(
                        rs.getString("Movie_hall_ID"),
                        rs.getString("Name"),
                        rs.getInt("Capacity")
                    );
                }
            }
        } catch (Exception e) {
            logger.error("Failed to search movie hall by ID: {}", id, e);
        }
        return null;
    }

    public List<Moviehall> getAllMovieHalls() {
        List<Moviehall> list = new ArrayList<>();
        String sql = "SELECT Movie_hall_ID, Name, Capacity FROM Movie_Hall_";

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                Moviehall hall = new Moviehall(
                    String.valueOf(rs.getInt("Movie_hall_ID")),
                    rs.getString("Name"),
                    rs.getInt("Capacity")
                );
                list.add(hall);
            }
        } catch (Exception e) {
            logger.error("Failed to fetch all movie halls", e);
        }
        return list;
    }

    // Not yet implemented - stubs carried over from the original interface
    public List<Moviehall> searchMovieHallByName(String hallName) {
        return List.of();
    }

    public List<Moviehall> getMovieHallsByCapacity(int capacity) {
        return List.of();
    }

    public List<Moviehall> getMovieHallsByMinCapacity(int min, int max) {
        return List.of();
    }

    public boolean isHallAvailable(String id) {
        return false;
    }

    public int getTotalHalls() {
        return 0;
    }
}
