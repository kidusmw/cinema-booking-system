import Controller.WelcomeController;
import domain.port.*;
import domain.service.AuthService;
import domain.service.BookingService;
import domain.service.PaymentService;
import infrastructure.config.AppConfig;
import infrastructure.persistence.*;
import infrastructure.security.BCryptPasswordHasher;
import infrastructure.security.PasswordHasher;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        AppConfig config = AppConfig.load("/db.properties");

        FlywayMigrator.migrate(config);

        ConnectionProvider connectionProvider = new DatabaseConnectionProvider();
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

        new WelcomeController(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
