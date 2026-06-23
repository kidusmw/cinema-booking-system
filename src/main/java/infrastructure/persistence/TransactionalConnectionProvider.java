package infrastructure.persistence;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionalConnectionProvider implements ConnectionProvider {
    private final ConnectionProvider delegate;
    private Connection connection;

    public TransactionalConnectionProvider(ConnectionProvider delegate) {
        this.delegate = delegate;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = delegate.getConnection();
        }
        return connection;
    }
}
