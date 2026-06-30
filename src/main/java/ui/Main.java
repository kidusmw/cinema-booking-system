package ui;

import application.AppContext;
import application.service.BookingFacade;
import domain.port.*;
import domain.service.AuthService;
import domain.service.BookingService;
import domain.service.PaymentService;
import infrastructure.config.AppConfig;
import infrastructure.persistence.ConnectionProvider;
import infrastructure.persistence.FlywayMigrator;
import infrastructure.persistence.HikariConnectionProvider;
import infrastructure.persistence.JdbcBookingRepository;
import infrastructure.persistence.JdbcHallRepository;
import infrastructure.persistence.JdbcMovieRepository;
import infrastructure.persistence.JdbcPaymentRepository;
import infrastructure.persistence.JdbcSeatRepository;
import infrastructure.persistence.JdbcShowtimeRepository;
import infrastructure.persistence.JdbcUserRepository;
import infrastructure.security.BCryptPasswordHasher;
import infrastructure.security.PasswordHasher;
import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ui.controller.common.NavigationManager;
import ui.controller.common.WelcomeController;

public class Main extends Application {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public void start(Stage stage) {
        Thread.setDefaultUncaughtExceptionHandler(
                (thread, throwable) ->
                        log.error(
                                "Uncaught exception in thread [{}]", thread.getName(), throwable));
        AppConfig config = AppConfig.load("/db.properties");

        FlywayMigrator.migrate(config);

        ConnectionProvider connectionProvider = HikariConnectionProvider::getConnection;
        PasswordHasher passwordHasher = new BCryptPasswordHasher();

        UserRepository userRepo = new JdbcUserRepository(connectionProvider);
        MovieRepository movieRepo = new JdbcMovieRepository(connectionProvider);
        HallRepository hallRepo = new JdbcHallRepository(connectionProvider);
        SeatRepository seatRepo = new JdbcSeatRepository(connectionProvider);
        ShowtimeRepository showtimeRepo = new JdbcShowtimeRepository(connectionProvider);
        BookingRepository bookingRepo = new JdbcBookingRepository(connectionProvider);
        PaymentRepository paymentRepo = new JdbcPaymentRepository(connectionProvider);

        AuthService authService = new AuthService(userRepo, passwordHasher);
        BookingService bookingService = new BookingService(bookingRepo, seatRepo);
        PaymentService paymentService = new PaymentService(paymentRepo);
        BookingFacade bookingFacade = new BookingFacade(connectionProvider);

        AppContext ctx =
                new AppContext(
                        userRepo,
                        movieRepo,
                        hallRepo,
                        seatRepo,
                        showtimeRepo,
                        bookingRepo,
                        paymentRepo,
                        authService,
                        bookingService,
                        paymentService,
                        bookingFacade);

        NavigationManager nav = new NavigationManager(stage, ctx);
        new WelcomeController(stage, ctx, nav);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
