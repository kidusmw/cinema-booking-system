package domain.port;

import domain.model.Hall;
import java.util.List;
import java.util.Optional;

public interface HallRepository {
    Optional<Hall> findById(Long id);

    List<Hall> findAll();

    Hall save(Hall hall);

    void delete(Long id);
}
