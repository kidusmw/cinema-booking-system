package infrastructure.persistence;

import domain.model.Movie;
import domain.port.MovieRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcMovieRepository implements MovieRepository {
    private final ConnectionProvider connectionProvider;

    public JdbcMovieRepository(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    @Override
    public Optional<Movie> findById(Long id) {
        String sql = "SELECT * FROM movie WHERE movie_id = ?";
        try (Connection conn = connectionProvider.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find movie by id", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Movie> findAll() {
        String sql = "SELECT * FROM movie ORDER BY movie_id";
        List<Movie> movies = new ArrayList<>();
        try (Connection conn = connectionProvider.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) movies.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all movies", e);
        }
        return movies;
    }

    @Override
    public List<Movie> searchByTitle(String title) {
        String sql = "SELECT * FROM movie WHERE title ILIKE ?";
        List<Movie> movies = new ArrayList<>();
        try (Connection conn = connectionProvider.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + title + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) movies.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to search movies by title", e);
        }
        return movies;
    }

    @Override
    public Movie save(Movie movie) {
        if (movie.getMovieId() == null) {
            return insert(movie);
        } else {
            return update(movie);
        }
    }

    private Movie insert(Movie movie) {
        String sql =
                "INSERT INTO movie (title, genre, duration_min, rating, description, release_date, language, poster_path) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connectionProvider.getConnection();
                PreparedStatement ps =
                        conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, movie.getTitle());
            ps.setString(2, movie.getGenre());
            ps.setInt(3, movie.getDuration());
            ps.setDouble(4, movie.getRating());
            ps.setString(5, movie.getDescription());
            ps.setDate(
                    6,
                    movie.getReleaseDate() != null
                            ? new java.sql.Date(movie.getReleaseDate().getTime())
                            : null);
            ps.setString(7, movie.getLanguage());
            ps.setString(8, movie.getPosterPath());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) movie.setMovieId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert movie", e);
        }
        return movie;
    }

    private Movie update(Movie movie) {
        String sql =
                "UPDATE movie SET title=?, genre=?, duration_min=?, rating=?, description=?, release_date=?, language=?, poster_path=? WHERE movie_id=?";
        try (Connection conn = connectionProvider.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, movie.getTitle());
            ps.setString(2, movie.getGenre());
            ps.setInt(3, movie.getDuration());
            ps.setDouble(4, movie.getRating());
            ps.setString(5, movie.getDescription());
            ps.setDate(
                    6,
                    movie.getReleaseDate() != null
                            ? new java.sql.Date(movie.getReleaseDate().getTime())
                            : null);
            ps.setString(7, movie.getLanguage());
            ps.setString(8, movie.getPosterPath());
            ps.setLong(9, movie.getMovieId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update movie", e);
        }
        return movie;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM movie WHERE movie_id=?";
        try (Connection conn = connectionProvider.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete movie", e);
        }
    }

    private Movie mapRow(ResultSet rs) throws SQLException {
        Movie movie =
                new Movie(
                        rs.getLong("movie_id"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getInt("duration_min"),
                        rs.getDouble("rating"),
                        rs.getString("description"),
                        rs.getDate("release_date"),
                        rs.getString("language"),
                        rs.getString("poster_path"));
        return movie;
    }
}
