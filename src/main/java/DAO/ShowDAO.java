package DAO;

import Model.Show;
import java.util.Date;
import java.util.List;

public interface ShowDAO {

    boolean addShow(Show show);
    boolean updateShow(Show show);
    boolean deleteShow(String showID);
    Show searchShowById(String showID);
    List<Show> getAllShows();
    List<Show> getShowsByMovie(String movieID);
    List<Show> getShowsByDate(Date date);
    List<Show> getShowsByDateRange(Date startDate, Date endDate);
    List<Show> getShowsByMovieAndDate(String movieID, Date date);
    List<Show> getShowsByHall(String movieHallID);
    boolean checkAvailability(String showID);
}
