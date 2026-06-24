package domain.port;

import domain.model.Movie;
import java.util.List;
import java.util.Optional;

public interface MovieRepository {
    Optional<Movie> findById(Long id);

    List<Movie> findAll();

    List<Movie> searchByTitle(String title);

    Movie save(Movie movie);

    void delete(Long id);
}
