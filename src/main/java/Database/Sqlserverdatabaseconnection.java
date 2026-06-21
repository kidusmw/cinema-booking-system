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

public class Sqlserverdatabaseconnection {

    private static final HikariDataSource dataSource;

    // Runs once, the first time anything calls getConnection() or
    // touches this class - this is what replaces the old "open a new
    // connection every call" approach with a shared pool.
    static {
        Properties props = loadProperties();

        String host = props.getProperty("db.host", "localhost");
        String port = props.getProperty("db.port", "1433");
        String database = props.getProperty("db.name", "Cinema_Booking_System");
        String user = props.getProperty("db.user", "java_user");
        String password = props.getProperty("db.password", "java123");

        String url =
            "jdbc:sqlserver://" +
            host +
            ":" +
            port +
            ";" +
            "databaseName=" +
            database +
            ";" +
            "encrypt=false;" +
            "trustServerCertificate=true;";

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.setDriverClassName(
            "com.microsoft.sqlserver.jdbc.SQLServerDriver"
        );
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setPoolName("CinemaBookingPool");

        dataSource = new HikariDataSource(config);
    }

    // Reads db.properties from the classpath (src/main/resources/db.properties).
    // Falls back to the hardcoded defaults above if the file is missing,
    // so nothing breaks if someone hasn't set it up yet.
    private static Properties loadProperties() {
        Properties props = new Properties();
        try (
            InputStream in =
                Sqlserverdatabaseconnection.class.getResourceAsStream(
                    "/db.properties"
                )
        ) {
            if (in != null) {
                props.load(in);
            } else {
                System.out.println(
                    "No db.properties found on classpath; using built-in defaults."
                );
            }
        } catch (IOException e) {
            System.out.println(
                "Failed to read db.properties, using defaults: " +
                    e.getMessage()
            );
        }
        return props;
    }

    // Borrows a connection from the pool. Callers are expected to use
    // try-with-resources, same as before - calling close() on a pooled
    // connection returns it to the pool instead of actually closing the
    // socket, so no DAO code needs to change.
    //
    // This now declares "throws SQLException" instead of silently
    // swallowing the error and returning null - the old version would
    // print "CONNECTION FAILED!" and hand back null, which meant the
    // very next line (conn.prepareStatement(...)) would throw a
    // NullPointerException instead of a meaningful SQL error.
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void printDatabaseInfo() {
        String sql = """
            SELECT
                DB_NAME() AS current_database,
                SUSER_SNAME() AS login_name,
                @@SERVERNAME AS server_name
            """;

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
                    "Server name: " + rs.getString("server_name")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
