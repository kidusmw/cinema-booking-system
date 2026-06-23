package domain.port;

import domain.model.Showtime;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ShowtimeRepository {
    List<Showtime> findByMovieId(Long movieId);

    Optional<Showtime> findById(Long id);

    List<Showtime> findByDate(LocalDate date);

    List<Showtime> findAll();

    Showtime save(Showtime showtime);

    void delete(Long id);
}
