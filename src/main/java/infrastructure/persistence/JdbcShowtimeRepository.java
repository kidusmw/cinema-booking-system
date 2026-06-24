package infrastructure.persistence;

import domain.model.Showtime;
import domain.port.ShowtimeRepository;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcShowtimeRepository implements ShowtimeRepository {
    private final ConnectionProvider connectionProvider;

    public JdbcShowtimeRepository(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    @Override
    public List<Showtime> findByMovieId(Long movieId) {
        String sql =
                "SELECT s.*, m.title AS movie_name, h.name AS hall_name "
                        + "FROM showtime s "
                        + "JOIN movie m ON s.movie_id = m.movie_id "
                        + "JOIN hall h ON s.hall_id = h.hall_id "
                        + "WHERE s.movie_id = ? ORDER BY s.show_date, s.show_time";
        List<Showtime> list = new ArrayList<>();
        try (Connection conn = connectionProvider.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, movieId.longValue());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find showtimes by movie id", e);
        }
        return list;
    }

    @Override
    public Optional<Showtime> findById(Long id) {
        String sql =
                "SELECT s.*, m.title AS movie_name, h.name AS hall_name "
                        + "FROM showtime s "
                        + "JOIN movie m ON s.movie_id = m.movie_id "
                        + "JOIN hall h ON s.hall_id = h.hall_id "
                        + "WHERE s.show_id = ?";
        try (Connection conn = connectionProvider.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id.longValue());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find showtime by id", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Showtime> findByDate(LocalDate date) {
        String sql =
                "SELECT s.*, m.title AS movie_name, h.name AS hall_name "
                        + "FROM showtime s "
                        + "JOIN movie m ON s.movie_id = m.movie_id "
                        + "JOIN hall h ON s.hall_id = h.hall_id "
                        + "WHERE s.show_date = ? ORDER BY s.show_time";
        List<Showtime> list = new ArrayList<>();
        try (Connection conn = connectionProvider.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find showtimes by date", e);
        }
        return list;
    }

    @Override
    public List<Showtime> findAll() {
        String sql =
                "SELECT s.*, m.title AS movie_name, h.name AS hall_name "
                        + "FROM showtime s "
                        + "JOIN movie m ON s.movie_id = m.movie_id "
                        + "JOIN hall h ON s.hall_id = h.hall_id "
                        + "ORDER BY s.show_date, s.show_time";
        List<Showtime> list = new ArrayList<>();
        try (Connection conn = connectionProvider.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all showtimes", e);
        }
        return list;
    }

    @Override
    public Showtime save(Showtime showtime) {
        if (showtime.getShowId() == null) {
            return insert(showtime);
        } else {
            return update(showtime);
        }
    }

    private Showtime insert(Showtime showtime) {
        String sql =
                "INSERT INTO showtime (movie_id, hall_id, show_date, show_time, ticket_price) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connectionProvider.getConnection();
                PreparedStatement ps =
                        conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, showtime.getMovieId().longValue());
            ps.setLong(2, showtime.getHallId().longValue());
            ps.setDate(3, Date.valueOf(showtime.getShowDate()));
            ps.setTime(4, Time.valueOf(showtime.getShowTime()));
            ps.setDouble(5, showtime.getTicketPrice());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) showtime.setShowId(Long.valueOf(keys.getLong(1)));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert showtime", e);
        }
        return showtime;
    }

    private Showtime update(Showtime showtime) {
        String sql =
                "UPDATE showtime SET movie_id=?, hall_id=?, show_date=?, show_time=?, ticket_price=? WHERE show_id=?";
        try (Connection conn = connectionProvider.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, showtime.getMovieId().longValue());
            ps.setLong(2, showtime.getHallId().longValue());
            ps.setDate(3, Date.valueOf(showtime.getShowDate()));
            ps.setTime(4, Time.valueOf(showtime.getShowTime()));
            ps.setDouble(5, showtime.getTicketPrice());
            ps.setLong(6, showtime.getShowId().longValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update showtime", e);
        }
        return showtime;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM showtime WHERE show_id=?";
        try (Connection conn = connectionProvider.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id.longValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete showtime", e);
        }
    }

    private static Showtime mapRow(ResultSet rs) throws SQLException {
        Showtime showtime =
                new Showtime(
                        Long.valueOf(rs.getLong("show_id")),
                        Long.valueOf(rs.getLong("movie_id")),
                        Long.valueOf(rs.getLong("hall_id")),
                        rs.getDate("show_date").toLocalDate(),
                        rs.getTime("show_time").toLocalTime(),
                        rs.getDouble("ticket_price"));
        showtime.setMovieName(rs.getString("movie_name"));
        showtime.setHallName(rs.getString("hall_name"));
        return showtime;
    }
}
