package DAO;

import Model.Seat;
import java.util.List;

public interface SeatDAO {

    boolean addSeat(Seat seat);
    boolean updateSeat(Seat seat);
    boolean deleteSeat(String seatID);
    Seat searchSeatById(String seatID);
    List<Seat> getAllSeats();
    boolean updateSeatStatus(String seatID, String status);
    boolean isSeatAvailable(String seatID);
    List<Seat> getSeatsByShow(String showID);
    List<Seat> getSeatsByHall(String movieHallID);
    List<Seat> getAvailableSeatsByHall(String movieHallID);
    boolean isSeatBookedForShow(int seatID, String showID);
}
