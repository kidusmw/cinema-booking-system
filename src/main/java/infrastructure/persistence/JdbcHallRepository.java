package infrastructure.persistence;

import domain.model.Hall;
import domain.port.HallRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcHallRepository implements HallRepository {
    private final ConnectionProvider connectionProvider;

    public JdbcHallRepository(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    @Override
    public Optional<Hall> findById(Long id) {
        String sql = "SELECT * FROM hall WHERE hall_id = ?";
        try (Connection conn = connectionProvider.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id.longValue());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find hall by id", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Hall> findAll() {
        String sql = "SELECT * FROM hall ORDER BY hall_id";
        List<Hall> halls = new ArrayList<>();
        try (Connection conn = connectionProvider.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) halls.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all halls", e);
        }
        return halls;
    }

    @Override
    public Hall save(Hall hall) {
        if (hall.getHallId() == null) {
            return insert(hall);
        } else {
            return update(hall);
        }
    }

    private Hall insert(Hall hall) {
        String sql = "INSERT INTO hall (name, capacity, hall_type) VALUES (?, ?, ?)";
        try (Connection conn = connectionProvider.getConnection();
                PreparedStatement ps =
                        conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, hall.getName());
            ps.setInt(2, hall.getCapacity());
            ps.setString(3, hall.getHallType());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) hall.setHallId(Long.valueOf(keys.getLong(1)));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert hall", e);
        }
        return hall;
    }

    private Hall update(Hall hall) {
        String sql = "UPDATE hall SET name=?, capacity=?, hall_type=? WHERE hall_id=?";
        try (Connection conn = connectionProvider.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hall.getName());
            ps.setInt(2, hall.getCapacity());
            ps.setString(3, hall.getHallType());
            ps.setLong(4, hall.getHallId().longValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update hall", e);
        }
        return hall;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM hall WHERE hall_id=?";
        try (Connection conn = connectionProvider.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id.longValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete hall", e);
        }
    }

    private static Hall mapRow(ResultSet rs) throws SQLException {
        return new Hall(
                Long.valueOf(rs.getLong("hall_id")),
                rs.getString("name"),
                rs.getInt("capacity"),
                rs.getString("hall_type"));
    }
}
