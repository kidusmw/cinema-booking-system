package ui;

import application.AppContext;
import application.service.BookingFacade;
import domain.service.AuthService;
import domain.service.BookingService;
import domain.service.PaymentService;
import infrastructure.config.AppConfig;
import infrastructure.persistence.*;
import infrastructure.security.BCryptPasswordHasher;
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

        ConnectionProvider connectionProvider = new DatabaseConnectionProvider();
        BCryptPasswordHasher passwordHasher = new BCryptPasswordHasher();

        JdbcUserRepository userRepo = new JdbcUserRepository(connectionProvider);
        JdbcMovieRepository movieRepo = new JdbcMovieRepository(connectionProvider);
        JdbcHallRepository hallRepo = new JdbcHallRepository(connectionProvider);
        JdbcSeatRepository seatRepo = new JdbcSeatRepository(connectionProvider);
        JdbcShowtimeRepository showtimeRepo = new JdbcShowtimeRepository(connectionProvider);
        JdbcBookingRepository bookingRepo = new JdbcBookingRepository(connectionProvider);
        JdbcPaymentRepository paymentRepo = new JdbcPaymentRepository(connectionProvider);

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
