package DAO;

import Model.Booking;
import java.util.List;

public interface BookingDAO {
    boolean addBooking(Booking booking);
    boolean updateBooking(Booking booking);
    boolean deleteBooking(int bookingID);
    Booking searchBookingById(int bookingID);
    List<Booking> getAllBookings();
    boolean cancelBooking(int bookingID);
}
