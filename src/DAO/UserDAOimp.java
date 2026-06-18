package DAO;

import Model.Admin;
import Model.Customer;
import Model.User;
import Database.Sqlserverdatabaseconnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOimp implements UserDAO {
    @Override
    public boolean addUser(User user) {
        String sql = "INSERT INTO User_ (First_N, Last_N, Email, Phone, user_Role, Username, user_Password) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Sqlserverdatabaseconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getRole());
            ps.setString(6, user.getUsername());
            ps.setString(7, user.getPassword());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setUserID(generatedKeys.getInt(1));
                    }
                }
            }

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public boolean updateUser(User user) {
        String sql = "UPDATE User_ SET First_N=?, Last_N=?, Email=?, Phone=?, user_Role=?, " +
                "Username=?, user_Password=? WHERE User_ID=?";

        try (Connection con = Sqlserverdatabaseconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getRole());
            ps.setString(6, user.getUsername());
            ps.setString(7, user.getPassword());
            ps.setInt(8, user.getUserID());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public boolean deleteUser(int userID) {
        String sql = "DELETE FROM User_ WHERE User_ID=?";

        try (Connection con = Sqlserverdatabaseconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userID);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public User searchUserByUsername(String username) {
        String sql = "SELECT * FROM User_ WHERE Username=?";

        try (Connection con = Sqlserverdatabaseconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public boolean isUsernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM User_ WHERE Username=?";

        try (Connection con = Sqlserverdatabaseconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public User login(String username, String password) {
        String sql = "SELECT * FROM User_ WHERE Username=? AND user_Password=?";

        try (Connection con = Sqlserverdatabaseconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM User_";

        try (Connection con = Sqlserverdatabaseconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        String role = rs.getString("user_Role");
        User user = "ADMIN".equalsIgnoreCase(role) ? new Admin() : new Customer();

        user.setUserID(rs.getInt("User_ID"));
        user.setFirstName(rs.getString("First_N"));
        user.setLastName(rs.getString("Last_N"));
        user.setEmail(rs.getString("Email"));
        user.setPhone(rs.getString("Phone"));
        user.setUsername(rs.getString("Username"));
        user.setPassword(rs.getString("user_Password"));
        user.setRole(role);

        return user;
    }
}
