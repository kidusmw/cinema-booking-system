package DAO;

import Database.Sqlserverdatabaseconnection;
import Model.Moviehall;
import javafx.collections.FXCollections;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MoviehallDAOimp implements MovieHallDAO {
    @Override
    public boolean addMovieHall(Moviehall hall) {
        String sql = "INSERT INTO Movie_Hall_ (Name, Capacity) VALUES (?, ?)";
        try (Connection conn = Sqlserverdatabaseconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, hall.getName());
            ps.setInt(2, hall.getCapacity());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Add Hall Error: " + e.getMessage());
            return false;
        }
    }
    @Override
    public boolean updateMovieHall(Moviehall hall) {
        String sql = "UPDATE Movie_Hall_ SET Name=?, Capacity=? WHERE Movie_hall_ID=?";
        try (Connection conn = Sqlserverdatabaseconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, hall.getName());
            ps.setInt(2, hall.getCapacity());
            ps.setString(3, hall.getId());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Update Hall Error: " + e.getMessage());
            return false;
        }
    }
    @Override
    public boolean deleteMovieHall(String id) {
        String sql = "DELETE FROM Movie_Hall_ WHERE Movie_hall_ID=?";
        try (Connection conn = Sqlserverdatabaseconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Delete Hall Error: " + e.getMessage());
            return false;
        }
    }
    @Override
    public Moviehall searchMovieHallById(String id) {
        String sql = "SELECT * FROM Movie_Hall_ WHERE Movie_hall_ID=?";
        try (Connection conn = Sqlserverdatabaseconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

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
            System.out.println("Search Hall Error: " + e.getMessage());
        }
        return null;
    }
    @Override
    public List<Moviehall> getAllMovieHalls() {
        List<Moviehall> list = new ArrayList<>();
        String sql = "SELECT Movie_hall_ID, Name, Capacity FROM Movie_Hall_";

        try (Connection conn = Sqlserverdatabaseconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Moviehall hall = new Moviehall(
                        String.valueOf(rs.getInt("Movie_hall_ID")),
                        rs.getString("Name"),
                        rs.getInt("Capacity")
                );
                list.add(hall);
            }

        } catch (Exception e) {

            System.err.println("DAO ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
    @Override public List<Moviehall> searchMovieHallByName(String hallName) { return List.of(); }
    @Override public List<Moviehall> getMovieHallsByCapacity(int capacity) { return List.of(); }
    @Override public List<Moviehall> getMovieHallsByMinCapacity(int min, int max) { return List.of(); }
    @Override public boolean isHallAvailable(String id) { return false; }
    @Override public int getTotalHalls() { return 0; }
}