package application;

import application.service.BookingFacade;
import domain.service.AuthService;
import domain.service.BookingService;
import domain.service.PaymentService;
import infrastructure.persistence.JdbcBookingRepository;
import infrastructure.persistence.JdbcHallRepository;
import infrastructure.persistence.JdbcMovieRepository;
import infrastructure.persistence.JdbcPaymentRepository;
import infrastructure.persistence.JdbcSeatRepository;
import infrastructure.persistence.JdbcShowtimeRepository;
import infrastructure.persistence.JdbcUserRepository;

public class AppContext {
    public final JdbcUserRepository userRepo;
    public final JdbcMovieRepository movieRepo;
    public final JdbcHallRepository hallRepo;
    public final JdbcSeatRepository seatRepo;
    public final JdbcShowtimeRepository showtimeRepo;
    public final JdbcBookingRepository bookingRepo;
    public final JdbcPaymentRepository paymentRepo;
    public final AuthService authService;
    public final BookingService bookingService;
    public final PaymentService paymentService;
    public final BookingFacade bookingFacade;

    public AppContext(
            JdbcUserRepository userRepo,
            JdbcMovieRepository movieRepo,
            JdbcHallRepository hallRepo,
            JdbcSeatRepository seatRepo,
            JdbcShowtimeRepository showtimeRepo,
            JdbcBookingRepository bookingRepo,
            JdbcPaymentRepository paymentRepo,
            AuthService authService,
            BookingService bookingService,
            PaymentService paymentService,
            BookingFacade bookingFacade) {
        this.userRepo = userRepo;
        this.movieRepo = movieRepo;
        this.hallRepo = hallRepo;
        this.seatRepo = seatRepo;
        this.showtimeRepo = showtimeRepo;
        this.bookingRepo = bookingRepo;
        this.paymentRepo = paymentRepo;
        this.authService = authService;
        this.bookingService = bookingService;
        this.paymentService = paymentService;
        this.bookingFacade = bookingFacade;
    }
}
