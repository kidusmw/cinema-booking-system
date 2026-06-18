package DAO;

import Database.Sqlserverdatabaseconnection;
import Model.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDAOimp implements MovieDAO {
    private Connection getConnection() throws SQLException {
        return Sqlserverdatabaseconnection.getConnection();
    }

    private Movie mapResultSetToMovie(ResultSet rs) throws SQLException {
        Movie movie = new Movie();
        movie.setMovieID(String.valueOf(rs.getInt("Movie_ID")));
        movie.setTitle(rs.getString("Title"));
        movie.setGenre(rs.getString("Genre"));
        movie.setDuration(rs.getInt("Duration"));
        movie.setRating(rs.getDouble("Rating"));
        movie.setPosterPath(rs.getString("Poster_Path"));
        movie.setLanguage(rs.getString("M_language"));
        movie.setReleaseDate(rs.getDate("releaseDate"));
        movie.setDescription(rs.getString("M_description"));
        return movie;
    }

    @Override
    public boolean addMovie(Movie movie) {
        String query = "INSERT INTO Movie_ (Title, Genre, Duration, Rating, Poster_Path, M_language, releaseDate, M_description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, movie.getTitle());
            ps.setString(2, movie.getGenre());
            ps.setInt(3, movie.getDuration());
            ps.setDouble(4, movie.getRating());
            ps.setString(5, movie.getPosterPath());
            ps.setString(6, movie.getLanguage());
            ps.setDate(7, movie.getReleaseDate() != null ? new java.sql.Date(movie.getReleaseDate().getTime()) : null);
            ps.setString(8, movie.getDescription());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateMovie(Movie movie) {
        String query = "UPDATE Movie_ SET Genre = ?, Duration = ?, Rating = ?, Poster_Path = ?, M_language = ?, releaseDate = ?, M_description = ? WHERE Title = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, movie.getGenre());
            ps.setInt(2, movie.getDuration());
            ps.setDouble(3, movie.getRating());
            ps.setString(4, movie.getPosterPath());
            ps.setString(5, movie.getLanguage());
            ps.setDate(6, movie.getReleaseDate() != null ? new java.sql.Date(movie.getReleaseDate().getTime()) : null);
            ps.setString(7, movie.getDescription());
            ps.setString(8, movie.getTitle());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteMovieByName(String title) {
        String query = "DELETE FROM Movie_ WHERE Title = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, title);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        String query = "SELECT * FROM Movie_";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                movies.add(mapResultSetToMovie(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }

    @Override
    public List<Movie> searchByTitle(String title) {
        List<Movie> movies = new ArrayList<>();
        String query = "SELECT * FROM Movie_ WHERE Title LIKE ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, "%" + title + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    movies.add(mapResultSetToMovie(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }
    @Override
    public List<Movie> getMoviesByLanguage(String language) {
        List<Movie> movies = new ArrayList<>();
        String query = "SELECT * FROM Movie_ WHERE M_language = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, language);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) movies.add(mapResultSetToMovie(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return movies;
    }

    @Override
    public List<Movie> getMoviesAfterDate(java.util.Date date) {
        List<Movie> movies = new ArrayList<>();
        String query = "SELECT * FROM Movie_ WHERE releaseDate > ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setDate(1, new java.sql.Date(date.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) movies.add(mapResultSetToMovie(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return movies;
    }

    @Override
    public List<Movie> getMoviesBeforeDate(java.util.Date date) {
        List<Movie> movies = new ArrayList<>();
        String query = "SELECT * FROM Movie_ WHERE releaseDate < ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setDate(1, new java.sql.Date(date.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) movies.add(mapResultSetToMovie(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return movies;
    }

    @Override
    public List<Movie> getMoviesWithPosters() {
        List<Movie> movies = new ArrayList<>();
        String query = "SELECT * FROM Movie_ WHERE Poster_Path IS NOT NULL AND Poster_Path <> ''";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) movies.add(mapResultSetToMovie(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return movies;
    }

    @Override
    public List<Movie> getMoviesByGenre(String genre) {
        List<Movie> movies = new ArrayList<>();
        String query = "SELECT * FROM Movie_ WHERE Genre = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, genre);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) movies.add(mapResultSetToMovie(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return movies;
    }

    @Override
    public List<Movie> getTopRatedMovies(int limit) {
        List<Movie> movies = new ArrayList<>();
        String query = "SELECT TOP (?) * FROM Movie_ ORDER BY Rating DESC";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) movies.add(mapResultSetToMovie(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return movies;
    }

    @Override
    public boolean movieExists(String movieID) {
        String query = "SELECT 1 FROM Movie_ WHERE Movie_ID = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, Integer.parseInt(movieID.trim()));
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (Exception e) { return false; }
    }}