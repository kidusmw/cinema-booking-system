package DAO;
import Model.Moviehall;

import java.util.List;

public interface MovieHallDAO {
    boolean addMovieHall(Moviehall hall);
    boolean updateMovieHall(Moviehall hall);
    boolean deleteMovieHall(String movieHallID);
    Moviehall searchMovieHallById(String movieHallID);
    List<Moviehall> getAllMovieHalls();
    List<Moviehall> searchMovieHallByName(String hallName);
    List<Moviehall> getMovieHallsByCapacity(int capacity);
    List<Moviehall> getMovieHallsByMinCapacity(int minCapacity,int maxCapacity);
    boolean isHallAvailable(String movieHallID);
    int getTotalHalls();
}