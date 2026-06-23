package infrastructure.persistence;

import infrastructure.config.AppConfig;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.output.MigrateResult;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class FlywayMigrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:16-alpine"))
        .withDatabaseName("cinema_booking_test")
        .withUsername("test_user")
        .withPassword("test_pass");

    @Test
    void bothMigrationsApplySuccessfully() {
        String jdbcUrl = postgres.getJdbcUrl();
        String user = postgres.getUsername();
        String password = postgres.getPassword();

        Flyway flyway = Flyway.configure()
            .dataSource(jdbcUrl, user, password)
            .locations("classpath:db/migration")
            .load();

        MigrateResult result = flyway.migrate();

        assertThat(result.success).isTrue();
        assertThat(result.migrationsExecuted).isEqualTo(2);

        // Verify all expected tables exist
        String[] expectedTables = {"user", "movie", "hall", "seat", "showtime", "booking", "booking_seat", "payment"};
        try (Connection conn = DriverManager.getConnection(jdbcUrl, user, password);
             Statement stmt = conn.createStatement()) {
            for (String table : expectedTables) {
                ResultSet rs = stmt.executeQuery(
                    "SELECT EXISTS (SELECT FROM information_schema.tables WHERE table_name = '" + table + "')");
                assertThat(rs.next()).isTrue();
                assertThat(rs.getBoolean(1))
                    .as("Table '%s' should exist after migrations", table)
                    .isTrue();
            }
        } catch (Exception e) {
            throw new AssertionError("Failed to verify tables", e);
        }
    }

    @Test
    void v2UpgradesAreApplied() throws Exception {
        String jdbcUrl = postgres.getJdbcUrl();
        String user = postgres.getUsername();
        String password = postgres.getPassword();

        Flyway flyway = Flyway.configure()
            .dataSource(jdbcUrl, user, password)
            .locations("classpath:db/migration")
            .load();

        flyway.migrate();

        try (Connection conn = DriverManager.getConnection(jdbcUrl, user, password);
             Statement stmt = conn.createStatement()) {

            // Verify BIGINT on user table PK
            ResultSet rs = stmt.executeQuery(
                "SELECT data_type FROM information_schema.columns " +
                "WHERE table_name = 'user' AND column_name = 'user_id'");
            assertThat(rs.next()).isTrue();
            assertThat(rs.getString("data_type")).isEqualTo("bigint");

            // Verify audit column exists
            rs = stmt.executeQuery(
                "SELECT EXISTS (SELECT FROM information_schema.columns " +
                "WHERE table_name = 'user' AND column_name = 'updated_at')");
            assertThat(rs.next()).isTrue();
            assertThat(rs.getBoolean(1)).isTrue();

            // Verify CHECK constraint on booking.status
            rs = stmt.executeQuery(
                "SELECT EXISTS (SELECT FROM information_schema.table_constraints " +
                "WHERE table_name = 'booking' AND constraint_type = 'CHECK')");
            assertThat(rs.next()).isTrue();
            assertThat(rs.getBoolean(1)).isTrue();
        }
    }

    @Test
    void migrationsAreIdempotent() {
        String jdbcUrl = postgres.getJdbcUrl();
        String user = postgres.getUsername();
        String password = postgres.getPassword();

        Flyway flyway = Flyway.configure()
            .dataSource(jdbcUrl, user, password)
            .locations("classpath:db/migration")
            .load();

        MigrateResult first = flyway.migrate();
        MigrateResult second = flyway.migrate();

        assertThat(first.migrationsExecuted).isEqualTo(2);
        assertThat(second.migrationsExecuted).isZero(); // nothing to apply
        assertThat(second.success).isTrue();
    }
}
