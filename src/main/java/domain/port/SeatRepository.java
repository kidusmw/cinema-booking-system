package domain.port;

import domain.model.Seat;
import java.util.List;
import java.util.Optional;

public interface SeatRepository {
    List<Seat> findByHallId(Long hallId);

    Optional<Seat> findById(Long id);

    void updateStatus(Long seatId, String status);

    Seat save(Seat seat);
}
