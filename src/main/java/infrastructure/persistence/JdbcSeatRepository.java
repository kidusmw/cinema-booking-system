package infrastructure.persistence;

import domain.model.Seat;
import domain.port.SeatRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcSeatRepository implements SeatRepository {
    private final ConnectionProvider connectionProvider;

    public JdbcSeatRepository(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    @Override
    public List<Seat> findByHallId(Long hallId) {
        String sql = "SELECT * FROM seat WHERE hall_id = ? ORDER BY seat_number";
        List<Seat> seats = new ArrayList<>();
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, hallId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) seats.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find seats by hall id", e);
        }
        return seats;
    }

    @Override
    public Optional<Seat> findById(Long id) {
        String sql = "SELECT * FROM seat WHERE seat_id = ?";
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find seat by id", e);
        }
        return Optional.empty();
    }

    @Override
    public void updateStatus(Long seatId, String status) {
        String sql = "UPDATE seat SET status = ? WHERE seat_id = ?";
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setLong(2, seatId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update seat status", e);
        }
    }

    @Override
    public Seat save(Seat seat) {
        if (seat.getSeatId() == null) {
            return insert(seat);
        } else {
            return update(seat);
        }
    }

    private Seat insert(Seat seat) {
        String sql = "INSERT INTO seat (hall_id, seat_number, seat_type, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, seat.getHallId());
            ps.setString(2, seat.getSeatNumber());
            ps.setString(3, seat.getSeatType());
            ps.setString(4, seat.getStatus());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) seat.setSeatId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert seat", e);
        }
        return seat;
    }

    private Seat update(Seat seat) {
        String sql = "UPDATE seat SET hall_id=?, seat_number=?, seat_type=?, status=? WHERE seat_id=?";
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, seat.getHallId());
            ps.setString(2, seat.getSeatNumber());
            ps.setString(3, seat.getSeatType());
            ps.setString(4, seat.getStatus());
            ps.setLong(5, seat.getSeatId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update seat", e);
        }
        return seat;
    }

    private Seat mapRow(ResultSet rs) throws SQLException {
        return new Seat(
                rs.getLong("seat_id"),
                rs.getLong("hall_id"),
                rs.getString("seat_number"),
                rs.getString("seat_type"),
                rs.getString("status")
        );
    }
}
