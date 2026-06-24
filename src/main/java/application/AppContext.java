package application;

import application.service.BookingFacade;
import domain.port.*;
import domain.service.AuthService;
import domain.service.BookingService;
import domain.service.PaymentService;

public class AppContext {
    public final UserRepository userRepo;
    public final MovieRepository movieRepo;
    public final HallRepository hallRepo;
    public final SeatRepository seatRepo;
    public final ShowtimeRepository showtimeRepo;
    public final BookingRepository bookingRepo;
    public final PaymentRepository paymentRepo;
    public final AuthService authService;
    public final BookingService bookingService;
    public final PaymentService paymentService;
    public final BookingFacade bookingFacade;

    public AppContext(
            UserRepository userRepo,
            MovieRepository movieRepo,
            HallRepository hallRepo,
            SeatRepository seatRepo,
            ShowtimeRepository showtimeRepo,
            BookingRepository bookingRepo,
            PaymentRepository paymentRepo,
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
