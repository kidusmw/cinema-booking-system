package infrastructure.persistence;

import infrastructure.config.AppConfig;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlywayMigrator {

    private static final Logger log = LoggerFactory.getLogger(FlywayMigrator.class);

    public static void migrate(AppConfig config) {
        Flyway flyway =
                Flyway.configure()
                        .dataSource(config.getJdbcUrl(), config.getUser(), config.getPassword())
                        .locations("classpath:db/migration")
                        .load();

        flyway.repair();
        var result = flyway.migrate();
        log.info(
                "Flyway migration applied {} migration(s)",
                Integer.valueOf(result.migrationsExecuted));
    }
}
