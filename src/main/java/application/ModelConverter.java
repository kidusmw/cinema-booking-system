package application;

import domain.model.Booking;
import domain.model.Hall;
import domain.model.Movie;
import domain.model.Payment;
import domain.model.Seat;
import domain.model.Showtime;
import domain.model.User;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import ui.model.Admin;
import ui.model.Customer;

public class ModelConverter {

    public static ui.model.User toOldUser(User user) {
        if (user == null) return null;
        if ("admin".equals(user.getRole())) {
            return new Admin(
                    user.getUserId().intValue(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getUsername(),
                    user.getPassword(),
                    "active",
                    null,
                    user.getRole(),
                    user.getPhone(),
                    user.getEmail(),
                    null);
        }
        return new Customer(
                user.getUserId().intValue(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getPassword(),
                "active",
                null,
                user.getRole(),
                user.getPhone(),
                user.getEmail(),
                null);
    }

    public static User toDomainUser(ui.model.User old) {
        if (old == null) return null;
        User user =
                new User(
                        (long) old.getUserID(),
                        old.getFirstName(),
                        old.getLastName(),
                        old.getUsername(),
                        old.getPassword(),
                        old.getRole(),
                        old.getPhone(),
                        old.getEmail());
        return user;
    }

    public static ui.model.Movie toOldMovie(Movie m) {
        if (m == null) return null;
        ui.model.Movie old = new ui.model.Movie();
        old.setMovieID(m.getMovieId() != null ? String.valueOf(m.getMovieId()) : null);
        old.setTitle(m.getTitle());
        old.setGenre(m.getGenre());
        old.setDuration(m.getDuration());
        old.setRating(m.getRating());
        old.setDescription(m.getDescription());
        old.setReleaseDate(m.getReleaseDate());
        old.setLanguage(m.getLanguage());
        old.setPosterPath(m.getPosterPath());
        return old;
    }

    public static Movie toDomainMovie(ui.model.Movie old) {
        if (old == null) return null;
        Movie m = new Movie();
        if (old.getMovieID() != null && !old.getMovieID().isEmpty()) {
            m.setMovieId(Long.parseLong(old.getMovieID()));
        }
        m.setTitle(old.getTitle());
        m.setGenre(old.getGenre());
        m.setDuration(old.getDuration());
        m.setRating(old.getRating());
        m.setDescription(old.getDescription());
        m.setReleaseDate(old.getReleaseDate());
        m.setLanguage(old.getLanguage());
        m.setPosterPath(old.getPosterPath());
        return m;
    }

    public static ui.model.Moviehall toOldHall(Hall h) {
        if (h == null) return null;
        ui.model.Moviehall old = new ui.model.Moviehall();
        old.setId(h.getHallId() != null ? String.valueOf(h.getHallId()) : null);
        old.setName(h.getName());
        old.setCapacity(h.getCapacity());
        return old;
    }

    public static Hall toDomainHall(ui.model.Moviehall old) {
        if (old == null) return null;
        Hall h = new Hall();
        if (old.getId() != null && !old.getId().isEmpty()) {
            h.setHallId(Long.parseLong(old.getId()));
        }
        h.setName(old.getName());
        h.setCapacity(old.getCapacity());
        return h;
    }

    public static ui.model.Show toOldShowtime(Showtime s) {
        if (s == null) return null;
        ui.model.Show old = new ui.model.Show();
        old.setShowID(s.getShowId() != null ? String.valueOf(s.getShowId()) : null);
        old.setMovieID(s.getMovieId() != null ? String.valueOf(s.getMovieId()) : null);
        old.setMovieHallID(s.getHallId() != null ? String.valueOf(s.getHallId()) : null);
        if (s.getShowDate() != null) {
            old.setShowDate(
                    Date.from(s.getShowDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
        old.setShowTime(s.getShowTime() != null ? s.getShowTime().toString() : null);
        old.setMovieName(s.getMovieName());
        old.setHallName(s.getHallName());
        return old;
    }

    public static Showtime toDomainShowtime(ui.model.Show old) {
        if (old == null) return null;
        Showtime s = new Showtime();
        if (old.getShowID() != null && !old.getShowID().isEmpty()) {
            s.setShowId(Long.parseLong(old.getShowID()));
        }
        if (old.getMovieID() != null && !old.getMovieID().isEmpty()) {
            s.setMovieId(Long.parseLong(old.getMovieID()));
        }
        if (old.getMovieHallID() != null && !old.getMovieHallID().isEmpty()) {
            s.setHallId(Long.parseLong(old.getMovieHallID()));
        }
        if (old.getShowDate() != null) {
            s.setShowDate(
                    old.getShowDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }
        if (old.getShowTime() != null && !old.getShowTime().isEmpty()) {
            s.setShowTime(LocalTime.parse(old.getShowTime()));
        }
        s.setMovieName(old.getMovieName());
        s.setHallName(old.getHallName());
        return s;
    }

    public static ui.model.Seat toOldSeat(Seat s) {
        if (s == null) return null;
        ui.model.Seat old = new ui.model.Seat();
        old.setSeatID(s.getSeatId() != null ? String.valueOf(s.getSeatId()) : null);
        old.setSeatNumber(s.getSeatNumber());
        old.setSeatType(s.getSeatType());
        old.setStatus(s.getStatus());
        old.setMovieHallID(s.getHallId() != null ? s.getHallId().intValue() : 0);
        return old;
    }

    public static Seat toDomainSeat(ui.model.Seat old) {
        if (old == null) return null;
        Seat s = new Seat();
        if (old.getSeatID() != null && !old.getSeatID().isEmpty()) {
            s.setSeatId(Long.parseLong(old.getSeatID()));
        }
        s.setHallId((long) old.getMovieHallID());
        s.setSeatNumber(old.getSeatNumber());
        s.setSeatType(old.getSeatType());
        s.setStatus(old.getStatus());
        return s;
    }

    public static ui.model.Booking toOldBooking(Booking b) {
        if (b == null) return null;
        ui.model.Booking old = new ui.model.Booking();
        old.setBookingID(b.getBookingId() != null ? b.getBookingId().intValue() : 0);
        old.setUserID(b.getUserId() != null ? b.getUserId().intValue() : 0);
        old.setShowID(b.getShowId() != null ? String.valueOf(b.getShowId()) : null);
        old.setAmount(b.getAmount());
        old.setBookingStatus(b.getBookingStatus());
        old.setMovieName(b.getMovieName());
        if (b.getBookingDate() != null) {
            old.setBookingDate(
                    Date.from(b.getBookingDate().atZone(ZoneId.systemDefault()).toInstant()));
        }
        return old;
    }

    public static Booking toDomainBooking(ui.model.Booking old) {
        if (old == null) return null;
        Booking b = new Booking();
        b.setBookingId((long) old.getBookingID());
        b.setUserId((long) old.getUserID());
        b.setMovieName(old.getMovieName());
        b.setAmount(old.getAmount());
        b.setBookingStatus(old.getBookingStatus());
        if (old.getShowID() != null && !old.getShowID().isEmpty()) {
            b.setShowId(Long.parseLong(old.getShowID()));
        }
        return b;
    }

    public static ui.model.Payment toOldPayment(Payment p) {
        if (p == null) return null;
        ui.model.Payment old = new ui.model.Payment();
        old.setPaymentID(p.getPaymentId() != null ? String.valueOf(p.getPaymentId()) : null);
        old.setBookingID(p.getBookingId() != null ? p.getBookingId().intValue() : 0);
        old.setTotalAmount(p.getTotalAmount());
        old.setStatus(p.getStatus());
        old.setOtp(p.getOtp());
        old.setPaymentMethod(p.getPaymentMethod());
        if (p.getPaymentDate() != null) {
            old.setPaymentDate(
                    Date.from(p.getPaymentDate().atZone(ZoneId.systemDefault()).toInstant()));
        }
        return old;
    }

    public static Payment toDomainPayment(ui.model.Payment old) {
        if (old == null) return null;
        Payment p = new Payment();
        if (old.getPaymentID() != null && !old.getPaymentID().isEmpty()) {
            p.setPaymentId(Long.parseLong(old.getPaymentID()));
        }
        p.setBookingId((long) old.getBookingID());
        p.setTotalAmount(old.getTotalAmount());
        p.setStatus(old.getStatus());
        p.setOtp(old.getOtp());
        p.setPaymentMethod(old.getPaymentMethod());
        return p;
    }
}
