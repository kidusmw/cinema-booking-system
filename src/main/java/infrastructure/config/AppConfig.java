package infrastructure.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    private final Properties props;

    public AppConfig(Properties props) {
        this.props = props;
    }

    public static AppConfig load(String resourcePath) {
        Properties props = new Properties();
        try (InputStream in = AppConfig.class.getResourceAsStream(resourcePath)) {
            if (in != null) {
                props.load(in);
            }
        } catch (IOException e) {
            System.out.println(
                    "Failed to load config from " + resourcePath + ": " + e.getMessage());
        }
        return new AppConfig(props);
    }

    public String getHost() {
        return props.getProperty("db.host", "localhost");
    }

    public int getPort() {
        return Integer.parseInt(props.getProperty("db.port", "5432"));
    }

    public String getDatabase() {
        return props.getProperty("db.name", "cinema_booking");
    }

    public String getUser() {
        return props.getProperty("db.user", "java_user");
    }

    public String getPassword() {
        return props.getProperty("db.password", "java_pass");
    }

    public String getJdbcUrl() {
        return "jdbc:postgresql://" + getHost() + ":" + getPort() + "/" + getDatabase();
    }
}
