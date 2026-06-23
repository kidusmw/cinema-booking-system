package infrastructure.persistence;

import Database.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectionProvider implements ConnectionProvider {
    @Override
    public Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }
}
