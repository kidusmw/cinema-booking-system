package application;

public class AppConfig {
    private static final String APP_NAME = "CinemaBook";

    private AppConfig() {}

    public static String getAppName() {
        return APP_NAME;
    }
}
