package DAO;

import Model.Movie;
import java.util.List;

public interface MovieDAO {
    boolean addMovie(Movie movie);

    boolean updateMovie(Movie movie);

    boolean deleteMovieByName(String title);
    List<Movie> getAllMovies();
    List<Movie> searchByTitle(String title);

    List<Movie> getMoviesByGenre(String genre);

    List<Movie> getTopRatedMovies(int limit);

    List<Movie> getMoviesByLanguage(String language);

    List<Movie> getMoviesAfterDate(java.util.Date date);

    List<Movie> getMoviesBeforeDate(java.util.Date date);

    List<Movie> getMoviesWithPosters();

    boolean movieExists(String movieID);
}
