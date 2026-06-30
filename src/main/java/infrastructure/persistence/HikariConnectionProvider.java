package infrastructure.persistence;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class HikariConnectionProvider {

    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(HikariConnectionProvider.class);
    private static HikariDataSource dataSource;

    private static synchronized HikariDataSource getDataSource() {
        if (dataSource == null) {
            Properties props = loadProperties();

            String host = props.getProperty("db.host", "localhost");
            String port = props.getProperty("db.port", "5432");
            String database = props.getProperty("db.name", "cinema_booking");
            String user = props.getProperty("db.user", "java_user");
            String password = props.getProperty("db.password", "java_pass");

            String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(url);
            config.setUsername(user);
            config.setPassword(password);
            config.setDriverClassName("org.postgresql.Driver");
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setPoolName("CinemaBookingPool");

            dataSource = new HikariDataSource(config);
        }
        return dataSource;
    }

    private static Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream in =
                HikariConnectionProvider.class.getResourceAsStream("/db.properties")) {
            if (in != null) {
                props.load(in);
            } else {
                log.info("No db.properties found on classpath; using built-in defaults.");
            }
        } catch (IOException e) {
            log.warn("Failed to read db.properties, using defaults: {}", e.getMessage());
        }
        return props;
    }

    public static Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }
}
