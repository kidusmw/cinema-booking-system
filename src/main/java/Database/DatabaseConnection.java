package Database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DatabaseConnection.class);
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

    // Reads db.properties from the classpath (src/main/resources/db.properties).
    // Falls back to the hardcoded defaults above if the file is missing,
    // so nothing breaks if someone hasn't set it up yet.
    private static Properties loadProperties() {
        Properties props = new Properties();
        try (
            InputStream in = DatabaseConnection.class.getResourceAsStream(
                "/db.properties"
            )
        ) {
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

    // Borrows a connection from the pool. Callers are expected to use
    // try-with-resources, same as before - calling close() on a pooled
    // connection returns it to the pool instead of actually closing the
    // socket, so no DAO code needs to change.
    public static Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    static HikariDataSource getDataSourceField() {
        return dataSource;
    }

    public static void printDatabaseInfo() {
        String sql =
            "SELECT current_database() AS current_database, " +
            "current_user AS login_name, " +
            "inet_server_addr() AS server_address";

        try (
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                System.out.println(
                    "Connected database: " + rs.getString("current_database")
                );
                System.out.println("Login name: " + rs.getString("login_name"));
                System.out.println(
                    "Server address: " + rs.getString("server_address")
                );
            }
        } catch (SQLException e) {
            log.error("Database info query failed", e);
        }
    }

    public static void closePool() {
        HikariDataSource ds = dataSource;
        if (ds != null && !ds.isClosed()) {
            ds.close();
        }
    }
}
