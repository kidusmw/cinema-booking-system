package domain.port;

import domain.model.Seat;
import domain.model.SeatStatus;
import java.util.List;
import java.util.Optional;

public interface SeatRepository {
    List<Seat> findByHallId(Long hallId);

    Optional<Seat> findById(Long id);

    /**
     * Atomically claim a seat: update from 'available' to 'booked'.
     *
     * @return 1 if the seat was claimed, 0 if it was already taken or not found
     */
    int claimSeat(Long seatId);

    /**
     * Unconditionally update seat status (used for cancellations, etc.).
     *
     * @return number of rows affected
     */
    int updateStatus(Long seatId, SeatStatus status);

    Seat save(Seat seat);
}
