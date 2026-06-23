package characterization;

import Database.DatabaseConnection;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Statement;

/**
 * Base class for characterization tests that need a real PostgreSQL database.
 * Spins up a TestContainers Postgres instance and initializes it with the
 * legacy schema that matches the current DAO queries.
 */
@Testcontainers
public abstract class AbstractDatabaseTest {

    @Container
    protected static final PostgreSQLContainer<?> postgres =
        new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"))
            .withDatabaseName("cinema_booking_test")
            .withUsername("test_user")
            .withPassword("test_pass");

    @BeforeAll
    static void setupSchema() throws Exception {
        replaceDataSource(
            postgres.getHost(),
            postgres.getMappedPort(5432),
            postgres.getDatabaseName(),
            postgres.getUsername(),
            postgres.getPassword()
        );

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            String schema = new String(
                AbstractDatabaseTest.class.getResourceAsStream("/legacy-schema.sql")
                    .readAllBytes()
            );
            stmt.execute(schema);
        }
    }

    @AfterAll
    static void tearDown() throws Exception {
        Field dsField = DatabaseConnection.class.getDeclaredField("dataSource");
        dsField.setAccessible(true);
        HikariDataSource ds = (HikariDataSource) dsField.get(null);
        if (ds != null && !ds.isClosed()) {
            ds.close();
        }
    }

    /**
     * Uses reflection to replace the static HikariCP dataSource in
     * DatabaseConnection. Necessary because the pool is created once in
     * the static initializer from db.properties; for tests we need it
     * pointed at the ephemeral TestContainers instance.
     */
    private static void replaceDataSource(
            String host, int port, String dbName, String user, String password)
            throws Exception {
        // Close old pool if it exists
        Field dsField = DatabaseConnection.class.getDeclaredField("dataSource");
        dsField.setAccessible(true);
        HikariDataSource oldDs = (HikariDataSource) dsField.get(null);
        if (oldDs != null && !oldDs.isClosed()) {
            oldDs.close();
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://" + host + ":" + port + "/" + dbName);
        config.setUsername(user);
        config.setPassword(password);
        config.setDriverClassName("org.postgresql.Driver");
        config.setMaximumPoolSize(5);
        config.setMinimumIdle(1);
        config.setPoolName("TestPool");

        dsField.set(null, new HikariDataSource(config));
    }
}
